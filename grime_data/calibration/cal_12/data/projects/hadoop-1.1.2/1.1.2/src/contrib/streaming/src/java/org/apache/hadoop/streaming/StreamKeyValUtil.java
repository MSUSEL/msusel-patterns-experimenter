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
package org.apache.hadoop.streaming;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

public class StreamKeyValUtil {

  /**
   * Find the first occured tab in a UTF-8 encoded string
   * @param utf a byte array containing a UTF-8 encoded string
   * @param start starting offset
   * @param length no. of bytes
   * @return position that first tab occures otherwise -1
   */
  public static int findTab(byte [] utf, int start, int length) {
    for(int i=start; i<(start+length); i++) {
      if (utf[i]==(byte)'\t') {
        return i;
      }
    }
    return -1;      
  }
  /**
   * Find the first occured tab in a UTF-8 encoded string
   * @param utf a byte array containing a UTF-8 encoded string
   * @return position that first tab occures otherwise -1
   */
  public static int findTab(byte [] utf) {
    return org.apache.hadoop.util.UTF8ByteArrayUtils.findNthByte(utf, 0, 
        utf.length, (byte)'\t', 1);
  }

  /**
   * split a UTF-8 byte array into key and value 
   * assuming that the delimilator is at splitpos. 
   * @param utf utf-8 encoded string
   * @param start starting offset
   * @param length no. of bytes
   * @param key contains key upon the method is returned
   * @param val contains value upon the method is returned
   * @param splitPos the split pos
   * @param separatorLength the length of the separator between key and value
   * @throws IOException
   */
  public static void splitKeyVal(byte[] utf, int start, int length, 
                                 Text key, Text val, int splitPos,
                                 int separatorLength) throws IOException {
    if (splitPos<start || splitPos >= (start+length))
      throw new IllegalArgumentException("splitPos must be in the range " +
                                         "[" + start + ", " + (start+length) + "]: " + splitPos);
    int keyLen = (splitPos-start);
    byte [] keyBytes = new byte[keyLen];
    System.arraycopy(utf, start, keyBytes, 0, keyLen);
    int valLen = (start+length)-splitPos-separatorLength;
    byte [] valBytes = new byte[valLen];
    System.arraycopy(utf, splitPos+separatorLength, valBytes, 0, valLen);
    key.set(keyBytes);
    val.set(valBytes);
  }

  /**
   * split a UTF-8 byte array into key and value 
   * assuming that the delimilator is at splitpos. 
   * @param utf utf-8 encoded string
   * @param start starting offset
   * @param length no. of bytes
   * @param key contains key upon the method is returned
   * @param val contains value upon the method is returned
   * @param splitPos the split pos
   * @throws IOException
   */
  public static void splitKeyVal(byte[] utf, int start, int length, 
                                 Text key, Text val, int splitPos) throws IOException {
    splitKeyVal(utf, start, length, key, val, splitPos, 1);
  }
  

  /**
   * split a UTF-8 byte array into key and value 
   * assuming that the delimilator is at splitpos. 
   * @param utf utf-8 encoded string
   * @param key contains key upon the method is returned
   * @param val contains value upon the method is returned
   * @param splitPos the split pos
   * @param separatorLength the length of the separator between key and value
   * @throws IOException
   */
  public static void splitKeyVal(byte[] utf, Text key, Text val, int splitPos, 
                                 int separatorLength) 
    throws IOException {
    splitKeyVal(utf, 0, utf.length, key, val, splitPos, separatorLength);
  }

  /**
   * split a UTF-8 byte array into key and value 
   * assuming that the delimilator is at splitpos. 
   * @param utf utf-8 encoded string
   * @param key contains key upon the method is returned
   * @param val contains value upon the method is returned
   * @param splitPos the split pos
   * @throws IOException
   */
  public static void splitKeyVal(byte[] utf, Text key, Text val, int splitPos) 
    throws IOException {
    splitKeyVal(utf, 0, utf.length, key, val, splitPos, 1);
  }
  
  /**
   * Read a utf8 encoded line from a data input stream. 
   * @param lineReader LineReader to read the line from.
   * @param out Text to read into
   * @return number of bytes read 
   * @throws IOException
   */
  public static int readLine(LineReader lineReader, Text out) 
  throws IOException {
    out.clear();
    return lineReader.readLine(out);
  }

}
