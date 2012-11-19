package com.chrisgammage.gwtjitsu.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/15/12
 * Time: 12:31 PM
 */
@Target(ElementType.METHOD)
public @interface AfterSet {
  String value();
}
