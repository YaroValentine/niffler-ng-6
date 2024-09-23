package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileWebTest {

  private static final Config CFG = Config.getInstance();

  @Category(
      username = "yaro",
      archived = true
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("yaro", "secret")
        .profilePage().open()
        .clickShowArchivedCategories()
        .verifyCategoryExists(category.name());
  }

  @Category(
      username = "yaro",
      archived = false
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("yaro", "secret")
        .profilePage().open()
        .clickShowArchivedCategories()
        .verifyCategoryExists(category.name());
  }


}
