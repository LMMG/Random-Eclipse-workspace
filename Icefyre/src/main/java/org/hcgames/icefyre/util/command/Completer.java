package org.hcgames.icefyre.util.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command Framework - Completer <br>
 * The completer annotation used to designate methods as duel completers. All
 * methods should have a single CommandArgs argument and return a String List
 * object
 *
 * @author minnymin3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Completer {

    String name();

    String[] aliases() default {};

}
