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

/**
 * The binary would be accepting the command of following format:
 * cluster-controller user hostname hadoop-daemon.sh-command
 */
int main(int argc, char **argv) {
  int errorcode;
  char *user;
  char *hostname;
  char *command;
  struct passwd user_detail;
  int i = 1;
  /*
   * Minimum number of arguments required for the binary to perform.
   */
  if (argc < 4) {
    fprintf(stderr, "Invalid number of arguments passed to the binary\n");
    return INVALID_ARGUMENT_NUMER;
  }

  user = argv[1];
  if (user == NULL) {
    fprintf(stderr, "Invalid user name\n");
    return INVALID_USER_NAME;
  }

  if (getuserdetail(user, &user_detail) != 0) {
    fprintf(stderr, "Invalid user name\n");
    return INVALID_USER_NAME;
  }

  if (user_detail.pw_gid == 0 || user_detail.pw_uid == 0) {
      fprintf(stderr, "Cannot run tasks as super user\n");
      return SUPER_USER_NOT_ALLOWED_TO_RUN_COMMANDS;
  }

  hostname = argv[2];
  command = argv[3];
  return process_controller_command(user, hostname, command);
}
