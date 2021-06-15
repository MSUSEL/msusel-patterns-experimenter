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

package Site;   # emacs mode -*-perl-*-
################################################################################
# $Id: Site.pm,v 1.2 2005/04/09 17:19:52 colinmacleod Exp $
#
# All site settings should be stored in this file. This should be the only file
# which needs to be customized for a particular Linux distribution
#
# This is the site customization file for Debian GNU/Linux. If you want to
# change these values, please do not change the contents of this file. Instead
# create a file in the same directory called "local.pl" with the values you'd
# like to use.
#
# Since: ivata groupware 0.9 (2002-09-13)
# Author: Colin MacLeod <colin.macleod@ivata.com>
# $Revision: 1.2 $
#
################################################################################
# Copyright (c) 2001 - 2005 ivata limited.
# All rights reserved.
# ---------------------------------------------------------
# ivata groupware may be redistributed under the GNU General Public
# License as published by the Free Software Foundation;
# version 2 of the License.
#
# These programs are free software; you can redistribute them and/or
# modify them under the terms of the GNU General Public License
# as published by the Free Software Foundation; version 2 of the License.
#
# These programs are distributed in the hope that they will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
#
# See the GNU General Public License in the file LICENSE.txt for more
# details.
#
# If you would like a copy of the GNU General Public License write to
#
# Free Software Foundation, Inc.
# 59 Temple Place - Suite 330
# Boston, MA 02111-1307, USA.
#
#
# To arrange commercial support and licensing, contact ivata at
#                  http://www.ivata.com/contact.jsp
#
################################################################################
#
# $Log: Site.pm,v $
# Revision 1.2  2005/04/09 17:19:52  colinmacleod
# Changed copyright text to GPL v2 explicitly.
#
# Revision 1.1.1.1  2005/03/10 17:49:14  colinmacleod
# Restructured ivata op around Hibernate/PicoContainer.
# Renamed ivata groupware.
#
# Revision 1.1  2004/11/03 18:23:24  colinmacleod
# Restructured Site.pm files.
#
# Revision 1.2  2004/03/21 21:16:32  colinmacleod
# Shortened name to ivata op.
#
# Revision 1.1  2004/02/05 21:54:07  colinmacleod
# Moved from webmail subproject.
#
# Revision 1.1.1.1  2004/01/27 20:59:57  colinmacleod
# Moved ivata op to sourceforge.
#
# Revision 1.2  2003/10/15 14:11:33  colin
# fixing for XDoclet
#
# Revision 1.3  2003/03/19 12:28:25  jano
# home directory of user on london server is diffrent, we have more customers sites
# FOLDER_PATH
#
# Revision 1.2  2003/02/25 09:15:57  nightly
# Added FOLDER_NAME_REGEX
#
# Revision 1.1  2003/02/24 19:09:24  colin
# moved to business
#
# Revision 1.8  2003/01/18 20:22:47  colin
# changes to get mail working with postfix, courier-imap and gentoo
#
# Revision 1.7  2002/11/15 12:21:39  colin
# fixed popauth changes to work with redhat/cobalt in London
#
# Revision 1.6  2002/11/15 10:46:10  colin
# added popauth setting to setPassword
#
# Revision 1.5  2002/10/31 15:12:30  ivatashop
# added virtusertable functionality for user aliases
#
# Revision 1.4  2002/10/25 14:07:01  jano
# directory of users is diffrent
#
# Revision 1.3  2002/10/25 12:05:57  jano
# whne user is creating make a home directory
#
# Revision 1.2  2002/10/25 07:29:52  jano
# new enviroment variable
#
# Revision 1.1  2002/10/24 11:56:22  colin
# First version of debian site settings
#
# Revision 1.1  2002/08/11 11:44:27  colin
################################################################################

################################################################################
# exports
use vars qw(@ISA @EXPORT $VERSION);
use Exporter;
use File::Basename qw(dirname);


$VERSION = 1.00;              # Or higher
@ISA = qw(Exporter);

