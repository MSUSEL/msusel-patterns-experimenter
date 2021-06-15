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

#include <pthread.h>
#include <grp.h>
#include <pwd.h>
#include <stdlib.h>

#include "fuse_dfs.h"



#if PERMS
/**
 * getpwuid and getgrgid return static structs so we safeguard the contents
 * while retrieving fields using the 2 structs below.
 * NOTE: if using both, always get the passwd struct firt!
 */
pthread_mutex_t passwdstruct_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t groupstruct_mutex = PTHREAD_MUTEX_INITIALIZER;
#endif

#if PERMS

/**
 * Utility for getting the user making the fuse call in char * form
 * NOTE: if non-null return, the return must be freed by the caller.
 */
char *getUsername(uid_t uid)
{
  //
  // Critical section - protect from concurrent calls in different threads.
  // since the struct below is static.
  // (no returns until end)
  //

  pthread_mutex_lock(&passwdstruct_mutex);

  struct passwd *userinfo = getpwuid(uid);
  char * ret = userinfo && userinfo->pw_name ? strdup(userinfo->pw_name) : NULL;

  pthread_mutex_unlock(&passwdstruct_mutex);

  //
  // End critical section 
  // 
  return ret;
}

/**
 * Cleans up a char ** group pointer
 */

void freeGroups(char **groups, int numgroups) {
  if (groups == NULL) {
    return;
  }
  int i ;
  for (i = 0; i < numgroups; i++) {
    free(groups[i]);
  }
  free(groups);
}

#define GROUPBUF_SIZE 5

char *getGroup(gid_t gid) {
  //
  // Critical section - protect from concurrent calls in different threads.
  // since the struct below is static.
  // (no returns until end)
  //

  pthread_mutex_lock(&groupstruct_mutex);

  struct group* grp = getgrgid(gid);
  char * ret = grp && grp->gr_name ? strdup(grp->gr_name) : NULL;

  //
  // End critical section 
  // 
  pthread_mutex_unlock(&groupstruct_mutex);

  return ret;
}


/**
 * Utility for getting the group from the uid
 * NOTE: if non-null return, the return must be freed by the caller.
 */
char *getGroupUid(uid_t uid) {
  //
  // Critical section - protect from concurrent calls in different threads
  // since the structs below are static.
  // (no returns until end)
  //

  pthread_mutex_lock(&passwdstruct_mutex);
  pthread_mutex_lock(&groupstruct_mutex);

  char *ret = NULL;
  struct passwd *userinfo = getpwuid(uid);
  if (NULL != userinfo) {
    struct group* grp = getgrgid( userinfo->pw_gid);
    ret = grp && grp->gr_name ? strdup(grp->gr_name) : NULL;
  }

  //
  // End critical section 
  // 
  pthread_mutex_unlock(&groupstruct_mutex);
  pthread_mutex_unlock(&passwdstruct_mutex);

  return ret;
}


/**
 * lookup the gid based on the uid
 */
gid_t getGidUid(uid_t uid) {
  //
  // Critical section - protect from concurrent calls in different threads
  // since the struct below is static.
  // (no returns until end)
  //

  pthread_mutex_lock(&passwdstruct_mutex);

  struct passwd *userinfo = getpwuid(uid);
  gid_t gid = userinfo == NULL ? 0 : userinfo->pw_gid;

  //
  // End critical section 
  // 
  pthread_mutex_unlock(&passwdstruct_mutex);

  return gid;
}

/**
 * Utility for getting the groups for the user making the fuse call in char * form
 */
char ** getGroups(uid_t uid, int *num_groups)
{
  char *user = getUsername(uid);

  if (user == NULL)
    return NULL;

  char **groupnames = NULL;

  // see http://www.openldap.org/lists/openldap-devel/199903/msg00023.html

  //#define GETGROUPS_T 1 
#ifdef GETGROUPS_T
  *num_groups = GROUPBUF_SIZE;

  gid_t* grouplist = malloc(GROUPBUF_SIZE * sizeof(gid_t)); 
  assert(grouplist != NULL);
  gid_t* tmp_grouplist; 
  int rtr;

  gid_t gid = getGidUid(uid);

  if ((rtr = getgrouplist(user, gid, grouplist, num_groups)) == -1) {
    // the buffer we passed in is < *num_groups
    if ((tmp_grouplist = realloc(grouplist, *num_groups * sizeof(gid_t))) != NULL) {
      grouplist = tmp_grouplist;
      getgrouplist(user, gid, grouplist, num_groups);
    }
  }

  groupnames = (char**)malloc(sizeof(char*)* (*num_groups) + 1);
  assert(groupnames);
  int i;
  for (i=0; i < *num_groups; i++)  {
    groupnames[i] = getGroup(grouplist[i]);
    if (groupnames[i] == NULL) {
      fprintf(stderr, "error could not lookup group %d\n",(int)grouplist[i]);
    }
  } 
  free(grouplist);
  assert(user != NULL);
  groupnames[i] = user;
  *num_groups = *num_groups + 1;
#else

  int i = 0;
  assert(user != NULL);
  groupnames[i] = user;
  i++;

  groupnames[i] = getGroupUid(uid);
  if (groupnames[i]) {
    i++;
  }

  *num_groups = i;

#endif
  return groupnames;
}
#endif
