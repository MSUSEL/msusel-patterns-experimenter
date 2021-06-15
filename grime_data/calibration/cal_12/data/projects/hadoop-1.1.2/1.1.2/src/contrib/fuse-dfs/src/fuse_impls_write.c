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

int dfs_write(const char *path, const char *buf, size_t size,
                     off_t offset, struct fuse_file_info *fi)
{
  TRACE1("write", path)

  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;
  int ret = 0;

  // check params and the context var
  assert(path);
  assert(dfs);
  assert('/' == *path);
  assert(fi);

  dfs_fh *fh = (dfs_fh*)fi->fh;
  assert(fh);

  hdfsFile file_handle = (hdfsFile)fh->hdfsFH;
  assert(file_handle);

  //
  // Critical section - make the sanity check (tell to see the writes are sequential) and the actual write 
  // (no returns until end)
  //
  pthread_mutex_lock(&fh->mutex);

  tSize length = 0;
  assert(fh->fs);

  tOffset cur_offset = hdfsTell(fh->fs, file_handle);
  if (cur_offset != offset) {
    syslog(LOG_ERR, "ERROR: user trying to random access write to a file %d!=%d for %s %s:%d\n",(int)cur_offset, (int)offset,path, __FILE__, __LINE__);
    ret =  -EIO;
  } else {
    length = hdfsWrite(fh->fs, file_handle, buf, size);
    if (length <= 0) {
      syslog(LOG_ERR, "ERROR: fuse problem - could not write all the bytes for %s %d!=%d%s:%d\n",path,length,(int)size, __FILE__, __LINE__);
      ret = -EIO;
    } 
    if (length != size) {
      syslog(LOG_ERR, "WARN: fuse problem - could not write all the bytes for %s %d!=%d%s:%d\n",path,length,(int)size, __FILE__, __LINE__);
    }
  }

  //
  // Critical section end 
  //

  pthread_mutex_unlock(&fh->mutex);

  return ret == 0 ? length : ret;
}
