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
package org.apache.hadoop.mapred.lib;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * An {@link InputSplit} that tags another InputSplit with extra data for use by
 * {@link DelegatingInputFormat}s and {@link DelegatingMapper}s.
 */
class TaggedInputSplit implements Configurable, InputSplit {

  private Class<? extends InputSplit> inputSplitClass;

  private InputSplit inputSplit;

  private Class<? extends InputFormat> inputFormatClass;

  private Class<? extends Mapper> mapperClass;

  private Configuration conf;

  public TaggedInputSplit() {
    // Default constructor.
  }

  /**
   * Creates a new TaggedInputSplit.
   * 
   * @param inputSplit The InputSplit to be tagged
   * @param conf The configuration to use
   * @param inputFormatClass The InputFormat class to use for this job
   * @param mapperClass The Mapper class to use for this job
   */
  public TaggedInputSplit(InputSplit inputSplit, Configuration conf,
      Class<? extends InputFormat> inputFormatClass,
      Class<? extends Mapper> mapperClass) {
    this.inputSplitClass = inputSplit.getClass();
    this.inputSplit = inputSplit;
    this.conf = conf;
    this.inputFormatClass = inputFormatClass;
    this.mapperClass = mapperClass;
  }

  /**
   * Retrieves the original InputSplit.
   * 
   * @return The InputSplit that was tagged
   */
  public InputSplit getInputSplit() {
    return inputSplit;
  }

  /**
   * Retrieves the InputFormat class to use for this split.
   * 
   * @return The InputFormat class to use
   */
  public Class<? extends InputFormat> getInputFormatClass() {
    return inputFormatClass;
  }

  /**
   * Retrieves the Mapper class to use for this split.
   * 
   * @return The Mapper class to use
   */
  public Class<? extends Mapper> getMapperClass() {
    return mapperClass;
  }

  public long getLength() throws IOException {
    return inputSplit.getLength();
  }

  public String[] getLocations() throws IOException {
    return inputSplit.getLocations();
  }

  @SuppressWarnings("unchecked")
  public void readFields(DataInput in) throws IOException {
    inputSplitClass = (Class<? extends InputSplit>) readClass(in);
    inputSplit = (InputSplit) ReflectionUtils
       .newInstance(inputSplitClass, conf);
    inputSplit.readFields(in);
    inputFormatClass = (Class<? extends InputFormat>) readClass(in);
    mapperClass = (Class<? extends Mapper>) readClass(in);
  }

  private Class<?> readClass(DataInput in) throws IOException {
    String className = Text.readString(in);
    try {
      return conf.getClassByName(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("readObject can't find class", e);
    }
  }

  public void write(DataOutput out) throws IOException {
    Text.writeString(out, inputSplitClass.getName());
    inputSplit.write(out);
    Text.writeString(out, inputFormatClass.getName());
    Text.writeString(out, mapperClass.getName());
  }

  public Configuration getConf() {
    return conf;
  }

  public void setConf(Configuration conf) {
    this.conf = conf;
  }

}
