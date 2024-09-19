package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  public record StaticUser(String username, String password, boolean empty) {
  }

  private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> NOT_EMPTY_USERS = new ConcurrentLinkedQueue<>();

  static {
    EMPTY_USERS.add(new StaticUser("lapa", "secret", true));
    EMPTY_USERS.add(new StaticUser("lena", "secret", true));
    NOT_EMPTY_USERS.add(new StaticUser("yaro", "secret", false));
    NOT_EMPTY_USERS.add(new StaticUser("olga", "secret", false));
    NOT_EMPTY_USERS.add(new StaticUser("dima", "secret", false));
    NOT_EMPTY_USERS.add(new StaticUser("dan", "secret", false));
    NOT_EMPTY_USERS.add(new StaticUser("jack", "secret", false));
  }

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface UserType {
    boolean empty() default true;
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserType.class))
        .findFirst()
        .map(parameter -> parameter.getAnnotation(UserType.class))
        .ifPresent(userType -> {
          // Get proper type of user from queue
          Optional<StaticUser> user = Optional.empty();
          StopWatch sw = StopWatch.createStarted(); // Wait for user for 30s
          while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
            user = userType.empty()
                ? Optional.ofNullable(EMPTY_USERS.poll())
                : Optional.ofNullable(NOT_EMPTY_USERS.poll());
          }
          // Reset Allure test execution time to the time when User was pulled from queue
          Allure.getLifecycle().updateTestCase(testCase ->
              testCase.setStart(new Date().getTime())
          );
          user.ifPresentOrElse(
              usr ->
                  context.getStore(NAMESPACE).put(
                      context.getUniqueId(),
                      usr
                  ),
              () -> {
                throw new IllegalStateException("Could`t obtain User after 30s.");
              }
          );
        });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    // Put User back into queue
    StaticUser user = context.getStore(NAMESPACE).get(
        context.getUniqueId(),
        StaticUser.class
    );
    if (user.empty()) {
      EMPTY_USERS.add(user);
    } else {
      NOT_EMPTY_USERS.add(user);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), StaticUser.class);
  }

  // User Queue Example with different types of users
  // https://github.com/YaroValentine/niffler-st2/blob/master/niffler-e-2-e-homeworks/src/test/java/niffler/jupiter/extensions/UsersQueueExtension.java
}
