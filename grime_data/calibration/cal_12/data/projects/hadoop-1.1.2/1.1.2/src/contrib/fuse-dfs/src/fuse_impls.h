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

#ifndef __FUSE_IMPLS_H__
#define __FUSE_IMPLS_H__

#include <fuse.h>
#include <syslog.h>

#include "fuse_context_handle.h"

/**
 * Implementations of the various fuse hooks.
 * All of these (should be) thread safe.
 *
 */

int dfs_mkdir(const char *path, mode_t mode);
int dfs_rename(const char *from, const char *to);
int dfs_getattr(const char *path, struct stat *st);
int dfs_readdir(const char *path, void *buf, fuse_fill_dir_t filler,
                off_t offset, struct fuse_file_info *fi);
int dfs_read(const char *path, char *buf, size_t size, off_t offset,
                    struct fuse_file_info *fi);
int dfs_statfs(const char *path, struct statvfs *st);
int dfs_mkdir(const char *path, mode_t mode);
int dfs_rename(const char *from, const char *to);
int dfs_rmdir(const char *path);
int dfs_unlink(const char *path);
int dfs_utimens(const char *path, const struct timespec ts[2]);
int dfs_chmod(const char *path, mode_t mode);
int dfs_chown(const char *path, uid_t uid, gid_t gid);
int dfs_open(const char *path, struct fuse_file_info *fi);
int dfs_write(const char *path, const char *buf, size_t size,
              off_t offset, struct fuse_file_info *fi);
int dfs_release (const char *path, struct fuse_file_info *fi);
int dfs_mknod(const char *path, mode_t mode, dev_t rdev) ;
int dfs_create(const char *path, mode_t mode, struct fuse_file_info *fi);
int dfs_flush(const char *path, struct fuse_file_info *fi);
int dfs_access(const char *path, int mask);
int dfs_truncate(const char *path, off_t size);
int dfs_symlink(const char *from, const char *to);

#endif



