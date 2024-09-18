package guru.qa.niffler.test;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Test;

import java.util.List;


public class QuickTest {


  private static final Config CFG = Config.getInstance();

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Test
  void quickTest() {

    System.out.println(System.getenv("GITHUB_TOKEN"));
  }


}
