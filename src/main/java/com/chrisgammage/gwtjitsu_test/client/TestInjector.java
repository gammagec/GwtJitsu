package com.chrisgammage.gwtjitsu_test.client;

import com.chrisgammage.ginjitsu.client.GinExtension;
import com.chrisgammage.ginjitsu.client.GinExtensions;
import com.chrisgammage.ginjitsu.client.JitsuInjector;
import com.chrisgammage.gwtjitsu.client.impl.ModelBase;
import com.chrisgammage.gwtjitsu.client.impl.PresenterBase;
import com.chrisgammage.gwtjitsu.server.ModelGenerator;
import com.chrisgammage.gwtjitsu.server.PresenterGenerator;
import com.google.gwt.inject.client.GinModules;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 11:00 AM
 */
@GinExtensions({
        @GinExtension(clazz = PresenterBase.class,
                generator = PresenterGenerator.class),
        @GinExtension(clazz = ModelBase.class,
                generator = ModelGenerator.class)
})
@GinModules(TestModule.class)
public interface TestInjector extends JitsuInjector {
  TestModel testModel();
  TestPresenter testPresenter();
  CommonModel commonModel();
  SubModel subModel();
}
