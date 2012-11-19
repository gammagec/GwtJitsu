package com.chrisgammage.gwtjitsu.shared;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 11/1/12
 * Time: 5:48 PM
 */
public @interface AfterSubModelCollectionPropertyChanged {
  String subModel();
  String collection();
  String property();
}
