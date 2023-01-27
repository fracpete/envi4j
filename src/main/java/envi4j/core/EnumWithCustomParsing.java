/*
 * EnumWithCustomParsing.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.core;

/**
 * Interface for enums that have a custom parse method.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public interface EnumWithCustomParsing<T extends Enum> {

  /**
   * Parses the given string and returns the associated enum.
   *
   * @param s		the string to parse
   * @return		the enum or null if not found
   */
  public T parse(String s);
}
