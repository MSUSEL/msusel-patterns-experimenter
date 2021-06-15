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
package org.apache.hadoop.streaming.io;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.typedbytes.TypedBytesWritable;

/**
 * This class is used to resolve a string identifier into the required IO
 * classes. By extending this class and pointing the property
 * <tt>stream.io.identifier.resolver.class</tt> to this extension, additional
 * IO classes can be added by external code.
 */
public class IdentifierResolver {

  // note that the identifiers are case insensitive
  public static final String TEXT_ID = "text";
  public static final String RAW_BYTES_ID = "rawbytes";
  public static final String TYPED_BYTES_ID = "typedbytes";
  
  private Class<? extends InputWriter> inputWriterClass = null;
  private Class<? extends OutputReader> outputReaderClass = null;
  private Class outputKeyClass = null;
  private Class outputValueClass = null;
  
  /**
   * Resolves a given identifier. This method has to be called before calling
   * any of the getters.
   */
  public void resolve(String identifier) {
    if (identifier.equalsIgnoreCase(RAW_BYTES_ID)) {
      setInputWriterClass(RawBytesInputWriter.class);
      setOutputReaderClass(RawBytesOutputReader.class);
      setOutputKeyClass(BytesWritable.class);
      setOutputValueClass(BytesWritable.class);
    } else if (identifier.equalsIgnoreCase(TYPED_BYTES_ID)) {
      setInputWriterClass(TypedBytesInputWriter.class);
      setOutputReaderClass(TypedBytesOutputReader.class);
      setOutputKeyClass(TypedBytesWritable.class);
      setOutputValueClass(TypedBytesWritable.class);
    } else { // assume TEXT_ID
      setInputWriterClass(TextInputWriter.class);
      setOutputReaderClass(TextOutputReader.class);
      setOutputKeyClass(Text.class);
      setOutputValueClass(Text.class);
    }
  }
  
  /**
   * Returns the resolved {@link InputWriter} class.
   */
  public Class<? extends InputWriter> getInputWriterClass() {
    return inputWriterClass;
  }

  /**
   * Returns the resolved {@link OutputReader} class.
   */
  public Class<? extends OutputReader> getOutputReaderClass() {
    return outputReaderClass;
  }
  
  /**
   * Returns the resolved output key class.
   */
  public Class getOutputKeyClass() {
    return outputKeyClass;
  }

  /**
   * Returns the resolved output value class.
   */
  public Class getOutputValueClass() {
    return outputValueClass;
  }
  
  
  /**
   * Sets the {@link InputWriter} class.
   */
  protected void setInputWriterClass(Class<? extends InputWriter>
    inputWriterClass) {
    this.inputWriterClass = inputWriterClass;
  }
  
  /**
   * Sets the {@link OutputReader} class.
   */
  protected void setOutputReaderClass(Class<? extends OutputReader>
    outputReaderClass) {
    this.outputReaderClass = outputReaderClass;
  }
  
  /**
   * Sets the output key class class.
   */
  protected void setOutputKeyClass(Class outputKeyClass) {
    this.outputKeyClass = outputKeyClass;
  }
  
  /**
   * Sets the output value class.
   */
  protected void setOutputValueClass(Class outputValueClass) {
    this.outputValueClass = outputValueClass;
  }

}
