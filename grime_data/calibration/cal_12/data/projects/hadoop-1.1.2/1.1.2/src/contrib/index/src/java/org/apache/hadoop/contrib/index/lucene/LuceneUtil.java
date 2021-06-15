/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.apache.hadoop.contrib.index.lucene;

import java.io.IOException;

import org.apache.lucene.store.Directory;

/**
 * This class copies some methods from Lucene's SegmentInfos since that class
 * is not public.
 */
public final class LuceneUtil {

  static final class IndexFileNames {
    /** Name of the index segment file */
    static final String SEGMENTS = "segments";

    /** Name of the generation reference file name */
    static final String SEGMENTS_GEN = "segments.gen";
  }

  /**
   * Check if the file is a segments_N file
   * @param name
   * @return true if the file is a segments_N file
   */
  public static boolean isSegmentsFile(String name) {
    return name.startsWith(IndexFileNames.SEGMENTS)
        && !name.equals(IndexFileNames.SEGMENTS_GEN);
  }

  /**
   * Check if the file is the segments.gen file
   * @param name
   * @return true if the file is the segments.gen file
   */
  public static boolean isSegmentsGenFile(String name) {
    return name.equals(IndexFileNames.SEGMENTS_GEN);
  }

  /**
   * Get the generation (N) of the current segments_N file in the directory.
   * 
   * @param directory -- directory to search for the latest segments_N file
   */
  public static long getCurrentSegmentGeneration(Directory directory)
      throws IOException {
    String[] files = directory.list();
    if (files == null)
      throw new IOException("cannot read directory " + directory
          + ": list() returned null");
    return getCurrentSegmentGeneration(files);
  }

  /**
   * Get the generation (N) of the current segments_N file from a list of
   * files.
   * 
   * @param files -- array of file names to check
   */
  public static long getCurrentSegmentGeneration(String[] files) {
    if (files == null) {
      return -1;
    }
    long max = -1;
    for (int i = 0; i < files.length; i++) {
      String file = files[i];
      if (file.startsWith(IndexFileNames.SEGMENTS)
          && !file.equals(IndexFileNames.SEGMENTS_GEN)) {
        long gen = generationFromSegmentsFileName(file);
        if (gen > max) {
          max = gen;
        }
      }
    }
    return max;
  }

  /**
   * Parse the generation off the segments file name and return it.
   */
  public static long generationFromSegmentsFileName(String fileName) {
    if (fileName.equals(IndexFileNames.SEGMENTS)) {
      return 0;
    } else if (fileName.startsWith(IndexFileNames.SEGMENTS)) {
      return Long.parseLong(
          fileName.substring(1 + IndexFileNames.SEGMENTS.length()),
          Character.MAX_RADIX);
    } else {
      throw new IllegalArgumentException("fileName \"" + fileName
          + "\" is not a segments file");
    }
  }

}
