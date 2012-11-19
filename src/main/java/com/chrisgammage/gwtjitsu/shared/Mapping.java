package com.chrisgammage.gwtjitsu.shared;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/19/12
 * Time: 12:23 AM
 */
public @interface Mapping {
  String property();
  Class<?> mapper();
}
