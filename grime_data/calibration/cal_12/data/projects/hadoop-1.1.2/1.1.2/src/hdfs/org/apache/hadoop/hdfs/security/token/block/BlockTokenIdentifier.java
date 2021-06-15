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
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.hdfs.security.token.block.BlockTokenSecretManager.AccessMode;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;

public class BlockTokenIdentifier extends TokenIdentifier {
  static final Text KIND_NAME = new Text("HDFS_BLOCK_TOKEN");

  private long expiryDate;
  private int keyId;
  private String userId;
  private long [] blockIds;
  private EnumSet<AccessMode> modes;

  private byte [] cache;
  
  public BlockTokenIdentifier() {
    this(null, new long [] {}, EnumSet.noneOf(AccessMode.class));
  }

  public BlockTokenIdentifier(String userId, long [] blockIds,
      EnumSet<AccessMode> modes) {
    if(blockIds == null) 
      throw new IllegalArgumentException("blockIds can't be null");
    this.cache = null;
    this.userId = userId;
    this.blockIds = Arrays.copyOf(blockIds, blockIds.length);
    Arrays.sort(this.blockIds);
    this.modes = modes == null ? EnumSet.noneOf(AccessMode.class) : modes;
  }

  @Override
  public Text getKind() {
    return KIND_NAME;
  }

  @Override
  public UserGroupInformation getUser() {
    if (userId == null || "".equals(userId)) {
      return UserGroupInformation.createRemoteUser(Arrays.toString(blockIds));
    }
    return UserGroupInformation.createRemoteUser(userId);
  }

  public long getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(long expiryDate) {
    cache = null;
    this.expiryDate = expiryDate;
  }

  public int getKeyId() {
    return this.keyId;
  }

  public void setKeyId(int keyId) {
    cache = null;
    this.keyId = keyId;
  }

  public String getUserId() {
    return userId;
  }

  /**
   * Return sorted array of blockIds this {@link BlockTokenIdentifier} includes
   */
  public long [] getBlockIds() {
    return blockIds;
  }
  
  /**
   * Is specified blockId included in this BlockTokenIdentifier?
   */
  public boolean isBlockIncluded(long blockId) {
    switch(blockIds.length) {
    case 1:
      return blockIds[0] == blockId;
    case 2:
      return (blockIds[0] == blockId) || (blockIds[1] == blockId);
    default:
      return Arrays.binarySearch(blockIds, blockId) >= 0;  
    }
  }

  public EnumSet<AccessMode> getAccessModes() {
    return modes;
  }

  @Override
  public String toString() {
    return "block_token_identifier (expiryDate=" + this.getExpiryDate()
        + ", keyId=" + this.getKeyId() + ", userId=" + this.getUserId()
        + ", blockIds=" + Arrays.toString(blockIds) + ", access modes="
        + this.getAccessModes() + ")";
  }

  static boolean isEqual(Object a, Object b) {
    return a == null ? b == null : a.equals(b);
  }

  /** {@inheritDoc} */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof BlockTokenIdentifier) {
      BlockTokenIdentifier that = (BlockTokenIdentifier) obj;
      return this.expiryDate == that.expiryDate && this.keyId == that.keyId
          && isEqual(this.userId, that.userId) 
          && Arrays.equals(this.blockIds, that.blockIds)
          && isEqual(this.modes, that.modes);
    }
    return false;
  }

  /** {@inheritDoc} */
  public int hashCode() {
    return (int) expiryDate ^ keyId ^ Arrays.hashCode(blockIds) ^ modes.hashCode()
        ^ (userId == null ? 0 : userId.hashCode());
  }

  public void readFields(DataInput in) throws IOException {
    cache = null;
    expiryDate = WritableUtils.readVLong(in);
    keyId = WritableUtils.readVInt(in);
    userId = WritableUtils.readString(in);
    blockIds = new long[WritableUtils.readVInt(in)];
    for(int i = 0; i < blockIds.length; i++)
      blockIds[i] = WritableUtils.readVLong(in);
    int length = WritableUtils.readVInt(in);
    for (int i = 0; i < length; i++) {
      modes.add(WritableUtils.readEnum(in, AccessMode.class));
    }
  }

  public void write(DataOutput out) throws IOException {
    WritableUtils.writeVLong(out, expiryDate);
    WritableUtils.writeVInt(out, keyId);
    WritableUtils.writeString(out, userId);
    WritableUtils.writeVInt(out, blockIds.length);
    for(int i = 0; i < blockIds.length; i++)
      WritableUtils.writeVLong(out, blockIds[i]);
    WritableUtils.writeVInt(out, modes.size());
    for (AccessMode aMode : modes) {
      WritableUtils.writeEnum(out, aMode);
    }
  }
  
  @Override
  public byte[] getBytes() {
    if(cache == null) cache = super.getBytes();
    
    return cache;
  }
  
  @InterfaceAudience.Private
  public static class Renewer extends Token.TrivialRenewer {
    @Override
    protected Text getKind() {
      return KIND_NAME;
    }
  }
}
