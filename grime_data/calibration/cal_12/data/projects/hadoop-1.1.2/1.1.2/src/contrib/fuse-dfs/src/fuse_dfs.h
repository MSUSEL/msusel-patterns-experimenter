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
#ifndef __FUSE_DFS_H__
#define __FUSE_DFS_H__

#define FUSE_USE_VERSION 26

#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <assert.h>
#include <strings.h>
#include <syslog.h>

#include <fuse.h>
#include <fuse/fuse_opt.h>

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#ifdef linux
/* For pread()/pwrite() */
#define _XOPEN_SOURCE 500
#endif

#ifdef HAVE_SETXATTR
#include <sys/xattr.h>
#endif

//
// Check if a path is in the mount option supplied protected paths.
//
int is_protected(const char *path);


//#define DOTRACE
#ifdef DOTRACE
#define TRACE(x) \
  syslog(LOG_ERR, "fuse_dfs TRACE - %s\n", x);  \
  fprintf(stderr, "fuse_dfs TRACE - %s\n", x);

#define TRACE1(x,y)                              \
  syslog(LOG_ERR, "fuse_dfs TRACE - %s %s\n", x,y);  \
  fprintf(stderr, "fuse_dfs TRACE - %s %s\n", x,y);
#else
#define TRACE(x) ; 
#define TRACE1(x,y) ; 
#endif

#endif // __FUSE_DFS_H__
