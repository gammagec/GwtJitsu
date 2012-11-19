package com.chrisgammage.gwtjitsu.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/31/12
 * Time: 4:36 PM
 */
@Target(ElementType.FIELD)
public @interface PresenterModel {
  String value() default "";
}
