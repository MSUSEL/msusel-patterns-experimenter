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
package org.apache.hadoop.metrics2.sink;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsException;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsSink;
import org.apache.hadoop.metrics2.MetricsTag;

/**
 * 
 */
public class FileSink implements MetricsSink {

  private static final String FILENAME_KEY = "filename";
  private PrintWriter writer;

  @Override
  public void init(SubsetConfiguration conf) {
    String filename = conf.getString(FILENAME_KEY);
    try {
      writer = filename == null
          ? new PrintWriter(new BufferedOutputStream(System.out))
          : new PrintWriter(new FileWriter(new File(filename), true));
    }
    catch (Exception e) {
      throw new MetricsException("Error creating "+ filename, e);
    }
  }

  @Override
  public void putMetrics(MetricsRecord record) {
    writer.print(record.timestamp());
    writer.print(" ");
    writer.print(record.context());
    writer.print(".");
    writer.print(record.name());
    String separator = ": ";
    for (MetricsTag tag : record.tags()) {
      writer.print(separator);
      separator = ", ";
      writer.print(tag.name());
      writer.print("=");
      writer.print(String.valueOf(tag.value()));
    }
    for (Metric metric : record.metrics()) {
      writer.print(separator);
      separator = ", ";
      writer.print(metric.name());
      writer.print("=");
      writer.print(metric.value());
    }
    writer.println();
  }

  @Override
  public void flush() {
    writer.flush();
  }

}
