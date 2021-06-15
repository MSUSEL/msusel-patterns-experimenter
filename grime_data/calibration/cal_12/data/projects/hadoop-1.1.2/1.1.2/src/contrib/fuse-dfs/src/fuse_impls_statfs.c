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


int dfs_statfs(const char *path, struct statvfs *st)
{
  TRACE1("statfs",path)

  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(path);
  assert(st);
  assert(dfs);

  // init the stat structure
  memset(st,0,sizeof(struct statvfs));

  hdfsFS userFS;
  // if not connected, try to connect and fail out if we can't.
  if ((userFS = doConnectAsUser(dfs->nn_hostname,dfs->nn_port))== NULL) {
    syslog(LOG_ERR, "ERROR: could not connect to dfs %s:%d\n", __FILE__, __LINE__);
    return -EIO;
  }

  const long cap   = hdfsGetCapacity(userFS);
  const long used  = hdfsGetUsed(userFS);
  const long bsize = hdfsGetDefaultBlockSize(userFS);

  // fill in the statvfs structure

  /* FOR REFERENCE:
     struct statvfs {
     unsigned long  f_bsize;    // file system block size
     unsigned long  f_frsize;   // fragment size
     fsblkcnt_t     f_blocks;   // size of fs in f_frsize units
     fsblkcnt_t     f_bfree;    // # free blocks
     fsblkcnt_t     f_bavail;   // # free blocks for non-root
     fsfilcnt_t     f_files;    // # inodes
     fsfilcnt_t     f_ffree;    // # free inodes
     fsfilcnt_t     f_favail;   // # free inodes for non-root
     unsigned long  f_fsid;     // file system id
     unsigned long  f_flag;     / mount flags
     unsigned long  f_namemax;  // maximum filename length
     };
  */

  st->f_bsize   =  bsize;
  st->f_frsize  =  bsize;

  st->f_blocks  =  cap/bsize;

  st->f_bfree   =  (cap-used)/bsize;
  st->f_bavail  =  (cap-used)/bsize;

  st->f_files   =  1000;
  st->f_ffree   =  500;
  st->f_favail  =  500;
  st->f_fsid    =  1023;
  st->f_flag    =  ST_RDONLY | ST_NOSUID;
  st->f_namemax =  1023;

  return 0;
}

