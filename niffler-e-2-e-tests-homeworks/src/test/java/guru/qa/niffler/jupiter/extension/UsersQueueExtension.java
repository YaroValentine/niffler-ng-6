package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
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

  @SuppressWarnings("unchecked")
  @Override
  public void beforeTestExecution(ExtensionContext context) {
    Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserType.class))
        .forEach(parameter -> {
          UserType userType = parameter.getAnnotation(UserType.class);
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
              usr -> {
                // Store the user in the context store
                Map<UserType, StaticUser> map = context.getStore(NAMESPACE)
                    .getOrComputeIfAbsent(context.getUniqueId(), key -> new HashMap<UserType, StaticUser>(), Map.class);
                map.put(userType, usr);
              },
              () -> {
                throw new IllegalStateException("Could`t obtain User after 30s.");
              }
          );
        });
  }

  @SuppressWarnings("unchecked")
  @Override
  public void afterTestExecution(ExtensionContext context) {
    // Put User back into queue
    // 1. Retrieve the map of users from the store
    Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
        context.getUniqueId(),
        Map.class
    );

    // 2. Iterate over the map entries and put users back into the appropriate queue
    for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
      if (e.getValue().empty()) {
        EMPTY_USERS.add(e.getValue());
      } else {
        NOT_EMPTY_USERS.add(e.getValue());
      }
  }
    }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
           && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    UserType userType = parameterContext.getParameter().getAnnotation(UserType.class);
    Map<UserType, StaticUser> userMap = (Map<UserType, StaticUser>) extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId());
    return userMap.get(userType);
  }

//  @Override
//  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
//    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId() + parameterContext.getParameter().getName(), StaticUser.class);
//  }


  // User Queue Example with different types of users
  // https://github.com/YaroValentine/niffler-st2/blob/master/niffler-e-2-e-homeworks/src/test/java/niffler/jupiter/extensions/UsersQueueExtension.java
}
