package com.chrisgammage.gwtjitsu.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/18/12
 * Time: 10:00 AM
 */
@Target(ElementType.METHOD)
public @interface BeforeGet {
  String value();
}
