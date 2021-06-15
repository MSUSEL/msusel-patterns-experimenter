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
#include <math.h>
#include <pthread.h>
#include <grp.h>
#include <pwd.h>

#include "fuse_dfs.h"
#include "fuse_stat_struct.h"
#include "fuse_context_handle.h"

#if PERMS
/**
 * getpwuid and getgrgid return static structs so we safeguard the contents
 * while retrieving fields using the 2 structs below.
 * NOTE: if using both, always get the passwd struct firt!
 */
extern pthread_mutex_t passwdstruct_mutex; 
extern pthread_mutex_t groupstruct_mutex;
#endif


const int default_id       = 99; // nobody  - not configurable since soon uids in dfs, yeah!
const int blksize = 512;

/**
 * Converts from a hdfs hdfsFileInfo to a POSIX stat struct
 *
 */
int fill_stat_structure(hdfsFileInfo *info, struct stat *st) 
{
  assert(st);
  assert(info);

  // initialize the stat structure
  memset(st, 0, sizeof(struct stat));

  // by default: set to 0 to indicate not supported for directory because we cannot (efficiently) get this info for every subdirectory
  st->st_nlink = (info->mKind == kObjectKindDirectory) ? 0 : 1;

  uid_t owner_id = default_id;
#if PERMS
  if (info->mOwner != NULL) {
    //
    // Critical section - protect from concurrent calls in different threads since
    // the struct below is static.
    // (no returns until end)
    //
    pthread_mutex_lock(&passwdstruct_mutex);

    struct passwd *passwd_info = getpwnam(info->mOwner);
    owner_id = passwd_info == NULL ? default_id : passwd_info->pw_uid;

    //
    // End critical section 
    // 
    pthread_mutex_unlock(&passwdstruct_mutex);

  } 
#endif
  gid_t group_id = default_id;
#if PERMS
  if (info->mGroup != NULL) {
    //
    // Critical section - protect from concurrent calls in different threads since
    // the struct below is static.
    // (no returns until end)
    //
    pthread_mutex_lock(&groupstruct_mutex);

    struct group *grp = getgrnam(info->mGroup);
    group_id = grp == NULL ? default_id : grp->gr_gid;

    //
    // End critical section 
    // 
    pthread_mutex_unlock(&groupstruct_mutex);

  }
#endif

  short perm = (info->mKind == kObjectKindDirectory) ? (S_IFDIR | 0777) :  (S_IFREG | 0666);
#if PERMS
  if (info->mPermissions > 0) {
    perm = (info->mKind == kObjectKindDirectory) ? S_IFDIR:  S_IFREG ;
    perm |= info->mPermissions;
  }
#endif

  // set stat metadata
  st->st_size     = (info->mKind == kObjectKindDirectory) ? 4096 : info->mSize;
  st->st_blksize  = blksize;
  st->st_blocks   =  ceil(st->st_size/st->st_blksize);
  st->st_mode     = perm;
  st->st_uid      = owner_id;
  st->st_gid      = group_id;
#if PERMS
  st->st_atime    = info->mLastAccess;
#else
  st->st_atime    = info->mLastMod;
#endif
  st->st_mtime    = info->mLastMod;
  st->st_ctime    = info->mLastMod;

  return 0;
}

