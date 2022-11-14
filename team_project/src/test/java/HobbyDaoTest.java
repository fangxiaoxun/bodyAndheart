import com.example.team_project.dao.HobbyDao;
import com.example.team_project.dao.impl.HobbyDaoImpl;
import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.pojo.Hobby;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class HobbyDaoTest {
    static {
        LoadConfig.load(Config.class);
    }

    @Test
    public void allTest(){
        HobbyDao dao = new HobbyDaoImpl();
        System.out.println(dao.all());
    }
    @Test
    public void addTest() throws SQLException {
        HobbyDao dao = new HobbyDaoImpl();
        Hobby hobby = new Hobby();
        hobby.setHobby(6);
        hobby.setName("test6");
        System.out.println(dao.add(hobby));
    }
    @Test
    public void delTest() throws SQLException {
        HobbyDao dao = new HobbyDaoImpl();
        System.out.println(dao.delete(2));
    }

}
