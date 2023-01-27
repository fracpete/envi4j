/*
 * DataType.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.header;

import envi4j.core.EnumWithCustomParsing;

/**
 * Defines the supported data types.
 *
 * See "data type" in: https://www.l3harrisgeospatial.com/docs/enviheaderfiles.html
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public enum DataType
  implements EnumWithCustomParsing<DataType> {

  UINT8(1, "Byte: 8-bit unsigned integer", 1),
  UINT16(12, "Unsigned integer: 16-bit", 2),
  UINT32(13, "Unsigned long integer: 32-bit", 4),
  UINT64(15, "64-bit unsigned long integer (unsigned)", 8),
  INT16(2, "Integer: 16-bit signed integer", 2),
  INT32(3, "Long: 32-bit signed integer", 4),
  INT64(14, "64-bit long integer (signed)", 8),
  FLOAT32(4, "Floating-point: 32-bit single-precision", 4),
  FLOAT64(5, "Double-precision: 64-bit double-precision floating-point", 8),
  COMPLEX32(6, "Complex: Real-imaginary pair of single-precision floating-point", 8),
  COMPLEX64(9, "Double-precision complex: Real-imaginary pair of double precision floating-point", 16);

  /** the type. */
  private int m_Type;

  /** the description. */
  private String m_Description;

  /** the number in bytes. */
  private int m_Size;

  /**
   * Initializes the enum item.
   *
   * @param type	the associated type
   */
  private DataType(int type, String description, int size) {
    m_Type        = type;
    m_Description = description;
    m_Size        = size;
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
   * Returns the size in bytes.
   *
   * @return		the size
   */
  public int getSize() {
    return m_Size;
  }

  /**
   * Parses the given string (ie data type integer) and returns the associated enum.
   *
   * @param s		the string to parse
   * @return		the enum or null if not found
   */
  @Override
  public DataType parse(String s) {
    return fromType(Integer.parseInt(s));
  }

  /**
   * Returns the data type enum item associated with the type integer.
   *
   * @param type	the type to get the data type for
   * @return		the type, null if failed to determine
   */
  public static DataType fromType(int type) {
    for (DataType dt: DataType.values()) {
      if (dt.getType() == type)
	return dt;
    }
    return null;
  }
}
