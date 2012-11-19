package com.chrisgammage.gwtjitsu_test.client;

import com.chrisgammage.gwtjitsu.client.Model;
import com.chrisgammage.gwtjitsu_test.client.impl.SubModelImpl;
import com.google.inject.ImplementedBy;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/18/12
 * Time: 9:48 PM
 */
@ImplementedBy(SubModelImpl.class)
public interface SubModel extends Model {

  public static final String PROP = "prop";
  public static final String STRINGS = "strings";

  void setProp(String property);
  String getProp();

  void addString(String str);
  void removeString(String str);
}
