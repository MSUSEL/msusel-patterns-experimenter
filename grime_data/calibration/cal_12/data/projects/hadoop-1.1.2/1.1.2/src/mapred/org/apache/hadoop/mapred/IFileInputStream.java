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
package org.apache.hadoop.mapred;

import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.ChecksumException;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.ReadaheadPool;
import org.apache.hadoop.io.ReadaheadPool.ReadaheadRequest;
import org.apache.hadoop.util.DataChecksum;
/**
 * A checksum input stream, used for IFiles.
 * Used to validate the checksum of files created by {@link IFileOutputStream}. 
 */

class IFileInputStream extends InputStream {
  
  private final InputStream in; //The input stream to be verified for checksum.
  private final FileDescriptor inFd; // the file descriptor, if it is known
  private final long length; //The total length of the input file
  private final long dataLength;
  private DataChecksum sum;
  private long currentOffset = 0;
  private final byte b[] = new byte[1];
  private byte csum[] = null;
  private int checksumSize;

  private ReadaheadRequest curReadahead = null;
  private ReadaheadPool raPool = ReadaheadPool.getInstance();
  private boolean readahead;
  private int readaheadLength;

  /**
   * Configuration key to enable/disable IFile readahead.
   */
  public static final String MAPRED_IFILE_READAHEAD =
    "mapreduce.ifile.readahead";

  public static final boolean DEFAULT_MAPRED_IFILE_READAHEAD = true;

  /**
   * Configuration key to set the IFile readahead length in bytes.
   */
  public static final String MAPRED_IFILE_READAHEAD_BYTES =
    "mapreduce.ifile.readahead.bytes";

  public static final int DEFAULT_MAPRED_IFILE_READAHEAD_BYTES =
    4 * 1024 * 1024;

  public static final Log LOG = LogFactory.getLog(IFileInputStream.class);

  /**
   * Create a checksum input stream that reads
   * @param in The input stream to be verified for checksum.
   * @param len The length of the input stream including checksum bytes.
   */
  public IFileInputStream(InputStream in, long len, Configuration conf) {
    this.in = in;
    this.inFd = getFileDescriptorIfAvail(in);
    sum = DataChecksum.newDataChecksum(DataChecksum.CHECKSUM_CRC32, 
        Integer.MAX_VALUE);
    checksumSize = sum.getChecksumSize();
    length = len;
    dataLength = length - checksumSize;

    conf = (conf != null) ? conf : new Configuration();
    readahead = conf.getBoolean(MAPRED_IFILE_READAHEAD,
        DEFAULT_MAPRED_IFILE_READAHEAD);
    readaheadLength = conf.getInt(MAPRED_IFILE_READAHEAD_BYTES,
        DEFAULT_MAPRED_IFILE_READAHEAD_BYTES);

    doReadahead();
  }

  private static FileDescriptor getFileDescriptorIfAvail(InputStream in) {
    FileDescriptor fd = null;
    try {
      if (in instanceof FileInputStream) {
        fd = ((FileInputStream)in).getFD();
      }
    } catch (IOException e) {
      LOG.info("Unable to determine FileDescriptor", e);
    }
    return fd;
  }

  /**
   * Close the input stream. Note that we need to read to the end of the
   * stream to validate the checksum.
   */
  @Override
  public void close() throws IOException {

    if (curReadahead != null) {
      curReadahead.cancel();
    }
    if (currentOffset < dataLength) {
      byte[] t = new byte[Math.min((int)
            (Integer.MAX_VALUE & (dataLength - currentOffset)), 32 * 1024)];
      while (currentOffset < dataLength) {
        int n = read(t, 0, t.length);
        if (0 == n) {
          throw new EOFException("Could not validate checksum");
        }
      }
    }
    in.close();
  }
  
  @Override
  public long skip(long n) throws IOException {
   throw new IOException("Skip not supported for IFileInputStream");
  }
  
  public long getPosition() {
    return (currentOffset >= dataLength) ? dataLength : currentOffset;
  }
  
  public long getSize() {
    return checksumSize;
  }
  
  /**
   * Read bytes from the stream.
   * At EOF, checksum is validated, but the checksum
   * bytes are not passed back in the buffer. 
   */
  public int read(byte[] b, int off, int len) throws IOException {

    if (currentOffset >= dataLength) {
      return -1;
    }

    doReadahead();

    return doRead(b,off,len);
  }

  private void doReadahead() {
    if (raPool != null && inFd != null && readahead) {
      curReadahead = raPool.readaheadStream(
          "ifile", inFd,
          currentOffset, readaheadLength, dataLength,
          curReadahead);
    }
  }

  /**
   * Read bytes from the stream.
   * At EOF, checksum is validated and sent back
   * as the last four bytes of the buffer. The caller should handle
   * these bytes appropriately
   */
  public int readWithChecksum(byte[] b, int off, int len) throws IOException {

    if (currentOffset == length) {
      return -1;
    }
    else if (currentOffset >= dataLength) {
      // If the previous read drained off all the data, then just return
      // the checksum now. Note that checksum validation would have 
      // happened in the earlier read
      int lenToCopy = (int) (checksumSize - (currentOffset - dataLength));
      if (len < lenToCopy) {
        lenToCopy = len;
      }
      System.arraycopy(csum, (int) (currentOffset - dataLength), b, off, 
          lenToCopy);
      currentOffset += lenToCopy;
      return lenToCopy;
    }

    int bytesRead = doRead(b,off,len);

    if (currentOffset == dataLength) {
      if (len >= bytesRead + checksumSize) {
        System.arraycopy(csum, 0, b, off + bytesRead, checksumSize);
        bytesRead += checksumSize;
        currentOffset += checksumSize;
      }
    }
    return bytesRead;
  }

  private int doRead(byte[]b, int off, int len) throws IOException {
    
    // If we are trying to read past the end of data, just read
    // the left over data
    if (currentOffset + len > dataLength) {
      len = (int) dataLength - (int)currentOffset;
    }
    
    int bytesRead = in.read(b, off, len);

    if (bytesRead < 0) {
      throw new ChecksumException("Checksum Error", 0);
    }
    
    sum.update(b,off,bytesRead);

    currentOffset += bytesRead;
    
    if (currentOffset == dataLength) {
      // The last four bytes are checksum. Strip them and verify
      csum = new byte[checksumSize];
      IOUtils.readFully(in, csum, 0, checksumSize);
      if (!sum.compare(csum, 0)) {
        throw new ChecksumException("Checksum Error", 0);
      }
    }
    return bytesRead;
  }


  @Override
  public int read() throws IOException {    
    b[0] = 0;
    int l = read(b,0,1);
    if (l < 0)  return l;
    
    // Upgrade the b[0] to an int so as not to misinterpret the
    // first bit of the byte as a sign bit
    int result = 0xFF & b[0];
    return result;
  }

  public byte[] getChecksum() {
    return csum;
  }
}
