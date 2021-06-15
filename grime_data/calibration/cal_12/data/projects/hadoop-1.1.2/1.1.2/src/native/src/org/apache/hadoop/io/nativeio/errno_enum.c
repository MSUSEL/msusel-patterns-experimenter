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
 #include <assert.h>
 #include <errno.h>
 #include <jni.h>

#include "org_apache_hadoop.h"

typedef struct errno_mapping {
  int errno_val;
  char *errno_str;
} errno_mapping_t;

#define MAPPING(x) {x, #x}
static errno_mapping_t ERRNO_MAPPINGS[] = {
  MAPPING(EPERM),
  MAPPING(ENOENT),
  MAPPING(ESRCH),
  MAPPING(EINTR),
  MAPPING(EIO),
  MAPPING(ENXIO),
  MAPPING(E2BIG),
  MAPPING(ENOEXEC),
  MAPPING(EBADF),
  MAPPING(ECHILD),
  MAPPING(EAGAIN),
  MAPPING(ENOMEM),
  MAPPING(EACCES),
  MAPPING(EFAULT),
  MAPPING(ENOTBLK),
  MAPPING(EBUSY),
  MAPPING(EEXIST),
  MAPPING(EXDEV),
  MAPPING(ENODEV),
  MAPPING(ENOTDIR),
  MAPPING(EISDIR),
  MAPPING(EINVAL),
  MAPPING(ENFILE),
  MAPPING(EMFILE),
  MAPPING(ENOTTY),
  MAPPING(ETXTBSY),
  MAPPING(EFBIG),
  MAPPING(ENOSPC),
  MAPPING(ESPIPE),
  MAPPING(EROFS),
  MAPPING(EMLINK),
  MAPPING(EPIPE),
  MAPPING(EDOM),
  MAPPING(ERANGE),
  {-1, NULL}
};

static jclass enum_class;
static jmethodID enum_valueOf;
static jclass errno_class;

void errno_enum_init(JNIEnv *env) {
  if (enum_class != NULL) return;

  enum_class = (*env)->FindClass(env, "java/lang/Enum");
  PASS_EXCEPTIONS(env);
  enum_class = (*env)->NewGlobalRef(env, enum_class);
  enum_valueOf = (*env)->GetStaticMethodID(env, enum_class,
    "valueOf", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;");
  PASS_EXCEPTIONS(env);

  errno_class = (*env)->FindClass(env, "org/apache/hadoop/io/nativeio/Errno");
  PASS_EXCEPTIONS(env);
  errno_class = (*env)->NewGlobalRef(env, errno_class);
}

void errno_enum_deinit(JNIEnv *env) {
  if (enum_class != NULL) {
    (*env)->DeleteGlobalRef(env, enum_class);
    enum_class = NULL;
  }
  if (errno_class != NULL) {
    (*env)->DeleteGlobalRef(env, errno_class);
    errno_class = NULL;
  }
  enum_valueOf = NULL;
}


static char *errno_to_string(int errnum) {
  int i;
  for (i = 0; ERRNO_MAPPINGS[i].errno_str != NULL; i++) {
    if (ERRNO_MAPPINGS[i].errno_val == errnum)
      return ERRNO_MAPPINGS[i].errno_str;
  }
  return "UNKNOWN";
}

jobject errno_to_enum(JNIEnv *env, int errnum) {
  char *str = errno_to_string(errnum);
  assert(str != NULL);

  jstring jstr = (*env)->NewStringUTF(env, str);
  PASS_EXCEPTIONS_RET(env, NULL);

  return (*env)->CallStaticObjectMethod(
    env, enum_class, enum_valueOf, errno_class, jstr);
}
