package com.chrisgammage.gwtjitsu_test.client;

import com.chrisgammage.gwtjitsu.client.Model;
import com.chrisgammage.gwtjitsu.client.mappers.ArrayToListMapper;
import com.chrisgammage.gwtjitsu.client.mappers.ListToArrayMapper;
import com.chrisgammage.gwtjitsu.shared.Mapper;
import com.chrisgammage.gwtjitsu.shared.Mapping;
import com.chrisgammage.gwtjitsu_test.client.impl.CommonModelImpl;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/16/12
 * Time: 1:16 AM
 */
@ImplementedBy(CommonModelImpl.class)
public interface CommonModel extends Model {

  public static final String SUB_PROPERTIES = "subProperties";
  public static final String STRINGS = "strings";
  public static final String STRINGS2 = "strings2";
  public static final String SOME_STRING = "someString";
  public static final String IDS = "iDs";
  public static final String SOME_BOOL = "someBool";
  public static final String SOME_INT = "someInt";
  public static final String TEST_CONSTRUCT = "testConstruct";
  public static final String TEXT = "text";
  public static final String FILTER_TEST = "filterTest";
  public static final String TEXT_SET = "textSet";
  public static final String TEXT_GET = "textGet";
  public static final String SOMETHING_FIRES_EVENT = "somethingFiresEvent";
  public static final String TEXT_OBS = "textObs";
  public static final String COUNTER = "counter";
  public static final String SUB_MODEL = "subModel";

  @Mapper
  @Mapping(property = "points", mapper = ArrayToListMapper.class)
  void mapTo(CommonDTO dto);

  @Mapper
  @Mapping(property = "points", mapper = ListToArrayMapper.class)
  void mapFrom(CommonDTO dto);

  void setSubModel(SubModel subModel);
  SubModel getSubModel();

  void addSubProperty(SubModel subModel);

  void addString(String str);
  void removeString(String str);
  String getString(int index);
  int getStringsCount();

  void addToStrings2(String str);
  void removeFromStrings2(String str);
  List<String> getStrings2();

  String getSomeString();

  void putIntoIDs(String name, Long id);
  Long getFromIDs(String name);
  void removeFromIDs(String name);
  Map<String, Long> getIDs();

  boolean getSomeBool();

  void setSomeInt(int someInt);
  int getSomeInt();

  void setTestConstruct(String testConstruct);
  String getTestConstruct();

  void setText(String text);
  String getText();

  void setFilterTest(int num);

  void setTextSet(boolean textSet);
  boolean isTextSet();

  boolean isTextGet();

  void setTextObs(String text);
  String getTextObs();

  void setSomethingFiresEvent(int somethingFiresEvent);
  int getSomethingFiresEvent();

  void doSomething();

  int getCounter();
}
