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

# set to true to debug! this will cause the program not to actually do anything
# but will let you see what the target file and directory would have been, had
# you acted.
debug=false

scriptDir=`dirname $0`
scriptName=`basename $0`

if [ "x$scriptDir" == "x." ]; then
    scriptDir=`pwd`"/$scriptDir"
fi

projectDir=`echo $scriptDir | sed "s/\/build\/src\/sh//"`
testWebDir="$projectDir/testWeb"
srcWeb="$testWebDir/src/web"
srcXML="$testWebDir/src/xml"
classesDir="$srcWeb/WEB-INF/classes"

# put out some debugging info at the start
if [ "x$debug" == "xtrue" ]; then
    echo "DEBUG MODE: script will not act."
    echo "script dir: $scriptDir"
    echo "project dir: $projectDir"
    echo "testWeb dir: $testWebDir"
    echo
fi

# work out the target directory and file name based on the system and input file
getTarget() {
    local system=$1
    local file=$2
    targetFile="$file"
    targetDir="$system"

    if [ "x$targetDir" = "xaddressbook" ]; then
      targetDir=addressBook
    fi
    if [ "x$targetDir" = "xcore" ]; then
      targetDir='.'
    fi
    if [ "x$targetDir" = "xsearch" ]; then
      targetDir='.'
    fi
    if [ "x$targetDir" = "xsecurity" ]; then
      targetDir='.'
    fi
    if [ "x$targetDir" = "xwar" ]; then
      targetDir='.'
    fi
    if [ "x$targetDir" = "xwebgui" ]; then
      targetDir='.'
    fi
    if [ "x$targetDir" = "xwebmail" ]; then
      targetDir=mail
    fi
    if [ "x$targetDir" = "xweb" ]; then
      targetDir='.'
    fi
    if [ "x$targetDir" = "xwebtheme" ]; then
      targetDir=theme
    fi
    # everything which is in a WEB-INF directory, goes to the WEB-INF directory!
    test=`echo "x$file" | grep "^xWEB-INF"`
    if [ "x$test" != "x" ]; then
      targetDir='.'
    fi
    # if you specified a style, that goes into the style directory
    test=`echo "x$file" | grep "^xstyle"`
    if [ "x$test" != "x" ]; then
      # some juggling here - the targetdir comes after the style,
      # and after the template too, if there is one
      test=`echo "x$file" | grep "^xstyle.template"`
      if [ "x$test" != "x" ]; then
          targetFile=`echo $file | sed "s/^style\/template\///"`
          if [ "x$targetDir" = "xtheme" ]; then
            targetDir=style/template
          else
            targetDir=style/template/$targetDir
          fi
      else
          targetFile=`echo $file | sed "s/^style\///"`
          targetDir=style/$targetDir
      fi
    fi

    # append the path to the target dir - split off just the file name with no
    # path
    targetSubDir=`dirname $targetFile`
    if [ "z$targetSubDir" != "z." ]; then
        targetDir="$targetDir/$targetSubDir"
    fi

    targetFile=`basename $targetFile`
    # if debug mode, out out some info
    if [ "x$debug" == "xtrue" ]; then
        echo "system: $system"
        echo "target dir: $targetDir"
        echo "target file: $targetFile"
    fi

}

# if you create a symlink called igwrm to this script, it can be used to delete
# the links individually. useful when you want to rename/delete a jsp
if [ "z$scriptName" == "zigwrm" ]; then
    system="$1"
    file="$2"
    if [ "z$system" == "z" ] || [ "z$file" == "z" ]; then
        echo "Useage: "
        echo "       $scriptName {system} {file}"
        echo
        echo " where:"
        echo "       system:   one of the project subprojects, such as addressbook,"
        echo "                     calendar, core, library, etc."
        echo "       file:     name of a source file in that directory."
        echo
        echo "See documentation at the start of this script for more information "
        echo "and examples."
        exit -1;
    fi
    getTarget $system $file

    thisSourceFile="$projectDir/$system/src/web/$file"
    link="$srcWeb/$targetDir/$targetFile"
    if [ "x$debug" == "xtrue" ]; then
        echo "would remove file:"
        echo "  $thisSourceFile"
        echo "would remove link:"
        echo "  $link"
        echo
    else
        rm -vi "$thisSourceFile"
        rm -v "$link"
    fi
