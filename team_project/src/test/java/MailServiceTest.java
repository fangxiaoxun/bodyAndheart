import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.pojo.Mail;
import com.example.team_project.service.MailService;
import com.example.team_project.service.impl.MailServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MailServiceTest {
    private MailService service = new MailServiceImpl();
    static {
        LoadConfig.load(Config.class);
    }

    @Test
    public void getRandomMailTest() {
        MailService service = new MailServiceImpl();
        List<Mail> mailList = service.randomMails(10, 1547842L);
        System.out.println(mailList);
    }

    @Test
    public void writeMailTest() {
        boolean result = service.writeMail(1547842, "希望修改完bug");
        System.out.println(result);
    }

    @Test
    public void replyMailTest() {
        System.out.println(service.replyMail(4, 1547842L, "祝你顺利"));
    }

    @Test
    public void setReadTest() {
        System.out.println(service.setRead("0a33ae076af2475ca33bceb13228ce37"));
    }

    @Test
    public void setUnReadTest() {
        System.out.println(service.setUnRead("0a33ae076af2475ca33bceb13228ce37"));
    }


}
