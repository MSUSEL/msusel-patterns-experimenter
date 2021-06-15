#!/bin/bash
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


# error out if any statements fail
set -e
set -x

function usage() {
  echo "$0 [options] <tag> <branch> <user> <email>"
  echo
  echo " tag : Release tag (eg: 2.1.4, 2.2-beta1, ...)"
  echo " user:  Git username"
  echo " email: Git email"
  echo 
}

if [ -z $4 ]; then
  usage
  exit
fi

tag=$1
branch=$2
git_user=$3
git_email=$4

# load properties + functions
. "$( cd "$( dirname "$0" )" && pwd )"/properties
. "$( cd "$( dirname "$0" )" && pwd )"/functions

if [ `is_version_num $tag` == "0" ]; then
  echo "$tag is a not a valid release tag"
  exit 1
fi
if [ `is_primary_branch_num $tag` == "1" ]; then
  echo "$tag is a not a valid release tag, can't be same as primary branch name"
  exit 1
fi

pushd ../../ > /dev/null

init_git $git_user $git_email

# switch to the release branch
git checkout rel_$tag

# ensure no changes on it
set +e
git status | grep "working directory clean"
if [ "$?" == "1" ]; then
  echo "branch rel_$tag dirty, exiting"
  exit 1
fi
set -e

# deploy the release to maven repo
if [ -z $SKIP_DEPLOY ]; then
  mvn deploy -DskipTests -Dall
  mvn -P deploy.opengeo deploy -DskipTests -Dall
fi

# get <major.minor> for sf release dir
if [ "$( echo $str | egrep "[0-9]+\.[0-9]+((\.|-).*)?" )" != "$str" ]; then
  echo "$tag is not a valid release version number"
  exit 1
fi
dir=`echo $tag | sed 's/\([0-9]*\)\.\([0-9]*\).*/\1/g'`

pushd $DIST_PATH/$tag > /dev/null

#JD: disabling this for now... i think something recently changes on the sf
# server in that we can't use ssh directly to log in, need to find an 
# to this command for remotely creating a directory on the server
#ssh -i $SF_PK $SF_USER@$SF_HOST mkdir -p "/home/pfs/project/g/ge/geotools/GeoTools\ $dir\ Releases"
rsync -ave "ssh -i $SF_PK" *.zip $SF_USER@$SF_HOST:"/home/pfs/project/g/ge/geotools/GeoTools\ $dir\ Releases/$tag/"

popd > /dev/null

# tag the release branch
git tag $tag

# push up tag
git push origin $tag

popd > /dev/null
