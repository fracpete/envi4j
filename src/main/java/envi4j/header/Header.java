/*
 * Header.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.header;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The header-information of an ENVI file.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Header
  implements Serializable {

  /** whether to be quiet in parsing. */
  protected boolean m_Quiet;

  /** the key/value pairs. */
  protected Map<String,String> m_Raw;

  /** the interpreted data. */
  protected Map<HeaderField,Object> m_Values;

  /** the number of lines. */
  protected int m_Lines;

  /** the number of samples. */
  protected int m_Samples;

  /** the number of bands. */
  protected int m_Bands;

  /** the data type. */
  protected DataType m_DataType;

  /** the byte order. */
  protected ByteOrder m_ByteOrder;

  /** the interleave type. */
  protected Interleave m_Interleave;

  /**
   * Initializes the header using the supplied information.
   *
   * @param info	the info string to parse
   * @param quiet 	whether to suppress warnings/errors
   */
  public Header(String info, boolean quiet) {
    m_Quiet      = quiet;
    m_Raw        = parse(info);
    m_Values     = interpret(m_Raw);
    m_Lines      = -1;
    m_Samples    = -1;
    m_Bands      = -1;
    m_DataType   = null;
    m_ByteOrder  = null;
    m_Interleave = null;
    check(m_Values);
  }

  /**
   * Returns whether the data was parsed in quiet mode.
   *
   * @return		true if warnings/errors suppressed
   */
  public boolean isQuiet() {
    return m_Quiet;
  }

  /**
   * Parses the string with the data and turns it into key/value pairs.
   *
   * @param info	the data to parse
   * @return		the key/value pairs
   */
  protected Map<String,String> parse(String info) {
    Map<String,String>	result;
    String[]		lines;
    boolean 		multi;
    String		key;
    String		value;

    result = new HashMap<>();
    lines  = info.split("\n");
    multi  = false;
    key    = null;
    value  = null;
    for (String line: lines) {
      line = line.trim();
      if (line.equals("ENVI"))
	continue;
      if (multi) {
	value += "\n" + line;
	if (line.endsWith("}")) {
	  result.put(key, value);
	  key   = null;
	  value = null;
	  multi = false;
	}
      }
      else {
	if (line.contains("=")) {
	  key   = line.substring(0, line.indexOf('=')).trim();
	  value = line.substring(line.indexOf('=') + 1).trim();
	  multi = value.startsWith("{") && !value.endsWith("}");
	  if (!multi) {
	    result.put(key, value);
	    key   = null;
	    value = null;
	  }
	}
	else {
	  if (!m_Quiet)
	    System.err.println("Failed to parse as key=value: " + line);
	}
      }
    }

    return result;
  }

  /**
   * Interprets the key/value pairs.
   *
   * @param data	the pairs to use
   */
  protected Map<HeaderField,Object> interpret(Map<String,String> data) {
    Map<HeaderField,Object>	result;
    HeaderField			field;
    String			value;

    result = new HashMap<>();
    for (String key: data.keySet()) {
      field = HeaderField.fromString(key);
      value = data.get(key);
      if (field == null) {
	if (!m_Quiet)
	  System.err.println("WARNING: non-standard header field '" + key + "'");
      }
      else {
	try {
	  result.put(field, field.parseValue(value));
	}
	catch (Exception e) {
	  if (!m_Quiet) {
	    System.err.println("Failed to parse the value of field '" + field.getName() + "': " + value);
	    e.printStackTrace();
	  }
	}
      }
    }

    return result;
  }

  /**
   * Checks whether the data is valid.
   *
   * @param data	the data to check
   */
  protected void check(Map<HeaderField,Object> data) {
    for (HeaderField field: HeaderField.values()) {
      if (field.isRequired()) {
	if (!data.containsKey(field)) {
	  if (!m_Quiet)
	    System.err.println("Missing required field: " + field.getName());
	}
      }
    }
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public Object getObject(HeaderField field) {
    return m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public Object getObject(HeaderField field, Object defValue) {
    return m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public String getString(HeaderField field) {
    return (String) m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public String getString(HeaderField field, String defValue) {
    return (String) m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public Boolean getBoolean(HeaderField field) {
    return (Boolean) m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public Boolean getBoolean(HeaderField field, boolean defValue) {
    return (Boolean) m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public Byte getByte(HeaderField field) {
    return (Byte) m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public Byte getByte(HeaderField field, byte defValue) {
    return (Byte) m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public Integer getInteger(HeaderField field) {
    return (Integer) m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public Integer getInteger(HeaderField field, int defValue) {
    return (Integer) m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public Long getLong(HeaderField field) {
    return (Long) m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public Long getLong(HeaderField field, long defValue) {
    return (Long) m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public Float getFloat(HeaderField field) {
    return (Float) m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public Float getFloat(HeaderField field, float defValue) {
    return (Float) m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public Double getDouble(HeaderField field) {
    return (Double) m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public Double getDouble(HeaderField field, double defValue) {
    return (Double) m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @return		the associated value, null if not available
   */
  public <T> T get(HeaderField field, Class<T> cls) {
    return (T) m_Values.get(field);
  }

  /**
   * Returns the specified value.
   *
   * @param field 	the field to retrieve
   * @param defValue 	the default value
   * @return		the associated value, default value if not available
   */
  public <T> T get(HeaderField field, T defValue, Class<T> cls) {
    return (T) m_Values.getOrDefault(field, defValue);
  }

  /**
   * Returns the underlying data type in the data.
   *
   * @return		the data type
   */
  public DataType getDataType() {
    if (m_DataType == null)
      m_DataType = get(HeaderField.DATA_TYPE, DataType.class);
    return m_DataType;
  }

  /**
   * Returns the underlying byte order in the data.
   *
   * @return		the byte order
   */
  public ByteOrder getByteOrder() {
    if (m_ByteOrder == null)
      m_ByteOrder = get(HeaderField.BYTE_ORDER, ByteOrder.class);
    return m_ByteOrder;
  }

  /**
   * Returns the underlying interleave type in the data.
   *
   * @return		the interleave type
   */
  public Interleave getInterleave() {
    if (m_Interleave == null)
      m_Interleave = get(HeaderField.INTERLEAVE, Interleave.class);
    return m_Interleave;
  }

  /**
   * Returns the number of cols/samples.
   *
   * @return		the number of samples
   */
  public int getSamples() {
    if (m_Samples == -1)
      m_Samples = getInteger(HeaderField.SAMPLES);
    return m_Samples;
  }

  /**
   * Returns the number of rows/lines.
   *
   * @return		the number of lines
   */
  public int getLines() {
    if (m_Lines == -1)
      m_Lines = getInteger(HeaderField.LINES);
    return m_Lines;
  }

  /**
   * Returns the number of bands.
   *
   * @return		the number of bands
   */
  public int getBands() {
    if (m_Bands == -1)
      m_Bands = getInteger(HeaderField.BANDS);
    return m_Bands;
  }

  /**
   * Returns the underlying key/value pairs.
   *
   * @return		the data
   */
  public String toString() {
    StringBuilder	result;
    List<String> 	keys;

    result = new StringBuilder();
    keys   = new ArrayList<>(m_Raw.keySet());
    Collections.sort(keys);

    for (String key: keys)
      result.append(key).append(" = ").append(m_Raw.get(key)).append("\n");

    return result.toString();
  }

  /**
   * Reads the header file and returns the generated header information.
   * Uses quiet mode.
   *
   * @param input	the file to read
   * @return		the header, null if failed to read
   */
  public static Header read(File input) {
    return read(input, true);
  }

  /**
   * Reads the header file and returns the generated header information.
   *
   * @param input	the file to read
   * @param quiet 	whether to suppress warnings/errors
   * @return		the header, null if failed to read
   */
  public static Header read(File input, boolean quiet) {
    try {
      return new Header(Files.readString(input.toPath()), quiet);
    }
    catch (Exception e) {
      if (!quiet) {
	System.err.println("Failed to read ENVI header: " + input);
	e.printStackTrace();
      }
      return null;
    }
  }
}
