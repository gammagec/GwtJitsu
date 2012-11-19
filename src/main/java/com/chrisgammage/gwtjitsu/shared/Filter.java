package com.chrisgammage.gwtjitsu.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/15/12
 * Time: 3:28 PM
 */
@Target(ElementType.METHOD)
public @interface Filter {
  String value();
}
