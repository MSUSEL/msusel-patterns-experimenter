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
#include "fuse_dfs.h"
#include "fuse_impls.h"
#include "fuse_file_handle.h"

static size_t min(const size_t x, const size_t y) {
  return x < y ? x : y;
}

/**
 * dfs_read
 *
 * Reads from dfs or the open file's buffer.  Note that fuse requires that
 * either the entire read be satisfied or the EOF is hit or direct_io is enabled
 *
 */
int dfs_read(const char *path, char *buf, size_t size, off_t offset,
                   struct fuse_file_info *fi)
{
  TRACE1("read",path)
  
  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(dfs);
  assert(path);
  assert(buf);
  assert(offset >= 0);
  assert(size >= 0);
  assert(fi);

  dfs_fh *fh = (dfs_fh*)fi->fh;

  assert(fh != NULL);
  assert(fh->fs != NULL);
  assert(fh->hdfsFH != NULL);

  // special case this as simplifies the rest of the logic to know the caller wanted > 0 bytes
  if (size == 0)
    return 0;

  // If size is bigger than the read buffer, then just read right into the user supplied buffer
  if ( size >= dfs->rdbuffer_size) {
    int num_read;
    size_t total_read = 0;
    while (size - total_read > 0 && (num_read = hdfsPread(fh->fs, fh->hdfsFH, offset + total_read, buf + total_read, size - total_read)) > 0) {
      total_read += num_read;
    }
    // if there was an error before satisfying the current read, this logic declares it an error
    // and does not try to return any of the bytes read. Don't think it matters, so the code
    // is just being conservative.
    if (total_read < size && num_read < 0) {
      total_read = -EIO;
    }
    return total_read;
  }

  //
  // Critical section - protect from multiple reads in different threads accessing the read buffer
  // (no returns until end)
  //

  pthread_mutex_lock(&fh->mutex);

  // used only to check the postcondition of this function - namely that we satisfy
  // the entire read or EOF is hit.
  int isEOF = 0;
  int ret = 0;

  // check if the buffer is empty or
  // the read starts before the buffer starts or
  // the read ends after the buffer ends

  if (fh->bufferSize == 0  || 
      offset < fh->buffersStartOffset || 
      offset + size > fh->buffersStartOffset + fh->bufferSize) 
    {
      // Read into the buffer from DFS
      int num_read = 0;
      size_t total_read = 0;

      while (dfs->rdbuffer_size  - total_read > 0 &&
             (num_read = hdfsPread(fh->fs, fh->hdfsFH, offset + total_read, fh->buf + total_read, dfs->rdbuffer_size - total_read)) > 0) {
        total_read += num_read;
      }

      // if there was an error before satisfying the current read, this logic declares it an error
      // and does not try to return any of the bytes read. Don't think it matters, so the code
      // is just being conservative.
      if (total_read < size && num_read < 0) {
        // invalidate the buffer 
        fh->bufferSize = 0; 
        syslog(LOG_ERR, "Read error - pread failed for %s with return code %d %s:%d", path, (int)num_read, __FILE__, __LINE__);
        ret = -EIO;
      } else {
        // Either EOF, all read or read beyond size, but then there was an error
        fh->bufferSize = total_read;
        fh->buffersStartOffset = offset;

        if (dfs->rdbuffer_size - total_read > 0) {
          // assert(num_read == 0); this should be true since if num_read < 0 handled above.
          isEOF = 1;
        }
      }
    }

  //
  // NOTE on EOF, fh->bufferSize == 0 and ret = 0 ,so the logic for copying data into the caller's buffer is bypassed, and
  //  the code returns 0 as required
  //
  if (ret == 0 && fh->bufferSize > 0) {

    assert(offset >= fh->buffersStartOffset);
    assert(fh->buf);

    const size_t bufferReadIndex = offset - fh->buffersStartOffset;
    assert(bufferReadIndex >= 0 && bufferReadIndex < fh->bufferSize);

    const size_t amount = min(fh->buffersStartOffset + fh->bufferSize - offset, size);
    assert(amount >= 0 && amount <= fh->bufferSize);

    const char *offsetPtr = fh->buf + bufferReadIndex;
    assert(offsetPtr >= fh->buf);
    assert(offsetPtr + amount <= fh->buf + fh->bufferSize);
    
    memcpy(buf, offsetPtr, amount);

    ret = amount;
  }

  //
  // Critical section end 
  //
  pthread_mutex_unlock(&fh->mutex);
 
  // fuse requires the below and the code should guarantee this assertion
  // 3 cases on return:
  //   1. entire read satisfied
  //   2. partial read and isEOF - including 0 size read
  //   3. error 
  assert(ret == size || isEOF || ret < 0);

 return ret;
}
