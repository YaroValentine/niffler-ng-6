package guru.qa.niffler.jupiter.annotations;

import guru.qa.niffler.jupiter.extensions.CreateCategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({
    CreateCategoryExtension.class
})
public @interface Category {

  String username();

  boolean archived();

}
