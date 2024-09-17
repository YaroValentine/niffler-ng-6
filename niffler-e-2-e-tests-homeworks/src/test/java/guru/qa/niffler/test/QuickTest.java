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

    List<CategoryJson> categories = spendApiClient.getAllCategories("yaro", false);
    CategoryJson categoryJson = categories.get(1);
    if (categoryJson != null) {
      CategoryJson updatedCategory = new CategoryJson(
          categoryJson.id(),
          categoryJson.name(),
          categoryJson.username(),
          true
      );
      spendApiClient.updateCategory(updatedCategory);
    }
  }


}
