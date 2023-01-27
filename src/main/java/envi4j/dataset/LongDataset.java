/*
 * LongDataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

/**
 * Interface for datasets that return long as type.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public interface LongDataset
  extends Dataset {

  /**
   * Returns the band as matrix.
   *
   * @param band	the band to retrieve
   * @return		the matrix
   */
  public long[][] getBand(int band);
}
