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
package org.apache.hadoop.hdfs.security.token.block;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;

/**
 * Object for passing block keys
 */
public class ExportedBlockKeys implements Writable {
  public static final ExportedBlockKeys DUMMY_KEYS = new ExportedBlockKeys();
  private boolean isBlockTokenEnabled;
  private long keyUpdateInterval;
  private long tokenLifetime;
  private BlockKey currentKey;
  private BlockKey[] allKeys;

  public ExportedBlockKeys() {
    this(false, 0, 0, new BlockKey(), new BlockKey[0]);
  }

  ExportedBlockKeys(boolean isBlockTokenEnabled, long keyUpdateInterval,
      long tokenLifetime, BlockKey currentKey, BlockKey[] allKeys) {
    this.isBlockTokenEnabled = isBlockTokenEnabled;
    this.keyUpdateInterval = keyUpdateInterval;
    this.tokenLifetime = tokenLifetime;
    this.currentKey = currentKey == null ? new BlockKey() : currentKey;
    this.allKeys = allKeys == null ? new BlockKey[0] : allKeys;
  }

  public boolean isBlockTokenEnabled() {
    return isBlockTokenEnabled;
  }

  public long getKeyUpdateInterval() {
    return keyUpdateInterval;
  }

  public long getTokenLifetime() {
    return tokenLifetime;
  }

  public BlockKey getCurrentKey() {
    return currentKey;
  }

  public BlockKey[] getAllKeys() {
    return allKeys;
  }
  
  // ///////////////////////////////////////////////
  // Writable
  // ///////////////////////////////////////////////
  static { // register a ctor
    WritableFactories.setFactory(ExportedBlockKeys.class,
        new WritableFactory() {
          public Writable newInstance() {
            return new ExportedBlockKeys();
          }
        });
  }

  /**
   */
  public void write(DataOutput out) throws IOException {
    out.writeBoolean(isBlockTokenEnabled);
    out.writeLong(keyUpdateInterval);
    out.writeLong(tokenLifetime);
    currentKey.write(out);
    out.writeInt(allKeys.length);
    for (int i = 0; i < allKeys.length; i++) {
      allKeys[i].write(out);
    }
  }

  /**
   */
  public void readFields(DataInput in) throws IOException {
    isBlockTokenEnabled = in.readBoolean();
    keyUpdateInterval = in.readLong();
    tokenLifetime = in.readLong();
    currentKey.readFields(in);
    this.allKeys = new BlockKey[in.readInt()];
    for (int i = 0; i < allKeys.length; i++) {
      allKeys[i] = new BlockKey();
      allKeys[i].readFields(in);
    }
  }

}