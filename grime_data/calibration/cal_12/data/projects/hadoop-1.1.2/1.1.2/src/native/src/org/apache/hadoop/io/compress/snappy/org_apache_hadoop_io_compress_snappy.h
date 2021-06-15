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

#if !defined ORG_APACHE_HADOOP_IO_COMPRESS_SNAPPY_SNAPPY_H
#define ORG_APACHE_HADOOP_IO_COMPRESS_SNAPPY_SNAPPY_H


#if defined HAVE_CONFIG_H
  #include <config.h>
#endif

#if defined HADOOP_SNAPPY_LIBRARY

  #if defined HAVE_STDDEF_H
    #include <stddef.h>
  #else
    #error 'stddef.h not found'
  #endif

  #if defined HAVE_SNAPPY_C_H
    #include <snappy-c.h>
  #else
    #error 'Please install snappy-development packages for your platform.'
  #endif

  #if defined HAVE_DLFCN_H
    #include <dlfcn.h>
  #else
    #error "dlfcn.h not found"
  #endif

  #if defined HAVE_JNI_H
    #include <jni.h>
  #else
    #error 'jni.h not found'
  #endif

  #include "org_apache_hadoop.h"

#endif //define HADOOP_SNAPPY_LIBRARY

#endif //ORG_APACHE_HADOOP_IO_COMPRESS_SNAPPY_SNAPPY_H
