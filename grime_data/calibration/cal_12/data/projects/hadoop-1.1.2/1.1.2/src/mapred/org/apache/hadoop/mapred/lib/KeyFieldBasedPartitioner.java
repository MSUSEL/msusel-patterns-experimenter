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

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;
import org.apache.hadoop.mapred.lib.KeyFieldHelper.KeyDescription;

 /**   
  *  Defines a way to partition keys based on certain key fields (also see
  *  {@link KeyFieldBasedComparator}.
  *  The key specification supported is of the form -k pos1[,pos2], where,
  *  pos is of the form f[.c][opts], where f is the number
  *  of the key field to use, and c is the number of the first character from
  *  the beginning of the field. Fields and character posns are numbered 
  *  starting with 1; a character position of zero in pos2 indicates the
  *  field's last character. If '.c' is omitted from pos1, it defaults to 1
  *  (the beginning of the field); if omitted from pos2, it defaults to 0 
  *  (the end of the field).
  * 
  */
public class KeyFieldBasedPartitioner<K2, V2> implements Partitioner<K2, V2> {

  private static final Log LOG = LogFactory.getLog(KeyFieldBasedPartitioner.class.getName());
  private int numOfPartitionFields;
  
  private KeyFieldHelper keyFieldHelper = new KeyFieldHelper();

  public void configure(JobConf job) {
    String keyFieldSeparator = job.get("map.output.key.field.separator", "\t");
    keyFieldHelper.setKeyFieldSeparator(keyFieldSeparator);
    if (job.get("num.key.fields.for.partition") != null) {
      LOG.warn("Using deprecated num.key.fields.for.partition. " +
      		"Use mapred.text.key.partitioner.options instead");
      this.numOfPartitionFields = job.getInt("num.key.fields.for.partition",0);
      keyFieldHelper.setKeyFieldSpec(1,numOfPartitionFields);
    } else {
      String option = job.getKeyFieldPartitionerOption();
      keyFieldHelper.parseOption(option);
    }
  }

  public int getPartition(K2 key, V2 value,
      int numReduceTasks) {
    byte[] keyBytes;

    List <KeyDescription> allKeySpecs = keyFieldHelper.keySpecs();
    if (allKeySpecs.size() == 0) {
      return getPartition(key.toString().hashCode(), numReduceTasks);
    }

    try {
      keyBytes = key.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("The current system does not " +
          "support UTF-8 encoding!", e);
    }
    // return 0 if the key is empty
    if (keyBytes.length == 0) {
      return 0;
    }
    
    int []lengthIndicesFirst = keyFieldHelper.getWordLengths(keyBytes, 0, 
        keyBytes.length);
    int currentHash = 0;
    for (KeyDescription keySpec : allKeySpecs) {
      int startChar = keyFieldHelper.getStartOffset(keyBytes, 0, keyBytes.length, 
          lengthIndicesFirst, keySpec);
       // no key found! continue
      if (startChar < 0) {
        continue;
      }
      int endChar = keyFieldHelper.getEndOffset(keyBytes, 0, keyBytes.length, 
          lengthIndicesFirst, keySpec);
      currentHash = hashCode(keyBytes, startChar, endChar, 
          currentHash);
    }
    return getPartition(currentHash, numReduceTasks);
  }
  
  protected int hashCode(byte[] b, int start, int end, int currentHash) {
    for (int i = start; i <= end; i++) {
      currentHash = 31*currentHash + b[i];
    }
    return currentHash;
  }

  protected int getPartition(int hash, int numReduceTasks) {
    return (hash & Integer.MAX_VALUE) % numReduceTasks;
  }
}
