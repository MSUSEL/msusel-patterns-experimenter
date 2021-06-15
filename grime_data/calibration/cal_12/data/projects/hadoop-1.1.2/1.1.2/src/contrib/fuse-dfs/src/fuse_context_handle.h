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
#ifndef __FUSE_CONTEXT_HANDLE_H__
#define __FUSE_CONTEXT_HANDLE_H__

#include <hdfs.h>
#include <stddef.h>
#include <sys/types.h>

//
// Structure to store fuse_dfs specific data
// this will be created and passed to fuse at startup
// and fuse will pass it back to us via the context function
// on every operation.
//
typedef struct dfs_context_struct {
  int debug;
  char *nn_hostname;
  int nn_port;
  hdfsFS fs;
  int read_only;
  int usetrash;
  int direct_io;
  char **protectedpaths;
  size_t rdbuffer_size;
  // todo:
  // total hack city - use this to strip off the dfs url from the filenames. (in fuse_impls_readdir.c)
  char dfs_uri[1024];
  int dfs_uri_len;
} dfs_context;

#endif
