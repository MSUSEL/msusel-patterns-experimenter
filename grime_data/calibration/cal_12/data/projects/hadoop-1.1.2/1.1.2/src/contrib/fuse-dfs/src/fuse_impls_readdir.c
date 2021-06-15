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
#include "fuse_stat_struct.h"
#include "fuse_connect.h"

int dfs_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
                       off_t offset, struct fuse_file_info *fi)
{
  TRACE1("readdir",path)

  (void) offset;
  (void) fi;

  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(dfs);
  assert(path);
  assert(buf);

  int path_len = strlen(path);

  hdfsFS userFS;
  // if not connected, try to connect and fail out if we can't.
  if ((userFS = doConnectAsUser(dfs->nn_hostname,dfs->nn_port))== NULL) {
    syslog(LOG_ERR, "ERROR: could not connect to dfs %s:%d\n", __FILE__, __LINE__);
    return -EIO;
  }

  // call dfs to read the dir
  int numEntries = 0;
  hdfsFileInfo *info = hdfsListDirectory(userFS,path,&numEntries);
  userFS = NULL;

  // NULL means either the directory doesn't exist or maybe IO error.
  if (NULL == info) {
    return -ENOENT;
  }

  int i ;
  for (i = 0; i < numEntries; i++) {

    // check the info[i] struct
    if (NULL == info[i].mName) {
      syslog(LOG_ERR,"ERROR: for <%s> info[%d].mName==NULL %s:%d", path, i, __FILE__,__LINE__);
      continue;
    }

    struct stat st;
    fill_stat_structure(&info[i], &st);

    // hack city: todo fix the below to something nicer and more maintainable but
    // with good performance
    // strip off the path but be careful if the path is solely '/'
    // NOTE - this API started returning filenames as full dfs uris
    const char *const str = info[i].mName + dfs->dfs_uri_len + path_len + ((path_len == 1 && *path == '/') ? 0 : 1);

    // pack this entry into the fuse buffer
    int res = 0;
    if ((res = filler(buf,str,&st,0)) != 0) {
      syslog(LOG_ERR, "ERROR: readdir filling the buffer %d %s:%d\n",res, __FILE__, __LINE__);
    }
  }

  // insert '.' and '..'
  const char *const dots [] = { ".",".."};
  for (i = 0 ; i < 2 ; i++)
    {
      struct stat st;
      memset(&st, 0, sizeof(struct stat));

      // set to 0 to indicate not supported for directory because we cannot (efficiently) get this info for every subdirectory
      st.st_nlink =  0;

      // setup stat size and acl meta data
      st.st_size    = 512;
      st.st_blksize = 512;
      st.st_blocks  =  1;
      st.st_mode    = (S_IFDIR | 0777);
      st.st_uid     = default_id;
      st.st_gid     = default_id;
      // todo fix below times
      st.st_atime   = 0;
      st.st_mtime   = 0;
      st.st_ctime   = 0;

      const char *const str = dots[i];

      // flatten the info using fuse's function into a buffer
      int res = 0;
      if ((res = filler(buf,str,&st,0)) != 0) {
        syslog(LOG_ERR, "ERROR: readdir filling the buffer %d %s:%d", res, __FILE__, __LINE__);
      }
    }
  // free the info pointers
  hdfsFreeFileInfo(info,numEntries);
  return 0;
}
