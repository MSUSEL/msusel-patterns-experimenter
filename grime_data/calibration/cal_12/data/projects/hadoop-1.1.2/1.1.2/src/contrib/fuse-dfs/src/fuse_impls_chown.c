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
#include "fuse_users.h"
#include "fuse_impls.h"
#include "fuse_connect.h"

 int dfs_chown(const char *path, uid_t uid, gid_t gid)
{
  TRACE1("chown", path)

  int ret = 0;

#if PERMS
  char *user = NULL;
  char *group = NULL;

  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(path);
  assert(dfs);
  assert('/' == *path);

  user = getUsername(uid);
  if (NULL == user) {
    syslog(LOG_ERR,"Could not lookup the user id string %d\n",(int)uid); 
    fprintf(stderr, "could not lookup userid %d\n", (int)uid); 
    ret = -EIO;
  }

  if (0 == ret) {
    group = getGroup(gid);
    if (group == NULL) {
      syslog(LOG_ERR,"Could not lookup the group id string %d\n",(int)gid); 
      fprintf(stderr, "could not lookup group %d\n", (int)gid); 
      ret = -EIO;
    } 
  }

  hdfsFS userFS = NULL;
  if (0 == ret) {
    // if not connected, try to connect and fail out if we can't.
    if ((userFS = doConnectAsUser(dfs->nn_hostname,dfs->nn_port))== NULL) {
      syslog(LOG_ERR, "ERROR: could not connect to dfs %s:%d\n", __FILE__, __LINE__);
      ret = -EIO;
    }
  }

  if (0 == ret) {
    //  fprintf(stderr, "DEBUG: chown %s %d->%s %d->%s\n", path, (int)uid, user, (int)gid, group);
    if (hdfsChown(userFS, path, user, group)) {
      syslog(LOG_ERR,"ERROR: hdfs trying to chown %s to %d/%d",path, (int)uid, gid);
      ret = -EIO;
    }
  }
  if (user) 
    free(user);
  if (group)
    free(group);
#endif
  return ret;

}
