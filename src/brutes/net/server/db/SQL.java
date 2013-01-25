package brutes.net.server.db;

import java.lang.annotation.*;

/**
 *
 * @author Thiktak
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SQL {

    public String name();

    public String type();
}