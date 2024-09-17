package guru.qa.niffler.jupiter.extensions;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotations.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class CreateSpendingExtension implements
    BeforeEachCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Spending.class)
        .ifPresent(annotation -> {
          SpendJson spend = new SpendJson(
              null,
              new Date(),
              new CategoryJson(
                  null,
                  annotation.category(),
                  annotation.username(),
                  false
              ),
              CurrencyValues.RUB,
              annotation.amount(),
              annotation.description(),
              annotation.username()
          );

          SpendJson createdSpend = spendApiClient.createSpend(spend);

          context.getStore(NAMESPACE).put(
              context.getUniqueId(),
              createdSpend
          );
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
  }

  @Override
  public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
    return context.getStore(CreateSpendingExtension.NAMESPACE).get(context.getUniqueId(), SpendJson.class);
  }

}
