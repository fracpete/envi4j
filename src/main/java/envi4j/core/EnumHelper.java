/*
 * EnumHelper.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.core;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Helper class for enums.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class EnumHelper {

  /**
   * Tries to instantiate an instance of the enumeration type.
   *
   * @param cls		the enum class
   * @return		the instance or null if failed to instantiate
   */
  public static EnumWithCustomParsing getEnumInstance(Class cls) {
    EnumWithCustomParsing	result;
    Method method;
    Object			values;

    try {
      method = cls.getMethod("values", new Class[0]);
      values = method.invoke(null, new Object[0]);
      result = (EnumWithCustomParsing) Array.get(values, 0);
    }
    catch (Exception e) {
      result = null;
      e.printStackTrace();
    }

    return result;
  }

}
