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
#include "fuse_connect.h"
#include "fuse_file_handle.h"

int dfs_open(const char *path, struct fuse_file_info *fi)
{
  TRACE1("open", path)

  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(path);
  assert('/' == *path);
  assert(dfs);

  int ret = 0;

  // 0x8000 is always passed in and hadoop doesn't like it, so killing it here
  // bugbug figure out what this flag is and report problem to Hadoop JIRA
  int flags = (fi->flags & 0x7FFF);

  // retrieve dfs specific data
  dfs_fh *fh = (dfs_fh*)malloc(sizeof (dfs_fh));
  if (fh == NULL) {
    syslog(LOG_ERR, "ERROR: malloc of new file handle failed %s:%d\n", __FILE__, __LINE__);
    return -EIO;
  }

  if ((fh->fs = doConnectAsUser(dfs->nn_hostname,dfs->nn_port)) == NULL) {
    syslog(LOG_ERR, "ERROR: could not connect to dfs %s:%d\n", __FILE__, __LINE__);
    return -EIO;
  }

  if ((fh->hdfsFH = hdfsOpenFile(fh->fs, path, flags,  0, 3, 0)) == NULL) {
    syslog(LOG_ERR, "ERROR: could not connect open file %s:%d\n", __FILE__, __LINE__);
    return -EIO;
  }

  // 
  // mutex needed for reads/writes
  //
  pthread_mutex_init(&fh->mutex, NULL);

  if (fi->flags & O_WRONLY || fi->flags & O_CREAT) {
    // write specific initialization
    fh->buf = NULL;
  } else  {
    // read specific initialization

    assert(dfs->rdbuffer_size > 0);

    if (NULL == (fh->buf = (char*)malloc(dfs->rdbuffer_size*sizeof (char)))) {
      syslog(LOG_ERR, "ERROR: could not allocate memory for file buffer for a read for file %s dfs %s:%d\n", path,__FILE__, __LINE__);
      ret = -EIO;
    }

    fh->buffersStartOffset = 0;
    fh->bufferSize = 0;
  }

  fi->fh = (uint64_t)fh;

  return ret;
}