# Symbols to autoexport (:DEFAULT tag)
@EXPORT      = qw(
                  $ALIASES_FILE
                  $ALIASES_VIRTUSERTABLE
                  $ALIASES_VIRTUSERTABLE_FILE
                  $DIR
                  $DOT_FORWARD
                  $DOT_FORWARD_OLD
                  $FOLDER_NAME_REGEX
                  $FOLDER_PATH
                  $FOLDER_PATH_INBOX
                  $FOLDER_PATH_MAILDIR
                  $FOLDER_PATH_MAILDIR_INBOX
                  $GID
                  $HOME
                  $HOME_DIR
                  $HOME_DIR_MODE
                  $LIST_DIRECTORY
                  $LOCKFILE_ALIASES
                  $LOCKFILE_PASSWD
                  $LOCKFILE_VIRTUSERTABLE
                  $LOCKFILE_WAIT
                  $MAIL_CYRUS
                  $MAIL_MAILDIRMAKE
                  $NEWALIASES
                  $PASSWD
                  %PASSWD_BAD_MESSAGES
                  $PASSWD_BAD_MESSAGE_DEFAULT
                  $PASSWD_COMMAND
                  $PASSWD_EXPECT
                  $PASSWD_EXPECT_BAD
                  $PASSWD_EXPECT_CONFIRM
                  $PASSWD_EXPECT_SUCCESS
                  $PASSWD_EXPECT_TIMEOUT
                  $PASSWD_POPAUTH
                  $PASSWD_POPAUTH_COMMAND
                  $PASSWD_POPAUTH_EXPECT
                  $PASSWD_POPAUTH_EXPECT_CONFIRM
                  $PASSWD_POPAUTH_LOG
                  $PASSWD_TRUNCATE
                  $PASSWD_SASL
                  $PASSWD_SASL_COMMAND
                  $PASSWD_SASL_EXPECT
                  $PASSWD_SASL_EXPECT_CONFIRM
                  $PASSWD_SLEEP
                  $SHADOW
                  $SHADOW_MIN_CHANGE
                  $SHADOW_MAX_CHANGE
                  $SHADOW_WARN_EXPIRE
                  $SHADOW_EXPIRE
                  $SHADOW_INACTIVE
                  $SHELL
                  $TOUCH
                  $USERADD
                  $USERDEL
                  $UID_FIRST
                 );
################################################################################

################################################################################
# prerequisites
unless(defined($ENV{"SITE_ID"})) {
        die "You must specify the environment variable SITE_ID!\n";
}
################################################################################

################################################################################
# variable declarations
# this is the file where the aliases are written and read from (getUserAliases/
# setUserAliases)
$ALIASES_FILE = "/etc/aliases";
# if set to non-zero (true) then the program uses the /etc/virtusertable file
# to map user aliases
$ALIASES_VIRTUSERTABLE = 0;
# this is the file where the aliases are written and read from (getUserAliases/
# setUserAliases) if ALIASES_VIRTUSERTABLE is non-zero
$ALIASES_VIRTUSERTABLE_FILE = "/etc/virtusertable";

# directory separator symbol
$DIR = "/";
# name of the forwarding file
$DOT_FORWARD=".forward";
# the name the forwarding file is changed to, if it is removed
$DOT_FORWARD_OLD=".forward.old";

# regular expression to remove the folder name from a full path
$FOLDER_NAME_REGEX="([A-Za-z]+)\$";
# folder paths for mail - use \$USER_NAME for the user name and \$FOLDER_NAME for the foldername
$FOLDER_PATH = "/home/\$USER_NAME/\$FOLDER_NAME";
$FOLDER_PATH_INBOX = "/var/spool/mail/\$USER_NAME";
# this is the path if you use maildir
$FOLDER_PATH_MAILDIR = "/home/\$USER_NAME/Maildir/\$FOLDER_NAME";
$FOLDER_PATH_MAILDIR_INBOX = "/home/\$USER_NAME/Maildir";

# group id to use - always the users group
($nameDummy, $passwdDummy, $GID) = getgrnam("users");

# path to the home directory
$HOME = "/home";
# chmod mode for new directories created in home dir
$HOME_DIR_MODE = 0755;
# paths for new home - use \$USER_NAME for the user name
$HOME_DIR = "\$HOME/\$USER_NAME";

# Location of lists directory. All lists are files containing the usernames.
# This directory must match the settings in /etc/exim.conf.
$LIST_DIRECTORY = "/etc/mail/lists/";
# lock file checks only one ivata intranet program is changing the alias
# file at a time
$LOCKFILE_ALIASES = "/var/lock/ivatagroupware/alias";
# lock file checks only one ivata intranet program is changing the password
# file at a time
$LOCKFILE_PASSWD = "/var/lock/ivatagroupware/passwd";
# lock file checks only one ivata intranet program is changing the /etc/virtusertable
# file at a time
$LOCKFILE_VIRTUSERTABLE = "/var/lock/ivatagroupware/virtusertable";
# this is the max. number of seconds to wait for the lock file to come free
$LOCKFILE_WAIT = 100;

# specify non-zero if using the cyrus imap server
$MAIL_CYRUS = 0;

# detects whether or not we're using Maildir format. If you are using Maildir
# you _must_ have the maildirmake program at the specified path
$MAIL_MAILDIRMAKE="/usr/bin/maildirmake";

# new aliases command, called after the aliases are created to make them
# take effect
$NEWALIASES = "/usr/bin/newaliases";

