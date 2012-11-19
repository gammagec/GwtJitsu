package com.chrisgammage.gwtjitsu.client.mappers;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/19/12
 * Time: 12:26 AM
 */
public class ListToArrayMapper {
  static <T> T[] map(List<T> objects) {
    return (T[])objects.toArray(new Object[objects.size()]);
  }
}
