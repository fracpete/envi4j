/*
 * HeaderField.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.header;

import envi4j.core.EnumHelper;
import envi4j.core.EnumWithCustomParsing;

/**
 * Defines the standard fields in the header.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public enum HeaderField
  implements EnumWithCustomParsing<HeaderField> {

  ACQUISITION_TIME("acquisition time"),
  BAND_NAMES("band names"),
  BANDS("bands", true, HeaderFieldValueType.INTEGER),
  BBL("bbl"),
  BYTE_ORDER("byte order", true, HeaderFieldValueType.ENUM, ByteOrder.class),
  CLASS_LOOKUP("class lookup"),
  CLASS_NAMES("class names"),
  CLASSES("classes"),
  CLOUD_COVER("cloud cover"),
  COMPLEX_FUNCTION("complex function"),
  COORDINATE_SYSTEM_STRING("coordinate system string"),
  DATA_GAIN_VALUES("data gain values"),
  DATA_IGNORE_VALUE("data ignore value"),
  DATA_OFFSET_VALUES("data offset values"),
  DATA_REFLECTANCE_GAIN_VALUES("data reflectance gain values"),
  DATA_REFLECTANCE_OFFSET_VALUES("data reflectance offset values"),
  DATA_TYPE("data type", true, HeaderFieldValueType.ENUM, DataType.class),
  DEFAULT_BANDS("default bands"),
  DEFAULT_STRETCH("default stretch"),
  DEM_BAND("dem band"),
  DEM_FILE("dem file"),
  DESCRIPTION("description"),
  FILE_TYPE("file type", true, HeaderFieldValueType.STRING),
  FWHM("fwhm"),
  GEO_POINTS("geo points"),
  HEADER_OFFSET("header offset", true, HeaderFieldValueType.INTEGER),
  INTERLEAVE("interleave", true, HeaderFieldValueType.ENUM, Interleave.class),
  LINES("lines", true, HeaderFieldValueType.INTEGER),
  MAP_INFO("map info"),
  PIXEL_SIZE("pixel size"),
  PROJECTION_INFO("projection info"),
  READ_PROCEDURES("read procedures"),
  REFLECTANCE_SCALE_FACTOR("reflectance scale factor"),
  RPC_INFO("rpc info"),
  SAMPLES("samples", true, HeaderFieldValueType.INTEGER),
  SECURITY_TAG("security tag"),
  SENSOR_TYPE("sensor type"),
  SOLAR_IRRADIANCE("solar irradiance"),
  SPECTRA_NAMES("spectra names"),
  SUN_AZIMUTH("sun azimuth"),
  SUN_ELEVATION("sun elevation"),
  TIMESTAMP("timestamp"),
  WAVELENGTH("wavelength"),
  WAVELENGTH_UNITS("wavelength units"),
  X_START("x start"),
  Y_START("y start"),
  Z_PLOT_AVERAGE("z plot average"),
  Z_PLOT_RANGE("z plot range"),
  Z_PLOT_TITLES("z plot titles");

  /** the name. */
  private String m_Name;

  /** whether the field is required. */
  private boolean m_Required;

  /** the data type. */
  private HeaderFieldValueType m_ValueType;

  /** the enum type. */
  private Class m_EnumType;

  /**
   * Initializes the optional field as string.
   *
   * @param name	the name of the field
   */
  private HeaderField(String name) {
    this(name, false, HeaderFieldValueType.STRING);
  }

  /**
   * Initializes the field.
   *
   * @param name	the name of the field
   * @param required	true if required field
   * @param valueType 	the type to use for parsing the value
   */
  private HeaderField(String name, boolean required, HeaderFieldValueType valueType) {
    this(name, required, valueType, null);
  }

  /**
   * Initializes the field.
   *
   * @param name	the name of the field
   * @param required	true if required field
   * @param valueType 	the type to use for parsing the value
   * @param enumType 	the enum type, ignored if null
   */
  private HeaderField(String name, boolean required, HeaderFieldValueType valueType, Class enumType) {
    m_Name      = name;
    m_Required  = required;
    m_ValueType = valueType;
    m_EnumType  = enumType;
  }

  /**
   * Returns the name of the field.
   *
   * @return		the name
   */
  public String getName() {
    return m_Name;
  }

  /**
   * Returns whether the field is required.
   *
   * @return		true if required
   */
  public boolean isRequired() {
    return m_Required;
  }

  /**
   * Returns the type of the value.
   *
   * @return		the value type
   */
  public HeaderFieldValueType getValueType() {
    return m_ValueType;
  }

  /**
   * Returns the enum type.
   *
   * @return		the enum type, null if not an enum
   */
  public Class getEnumType() {
    return m_EnumType;
  }

  /**
   * Parses the value string into the appropriate type.
   *
   * @param value	the string to parse
   * @return		the generated object
   */
  public Object parseValue(String value) {
    switch (m_ValueType) {
      case STRING:
        return value;
      case BOOLEAN:
        return Boolean.parseBoolean(value);
      case BYTE:
        return Byte.parseByte(value);
      case INTEGER:
        return Integer.parseInt(value);
      case FLOAT:
        return Float.parseFloat(value);
      case DOUBLE:
        return Double.parseDouble(value);
      case ENUM:
        if (m_EnumType == null)
          throw new IllegalStateException("No enum class defined for: " + m_Name);
        if (EnumWithCustomParsing.class.isAssignableFrom(m_EnumType)) {
          return EnumHelper.getEnumInstance(m_EnumType).parse(value);
        }
        else {
          return Enum.valueOf(m_EnumType, value);
        }
      default:
        throw new IllegalStateException("Unhandled header field value type: " + m_ValueType);
    }
  }

  /**
   * Parses the given string and returns the associated enum.
   *
   * @param s		the string to parse
   * @return		the enum or null if not found
   */
  @Override
  public HeaderField parse(String s) {
    return fromString(s);
  }

  /**
   * Returns the field associated with the type.
   *
   * @param type	the type to get the data type for
   * @return		the type, null if failed to determine
   */
  public static HeaderField fromString(String type) {
    for (HeaderField hf : HeaderField.values()) {
      if (hf.getName().equalsIgnoreCase(type))
	return hf;
    }
    return null;
  }
}
