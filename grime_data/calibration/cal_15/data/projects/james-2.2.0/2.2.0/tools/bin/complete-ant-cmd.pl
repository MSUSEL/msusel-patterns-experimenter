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

my $cmdLine = $ENV{'COMP_LINE'};
my $antCmd = $ARGV[0];
my $word = $ARGV[1];

my @completions;
if ($word =~ /^-/) {
    list( restrict( $word, getArguments() ));
} elsif ($cmdLine =~ /-(f|buildfile)\s+\S*$/) {
    list( getBuildFiles($word) );
} else {
    list( restrict( $word, getTargets() ));
}

exit(0);

sub list {
    for (@_) {
        print "$_\n";
    }
}

sub restrict {
    my ($word, @completions) = @_;
    grep( /^\Q$word\E/, @completions );
}

sub getArguments {
    qw(-buildfile -debug -emacs -f -find -help -listener -logfile 
       -logger -projecthelp -quiet -verbose -version); 
}


sub getBuildFiles {
    my ($word) = @_;
    grep( /\.xml$/, glob( "$word*" ));
}

sub getTargets {

    # Look for build-file
    my $buildFile = 'build.xml';
    if ($cmdLine =~ /-(f|buildfile)\s+(\S+)/) {
        $buildFile = $2;
    }
    return () unless (-f $buildFile);

    # Run "ant -projecthelp" to list targets.  Keep a cache of results in a
    # cache-file.
    my $cacheFile = $buildFile;
    $cacheFile =~ s|(.*/)?(.*)|${1}.ant-targets-${2}|;
    if ((!-e $cacheFile) || (-M $buildFile) < (-M $cacheFile)) {
        open( CACHE, '>'.$cacheFile ) || die "can\'t write $cacheFile: $!\n";
        open( HELP, "$antCmd -projecthelp -f '$buildFile'|" ) || return(); 
        my %targets;
        while( <HELP> ) {
            if (/^\s+(\S+)/) {
                $targets{$1}++;
            }
        }
        my @targets = sort keys %targets;
        for (@targets) { print CACHE "$_\n"; }
        return @targets;
    }
    
    # Read the target-cache
    open( CACHE, $cacheFile ) || die "can\'t read $cacheFile: $!\n";
    my @targets;
    while (<CACHE>) {
        chop;
        s/\r$//;  # for Cygwin
        push( @targets, $_ );
    }
    close( CACHE );
    @targets;

}



