package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {

  private final SelenideElement
      header = $(".form__paragraph"),
      usernameFld = $("#username"),
      passwordFld = $("#password"),
      passwordSubmitFld = $("#passwordSubmit"),
      signUpBtn = $("button[type='submit']"),
      formError = $(".form__error"),
      youHaveRegistered = $(byText("Congratulations! You've registered!"));

  public RegistrationPage checkThatPageLoaded() {
    header.shouldHave(text("Registration form"));
    return this;
  }

  public RegistrationPage fillRegistrationForm(String username, String password, String passwordSubmit) {
    setUsername(username);
    setPassword(password);
    setPasswordSubmit(passwordSubmit);
    return this;
  }

  public RegistrationPage submitRegistration() {
    signUpBtn.click();
    return this;
  }

  public void setPasswordSubmit(String passwordSubmit) {
    passwordSubmitFld.val(passwordSubmit);
  }

  public void setPassword(String password) {
    passwordFld.val(password);
  }

  public void setUsername(String username) {
    usernameFld.val(username);
  }

  public RegistrationPage checkErrorMessage(String expectedMessage) {
    formError.shouldHave(text(expectedMessage));
    return this;
  }

  public void checkYouHaveRegisteredIsDisplayed() {
    youHaveRegistered.shouldBe(visible);
  }

  public void checkUsernameAlreadyExistsErrorIsDisplayed(String username) {
    formError.shouldHave(text("Username `" + username + "` already exists"));
  }
}
