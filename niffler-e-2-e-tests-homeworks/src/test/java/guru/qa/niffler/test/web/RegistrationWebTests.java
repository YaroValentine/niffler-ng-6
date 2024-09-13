package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegistrationWebTests {

  private static final Config CFG = Config.getInstance();

  @Test
  void shouldRegisterNewUser() {
    final String username = new Faker().name().username();
    final String password = "secret";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .clickCreateNewAccount()
        .fillRegistrationForm(username, password, password)
        .submitRegistration()
        .checkYouHaveRegisteredIsDisplayed();
  }

  @Test
  void shouldNotRegisterUserWithExistingUsername() {
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
  }

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {

  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {

  }
}
