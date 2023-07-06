/*
 * Interleave.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.header;

import envi4j.core.EnumWithCustomParsing;

/**
 * The types of image files are defined by their interleave type:
 *
 * https://www.l3harrisgeospatial.com/docs/enviimagefiles.html
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public enum Interleave
  implements EnumWithCustomParsing<Interleave> {

  BAND_SEQUENTIAL("bsq", "Band Sequential"),
  BAND_INTERLEAVED_BY_PIXEL("bip", "Band-interleaved-by-pixel"),
  BAND_INTERLEAVED_BY_LINE("bil", "Band-interleaved-by-line");

  /** the type. */
  private String m_Type;

  /** the description. */
  private String m_Description;

  /**
   * Initializes the enum item.
   *
   * @param type	the type (used in header file)
   * @param description	the description
   */
  private Interleave(String type, String description) {
    m_Type        = type;
    m_Description = description;
  }

  /**
   * Returns the type.
   *
   * @return		the type
   */
  public String getType() {
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
   * Parses the given string and returns the associated enum.
   *
   * @param s		the string to parse
   * @return		the enum or null if not found
   */
  @Override
  public Interleave parse(String s) {
    return fromString(s);
  }

  /**
   * Returns the interleave enum item associated with the type string.
   *
   * @param type	the type to get the interleave for
   * @return		the type, null if failed to determine
   */
  public static Interleave fromString(String type) {
    for (Interleave i : Interleave.values()) {
      if (i.getType().equalsIgnoreCase(type))
	return i;
    }
    return null;
  }
}
