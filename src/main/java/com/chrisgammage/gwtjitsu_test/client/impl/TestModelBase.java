package com.chrisgammage.gwtjitsu_test.client.impl;

import com.chrisgammage.gwtjitsu.client.impl.ModelBase;
import com.chrisgammage.gwtjitsu.shared.Property;
import com.chrisgammage.gwtjitsu_test.client.TestModel;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 12:03 PM
 */
public abstract class TestModelBase extends ModelBase implements TestModel {

  @Inject
  @Property
  @Named(TestModel.TEST_STRING) String testString;
}
