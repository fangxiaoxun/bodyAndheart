import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.service.IconService;
import com.example.team_project.service.IslandService;
import com.example.team_project.service.impl.IconServiceImpl;
import com.example.team_project.service.impl.IslandServiceImpl;
import org.junit.jupiter.api.Test;

public class IconServiceTest {

    static {
        LoadConfig.load(Config.class);
    }

    @Test
    public void getAllTest(){
        IconService service = new IconServiceImpl();
        System.out.println(service.allIcons(1, 10));
    }
    @Test
    public void setIcon() {
        IconService service = new IconServiceImpl();
        System.out.println(service.setIcon(1547825, 2));
    }

    @Test
    public void AllIslandIconsTest(){
        IslandService service = new IslandServiceImpl();
        System.out.println(service.allIslandIcons());
    }
    @Test
    public void getIslandIconByMarkTest(){
        IslandService service = new IslandServiceImpl();
        System.out.println(service.getIslandIconByMark(1));
    }
    @Test
    public void setIslandIcon() {
        IslandService service = new IslandServiceImpl();
        System.out.println(service.setIslandIcon(1, 1));
    }
}
