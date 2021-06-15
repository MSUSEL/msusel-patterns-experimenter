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

#be fussy about variables
use strict;

#platform specifics (disabled)
#use File::Spec::Functions;

#turn warnings on during dev; generates a few spurious uninitialised var access warnings
#use warnings;

#and set $debug to 1 to turn on trace info
my $debug=0;

#######################################################################
#
# check to make sure environment is setup
#

my $HOME = $ENV{ANT_HOME};
if ($HOME eq "")
        {
    die "\n\nANT_HOME *MUST* be set!\n\n";
        }

my $JAVACMD = $ENV{JAVACMD};
$JAVACMD = "java" if $JAVACMD eq "";

my $onnetware = 0;
if ($^O eq "NetWare")
{
  $onnetware = 1;
}

#ISSUE: what java wants to split up classpath varies from platform to platform 
#and perl is not too hot at hinting which box it is on.
#here I assume ":" 'cept on win32, dos, and netware. Add extra tests here as needed.
my $s=":";
if(($^O eq "MSWin32") || ($^O eq "dos") || ($^O eq "cygwin") ||
   ($onnetware == 1))
        {
        $s=";";
        }

#build up standard classpath
my $localpath=$ENV{CLASSPATH};
if ($localpath eq "")
        {
        print "warning: no initial classpath\n" if ($debug);
        $localpath="";
        }
if ($onnetware == 1)
{
# avoid building a command line bigger than 512 characters - make localpath
# only include the "extra" stuff, and add in the system classpath as an expanded
# variable. 
  $localpath="";
} 

#add jar files. I am sure there is a perl one liner to do this.
my $jarpattern="$HOME/lib/*.jar";
my @jarfiles =glob($jarpattern);
print "jarfiles=@jarfiles\n" if ($debug);
my $jar;
foreach $jar (@jarfiles )
        {
        $localpath.="$s$jar";
        }

#if Java home is defined, look for tools.jar & classes.zip and add to classpath
my $JAVA_HOME = $ENV{JAVA_HOME};
if ($JAVA_HOME ne "")
        {
        my $tools="$JAVA_HOME/lib/tools.jar";
        if (-e "$tools")
                {
                $localpath .= "$s$tools";
                }
        my $classes="$JAVA_HOME/lib/classes.zip";
        if (-e $classes)
                {
                $localpath .= "$s$classes";
                }
        }
else
        {
    print "\n\nWarning: JAVA_HOME environment variable is not set.\n".
                "If the build fails because sun.* classes could not be found\n".
                "you will need to set the JAVA_HOME environment variable\n".
                "to the installation directory of java\n";
        }

#set JVM options and Ant arguments, if any
my @ANT_OPTS=split(" ", $ENV{ANT_OPTS});
my @ANT_ARGS=split(" ", $ENV{ANT_ARGS});

#jikes
if($ENV{JIKESPATH} ne "")
        {
        push @ANT_OPTS, "-Djikes.class.path=$ENV{JIKESPATH}";
        }

#construct arguments to java
my @ARGS;
if ($onnetware == 1)
{
# make classpath literally $CLASSPATH; and then the contents of $localpath
# this is to avoid pushing us over the 512 character limit
# even skip the ; - that is already in $localpath
  push @ARGS, "-classpath", "\$CLASSPATH$localpath";
}
else
{
  push @ARGS, "-classpath", "$localpath";
}
push @ARGS, "-Dant.home=$HOME";
push @ARGS, @ANT_OPTS;
push @ARGS, "org.apache.tools.ant.Main", @ANT_ARGS;
push @ARGS, @ARGV;

print "\n $JAVACMD @ARGS\n\n" if ($debug);

my $returnValue = system $JAVACMD, @ARGS;
if ($returnValue eq 0)
        {
        exit 0;
        }
else
        {
        # only 0 and 1 are widely recognized as exit values
        # so change the exit value to 1
        exit 1;
        }
