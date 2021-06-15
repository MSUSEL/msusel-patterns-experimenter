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
package org.apache.hadoop.fs.s3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Holds file metadata including type (regular file, or directory),
 * and the list of blocks that are pointers to the data.
 */
public class INode {
	
  enum FileType {
    DIRECTORY, FILE
  }
  
  public static final FileType[] FILE_TYPES = {
    FileType.DIRECTORY,
    FileType.FILE
  };

  public static final INode DIRECTORY_INODE = new INode(FileType.DIRECTORY, null);
  
  private FileType fileType;
  private Block[] blocks;

  public INode(FileType fileType, Block[] blocks) {
    this.fileType = fileType;
    if (isDirectory() && blocks != null) {
      throw new IllegalArgumentException("A directory cannot contain blocks.");
    }
    this.blocks = blocks;
  }

  public Block[] getBlocks() {
    return blocks;
  }
  
  public FileType getFileType() {
    return fileType;
  }

  public boolean isDirectory() {
    return fileType == FileType.DIRECTORY;
  }  

  public boolean isFile() {
    return fileType == FileType.FILE;
  }
  
  public long getSerializedLength() {
    return 1L + (blocks == null ? 0 : 4 + blocks.length * 16);
  }
  

  public InputStream serialize() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bytes);
    out.writeByte(fileType.ordinal());
    if (isFile()) {
      out.writeInt(blocks.length);
      for (int i = 0; i < blocks.length; i++) {
        out.writeLong(blocks[i].getId());
        out.writeLong(blocks[i].getLength());
      }
    }
    out.close();
    return new ByteArrayInputStream(bytes.toByteArray());
  }
  
  public static INode deserialize(InputStream in) throws IOException {
    if (in == null) {
      return null;
    }
    DataInputStream dataIn = new DataInputStream(in);
    FileType fileType = INode.FILE_TYPES[dataIn.readByte()];
    switch (fileType) {
    case DIRECTORY:
      in.close();
      return INode.DIRECTORY_INODE;
    case FILE:
      int numBlocks = dataIn.readInt();
      Block[] blocks = new Block[numBlocks];
      for (int i = 0; i < numBlocks; i++) {
        long id = dataIn.readLong();
        long length = dataIn.readLong();
        blocks[i] = new Block(id, length);
      }
      in.close();
      return new INode(fileType, blocks);
    default:
      throw new IllegalArgumentException("Cannot deserialize inode.");
    }    
  }  
  
}
