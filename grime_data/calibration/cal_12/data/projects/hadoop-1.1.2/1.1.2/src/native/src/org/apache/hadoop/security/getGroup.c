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
#include <grp.h>
#include <stdio.h>
#include <unistd.h>
#include <pwd.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>

#define MAX(a, b) (a > b ? a : b)
/*Helper functions for the JNI implementation of unix group mapping service*/


/**
 * Gets the group IDs for a given user. The groups argument is allocated
 * internally, and it contains the list of groups. The ngroups is updated to 
 * the number of groups
 * Returns 0 on success (on success, the caller must free the memory allocated
 * internally)
 */
int getGroupIDList(const char *user, int *ngroups, gid_t **groups) {
  *ngroups = 0;
  char *pwbuf = NULL;
  *groups = NULL;
  /*Look up the password database first*/
  int error = getPW(user, &pwbuf);
  if (error != 0) {
    if (pwbuf != NULL) {
      free(pwbuf);
    }
    return error;
  } 
  struct passwd *pw = (struct passwd*)pwbuf;
  int ng = 0;
  /*Get the groupIDs that this user belongs to*/
  if (getgrouplist(user, pw->pw_gid, NULL, &ng) < 0) {
    *ngroups = ng;
    *groups = (gid_t *) malloc(ng * sizeof (gid_t));
    if (!*groups) {
      free(pwbuf);
      return ENOMEM;
    }
    if (getgrouplist(user, pw->pw_gid, *groups, &ng) < 0) {
      free(pwbuf);
      free(*groups);
      *groups = NULL;
      return ENOENT;
    }
  }
  free(pwbuf);
  return 0;
}

/**
 * Gets the group structure for a given group ID. 
 * The grpBuf argument is allocated internally and it contains the 
 * struct group for the given group ID. 
 * Returns 0 on success (on success, the caller must free the memory allocated
 * internally)
 */
int getGroupDetails(gid_t group, char **grpBuf) {
  struct group * grp = NULL;
  size_t currBufferSize = MAX(sysconf(_SC_GETGR_R_SIZE_MAX), 2048);
  *grpBuf = NULL; 
  char *buf = (char*)malloc(sizeof(char) * currBufferSize);

  if (!buf) {
    return ENOMEM;
  }
  int error;
  for (;;) {
    error = getgrgid_r(group, (struct group*)buf,
                       buf + sizeof(struct group),
                       currBufferSize - sizeof(struct group), &grp);
    if(error != ERANGE) {
       break;
    }
    free(buf);
    currBufferSize *= 2;
    buf = malloc(sizeof(char) * currBufferSize);
    if(!buf) {
      return ENOMEM;
    }
  }
  if(!grp && !error) {
    free(buf);
    return ENOENT;
  } else  if (error) {
    free(buf);
    return error;
  }
  *grpBuf = buf;
  return 0;
}

/**
 * Gets the password database entry for a given user. 
 * The pwbuf argument is allocated internally and it contains the 
 * broken out fields for the password database entry
 * Returns 0 on success (on success, the caller must free the memory allocated 
 * internally).
 */
int getPW(const char *user, char **pwbuf) {
  struct passwd *pwbufp = NULL;
  size_t currBufferSize = MAX(sysconf(_SC_GETPW_R_SIZE_MAX), 2048);
  *pwbuf = NULL;
  char *buf = (char*)malloc(sizeof(char) * currBufferSize);
  
  if (!buf) {
    return ENOMEM;
  } 
  int error;
  
  for (;;) {
    error = getpwnam_r(user, (struct passwd*)buf, buf + sizeof(struct passwd),
                       currBufferSize - sizeof(struct passwd), &pwbufp);
    if (error != ERANGE) {
      break;
    }
    free(buf);
    currBufferSize *= 2;
    buf = (char*)malloc(sizeof(char) * currBufferSize);
    if (!buf) {
      return ENOMEM;
    }
  } 
  if (!pwbufp && !error) {
    free(buf);
    return ENOENT;
  } else  if (error) {
    free(buf);
    return error;
  }
  *pwbuf = buf;
  return 0;
} 

#undef TESTING

#ifdef TESTING
int main(int argc, char **argv) {
  int ngroups;
  gid_t *groups = NULL;
  char *user = "ddas";
  if (argc == 2) user = argv[1];
  int error = getGroupIDList(user, &ngroups, &groups);
  if (error != 0) {
    printf("Couldn't obtain grp for user %s", user);
    return;
  }
  int i;
  for (i = 0; i < ngroups; i++) {
    char *grpbuf = NULL;
    error = getGroupDetails(groups[i], &grpbuf);
    printf("grps[%d]: %s ",i, ((struct group*)grpbuf)->gr_name);
    free(grpbuf);
  }
  free(groups);
  return 0;
}
#endif
