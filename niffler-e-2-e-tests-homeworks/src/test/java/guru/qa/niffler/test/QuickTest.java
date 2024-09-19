package guru.qa.niffler.test;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.CategoryJson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;

@ExtendWith(UsersQueueExtension.class)
public class QuickTest {


//  private static final Config CFG = Config.getInstance();
//  private final SpendApiClient spendApiClient = new SpendApiClient();


  @SneakyThrows
  @Test
  void testWithEmptyUser0(@UserType(empty = true) StaticUser user1,
                          @UserType(empty = false) StaticUser user2) {
    Thread.sleep(1000);
    System.out.println(user1);
    System.out.println(user2);
    System.out.println("Test1");
  }

  @SneakyThrows
  @Test
  void testWithNotEmptyUser1(@UserType(empty = false) StaticUser user) {
    Thread.sleep(1000);
    System.out.println(user);
    System.out.println("Test2");
  }

  @SneakyThrows
  @Test
  void testWithNotEmptyUser2(@UserType(empty = false) StaticUser user) {
    Thread.sleep(1000);
    System.out.println(user);
    System.out.println("Test3");
  }

  @SneakyThrows
  @Test
  void testWithNotEmptyUser3() {
    Thread.sleep(1000);
    System.out.println("Test4");
  }



}
