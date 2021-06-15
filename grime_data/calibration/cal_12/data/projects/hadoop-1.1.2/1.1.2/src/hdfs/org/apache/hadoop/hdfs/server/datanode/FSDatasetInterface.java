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
package org.apache.hadoop.hdfs.server.datanode;


import java.io.Closeable;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;




import org.apache.hadoop.hdfs.server.datanode.metrics.FSDatasetMBean;
import org.apache.hadoop.hdfs.server.protocol.BlockRecoveryInfo;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.protocol.BlockLocalPathInfo;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.DiskChecker.DiskErrorException;

/**
 * This is an interface for the underlying storage that stores blocks for
 * a data node. 
 * Examples are the FSDataset (which stores blocks on dirs)  and 
 * SimulatedFSDataset (which simulates data).
 *
 */
public interface FSDatasetInterface extends FSDatasetMBean {

  
  /**
   * Returns the length of the metadata file of the specified block
   * @param b - the block for which the metadata length is desired
   * @return the length of the metadata file for the specified block.
   * @throws IOException
   */
  public long getMetaDataLength(Block b) throws IOException;

  /**
   * This class provides the input stream and length of the metadata
   * of a block
   *
   */
  static class MetaDataInputStream extends FilterInputStream {
    MetaDataInputStream(InputStream stream, long len) {
      super(stream);
      length = len;
    }
    private long length;
    
    public long getLength() {
      return length;
    }
  }
  
  /**
   * Returns metaData of block b as an input stream (and its length)
   * @param b - the block
   * @return the metadata input stream; 
   * @throws IOException
   */
  public MetaDataInputStream getMetaDataInputStream(Block b)
        throws IOException;
  
  /**
   * Does the meta file exist for this block?
   * @param b - the block
   * @return true of the metafile for specified block exits
   * @throws IOException
   */
  public boolean metaFileExists(Block b) throws IOException;


  /**
   * Returns the specified block's on-disk length (excluding metadata)
   * @param b
   * @return   the specified block's on-disk length (excluding metadta)
   * @throws IOException
   */
  public long getLength(Block b) throws IOException;

  /**
   * Returns the specified block's visible length (has metadata for this)
   * @param b
   * @return   the specified block's visible length
   * @throws IOException
   */
  public long getVisibleLength(Block b) throws IOException;

  /**
   * update the specified blocks visible meta data.  NOTE: only applies
   * to blocks that are being written to.  If called on closed blocks,
   * throws IOException
   * 
   * @param b block to update the length for
   * @param length value to set visible length to
   * @throws IOException if the block is not in ongoingCreates
   */
  public void setVisibleLength(Block b, long length) throws IOException;

  /**
   * @return the generation stamp stored with the block.
   */
  public Block getStoredBlock(long blkid) throws IOException;

  /**
   * Returns an input stream to read the contents of the specified block
   * @param b
   * @return an input stream to read the contents of the specified block
   * @throws IOException
   */
  public InputStream getBlockInputStream(Block b) throws IOException;
  
  /**
   * Returns an input stream at specified offset of the specified block
   * @param b
   * @param seekOffset
   * @return an input stream to read the contents of the specified block,
   *  starting at the offset
   * @throws IOException
   */
  public InputStream getBlockInputStream(Block b, long seekOffset)
            throws IOException;

  /**
   * Returns an input stream at specified offset of the specified block
   * The block is still in the tmp directory and is not finalized
   * @param b
   * @param blkoff
   * @param ckoff
   * @return an input stream to read the contents of the specified block,
   *  starting at the offset
   * @throws IOException
   */
  public BlockInputStreams getTmpInputStreams(Block b, long blkoff, long ckoff)
            throws IOException;

     /**
      * 
      * This class contains the output streams for the data and checksum
      * of a block
      *
      */
     static class BlockWriteStreams {
      OutputStream dataOut;
      OutputStream checksumOut;
      BlockWriteStreams(OutputStream dOut, OutputStream cOut) {
        dataOut = dOut;
        checksumOut = cOut;
      }
      
    }

  /**
   * This class contains the input streams for the data and checksum
   * of a block
   */
  static class BlockInputStreams implements Closeable {
    final InputStream dataIn;
    final InputStream checksumIn;

    BlockInputStreams(InputStream dataIn, InputStream checksumIn) {
      this.dataIn = dataIn;
      this.checksumIn = checksumIn;
    }

