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
#include "runAs.h"

/*
 * Function to get the user details populated given a user name. 
 */
int getuserdetail(char *user, struct passwd *user_detail) {
  struct passwd *tempPwdPtr;
  int size = sysconf(_SC_GETPW_R_SIZE_MAX);
  char pwdbuffer[size];
  if ((getpwnam_r(user, user_detail, pwdbuffer, size, &tempPwdPtr)) != 0) {
    fprintf(stderr, "Invalid user provided to getpwnam\n");
    return -1;
  }
  return 0;
}

/**
 * Function to switch the user identity and set the appropriate 
 * group control as the user specified in the argument.
 */
int switchuser(char *user) {
  //populate the user details
  struct passwd user_detail;
  if ((getuserdetail(user, &user_detail)) != 0) {
    return INVALID_USER_NAME;
  }
  //set the right supplementary groups for the user.
  if (initgroups(user_detail.pw_name, user_detail.pw_gid) != 0) {
    fprintf(stderr, "Init groups call for the user : %s failed\n",
        user_detail.pw_name);
    return INITGROUPS_FAILED;
  }
  errno = 0;
  //switch the group.
  setgid(user_detail.pw_gid);
  if (errno != 0) {
    fprintf(stderr, "Setgid for the user : %s failed\n", user_detail.pw_name);
    return SETUID_OPER_FAILED;
  }
  errno = 0;
  //swith the user
  setuid(user_detail.pw_uid);
  if (errno != 0) {
    fprintf(stderr, "Setuid for the user : %s failed\n", user_detail.pw_name);
    return SETUID_OPER_FAILED;
  }
  errno = 0;
  //set the effective user id.
  seteuid(user_detail.pw_uid);
  if (errno != 0) {
    fprintf(stderr, "Seteuid for the user : %s failed\n", user_detail.pw_name);
    return SETUID_OPER_FAILED;
  }
  return 0;
}

/*
 * Top level method which processes a cluster management
 * command.
 */
int process_cluster_command(char * user,  char * node , char *command) {
  char *finalcommandstr;
  int len;
  int errorcode = 0;
  if (strncmp(command, "", strlen(command)) == 0) {
    fprintf(stderr, "Invalid command passed\n");
    return INVALID_COMMAND_PASSED;
  }
  len = STRLEN + strlen(command);
  finalcommandstr = (char *) malloc((len + 1) * sizeof(char));
  snprintf(finalcommandstr, len, SCRIPT_DIR_PATTERN, HADOOP_HOME,
      command);
  finalcommandstr[len + 1] = '\0';
  errorcode = switchuser(user);
  if (errorcode != 0) {
    fprintf(stderr, "switch user failed\n");
    return errorcode;
  }
  errno = 0;
  execlp(SSH_COMMAND, SSH_COMMAND, node, finalcommandstr, NULL);
  if (errno != 0) {
    fprintf(stderr, "Excelp failed dude to : %s\n", strerror(errno));
  }
  return 0;
}

/*
 * Process cluster controller command the API exposed to the 
 * main in order to execute the cluster commands.
 */
int process_controller_command(char *user, char * node, char *command) {
  return process_cluster_command(user, node, command);
}
