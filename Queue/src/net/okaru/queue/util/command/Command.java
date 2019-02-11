package net.okaru.queue.util.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command
{
  String name();
  
  String permission() default "";
  
  String[] aliases() default {};
  
  String description() default "";
  
  String usage() default "";
  
  boolean inGameOnly() default true;
  
  boolean isAdminOnly() default false;
}
