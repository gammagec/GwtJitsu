package com.chrisgammage.gwtjitsu.client;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/3/12
 * Time: 3:12 PM
 */
public interface HasProperties extends HasPropertyChangeHandlers {

  List<String> getPropertyNames();

  void setProperty(String property, Object value);

  void setProperty(String property, Object value, boolean fireEvents);

  Object getProperty(String property);
}
