package com.chrisgammage.gwtjitsu_test.client;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/18/12
 * Time: 11:38 PM
 */
public class CommonDTO {

  private String someString;
  private boolean someBool;
  private int someInt;
  private String testConstruct;
  private String text;

  public String getSomeString() {
    return someString;
  }

  public void setSomeString(String someString) {
    this.someString = someString;
  }

  public boolean isSomeBool() {
    return someBool;
  }

  public void setSomeBool(boolean someBool) {
    this.someBool = someBool;
  }

  public int getSomeInt() {
    return someInt;
  }

  public void setSomeInt(int someInt) {
    this.someInt = someInt;
  }

  public String getTestConstruct() {
    return testConstruct;
  }

  public void setTestConstruct(String testConstruct) {
    this.testConstruct = testConstruct;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
