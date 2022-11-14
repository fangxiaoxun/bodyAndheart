import com.example.team_project.dao.*;
import com.example.team_project.dao.impl.*;
import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.pojo.*;
import com.example.team_project.service.FoodService;
import com.example.team_project.service.MailService;
import com.example.team_project.service.SecurityService;
import com.example.team_project.service.impl.FoodServiceImpl;
import com.example.team_project.service.impl.MailServiceImpl;
import com.example.team_project.service.impl.SecurityServiceImpl;
import com.example.websocket.pojo.vo.Message;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;

/**
 * 由于dao层全部依赖于同一个数据库连接池，且被容器接管，故统一在DaoTest类进行测试
 */
public class DaoTest {
    /**
     * 用于对缓存的测试
     */
    private static PostLikeDao dao;

    static {
        LoadConfig.load(Config.class);
        dao = new PostLikeDaoImpl();
        dao.alreadyLike(1, 1547825L);
        dao.alreadyLike(1, 1547832L);
    }

    @Test
    public void securityDaoTest() {
        SecurityDao dao = new SecurityDaoImpl();
        List<Question> all = dao.all();
        System.out.println(all);
    }

    @Test
    public void addQuestionTest() {
        SecurityDao dao = new SecurityDaoImpl();
        try {
            boolean test1 = dao.addQuestion(1547825, 1, "test1");
        } catch (Exception e) {
            dao.rollback();
            e.printStackTrace();
        } finally {
            dao.commit();
        }
    }

    @Test
    public void compareTest() {
        SecurityDao dao = new SecurityDaoImpl();
        boolean test1 = dao.compare(1547825, 1, "test1");
        System.out.println(test1);
    }

    @Test
    public void hasQuestionTest() {
        SecurityDao dao = new SecurityDaoImpl();
        List<Question> questions = dao.hasQuestions(1547825);
        System.out.println(questions);
    }

    @Test
    public void findNoFound() {
        SecurityService service = new SecurityServiceImpl();
        System.out.println(service.findNoHave(1547825));
    }

    @Test
    public void getPostReplyCountTest() {
        PostDao postDao = new PostDaoImpl();
        long[] count = postDao.getPostReplyCount(1, 2);
        System.out.println(Arrays.toString(count));

        long count1 = postDao.getPostReplyCount(1);
        System.out.println(count1);
    }

    @Test
    public void getPosts() {
        PostDao postDao = new PostDaoImpl();
        System.out.println(postDao.getPostByPage(1547825L, 0, 2, new User()));
        System.out.println(postDao.getPost(1547825L));
    }

    @Test
    public void getIslandsTest() {
        IslandDao islandDao = new IslandDaoImpl();
        long[] ids = islandDao.getIslandsIdByPage(0, 2);
        int get = (int) (Math.random() * ids.length);
        System.out.println(islandDao.getIslandByIds(ids[get]));
    }

    @Test
    public void getCommentsTest() {
        CommentDao commentDao = new CommentDaoImpl();
        System.out.println(commentDao.getComments(1));

        System.out.println(commentDao.getCommentsByPage(1, 0, 1));
    }

    @Test
    public void alreadyLikeTest() {
        System.out.println(dao.alreadyLike(1, 1547825L));
    }

    @Test
    public void likeTest() {

        PostLike postLike = new PostLike();
        postLike.setLiker(1547825L);
        postLike.setPostId(1L);
        System.out.println(dao.like(1547825L, 1));
        postLike.setLiker(1547832L);
        System.out.println(dao.like(1547832L, 1));
        //测试缓存在超时之后，是否确实会被清除，并且进行持久化
//        while (true){
//            dao.alreadyLike(1, 1547825L);
//        }
    }

    @Test
    public void deleteLikeTest() {
        boolean delete = dao.delete(1L, 1547825L);
        System.out.println(delete);

//        while (true) {
//            dao.alreadyLike(1,1547832L);
//        }
    }

