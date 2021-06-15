#!/bin/sh
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

args=$#

if [ "$args" -ne 2 ]; then
  echo "$0: script to copy over jsp files under development." 1>&2
  echo "    this script must be run from the openportal/war directory." 1>&2
  echo "" 1>&2
  echo "Usage: $0 {system} {file}" 1>&2
  echo "    where:" 1>&2
  echo "          {system}:     one of the ivata op subsystems (addressbook, core, etc.)" 1>&2
  echo "          {file}:       jsp file to be copied over" 1>&2
  exit -1;
fi

system=$1
file=$2
targetfile=$file
targetdir=$system


# some exceptions to the targetdir = system...
if [ "x$targetdir" = "xaddressbook" ]; then
  targetdir=addressBook
fi
if [ "x$targetdir" = "xcore" ]; then
  targetdir='.'
fi
if [ "x$targetdir" = "xsearch" ]; then
  targetdir='.'
fi
if [ "x$targetdir" = "xsecurity" ]; then
  targetdir='.'
fi
if [ "x$targetdir" = "xwar" ]; then
  targetdir='.'
  system=package/war
fi
if [ "x$targetdir" = "xwebgui" ]; then
  targetdir='.'
fi
if [ "x$targetdir" = "xwebmail" ]; then
  targetdir=mail
fi
# everything which is in a WEB-INF directory, goes to the WEB-INF directory!
dir=`dirname $file`
test=`echo "x$file" | grep "^WEB-INF"`
if [ "x$test" != "x" ]; then
  targetdir='.'
fi
# if you specified a style, that goes into the style directory
test=`echo "x$file" | grep "^xstyle"`
if [ "x$test" != "x" ]; then
  # some juggling here - the targetdir comes after the style
  targetfile=`echo $file | sed "s/^style\///"`
  targetdir=style/$targetdir
fi

cp -v ../../$system/src/web/$file target/ivatagroupware-war/$targetdir/$targetfile
exit 0;

