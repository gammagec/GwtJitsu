package com.chrisgammage.gwtjitsu.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/18/12
 * Time: 1:36 AM
 */
@Target(ElementType.METHOD)
public @interface AfterAdd {
  String value();
}
