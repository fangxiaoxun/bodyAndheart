import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.service.IslandService;
import com.example.team_project.service.impl.IslandServiceImpl;
import org.junit.jupiter.api.Test;

public class IslandServiceTest {
    private IslandService service = new IslandServiceImpl();
    static {
        LoadConfig.load(Config.class);
    }
    @Test
    public void createTest(){
        System.out.println(service.createIsland(1547825L, "myIsland", "test", (byte) 0, 1));
    }
    @Test
    public void getIcon(){
        System.out.println(service.getIslandIconByMark(1));
    }
    @Test
    public void hasIsland(){
        System.out.println(service.hasIsland(1547825));
    }
    @Test
    public void addRecordTest() {
        System.out.println(service.addRecord(1547856L, 1547855));
    }
    @Test
    public void showRecordTest() {
        System.out.println(service.getHasBeen(1547852L, 2));
    }
}
