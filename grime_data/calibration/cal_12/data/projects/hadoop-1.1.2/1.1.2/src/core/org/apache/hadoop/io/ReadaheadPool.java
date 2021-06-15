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
package org.apache.hadoop.io;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.io.nativeio.NativeIO;

/**
 * Manages a pool of threads which can issue readahead requests on file descriptors.
 */
@InterfaceAudience.Private
@InterfaceStability.Evolving
public class ReadaheadPool {
  static final Log LOG = LogFactory.getLog(ReadaheadPool.class);
  private static final int POOL_SIZE = 4;
  private static final int MAX_POOL_SIZE = 16;
  private static final int CAPACITY = 1024;
  private final ThreadPoolExecutor pool;

  private static ReadaheadPool instance;
  private static AtomicLong THREAD_COUNTER = new AtomicLong(0);

  /**
   * Return the singleton instance for the current process.
   */
  public static ReadaheadPool getInstance() {
    synchronized (ReadaheadPool.class) {
      if (instance == null && NativeIO.isAvailable()) {
        instance = new ReadaheadPool();
      }
      return instance;
    }
  }

  private ReadaheadPool() {
    pool = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, 3L, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(CAPACITY));
    pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
    pool.setThreadFactory(new ThreadFactory() {
      @Override
      public Thread newThread(Runnable runnable) {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName(String.format("Readahead Thread #%d",
            THREAD_COUNTER.getAndIncrement()));
        thread.setDaemon(true);
        return thread;
      }
    });
  }
  
  /**
   * Issue a request to readahead on the given file descriptor.
   *
   * @param identifier a textual identifier that will be used in error
   * messages (e.g. the file name)
   * @param fd the file descriptor to read ahead
   * @param curPos the current offset at which reads are being issued
   * @param readaheadLength the configured length to read ahead
   * @param maxOffsetToRead the maximum offset that will be readahead
   *        (useful if, for example, only some segment of the file is
   *        requested by the user). Pass {@link Long.MAX_VALUE} to allow
   *        readahead to the end of the file.
   * @param lastReadahead the result returned by the previous invocation
   *        of this function on this file descriptor, or null if this is
   *        the first call
   * @return an object representing this outstanding request, or null
   *        if no readahead was performed
   */
  public ReadaheadRequest readaheadStream(
      String identifier,
      FileDescriptor fd,
      long curPos,
      long readaheadLength,
      long maxOffsetToRead,
      ReadaheadRequest lastReadahead) {

    if (curPos > maxOffsetToRead) {
      throw new IllegalArgumentException("Readahead position" + curPos
          + "higher than maxOffsetToRead" + maxOffsetToRead);
    }

    if (readaheadLength <= 0) {
      return null;
    }

    long lastOffset = Long.MIN_VALUE;

    if (lastReadahead != null) {
      lastOffset = lastReadahead.getOffset();
    }

    // trigger each readahead when we have reached the halfway mark
    // in the previous readahead. This gives the system time
    // to satisfy the readahead before we start reading the data.
    long nextOffset = lastOffset + readaheadLength / 2;
    if (curPos >= nextOffset) {
      // cancel any currently pending readahead, to avoid
      // piling things up in the queue. Each reader should have at most
      // one outstanding request in the queue.
      if (lastReadahead != null) {
        lastReadahead.cancel();
        lastReadahead = null;
      }

      long length = Math.min(readaheadLength,
          maxOffsetToRead - curPos);

      if (length <= 0) {
        // we've reached the end of the stream
        return null;
      }

      return submitReadahead(identifier, fd, curPos, length);
    } else {
      return lastReadahead;
    }
  }
  /**
   * Submit a request to readahead on the given file descriptor.
   * @param identifier a textual identifier used in error messages, etc.
   * @param fd the file descriptor to readahead
   * @param off the offset at which to start the readahead
   * @param len the number of bytes to read
   * @return an object representing this pending request
   */
  public ReadaheadRequest submitReadahead(
      String identifier, FileDescriptor fd, long off, long len) {
    ReadaheadRequestImpl req = new ReadaheadRequestImpl(
        identifier, fd, off, len);
    pool.execute(req);
    if (LOG.isTraceEnabled()) {
      LOG.trace("submit readahead: " + req);
    }
    return req;
  }

  /**
   * An outstanding readahead request that has been submitted to
   * the pool. This request may be pending or may have been
   * completed.
   */
  public interface ReadaheadRequest {
    /**
     * Cancels the request for readahead. This should be used
     * if the reader no longer needs the requested data, <em>before</em>
     * closing the related file descriptor.
     *
     * It is safe to use even if the readahead request has already
     * been fulfilled.
     */
    public void cancel();

    /**
     * @return the requested offset
     */
    public long getOffset();

    /**
     * @return the requested length
     */
    public long getLength();
  }

  private static class ReadaheadRequestImpl implements Runnable, ReadaheadRequest {
    private final String identifier;
    private final FileDescriptor fd;
    private final long off, len;
    private volatile boolean canceled = false;

    private ReadaheadRequestImpl(String identifier, FileDescriptor fd, long off, long len) {
      this.identifier = identifier;
      this.fd = fd;
      this.off = off;
      this.len = len;
    }

    public void run() {
      if (canceled) return;
      // There's a very narrow race here that the file will close right at
      // this instant. But if that happens, we'll likely receive an EBADF
      // error below, and see that it's canceled, ignoring the error.
      // It's also possible that we'll end up requesting readahead on some
      // other FD, which may be wasted work, but won't cause a problem.
      try {
        NativeIO.posixFadviseIfPossible(fd, off, len,
            NativeIO.POSIX_FADV_WILLNEED);
      } catch (IOException ioe) {
        if (canceled) {
          // no big deal - the reader canceled the request and closed
          // the file.
          return;
        }
        LOG.warn("Failed readahead on " + identifier,
            ioe);
      }
    }

    @Override
    public void cancel() {
      canceled = true;
      // We could attempt to remove it from the work queue, but that would
      // add complexity. In practice, the work queues remain very short,
      // so removing canceled requests has no gain.
    }

    @Override
    public long getOffset() {
      return off;
    }

    @Override
    public long getLength() {
      return len;
    }

    @Override
    public String toString() {
      return "ReadaheadRequestImpl [identifier='" + identifier + "', fd=" + fd
          + ", off=" + off + ", len=" + len + "]";
    }
  }
}