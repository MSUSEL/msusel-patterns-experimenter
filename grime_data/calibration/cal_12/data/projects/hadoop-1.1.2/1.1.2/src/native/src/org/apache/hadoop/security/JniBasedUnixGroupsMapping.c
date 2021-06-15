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
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <errno.h>
#include <grp.h>
#include <stdio.h>
#include <pwd.h>
#include <string.h>

#include "org_apache_hadoop_security_JniBasedUnixGroupsMapping.h"
#include "org_apache_hadoop.h"

#define CHECK_ERROR_AND_RETURN() \
{\
  if (error == ENOMEM) {\
    THROW(env, "java/lang/OutOfMemoryError", NULL);\
  }\
  if (error == ENOENT) {\
    THROW(env, "java/io/IOException", "No entry for user");\
  }\
  freeObjs(grpBuf, groups, env, juser, cuser);\
  return NULL;\
}

JNIEXPORT jobjectArray JNICALL 
Java_org_apache_hadoop_security_JniBasedUnixGroupsMapping_getGroupForUser 
(JNIEnv *env, jobject jobj, jstring juser) {
  void freeObjs(char *grpBuf, gid_t *groups, JNIEnv *env, jstring juser, 
                const char *cuser);
  extern int getGroupIDList(const char *user, int *ngroups, gid_t **groups);
  extern int getGroupDetails(gid_t group, char **grpBuf);

  char *grpBuf = NULL;
  const char *cuser = (*env)->GetStringUTFChars(env, juser, NULL);
  if (cuser == NULL) {
    return NULL;
  }

  /*Get the number of the groups, and their IDs, this user belongs to*/
  gid_t *groups = NULL;
  int ngroups = 0;
  int error = getGroupIDList(cuser, &ngroups, &groups);
  if (error != 0) {
    CHECK_ERROR_AND_RETURN();
  }

  jobjectArray jgroups = (jobjectArray)(*env)->NewObjectArray(env, ngroups, 
            (*env)->FindClass(env, "java/lang/String"), NULL);
  if (jgroups == NULL) {
    freeObjs(grpBuf, groups, env, juser, cuser);
    THROW(env, "java/lang/OutOfMemoryError", NULL);
    return NULL;
  }

  /*Iterate over the groupIDs and get the group structure for each*/
  int i = 0;
  for (i = 0; i < ngroups; i++) {
    error = getGroupDetails(groups[i],&grpBuf);
    if (error != 0) {
      CHECK_ERROR_AND_RETURN();
    }
    jstring jgrp = (*env)->NewStringUTF(env, ((struct group*)grpBuf)->gr_name);
    if (jgrp == NULL) {
      freeObjs(grpBuf, groups, env, juser, cuser);
      THROW(env, "java/lang/OutOfMemoryError", NULL);
      return NULL;
    }
    (*env)->SetObjectArrayElement(env, jgroups,i,jgrp);
    free(grpBuf);
    grpBuf = NULL;
  }

  freeObjs(grpBuf, groups, env, juser, cuser);
  return jgroups;
}

void freeObjs(char *grpBuf, gid_t *groups, JNIEnv *env, jstring juser, const char *cuser ) {
  if (groups != NULL) {
    free(groups);
    groups = NULL;
  }
  if (grpBuf != NULL) {
    free(grpBuf);
    grpBuf = NULL;
  }
  (*env)->ReleaseStringUTFChars(env, juser, cuser);
}