else
    # we're setting up a new copy of the webdir - clear any existing one
    # only do this if the first param is "clear"
    if [ "x$debug" != "xtrue" ]; then
        if [ "z$1" == "zclear" ]; then
            echo "Removing previous web dir..."
            if test -e  "$srcWeb"; then
                rm -rf "$srcWeb"
            fi
        fi
        if test ! -e "$srcWeb"; then
            mkdir -p "$srcWeb"
        fi
    fi

    cd $projectDir/..
    cvslocal=`pwd`
    for webFile in `find ivatagroupware/*/src/web ivatagroupware/package/*/src/web ivatamasks/*/src/web -type f | grep -v CVS | grep -v ivatamasks/demo | grep -v testWeb`;
    do
        sourceDir=`echo $webFile | sed "s/\/src.*//"`
        system=`echo $sourceDir | sed "s/ivatagroupware\///"  | sed "s/package\///" | sed "s/ivatamasks\///" `
        file=`echo $webFile | sed "s/$system\/src\/web\///" | sed "s/ivatagroupware\///"  | sed "s/package\///" | sed "s/ivatamasks\///"`

        getTarget $system $file

        if [ "x$debug" == "xtrue" ]; then
            echo "web file: $webFile"
            if test ! -e "$srcWeb/$targetDir/$targetFile"; then
                echo "  link  $cvslocal/$webFile"
                echo "  to    $srcWeb/$targetDir/$targetFile"
            fi
            echo
        fi

        # only act if debug is not true
        if [ "x$debug" != "xtrue" ]; then
            if test ! -e "$srcWeb/$targetDir"; then
                mkdir -p "$srcWeb/$targetDir"
            fi
            # only create the link if it is not already there!
            if test ! -e "$srcWeb/$targetDir/$targetFile"; then
                ln -v "$cvslocal/$webFile" "$srcWeb/$targetDir/$targetFile"
            fi
        fi
    done
    # bind the hibernate and groovy files
    hibernateSourceDir="ivatagroupware/hibernate/target/xdoclet/hibernatedoclet"
    groovySourceDir="ivatagroupware"
    for hibernateFile in `find $hibernateSourceDir -name '*.xml'` `find "$groovySourceDir" -name '*.groovy' | grep -v classes | grep -v target`;
    do
        targetDir=`dirname $hibernateFile`
        targetDir=`echo $targetDir | sed "s/ivatagroupware\/hibernate\/target\/xdoclet\/hibernatedoclet[\/]*//"`
        targetDir=`echo $targetDir | sed "s/ivatagroupware\/[^\/]*\/src\/groovy[\/]*//"`
        targetDir=`echo $targetDir | sed "s/ivatagroupware\/package\/war\/src\/groovy[\/]*//"`
        targetFile=`basename $hibernateFile`
        if [ "x$debug" == "xtrue" ]; then
            echo "hibernate file: $hibernateFile"
            if test ! -e "$classesDir/$targetDir/$targetFile"; then
                echo "  link  $hibernateFile"
                echo "  to    $classesDir/$targetDir/$targetFile"
            fi
            echo
        fi

        # only act if debug is not true
        if [ "x$debug" != "xtrue" ]; then
            if test ! -e "$classesDir/$targetDir"; then
                mkdir -p "$classesDir/$targetDir"
            fi
            # only create the link if it is not already there!
            if test ! -e "$classesDir/$targetDir/$targetFile"; then
                ln -v "$hibernateFile" "$classesDir/$targetDir/$targetFile"
            fi
        fi
    done

    # copy conf files
    cd $projectDir/.. || die "Can't change to the project dir .. '$projectDir/..': $!";
    for confDir in `find ivatagroupware ivatamasks -name "conf" -type d`;
    do
        for confFile in `find "$confDir" -type f | grep -v CVS`;
        do
            targetFile=$confFile
            targetFile=`echo $targetFile | sed "s/.*src\/conf\///"`
            confTarget="$classesDir/$targetFile"
            confTargetDir=`dirname $confTarget`
            if test ! -e "$confTargetDir"; then
                mkdir -p "$confTargetDir"
            fi
            if test ! -e "$confTarget"; then
                if [ "x$debug" == "xtrue" ]; then
                    echo "Would copy conf file $confFile"
                else
                    cp -v "$confFile" "$confTarget"
                fi
            fi
        done
    done

    # go thro' all the web libraries - you must have run maven on the
    # ivatagroupware main project first :-)
    cd $projectDir || die "Can't change to the project dir '$projectDir': $!";
    libDir="$projectDir/package/war/target/ivatagroupware-war/WEB-INF/lib"
    if test -e "$libDir"; then
        libTargetDir="$srcWeb/WEB-INF/lib"
        if test ! -e "$libTargetDir"; then
            mkdir -p "$libTargetDir"
        fi
        for libSource in `find "$libDir" -type f | grep -v "ivatagroupware[-.a-zA-Z_0-9]*jar"  | grep -v "ivatamasks[-.a-zA-Z_0-9]*jar"` $projectDir/package/war/target/ivatagroupware-war/WEB-INF/lib/ivatagroupware-startdb*.jar;
        do
            jarFile=`basename "$libSource"`
            libTarget="$libTargetDir/$jarFile"
            if test ! -e "$libTarget"; then
                if [ "x$debug" == "xtrue" ]; then
                    echo "Would copy library $jarFile"
                else
                    cp -v "$libSource" "$libTarget"
                fi
            fi
        done
    else
        echo "No WEB-INF/lib found. Run maven first, then run $scriptName again to copy missing libraries."
    fi
fi

exit 0

