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

package Exim;
################################################################################
# $Id: Exim.pm,v 1.2 2005/04/09 17:19:52 colinmacleod Exp $
#
# Routines shared by scripts which access the exim mail system. Although this
# is called Exim.pm, it works equally well for Sendmail/Postfix systems - Exim
# was just the first system we created the script for, and it's shorter to
# write :-)
#
# Since: ivata groupware 0.9 (2002-07-21)
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
################################################################################
#
# $Log: Exim.pm,v $
# Revision 1.2  2005/04/09 17:19:52  colinmacleod
# Changed copyright text to GPL v2 explicitly.
#
# Revision 1.1.1.1  2005/03/10 17:49:17  colinmacleod
# Restructured ivata op around Hibernate/PicoContainer.
# Renamed ivata groupware.
#
# Revision 1.3  2004/11/03 18:34:07  colinmacleod
# Removed getGroupId method - use GID var in Site.pm instead.
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
# Revision 1.1  2003/02/24 19:09:24  colin
# moved to business
#
# Revision 1.5  2003/01/18 20:22:47  colin
# changes to get mail working with postfix, courier-imap and gentoo
#
# Revision 1.4  2002/11/07 15:53:45  ivatashop
# new function getGroupId to get id of group called SITE_ID
#
# Revision 1.3  2002/10/25 07:29:36  jano
# we have new method for converting arguments fro command line
#
# Revision 1.2  2002/10/24 11:51:26  colin
# Testing in London Red Hat/Sub Cobalt server
#
# Revision 1.1  2002/08/11 11:44:27  colin
# first version
# scripts written for and tested on Debian exim server
#
################################################################################

################################################################################
# includes
use strict;
use Site;
################################################################################

################################################################################
# exports
use vars qw(@ISA @EXPORT $VERSION);
use Exporter;
$VERSION = 1.00;              # Or higher
@ISA = qw(Exporter) ;

# Symbols to autoexport (:DEFAULT tag)
@EXPORT      = qw(
                  checkUserExists
                  lockFile
                  unlockAndDie
                  unlockFile
                  convertCommandLine
                  dieInvalidArguments
                 );
################################################################################

################################################################################
# variable declarations
################################################################################

################################################################################
# function prototypes
sub checkUserExsits($);
sub lockFile($);
sub unlockAndDie($$);
sub unlockFile($);
################################################################################

################################################################################
# checkUserExists: check a user exists and throw an error if she doesn't
# params:
#        user name
# returns:
#        error message if the user does not exist
sub checkUserExists($) {
  my $user = shift;

  my $foundUser = 0;
  my @userDetails;
  setpwent();
 FIND_USER:
  while (@userDetails = getpwent()) {
    if($userDetails[0] eq $user) {
      $foundUser = 1;
      last FIND_USER;
    }
  }
  endpwent();

  # if the user was not found, break down and die here...
  return "User $user not in system. Please add the user before creating this list.\n" unless($foundUser);
  return 0;
}
################################################################################
# dieInvalidArguments: die because program arguments incorrect: go through all
#     arguments and send them to the standard error stream
# params:
#        lock file name
# returns:
#        NONE
sub dieInvalidArguments() {
  my $dyingGasp = "Invalid program arguments ";
  my $argument;
  foreach $argument(@ARGV) {
    $dyingGasp .= "'$argument' ";
  }
  die "$dyingGasp\n";
}
################################################################################
# lockFile: see if the specified lock file exists. If it does wait until it is
#     free, and then create a new file.
# params:
#        lock file name
# returns:
#        NONE
sub lockFile($) {
  my $lockFile = shift;

  # check there is no lock file
  my $timeOut = time() + $LOCKFILE_WAIT;
  while((-e $lockFile) && (time() < $timeOut)) {
    ;
  }
  die "Timeout while waiting for the lockfile '$lockFile' to be removed." if(-e $lockFile);
  system("$TOUCH $lockFile")  && die("Could not create the file '$lockFile': $!");
}

##########################################################
# unlockAndDie: unlock a file and die
# params:
#        file to unlock
#        fatal error message
# returns:
#        NONE
sub unlockAndDie($$) {
  my $lockFile = shift;
  my $dyingGasp = shift;
  unlockFile($lockFile);
  die $dyingGasp;
}

################################################################################
# unlockFile: remove a lock file
# params:
#        lock file name
# returns:
#        NONE
sub unlockFile($) {
  unlink(shift());
}
################################################################################

################################################################################
# convertCommandLine: converting from URL format to ASCI
# params:
#        string to convert
# returns:
#        converted string
sub convertCommandLine($) {
  my $in = shift;

    # rubbish in, rubbish out!
    return $in
        unless defined($in);

  my $out;

  my $tmp_i = 0;
  my $tmp_ii = 0;
  my $tmp_ch = "";
  while ($in =~ /(.)/g) {
    if ($1 =~ /^[%]+$/) {
      $tmp_i++;
      $tmp_ch=substr($in,$tmp_i,2);
      if (hex($tmp_ch)==0) {
        $out = "";
      } else {
        $out .= chr(hex($tmp_ch));
      }
      $tmp_i+=2;
      $tmp_ii=3;
    } elsif ($tmp_ii==0){
      $out .= $1;
      $tmp_i++;
    }
    if ($tmp_ii>0) {
      $tmp_ii--;
    }
  }

  return $out;
}
################################################################################

################################################################################
# return true;
-1;
################################################################################

