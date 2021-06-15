#
# The MIT License (MIT)
#
# MSUSEL Arc Framework
# Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
# Software Engineering Laboratory and Idaho State University, Informatics and
# Computer Science, Empirical Software Engineering Laboratory
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#

# use virtusertable file
$ALIASES_VIRTUSERTABLE = -1;

# use cyrus
$MAIL_CYRUS = 1;

# use saslpasswd
$PASSWD_SASL = 1;

$TOUCH = "/bin/touch";
# id of the first user
$UID_FIRST = 500;
# use the add/del user scripts
$USERADD = "/usr/sbin/useradd";
$USERDEL = "/usr/sbin/userdel";

# this should match the output from the passwd command
$PASSWD_EXPECT = "New UNIX password: ";
# type a short password to check this matches
$PASSWD_EXPECT_BAD = "BAD PASSWORD: [^\\n]+\\n";
# the message the system sends when the password should be re-entered, to confirm
$PASSWD_EXPECT_CONFIRM = "Retype new UNIX password:";
# this should match the output from the passwd command on successs
$PASSWD_EXPECT_SUCCESS = "passwd: all authentication tokens updated successfully";
$PASSWD_POPAUTH_COMMAND = "/usr/sbin/popauth -user";
################################################################################

