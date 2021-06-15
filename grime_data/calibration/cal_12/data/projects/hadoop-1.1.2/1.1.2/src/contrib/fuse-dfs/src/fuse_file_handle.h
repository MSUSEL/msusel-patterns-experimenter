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
#ifndef __FUSE_FILE_HANDLE_H__
#define __FUSE_FILE_HANDLE_H__

#include <hdfs.h>
#include <pthread.h>

/**
 *
 * dfs_fh_struct is passed around for open files. Fuse provides a hook (the context) 
 * for storing file specific data.
 *
 * 2 Types of information:
 * a) a read buffer for performance reasons since fuse is typically called on 4K chunks only
 * b) the hdfs fs handle 
 *
 */
typedef struct dfs_fh_struct {
  hdfsFile hdfsFH;
  char *buf;
  tSize bufferSize;  //what is the size of the buffer we have
  off_t buffersStartOffset; //where the buffer starts in the file
  hdfsFS fs; // for reads/writes need to access as the real user
  pthread_mutex_t mutex;
} dfs_fh;

#endif