    /** {@inheritDoc} */
    public void close() {
      IOUtils.closeStream(dataIn);
      IOUtils.closeStream(checksumIn);
    }
  }
    
  /**
   * Creates the block and returns output streams to write data and CRC
   * @param b
   * @param isRecovery True if this is part of error recovery, otherwise false
   * @param isReplicationRequest True if this is part of block replication request
   * @return a BlockWriteStreams object to allow writing the block data
   *  and CRC
   * @throws IOException
   */
  public BlockWriteStreams writeToBlock(Block b, boolean isRecovery, 
                                        boolean isReplicationRequest) throws IOException;

  /**
   * Update the block to the new generation stamp and length.  
   */
  public void updateBlock(Block oldblock, Block newblock) throws IOException;
  
  /**
   * Finalizes the block previously opened for writing using writeToBlock.
   * The block size is what is in the parameter b and it must match the amount
   *  of data written
   * @param b
   * @throws IOException
   */
  public void finalizeBlock(Block b) throws IOException;

  /**
   * Finalizes the block previously opened for writing using writeToBlock 
   * if not already finalized
   * @param b
   * @throws IOException
   */
  public void finalizeBlockIfNeeded(Block b) throws IOException;

  /**
   * Unfinalizes the block previously opened for writing using writeToBlock.
   * The temporary file associated with this block is deleted.
   * @param b
   * @throws IOException
   */
  public void unfinalizeBlock(Block b) throws IOException;

  /**
   * Returns the block report - the full list of blocks stored
   * Returns only finalized blocks
   * @return - the block report - the full list of blocks stored
   */
  public Block[] getBlockReport();
  
  /**
   * Request that a block report be prepared.
   */
  public void requestAsyncBlockReport();

  /**
   * @return true if an asynchronous block report is ready
   */
  public boolean isAsyncBlockReportReady();

  /**
   * Retrieve an asynchronously prepared block report. Callers should first
   * call {@link #requestAsyncBlockReport()}, and then poll
   * {@link #isAsyncBlockReportReady()} until it returns true.
   *
   * Retrieving the asynchronous block report also resets it; a new
   * one must be prepared before this method may be called again.
   *
   * @throws IllegalStateException if an async report is not ready
   */
  public Block[] retrieveAsyncBlockReport();

  
  /**
   * Returns the blocks being written report 
   * @return - the blocks being written report
   */
  public Block[] getBlocksBeingWrittenReport();

  /**
   * Is the block valid?
   * @param b
   * @return - true if the specified block is valid
   */
  public boolean isValidBlock(Block b);

  /**
   * Invalidates the specified blocks
   * @param invalidBlks - the blocks to be invalidated
   * @throws IOException
   */
  public void invalidate(Block invalidBlks[]) throws IOException;

    /**
     * Check if all the data directories are healthy
     * @throws DiskErrorException
     */
  public void checkDataDir() throws DiskErrorException;
      
    /**
     * Stringifies the name of the storage
     */
  public String toString();
 
  /**
   * Shutdown the FSDataset
   */
  public void shutdown();

  /**
   * Returns the current offset in the data stream.
   * @param b
   * @param stream The stream to the data file and checksum file
   * @return the position of the file pointer in the data stream
   * @throws IOException
   */
  public long getChannelPosition(Block b, BlockWriteStreams stream) throws IOException;

  /**
   * Sets the file pointer of the data stream and checksum stream to
   * the specified values.
   * @param b
   * @param stream The stream for the data file and checksum file
   * @param dataOffset The position to which the file pointre for the data stream
   *        should be set
   * @param ckOffset The position to which the file pointre for the checksum stream
   *        should be set
   * @throws IOException
   */
  public void setChannelPosition(Block b, BlockWriteStreams stream, long dataOffset,
                                 long ckOffset) throws IOException;

  /**
   * Validate that the contents in the Block matches
   * the file on disk. Returns true if everything is fine.
   * @param b The block to be verified.
   * @throws IOException
   */
  public void validateBlockMetadata(Block b) throws IOException;

  /**
   * checks how many valid storage volumes are there in the DataNode
   * @return true if more then minimum valid volumes left in the FSDataSet
   */
  public boolean hasEnoughResource();

  public BlockRecoveryInfo startBlockRecovery(long blockId) throws IOException;

  /**
   * Get {@link BlockLocalPathInfo} for the given block.
   **/
  public BlockLocalPathInfo getBlockLocalPathInfo(Block b) throws IOException;
}