# path to the passwd file
$PASSWD = "/etc/passwd";
# map of bad messages to internationalized keys
# each of the keys in this hashmap is a regex which should be applied to the
# bad password message
%PASSWD_BAD_MESSAGES = (
            "not registered"                              => "password.error.bad.unregistered",       # user not registered
            "based on your username"                      => "password.error.bad.username",           # based on username
            "based"                                       => "password.error.bad.based",              # based on passwd details
            "derived"                                     => "password.error.bad.derived",            # derived from passwd details
            "derivable"                                   => "password.error.bad.derived",            # derivable from passwd details
            "short"                                       => "password.error.bad.short",              # too short/WAY too short
            "DIFFERENT"                                   => "password.error.bad.different",          # doesn't contain enough different chars
            "different"                                   => "password.error.bad.different",          # doesn't contain enough different chars
            "whitespace"                                  => "password.error.bad.whitespace",         # only contains whitespace
            "simplistic"                                  => "password.error.bad.simple",             # is too simple/systematic
            "simple"                                      => "password.error.bad.simple",             # is too simple/systematic
            "National Insurance"                          => "password.error.bad.national.insurance", # based on NI number
            "reversed"                                    => "password.error.bad.word.reversed",      # based on a reversed dictionary word or name
            "dictionary"                                  => "password.error.bad.word",               # based on a dictionary word or name
            "word"                                        => "password.error.bad.word",               # based on a dictionary word or name
            );
# key to use if none other recognized
$PASSWD_BAD_MESSAGE_DEFAULT = "password.error.bad.default";
# path to the passwd command for changing passwords
$PASSWD_COMMAND = "/usr/bin/passwd";
# the message the system sends to enter a password
$PASSWD_EXPECT = "Enter new UNIX password: ";
# the message the system sends to query a bad password (regex)
$PASSWD_EXPECT_BAD = "passwd: Authentication failure";
# the message the system sends when the password should be re-entered, to confirm
$PASSWD_EXPECT_CONFIRM = "Retype new UNIX password:";
# the message the system sends when the password is changed successfully
$PASSWD_EXPECT_SUCCESS = "passwd: password updated successfully";
# time to wait for passwd commands (seconds)
$PASSWD_EXPECT_TIMEOUT = 3;
# set to the maximum length of a password to truncate - leave 0 to never truncate
$PASSWD_TRUNCATE = 8;
# set to non-zero to use popauth security as well as unix passwd
$PASSWD_POPAUTH = 0;
# set to the command to run popauth (must include full path and any options needed)
$PASSWD_POPAUTH_COMMAND = "/usr/bin/popauth -user";
# the message the popauth system sends to enter a password
$PASSWD_POPAUTH_EXPECT = "New password:";
# the message the popauth system sends to confirm a password
$PASSWD_POPAUTH_EXPECT_CONFIRM = "Retype new password:";
# output for popauth commands is sent here - commands do not fail on popauth
$PASSWD_POPAUTH_LOG = "/var/log/popauth.log";
# set to non-zero to use sasl security as well as unix passwd
$PASSWD_SASL = 0;
# set to the command to run saslpasswd (must include full path and any options needed)
$PASSWD_SASL_COMMAND = "/usr/sbin/saslpasswd2 -c";
# the message the popauth system sends to enter a password
$PASSWD_SASL_EXPECT = "Password:";
# the message the popauth system sends to confirm a password
$PASSWD_SASL_EXPECT_CONFIRM = "Again .for verification.:";
# the amount of time to sleep before expecting a reply
# you can try reducing this to make the script; i've found it needs to be 3 on FC3
$PASSWD_SLEEP = 3;


# path to the shadow file
$SHADOW = "/etc/shadow";
# constants for setting the shadow file, if we need to generate the entry ourselves
$SHADOW_MIN_CHANGE=0;
$SHADOW_MAX_CHANGE=99999;
$SHADOW_WARN_EXPIRE=14;
$SHADOW_EXPIRE="";
$SHADOW_INACTIVE=7;
# default shell to be used
$SHELL = "/bin/badsh";

# system command for touch
$TOUCH = "/usr/bin/touch";

# system command for useradd: if specified, useradd is called to actually add
# the user in the addUser script: set to 0 if you do not want to use this
$USERADD = "/usr/sbin/useradd";
# system command for userdel: if specified, userdel is called to actually
# remove the user in removeUser: set to 0 if you do not want to use this
$USERDEL = "/usr/sbin/userdel";

# first user id to use, if there are no users in the system
$UID_FIRST = 500;
################################################################################

################################################################################
# see if there is a local config file for this OS to override the Debian values
$thisDirectory = dirname($0);
if (-e "$thisDirectory/os.pl") {
    do "$thisDirectory/os.pl";
}
# see if there is a user-defined local config file
if (-e "$thisDirectory/local.pl") {
    do "$thisDirectory/local.pl";
}
################################################################################

################################################################################
# return true;
-1;
################################################################################

