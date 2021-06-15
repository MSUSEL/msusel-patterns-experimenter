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

/**
 * For now implement truncate here and only for size == 0.
 * Weak implementation in that we just delete the file and 
 * then re-create it, but don't set the user, group, and times to the old
 * file's metadata. 
 */
int dfs_truncate(const char *path, off_t size)
{
  TRACE1("truncate", path)
  if (size != 0) {
    return -ENOTSUP;
  }

  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  assert(path);
  assert('/' == *path);
  assert(dfs);

  int ret = dfs_unlink(path);
  if (ret != 0) {
    return ret;
  }

  hdfsFS userFS;
  // if not connected, try to connect and fail out if we can't.
  if ((userFS = doConnectAsUser(dfs->nn_hostname,dfs->nn_port)) == NULL) {
    syslog(LOG_ERR, "ERROR: could not connect to dfs %s:%d\n", __FILE__, __LINE__);
    return -EIO;
  }

  int flags = O_WRONLY | O_CREAT;

  hdfsFile file;
  if ((file = (hdfsFile)hdfsOpenFile(userFS, path, flags,  0, 3, 0)) == NULL) {
    syslog(LOG_ERR, "ERROR: could not connect open file %s:%d\n", __FILE__, __LINE__);
    return -EIO;
  }

  if (hdfsCloseFile(userFS, file) != 0) {
    syslog(LOG_ERR, "ERROR: could not connect close file %s:%d\n", __FILE__, __LINE__);
    return -EIO;
  }
  return 0;
}
