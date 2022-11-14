import com.example.team_project.framkwork.config.Config;
import com.example.team_project.framkwork.core.mvc.LoadConfig;
import com.example.team_project.service.FoodService;
import com.example.team_project.service.impl.FoodServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class FoodServiceTest {
    static {
        LoadConfig.load(Config.class);
    }
    @Test
    public void orderFoodListTest() {
        FoodService service = new FoodServiceImpl();
        Set<String> orderBy = new HashSet<>(5);
        orderBy.add("edible");
        System.out.println(service.orderedFoodList(orderBy, 1,false));
    }

    @Test
    public void simpleSearchTest() {
        FoodService service = new FoodServiceImpl();
        System.out.println(service.simpleFoodSearch("萝卜"));
    }
}
