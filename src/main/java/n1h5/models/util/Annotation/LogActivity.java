package n1h5.models.util.Annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogActivity {
    String action();      // VD: "CREATE", "UPDATE", "DELETE"
    String entityName();  // VD: "CarSeries", "Product"
}