package com.chrisgammage.gwtjitsu_test.client.impl;

import com.chrisgammage.gwtjitsu.client.impl.ModelBase;
import com.chrisgammage.gwtjitsu.shared.Collection;
import com.chrisgammage.gwtjitsu.shared.Property;
import com.chrisgammage.gwtjitsu_test.client.SubModel;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/28/12
 * Time: 10:57 AM
 */
public abstract class SubModelImpl extends ModelBase implements SubModel {

  @Property
  protected String prop;

  @Collection(singular = "string")
  protected ArrayList<String> strings = new ArrayList<String>();
}
