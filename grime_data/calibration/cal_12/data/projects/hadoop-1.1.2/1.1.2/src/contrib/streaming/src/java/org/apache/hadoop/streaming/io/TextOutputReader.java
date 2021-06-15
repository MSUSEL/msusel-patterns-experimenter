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

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.streaming.PipeMapRed;
import org.apache.hadoop.streaming.StreamKeyValUtil;
import org.apache.hadoop.util.LineReader;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.UTF8ByteArrayUtils;

/**
 * OutputReader that reads the client's output as text.
 */
public class TextOutputReader extends OutputReader<Text, Text> {

  private LineReader lineReader;
  private byte[] bytes;
  private DataInput clientIn;
  private Configuration conf;
  private int numKeyFields;
  private byte[] separator;
  private Text key;
  private Text value;
  private Text line;
  
  @Override
  public void initialize(PipeMapRed pipeMapRed) throws IOException {
    super.initialize(pipeMapRed);
    clientIn = pipeMapRed.getClientInput();
    conf = pipeMapRed.getConfiguration();
    numKeyFields = pipeMapRed.getNumOfKeyFields();
    separator = pipeMapRed.getFieldSeparator();
    lineReader = new LineReader((InputStream)clientIn, conf);
    key = new Text();
    value = new Text();
    line = new Text();
  }
  
  @Override
  public boolean readKeyValue() throws IOException {
    if (lineReader.readLine(line) <= 0) {
      return false;
    }
    bytes = line.getBytes();
    splitKeyVal(bytes, line.getLength(), key, value);
    line.clear();
    return true;
  }
  
  @Override
  public Text getCurrentKey() throws IOException {
    return key;
  }
  
  @Override
  public Text getCurrentValue() throws IOException {
    return value;
  }

  @Override
  public String getLastOutput() {
    try {
      return new String(bytes, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return "<undecodable>";
    }
  }

  // split a UTF-8 line into key and value
  private void splitKeyVal(byte[] line, int length, Text key, Text val)
    throws IOException {
    // Need to find numKeyFields separators
    int pos = UTF8ByteArrayUtils.findBytes(line, 0, length, separator);
    for(int k=1; k<numKeyFields && pos!=-1; k++) {
      pos = UTF8ByteArrayUtils.findBytes(line, pos + separator.length, 
        length, separator);
    }
    try {
      if (pos == -1) {
        key.set(line, 0, length);
        val.set("");
      } else {
        StreamKeyValUtil.splitKeyVal(line, 0, length, key, val, pos,
          separator.length);
      }
    } catch (CharacterCodingException e) {
      throw new IOException(StringUtils.stringifyException(e));
    }
  }
  
}
