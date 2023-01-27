/*
 * ByteOrder.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.header;

import envi4j.core.EnumWithCustomParsing;

/**
 * Defines the byte order in the data.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public enum ByteOrder
  implements EnumWithCustomParsing<ByteOrder> {

  LITTLE_ENDIAN(0, "Intel: least significant byte first (LSF) data (DEC and MS-DOS systems)"),
  BIG_ENDIAN(1, "IEEE: most significant byte first (MSF) data (all other platforms)");

  /** the type. */
  private int m_Type;

  /** the description. */
  private String m_Description;

  /**
   * Initializes the
   * @param type
   * @param description
   */
  private ByteOrder(int type, String description) {
    m_Type        = type;
    m_Description = description;
  }

  /**
   * Returns the type.
   *
   * @return		the type
   */
  public int getType() {
    return m_Type;
  }

  /**
   * Returns the description.
   *
   * @return		the description
   */
  public String getDescription() {
    return m_Description;
  }

  /**
   * Parses the given string (ie data type integer) and returns the associated enum.
   *
   * @param s		the string to parse
   * @return		the enum or null if not found
   */
  @Override
  public ByteOrder parse(String s) {
    return fromType(Integer.parseInt(s));
  }

  /**
   * Returns the byte order enum item associated with the type integer.
   *
   * @param type	the type to get the byte order for
   * @return		the type, null if failed to determine
   */
  public static ByteOrder fromType(int type) {
    for (ByteOrder bo : ByteOrder.values()) {
      if (bo.getType() == type)
        return bo;
    }
    return null;
  }
}
