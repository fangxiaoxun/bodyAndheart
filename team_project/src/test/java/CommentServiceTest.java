import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.pojo.Comment;
import com.example.team_project.service.CommentService;
import com.example.team_project.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;

public class CommentServiceTest {

    static {
        LoadConfig.load(Config.class);
    }
    @Test
    public void releaseTest(){
        CommentService service = new CommentServiceImpl();
        Comment comment = new Comment();
        comment.setPostId(1L);
        comment.setFatherId(2L);
        comment.setDiscussant(1547832L);
        System.out.println(service.release(comment));
    }
}
