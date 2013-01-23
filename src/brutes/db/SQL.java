/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.db;

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