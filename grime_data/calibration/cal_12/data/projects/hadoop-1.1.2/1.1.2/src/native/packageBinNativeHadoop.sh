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


# packageBinNativeHadoop.sh - A simple script to help package a single set of 
#     native-hadoop libraries for bin-packaging

#
# Note: 
# This script relies on the following environment variables to function correctly:
#  * BASE_NATIVE_LIB_DIR
#  * BUILD_NATIVE_DIR
#  * DIST_LIB_DIR
#  * NATIVE_PLATFORM
# All these are setup by build.xml.
#

TAR='tar cf -'
UNTAR='tar xfBp -'
platform=$NATIVE_PLATFORM

# Copy the pre-built libraries in $BASE_NATIVE_LIB_DIR
if [ -d $BASE_NATIVE_LIB_DIR ]
then
  echo "Copying libraries in $BASE_NATIVE_LIB_DIR/$platform to $DIST_LIB_DIR/"
  if [ ! -d $BASE_NATIVE_LIB_DIR/$platform ]
  then 
    echo "ERROR: Platform $platform does not exist in $BASE_NATIVE_LIB_DIR"
    exit -1
  fi
  cd $BASE_NATIVE_LIB_DIR/$platform/
  $TAR *hadoop* | (cd $DIST_LIB_DIR/; $UNTAR)
fi

# Copy the custom-built libraries in $BUILD_NATIVE_DIR
if [ -d $BUILD_NATIVE_DIR ]
then 
  echo "Copying libraries in $BUILD_NATIVE_DIR/$platform/lib to $DIST_LIB_DIR/"
  if [ ! -d $BUILD_NATIVE_DIR/$platform ]
  then
    echo "ERROR: Platform $platform does not exist in $BUILD_NATIVE_DIR"
    exit -1
  fi
  cd $BUILD_NATIVE_DIR/$platform/lib
  $TAR *hadoop* | (cd $DIST_LIB_DIR/; $UNTAR)
fi