    @Test
    public void allPostConditionTest() {
        PostConditionDao postConditionDao = new PostConditionDaoImpl();
        List<PostCondition> all = postConditionDao.all();
        System.out.println(all);
    }

    @Test
    public void getDataByIdTest() {
        FoodDao foodDao = new FoodDaoImpl();
        Food food = foodDao.getDataById(259);
        System.out.println(food);
    }

    @Test
    public void findGroupTest() {
        FoodDao foodDao = new FoodDaoImpl();
        Food food = foodDao.getDataById(313);
        System.out.println(foodDao.findGroup(food.getDetailed()));
    }

    @Test
    public void allGroupTest() {
        FoodService service = new FoodServiceImpl();
        System.out.println(service.allGroup());
    }
    @Test
    public void getFoodsByDetailedTest() {
        FoodDao foodDao = new FoodDaoImpl();
        System.out.println(foodDao.getFoodsByDetailed(1));
    }
    @Test
    public void orderedListTest() {
        FoodDao foodDao = new FoodDaoImpl();
        Set<String> set = new HashSet<>(5);
        set.add("edible");
        System.out.println(foodDao.orderedList(set, 1, true));
    }
    @Test
    public void getTopPostTest() {
        IslandDao islandDao = new IslandDaoImpl();
        long topPost = islandDao.getTopPost(1547825L);
        System.out.println(topPost);
    }

    @Test
    public void changeTopPostTest() throws SQLException {
        IslandDao islandDao = new IslandDaoImpl();
        int result = islandDao.changeTopPost(1547825L, null);
        islandDao.commit();
        System.out.println(result);
    }
    @Test
    public void getMarkCountTest() throws SQLException {
        IslandMarkDao islandMarkDao = new IslandMarkDaoImpl();
        System.out.println(islandMarkDao.getMarkCount(1547825L));
    }
    @Test
    public void addMailTest() throws SQLException {
        MailDao mailDao = new MailDaoImpl();
        Mail mail = new Mail();
        mail.setSender(1547825L);
        mail.setBody("烦恼软设");
        mailDao.writeMail(mail);
        mailDao.commit();
    }

    @Test
    public void randomMailTest() {
        MailService mailService = new MailServiceImpl();
        System.out.println(mailService.randomMails(2,1547842L));
    }
    @Test
    public void likeCountTest() {
        PostLikeDao postLikeDao = new PostLikeDaoImpl();
        System.out.println(postLikeDao.likeCount(1L));
        System.out.println(postLikeDao.like(1547825L,1L));
        System.out.println(postLikeDao.delete(1L, 1547825L));
        System.out.println(postLikeDao.likeCount(1L));
    }
    @Test
    public void getMailsTest() {
        MailDao mailDao = new MailDaoImpl();
        System.out.println(mailDao.getMails(new Long[]{1L,2L}));
    }

    @Test
    public void getMailRepliesTest() {
        MailReplyDao mailReplyDao = new MailReplyDaoImpl();
        System.out.println(mailReplyDao.getReplies(1547842));
        System.out.println(mailReplyDao.getReplies(1547856));
    }

    @Test
    public void addMessageTest() {
//        ChatRecordDao chatRecordDao = BeanFactory.getBean("chatRecordDao", ChatRecordDao.class);
//        Message message = new Message(false,1547851L,"1547851","test");
//        System.out.println(chatRecordDao.addRecord(message));
    }

    @Test
    public void delMessageTest() {
        ChatRecordDao chatRecordDao = BeanFactory.getBean("chatRecordDao", ChatRecordDao.class);
        System.out.println(chatRecordDao.removeRecord("1547851"));
    }

    @Test
    public void historyMailTest() {
        MailService service = BeanFactory.getBean(MailService.class);
        System.out.println(service.historyMail(9L, 1, 5));
    }

    @Test
    public void replyCountTest() {
        MailReplyDao replyDao = BeanFactory.getBean(MailReplyDao.class);
        System.out.println(replyDao.replyCount(9L));
    }

}
