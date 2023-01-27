/*
 * FloatDataset.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package envi4j.dataset;

/**
 * Ancestor for datasets that return float as type.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public interface FloatDataset
  extends Dataset {

  /**
   * Returns the band as matrix.
   *
   * @param band	the band to retrieve
   * @return		the matrix
   */
  public float[][] getBand(int band);
}
