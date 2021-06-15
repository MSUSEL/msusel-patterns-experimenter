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

#include <hdfs.h>
#include <strings.h>

#include "fuse_dfs.h"
#include "fuse_trash.h"
#include "fuse_context_handle.h"


const char *const TrashPrefixDir = "/user/root/.Trash";
const char *const TrashDir = "/user/root/.Trash/Current";

#define TRASH_RENAME_TRIES  100

//
// NOTE: this function is a c implementation of org.apache.hadoop.fs.Trash.moveToTrash(Path path).
//

int move_to_trash(const char *item, hdfsFS userFS) {

  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(item);
  assert(dfs);
  assert('/' == *item);
  assert(rindex(item,'/') >= 0);


  char fname[4096]; // or last element of the directory path
  char parent_directory[4096]; // the directory the fname resides in

  if (strlen(item) > sizeof(fname) - strlen(TrashDir)) {
    syslog(LOG_ERR, "ERROR: internal buffer too small to accomodate path of length %d %s:%d\n", (int)strlen(item), __FILE__, __LINE__);
    return -EIO;
  }

  // separate the file name and the parent directory of the item to be deleted
  {
    int length_of_parent_dir = rindex(item, '/') - item ;
    int length_of_fname = strlen(item) - length_of_parent_dir - 1; // the '/'

    // note - the below strncpys should be safe from overflow because of the check on item's string length above.
    strncpy(parent_directory, item, length_of_parent_dir);
    parent_directory[length_of_parent_dir ] = 0;
    strncpy(fname, item + length_of_parent_dir + 1, strlen(item));
    fname[length_of_fname + 1] = 0;
  }

  // create the target trash directory
  char trash_dir[4096];
  if (snprintf(trash_dir, sizeof(trash_dir), "%s%s",TrashDir,parent_directory) >= sizeof trash_dir) {
    syslog(LOG_ERR, "move_to_trash error target is not big enough to hold new name for %s %s:%d\n",item, __FILE__, __LINE__);
    return -EIO;
  }

  // create the target trash directory in trash (if needed)
  if ( hdfsExists(userFS, trash_dir)) {
    // make the directory to put it in in the Trash - NOTE
    // hdfsCreateDirectory also creates parents, so Current will be created if it does not exist.
    if (hdfsCreateDirectory(userFS, trash_dir)) {
      return -EIO;
    }
  }

  //
  // if the target path in Trash already exists, then append with
  // a number. Start from 1.
  //
  char target[4096];
  int j ;
  if ( snprintf(target, sizeof target,"%s/%s",trash_dir, fname) >= sizeof target) {
    syslog(LOG_ERR, "move_to_trash error target is not big enough to hold new name for %s %s:%d\n",item, __FILE__, __LINE__);
    return -EIO;
  }

  // NOTE: this loop differs from the java version by capping the #of tries
  for (j = 1; ! hdfsExists(userFS, target) && j < TRASH_RENAME_TRIES ; j++) {
    if (snprintf(target, sizeof target,"%s/%s.%d",trash_dir, fname, j) >= sizeof target) {
      syslog(LOG_ERR, "move_to_trash error target is not big enough to hold new name for %s %s:%d\n",item, __FILE__, __LINE__);
      return -EIO;
    }
  }
  if (hdfsRename(userFS, item, target)) {
    syslog(LOG_ERR,"ERROR: hdfs trying to rename %s to %s",item, target);
    return -EIO;
  }
  return 0;
} 


int hdfsDeleteWithTrash(hdfsFS userFS, const char *path, int useTrash) {

  // move the file to the trash if this is enabled and its not actually in the trash.
  if (useTrash && strncmp(path, TrashPrefixDir, strlen(TrashPrefixDir)) != 0) {
    int ret= move_to_trash(path, userFS);
    return ret;
  }

  if (hdfsDelete(userFS, path)) {
    syslog(LOG_ERR,"ERROR: hdfs trying to delete the file %s",path);
    return -EIO;
  }
  return 0;

}
