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

#include <netdb.h>

#include "org_apache_hadoop_security_JniBasedUnixGroupsNetgroupMapping.h"
#include "org_apache_hadoop.h"

struct listElement {
   char * string;
   struct listElement * next;
};

typedef struct listElement UserList;

JNIEXPORT jobjectArray JNICALL 
Java_org_apache_hadoop_security_JniBasedUnixGroupsNetgroupMapping_getUsersForNetgroupJNI
(JNIEnv *env, jobject jobj, jstring jgroup) {

  // pointers to free at the end
  const char *cgroup  = NULL;
  jobjectArray jusers = NULL;

  // do we need to end the group lookup?
  int setnetgrentCalledFlag = 0;

  // if not NULL then THROW exception
  char *errorMessage = NULL;

  cgroup = (*env)->GetStringUTFChars(env, jgroup, NULL);
  if (cgroup == NULL) {
    goto END;
  }

  //--------------------------------------------------
  // get users
  // see man pages for setnetgrent, getnetgrent and endnetgrent

  UserList *userListHead = NULL;
  int       userListSize = 0;

  // set the name of the group for subsequent calls to getnetgrent
  // note that we want to end group lokup regardless whether setnetgrent
  // was successfull or not (as long as it was called we need to call
  // endnetgrent)
  setnetgrentCalledFlag = 1;
  if(setnetgrent(cgroup) == 1) {
    UserList *current = NULL;
    // three pointers are for host, user, domain, we only care
    // about user now
    char *p[3];
    while(getnetgrent(p, p + 1, p + 2)) {
      if(p[1]) {
        current = (UserList *)malloc(sizeof(UserList));
        current->string = malloc(strlen(p[1]) + 1);
        strcpy(current->string, p[1]);
        current->next = userListHead;
        userListHead = current;
        userListSize++;
      }
    }
  }

  //--------------------------------------------------
  // build return data (java array)

  jusers = (jobjectArray)(*env)->NewObjectArray(env,
    userListSize, 
    (*env)->FindClass(env, "java/lang/String"),
    NULL);
  if (jusers == NULL) {
    errorMessage = "java/lang/OutOfMemoryError";
    goto END;
  }

  UserList * current = NULL;

  // note that the loop iterates over list but also over array (i)
  int i = 0;
  for(current = userListHead; current != NULL; current = current->next) {
    jstring juser = (*env)->NewStringUTF(env, current->string);
    if (juser == NULL) {
      errorMessage = "java/lang/OutOfMemoryError";
      goto END;
    }
    (*env)->SetObjectArrayElement(env, jusers, i++, juser);
  }


END:

  // cleanup
  if(cgroup) { (*env)->ReleaseStringUTFChars(env, jgroup, cgroup); }
  if(setnetgrentCalledFlag) { endnetgrent(); }
  while(userListHead) {
    UserList *current = userListHead;
    userListHead = userListHead->next;
    if(current->string) { free(current->string); }
    free(current);
  }

  // return results or THROW
  if(errorMessage) {
    THROW(env, errorMessage, NULL);
    return NULL;
  } else {
    return jusers;
  }
}
