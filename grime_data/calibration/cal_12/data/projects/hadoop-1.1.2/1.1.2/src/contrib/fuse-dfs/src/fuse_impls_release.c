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

/**
 * This mutex is to protect releasing a file handle in case the user calls close in different threads
 * and fuse passes these calls to here.
 */
pthread_mutex_t release_mutex = PTHREAD_MUTEX_INITIALIZER;

int dfs_release (const char *path, struct fuse_file_info *fi) {
  TRACE1("release", path)

  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(path);
  assert(dfs);
  assert('/' == *path);

  int ret = 0;

  //
  // Critical section - protect from multiple close calls in different threads.
  // (no returns until end)
  //

  pthread_mutex_lock(&release_mutex);

  if (NULL != (void*)fi->fh) {

    dfs_fh *fh = (dfs_fh*)fi->fh;
    assert(fh);

    hdfsFile file_handle = (hdfsFile)fh->hdfsFH;

    if (NULL != file_handle) {
      if (hdfsCloseFile(fh->fs, file_handle) != 0) {
        syslog(LOG_ERR, "ERROR: dfs problem - could not close file_handle(%ld) for %s %s:%d\n",(long)file_handle,path, __FILE__, __LINE__);
        fprintf(stderr, "ERROR: dfs problem - could not close file_handle(%ld) for %s %s:%d\n",(long)file_handle,path, __FILE__, __LINE__);
        ret = -EIO;
      }
    }

    if (fh->buf != NULL) {
      free(fh->buf);
    }
    // this is always created and initialized, so always destroy it. (see dfs_open)
    pthread_mutex_destroy(&fh->mutex);

    free(fh);

    fi->fh = (uint64_t)0;
  }

  pthread_mutex_unlock(&release_mutex);

  //
  // End critical section 
  // 

  return ret;
}
