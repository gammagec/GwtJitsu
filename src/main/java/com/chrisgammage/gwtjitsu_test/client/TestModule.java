package com.chrisgammage.gwtjitsu_test.client;

import com.google.gwt.inject.client.AbstractGinModule;

import static com.google.inject.name.Names.named;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 11:01 AM
 */
public class TestModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bindConstant().annotatedWith(named(TestModel.TEST_STRING)).to("test");
  }
}
