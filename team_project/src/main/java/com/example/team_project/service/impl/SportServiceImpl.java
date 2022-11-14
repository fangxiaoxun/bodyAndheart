package com.example.team_project.service.impl;

import com.example.team_project.dao.SportAnswerDao;
import com.example.team_project.dao.SportAnswerLikeDao;
import com.example.team_project.dao.SportQuestionDao;
import com.example.team_project.framkwork.core.annotation.AutoWire;
import com.example.team_project.framkwork.core.annotation.Bean;
import com.example.team_project.pojo.SportAnswer;
import com.example.team_project.pojo.SportAnswerLike;
import com.example.team_project.pojo.SportQuestion;
import com.example.team_project.service.SportService;

import java.sql.SQLException;
import java.util.*;

@Bean("sportService")
public class SportServiceImpl implements SportService {
    @AutoWire
    private SportAnswerDao answerDao;
    @AutoWire
    private SportQuestionDao questionDao;
    @AutoWire
    private SportAnswerLikeDao answerLikeDao;

    @Override
    public List<Map<String, Object>> getSportQuestionsWithAnswers(int size, int currentPage, int count) {
        List<SportQuestion> randomQuestions = questionDao.getQuestionsRandom(size);
        List<Map<String, Object>> result = new ArrayList<>(randomQuestions.size());
        for (SportQuestion randomQuestion : randomQuestions) {
            List<SportAnswer> answers = answerDao.getAnswersByPage(randomQuestion.getQuestion(), currentPage - 1, count);
            //给每个回复都加上点赞数
            for (SportAnswer answer : answers) {
                answer.setLikeCount(answerLikeDao.likeCount(answer.getUuid()));
            }
            //逆序排序
            answers.sort(((o1, o2) -> (int) (o2.getLikeCount() - o1.getLikeCount())));
            Map<String, Object> map = new HashMap<>(4);
            map.put("question", randomQuestion);
            map.put("answers", answers);
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getSportQuestionsWithAnswers(int size) {
        //获取size个随机提问
        List<SportQuestion> randomQuestions = questionDao.getQuestionsRandom(size);
        List<Map<String, Object>> result = new ArrayList<>(randomQuestions.size());
        for (SportQuestion randomQuestion : randomQuestions) {
            //获取回复
            List<SportAnswer> allAnswers = answerDao.getAllAnswers(randomQuestion.getQuestion());
            //把每一个回复都加上回复数
            for (SportAnswer allAnswer : allAnswers) {
                allAnswer.setLikeCount(answerLikeDao.likeCount(allAnswer.getUuid()));
            }
            //逆序排序
            allAnswers.sort(((o1, o2) -> (int) (o2.getLikeCount() - o1.getLikeCount())));
            Map<String, Object> map = new HashMap<>();
            map.put("question", randomQuestion);
            map.put("answers", allAnswers);
            result.add(map);
        }
        return result;
    }

    @Override
    public String replyQuestion(String question, String answerBody, long replier) {
        String resultId = null;
        if (question != null && answerBody != null && replier > 0) {
            SportAnswer answer = new SportAnswer();
            answer.setQuestion(question);
            answer.setAnswer(answerBody);
            answer.setReplier(replier);
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            answer.setUuid(id);
            try {
                int reply = answerDao.reply(answer);
                if (reply != 0) {
                    resultId = id;
                }
            } catch (SQLException e) {
                answerDao.rollback();
                e.printStackTrace();
            } finally {
                answerDao.commit();
            }
        }
        return resultId;
    }

    @Override
    public boolean delAnswer(String uuid) {
        boolean flag = false;
        if (uuid != null) {
            try {
                int result = answerDao.delReply(uuid);
                if (result != 0) {
                    flag = true;
                }
            } catch (SQLException e) {
                answerDao.rollback();
                e.printStackTrace();
            } finally {
                answerDao.commit();
            }
        }
        return flag;
    }

    @Override
    public String releaseQuestion(String title, String body, long quizzer) {
        String id = null;
        if (title != null && body != null && quizzer > 0) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            SportQuestion question = new SportQuestion(uuid, title, body, quizzer);
            try {
                int result = questionDao.releaseQuestion(question);
                if (result != 0) {
                    id = uuid;
                }
            } catch (SQLException e) {
                questionDao.rollback();
                e.printStackTrace();
            } finally {
                questionDao.commit();
            }
        }
        return id;
    }

    @Override
    public boolean delQuestion(String question) {
        boolean flag = false;
        if (question != null) {
            try {
                int result = questionDao.delQuestion(question);
                if (result != 0) {
                    flag = true;
                }
            } catch (SQLException e) {
                questionDao.rollback();
                e.printStackTrace();
            } finally {
                questionDao.commit();
            }
        }
        return flag;
    }

    @Override
    public List<SportAnswer> getAnswerByPage(String question, int currentPage, int count) {
        List<SportAnswer> answers = null;
        if (question != null && currentPage > 0 && count > 0) {
            answers = answerDao.getAnswersByPage(question, currentPage - 1, count);
        }
        if (answers == null) {
            //若参数错误，则返回一个初始值为0的ArrayList
            answers = new ArrayList<>(0);
        }
        return answers;
    }

    @Override
    public SportAnswer getOneAnswer(String answerId) {
        if (answerId != null) {
            SportAnswer answer = new SportAnswer();
            answer.setUuid(answerId);
            return answerDao.selectOne(answer);
        }
        return null;
    }

    @Override
    public SportQuestion getOneQuestion(String qid) {
        SportQuestion question = new SportQuestion();
        question.setQuestion(qid);
        return questionDao.selectOne(question);
    }

    @Override
    public long answerCount(String qid) {
        SportAnswer answer = new SportAnswer();
        answer.setQuestion(qid);
        return answerDao.count(answer);
    }

    @Override
    public boolean likeAnswer(String answer, long uid) {
        boolean like = false;
        if (answer != null && uid > 0) {
            like = answerLikeDao.like(answer,uid);
        }
        return like;
    }

    @Override
    public boolean removeLike(String answer, long uid) {
        boolean remove = false;
        if (answer != null && uid > 0) {
            remove = answerLikeDao.removeLike(answer, uid);
        }
        return remove;
    }

    @Override
    public boolean oneHasLike(String answer, long uid) {
        boolean result = false;
        if (answer != null && uid > 0) {
            SportAnswerLike like = answerLikeDao.alreadyLike(answer, uid);
            if (like != null) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public Set<SportAnswerLike> getLikeDetail(String answer) {
        return answerLikeDao.getLikeDetail(answer);
    }

    @Override
    public List<Boolean> hasLike(String[] answer, long uid) {
        List<Boolean> result;
        if (answer != null) {
            //获取每一个回答都是否点过赞，这样做能够使得每一个都能够对应
            result = new ArrayList<>(answer.length);
            for (String id : answer) {
                result.add(oneHasLike(id, uid));
            }
        }else {
            result = new ArrayList<>(0);
        }
        return result;
    }

    @Override
    public long likeCount(String answer) {
        long count = 0;
        if (answer != null) {
            count = answerLikeDao.likeCount(answer);
        }
        return count;
    }

    @Override
    public List<Long> likesCount(String[] answer) {
        List<Long> likesCount;
        if (answer != null) {
            likesCount = new ArrayList<>(answer.length);
            //每一个都去获取，这样能够保证逐一对应
            for (String id : answer) {
                likesCount.add(answerLikeDao.likeCount(id));
            }
        } else {
            likesCount = new ArrayList<>(0);
        }
        return likesCount;
    }


}
