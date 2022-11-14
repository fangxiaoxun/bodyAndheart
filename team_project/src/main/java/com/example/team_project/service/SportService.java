package com.example.team_project.service;

import com.example.team_project.pojo.SportAnswer;
import com.example.team_project.pojo.SportAnswerLike;
import com.example.team_project.pojo.SportQuestion;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SportService {
    /**
     * 获取一定数量的提问，每个提问都带一定数量的回答
     * @param size 提问的数量
     * @param currentPage 回答的当前的页数
     * @param count 回答的数量
     * @return 返回一定数量和带回复的问题
     */
    List<Map<String, Object>> getSportQuestionsWithAnswers(int size, int currentPage, int count);

    /**
     * 获取一定数量的提问，每个提问都带所有数量的回答
     * @param size 提问的数量
     * @return 返回一定数量的回复
     */
    List<Map<String,Object>> getSportQuestionsWithAnswers(int size);

    /**
     * 回复提问
     * @param question 回复的提问的id
     * @param answerBody 回复的正文
     * @param replier 回复者的id
     * @return 回复的唯一标识
     */
    String replyQuestion(String question, String answerBody, long replier);

    /**
     * 删除回答
     * @param uuid 回答的唯一标识
     * @return 是否成功
     */
    boolean delAnswer(String uuid);

    /**
     * 发布问题
     * @param title 问题的标题
     * @param body 问题的正文
     * @param quizzer 提问者
     * @return 发布的问题的唯一标识
     */
    String releaseQuestion(String title, String body, long quizzer);

    /**
     * 删除问题
     * @param question 问题的唯一标识
     * @return 是否成功
     */
    boolean delQuestion(String question);

    /**
     * 获得回复，分页
     * @param question 要获得的答案在哪个问题下
     * @param currentPage 当前页码
     * @param count 一次显示的条目数
     * @return
     */
    List<SportAnswer> getAnswerByPage(String question, int currentPage, int count);

    /**
     * 获取单个回复
     * @param answerId 回复的唯一标识
     */
    SportAnswer getOneAnswer(String answerId);

    /**
     * 获取单个提问，不带回复
     * @param qid 提问的唯一标识
     */
    SportQuestion getOneQuestion(String qid);

    /**
     * 获取问题中回复的条数
     * @param qid 问题的id
     * @return 条数
     */
    long answerCount(String qid);

    /**
     * 点赞回答
     * @return 是否点赞成功
     */
    boolean likeAnswer(String answer, long uid);

    /**
     * 取消点赞
     * @return 是否成功取消
     */
    boolean removeLike(String answer, long uid);

    /**
     * 获取是否存在带着这些参数的点赞
     * @param answer 点赞的回复
     * @param uid 点赞的用户
     * @return 是否有
     */
    boolean oneHasLike(String answer, long uid);

    /**
     * 获取回复的所有点赞信息
     * @param answer 回复的id
     * @return 所有点赞信息，不为null
     */
    Set<SportAnswerLike> getLikeDetail(String answer);

    /**
     * 批量获取是否已经点过赞，按照传入顺序进行判断
     * @return 点赞信息
     */
    List<Boolean> hasLike(String[] answer, long uid);

    /**
     * 某个回答的点赞数量
     * @param answer 要获取点赞数量的回答
     * @return 回答数
     */
    long likeCount(String answer);

    /**
     * 批量获取回答的点赞数，按数组顺序
     * @param answer 要获取点赞数的回答们
     * @return 回答数的集合
     */
    List<Long> likesCount(String[] answer);

}
