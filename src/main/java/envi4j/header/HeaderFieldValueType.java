/*
 * HeaderFieldValueType.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.header;

/**
 * Defines the type for the value of a field in the header.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public enum HeaderFieldValueType {
  STRING,
  BOOLEAN,
  BYTE,
  INTEGER,
  FLOAT,
  DOUBLE,
  ENUM,
}
