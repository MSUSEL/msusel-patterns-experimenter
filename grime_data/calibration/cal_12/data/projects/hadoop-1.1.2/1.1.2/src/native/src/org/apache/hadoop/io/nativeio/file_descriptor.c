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
#include <jni.h>
#include "file_descriptor.h"
#include "org_apache_hadoop.h"

// class of java.io.FileDescriptor
static jclass fd_class;
// the internal field for the integer fd
static jfieldID fd_descriptor;
// the no-argument constructor
static jmethodID fd_constructor;


void fd_init(JNIEnv* env)
{
  if (fd_class != NULL) return; // already initted

  fd_class = (*env)->FindClass(env, "java/io/FileDescriptor");
  PASS_EXCEPTIONS(env);
  fd_class = (*env)->NewGlobalRef(env, fd_class);

  fd_descriptor = (*env)->GetFieldID(env, fd_class, "fd", "I");
  PASS_EXCEPTIONS(env);
  fd_constructor = (*env)->GetMethodID(env, fd_class, "<init>", "()V");
}

void fd_deinit(JNIEnv *env) {
  if (fd_class != NULL) {
    (*env)->DeleteGlobalRef(env, fd_class);
    fd_class = NULL;
  }
  fd_descriptor = NULL;
  fd_constructor = NULL;
}

/*
 * Given an instance 'obj' of java.io.FileDescriptor, return the
 * underlying fd, or throw if unavailable
 */
int fd_get(JNIEnv* env, jobject obj) {
  if (obj == NULL) {
    THROW(env, "java/lang/NullPointerException",
          "FileDescriptor object is null");
    return -1;
  }
  return (*env)->GetIntField(env, obj, fd_descriptor);
}

/*
 * Create a FileDescriptor object corresponding to the given int fd
 */
jobject fd_create(JNIEnv *env, int fd) {
  jobject obj = (*env)->NewObject(env, fd_class, fd_constructor);
  PASS_EXCEPTIONS_RET(env, NULL);

  (*env)->SetIntField(env, obj, fd_descriptor, fd);
  return obj;
} 
