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

package org.hsqldb.persist;

import org.hsqldb.lib.HsqlArrayList;

/**
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class LobStoreMem implements LobStore {

    final int     lobBlockSize;
    int           blocksInLargeBlock = 128;
    int           largeBlockSize;
    HsqlArrayList byteStoreList;

    public LobStoreMem(int lobBlockSize) {

        this.lobBlockSize = lobBlockSize;
        largeBlockSize    = lobBlockSize * blocksInLargeBlock;
        byteStoreList     = new HsqlArrayList();
    }

    public byte[] getBlockBytes(int blockAddress, int blockCount) {

        byte[] dataBytes       = new byte[blockCount * lobBlockSize];
        int    dataBlockOffset = 0;

        while (blockCount > 0) {
            int    largeBlockIndex   = blockAddress / blocksInLargeBlock;
            byte[] largeBlock = (byte[]) byteStoreList.get(largeBlockIndex);
            int    blockOffset       = blockAddress % blocksInLargeBlock;
            int    currentBlockCount = blockCount;

            if ((blockOffset + currentBlockCount) > blocksInLargeBlock) {
                currentBlockCount = blocksInLargeBlock - blockOffset;
            }

            System.arraycopy(largeBlock, blockOffset * lobBlockSize,
                             dataBytes, dataBlockOffset * lobBlockSize,
                             currentBlockCount * lobBlockSize);

            blockAddress    += currentBlockCount;
            dataBlockOffset += currentBlockCount;
            blockCount      -= currentBlockCount;
        }

        return dataBytes;
    }

    public void setBlockBytes(byte[] dataBytes, int blockAddress,
                              int blockCount) {

        int dataBlockOffset = 0;

        while (blockCount > 0) {
            int largeBlockIndex = blockAddress / blocksInLargeBlock;
            int largeBlockLimit = (blockAddress + blockCount)
                                  / blocksInLargeBlock;

            if ((blockAddress + blockCount) % blocksInLargeBlock != 0) {
                largeBlockLimit++;
            }

            if (largeBlockIndex >= byteStoreList.size()) {
                byteStoreList.add(new byte[largeBlockSize]);
            }

            byte[] largeBlock = (byte[]) byteStoreList.get(largeBlockIndex);
            int    blockOffset       = blockAddress % blocksInLargeBlock;
            int    currentBlockCount = blockCount;

            if ((blockOffset + currentBlockCount) > blocksInLargeBlock) {
                currentBlockCount = blocksInLargeBlock - blockOffset;
            }

            System.arraycopy(dataBytes, dataBlockOffset * lobBlockSize,
                             largeBlock, blockOffset * lobBlockSize,
                             currentBlockCount * lobBlockSize);

            blockAddress    += currentBlockCount;
            dataBlockOffset += currentBlockCount;
            blockCount      -= currentBlockCount;
        }
    }

    public int getBlockSize() {
        return lobBlockSize;
    }

    public void close() {
        byteStoreList.clear();
    }
}
