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

int dfs_flush(const char *path, struct fuse_file_info *fi) {
  TRACE1("flush", path)

  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(path);
  assert(dfs);
  assert('/' == *path);
  assert(fi);

  if (NULL == (void*)fi->fh) {
    return  0;
  }

  // note that fuse calls flush on RO files too and hdfs does not like that and will return an error
  if (fi->flags & O_WRONLY) {

    dfs_fh *fh = (dfs_fh*)fi->fh;
    assert(fh);
    hdfsFile file_handle = (hdfsFile)fh->hdfsFH;
    assert(file_handle);

    assert(fh->fs);
    if (hdfsFlush(fh->fs, file_handle) != 0) {
      syslog(LOG_ERR, "ERROR: dfs problem - could not flush file_handle(%lx) for %s %s:%d\n",(long)file_handle,path, __FILE__, __LINE__);
      return -EIO;
    }
  }

  return 0;
}
