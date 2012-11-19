package com.chrisgammage.gwtjitsu_test.client;

import com.chrisgammage.gwtjitsu.client.Model;
import com.chrisgammage.gwtjitsu_test.client.impl.TestModelBase;
import com.google.inject.ImplementedBy;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 12:02 PM
 */
@ImplementedBy(TestModelBase.class)
public interface TestModel extends Model {

  public static final String TEST_STRING = "testString";

  String getTestString();
  void setTestString(String testString);
}
