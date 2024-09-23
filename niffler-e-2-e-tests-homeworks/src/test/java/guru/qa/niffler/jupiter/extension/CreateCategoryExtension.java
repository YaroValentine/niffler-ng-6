package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.Utils;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CreateCategoryExtension implements
    BeforeEachCallback,
    AfterEachCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
        .ifPresent(annotation -> {
          CategoryJson category = new CategoryJson(
              null,
              Utils.generateRandomString(10),
              annotation.username(),
              annotation.archived()
          );

          CategoryJson createdCategory = spendApiClient.addCategory(category);

          context.getStore(NAMESPACE).put(
              context.getUniqueId(),
              createdCategory
          );
        });
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    // Clean Up:
    // archive category after test if not archived
    CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
    if (category != null && !category.archived()) {
      CategoryJson updatedCategory = new CategoryJson(
          category.id(),
          category.name(),
          category.username(),
          true
      );
      spendApiClient.updateCategory(updatedCategory);
    }
  }



  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}
