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

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.hadoop.fs.s3.INode.FileType;

public class TestINode extends TestCase {

  public void testSerializeFileWithSingleBlock() throws IOException {
    Block[] blocks = { new Block(849282477840258181L, 128L) };
    INode inode = new INode(FileType.FILE, blocks);

    assertEquals("Length", 1L + 4 + 16, inode.getSerializedLength());
    InputStream in = inode.serialize();

    INode deserialized = INode.deserialize(in);

    assertEquals("FileType", inode.getFileType(), deserialized.getFileType());
    Block[] deserializedBlocks = deserialized.getBlocks();
    assertEquals("Length", 1, deserializedBlocks.length);
    assertEquals("Id", blocks[0].getId(), deserializedBlocks[0].getId());
    assertEquals("Length", blocks[0].getLength(), deserializedBlocks[0]
                 .getLength());

  }
  
  public void testSerializeDirectory() throws IOException {
    INode inode = INode.DIRECTORY_INODE;
    assertEquals("Length", 1L, inode.getSerializedLength());
    InputStream in = inode.serialize();
    INode deserialized = INode.deserialize(in);    
    assertSame(INode.DIRECTORY_INODE, deserialized);
  }
  
  public void testDeserializeNull() throws IOException {
    assertNull(INode.deserialize(null));
  }

}
