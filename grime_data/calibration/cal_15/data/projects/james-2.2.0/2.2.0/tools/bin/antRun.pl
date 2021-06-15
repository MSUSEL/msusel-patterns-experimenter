#!/usr/bin/perl
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

#######################################################################
#
# antRun.pl
#
# wrapper script for invoking commands on a platform with Perl installed
# this is akin to antRun.bat, and antRun the SH script 
#
# created:         2001-10-18
# last modified:   2001-11-13
# author:          Jeff Tulley jtulley@novell.com 
#######################################################################
#be fussy about variables
use strict;

#turn warnings on during dev; generates a few spurious uninitialised var access warnings
#use warnings;

#and set $debug to 1 to turn on trace info (currently unused)
my $debug=1;

#######################################################################
# change drive and directory to "%1"
my $ANT_RUN_CMD = @ARGV[0];

# assign current run command to "%2"
chdir (@ARGV[0]) || die "Can't cd to $ARGV[0]: $!\n";
if ($^O eq "NetWare") {
    # There is a bug in Perl 5 on NetWare, where chdir does not
    # do anything.  On NetWare, the following path-prefixed form should 
    # always work. (afaict)
    $ANT_RUN_CMD .= "/".@ARGV[1];
}
else {
    $ANT_RUN_CMD = @ARGV[1];
}

# dispose of the first two arguments, leaving only the command's args.
shift;
shift;

# run the command
my $returnValue = system $ANT_RUN_CMD, @ARGV;
if ($returnValue eq 0) {
    exit 0;
}
else {
    # only 0 and 1 are widely recognized as exit values
    # so change the exit value to 1
    exit 1;
}
