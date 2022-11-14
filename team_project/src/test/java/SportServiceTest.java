import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.service.SportService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SportServiceTest {

    static {
        LoadConfig.load(Config.class);
    }
    @Test
    public void getSportQuestionsWithAnswersTest() {
        SportService service = BeanFactory.getBean(SportService.class);
        System.out.println(service.getSportQuestionsWithAnswers(5));
        System.out.println(service.getSportQuestionsWithAnswers(5, 1, 0));
    }

    @Test
    public void replyQuestionTest() {
        SportService service = BeanFactory.getBean(SportService.class);
        System.out.println(service.replyQuestion("1", "test", 1547844L));
    }

    @Test
    public void delAnswerTest() {
        SportService service = BeanFactory.getBean(SportService.class);
        System.out.println(service.delAnswer("561113c0fadc4b0f8b7dc570a80b12cc"));
    }

    @Test
    public void releaseQuestionTest() {
        SportService service = BeanFactory.getBean(SportService.class);
        System.out.println(service.releaseQuestion("烦恼软设", "烦烦烦", 1547844L));
    }

    @Test
    public void delQuestionTest() {
        SportService service = BeanFactory.getBean(SportService.class);
        System.out.println(service.delQuestion("26353bbe35a24fc397d1853863ab932c"));
    }

    @Test
    public void likeTest() throws InterruptedException {
        SportService sportService = BeanFactory.getBean(SportService.class);
        System.out.println(sportService.likeCount("1325407132a948e3a80c19fd3bd6497b"));
        System.out.println(sportService.likeAnswer("1325407132a948e3a80c19fd3bd6497b", 1547851L));
        Thread.sleep(100);
        System.out.println(sportService.likeAnswer("1325407132a948e3a80c19fd3bd6497b", 1547851L));
        System.out.println(sportService.likeCount("1325407132a948e3a80c19fd3bd6497b"));
    }

    @Test
    public void delLikeTest() throws InterruptedException {
        SportService sportService = BeanFactory.getBean(SportService.class);
        System.out.println(sportService.removeLike("babd4582ad974a488c2d110dae6a18e2", 1547851L));
        Thread.sleep(10);
        sportService.removeLike("babd4582ad974a488c2d110dae6a18e2", 1547851L);
        System.out.println(sportService.likeCount("babd4582ad974a488c2d110dae6a18e2"));
    }

    @Test
    public void oneHasLike() {
        SportService sportService = BeanFactory.getBean(SportService.class);
        System.out.println(sportService.oneHasLike("1325407132a948e3a80c19fd3bd6497b", 1547852L));
    }

}
