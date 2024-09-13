package guru.qa.niffler.page;

public class BasePage {

  private LoginPage loginPage;
  private MainPage mainPage;
  private ProfilePage profilePage;
  private RegistrationPage registrationPage;
  private EditSpendingPage editSpendingPage;

  public LoginPage loginPage() {
    return loginPage == null ? loginPage = new LoginPage() : loginPage;
  }

  public MainPage mainPage() {
    return mainPage == null ? mainPage = new MainPage() : mainPage;
  }

  public ProfilePage profilePage() {
    return profilePage == null ? profilePage = new ProfilePage() : profilePage;
  }

  public RegistrationPage registrationPage() {
    return registrationPage == null ? registrationPage = new RegistrationPage() : registrationPage;
  }

  public EditSpendingPage editSpendingPage() {
    return editSpendingPage == null ? editSpendingPage = new EditSpendingPage() : editSpendingPage;
  }

}
