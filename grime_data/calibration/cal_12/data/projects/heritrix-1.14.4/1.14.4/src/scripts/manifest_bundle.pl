#!/usr/bin/env perl
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


use strict;


my $LFLAG = "-L";
my $CFLAG = "-C";
my $RFLAG = "-R";

my $lFlagDir = "logs";
my $cFlagDir = "configurations";
my $rFlagDir = "reports";
my $v = 0;

my $tarFile;
my $tarFlags = "ch";
my $compress = 0;
my %flags = ($LFLAG => $lFlagDir, $CFLAG => $cFlagDir, $RFLAG => $rFlagDir);

my $crawlName = shift;
my $manifestFile = shift;

die "Could not find $manifestFile: ($!)\n" if ( ! -f $manifestFile);

while (@ARGV > 0) {
    if ($ARGV[0] eq "-f") {
	shift;
	$tarFile = shift;
	$tarFlags = $tarFlags . "f $tarFile";
    }elsif ($ARGV[0] eq "-z") {
	shift;
	$tarFlags = "z" . $tarFlags;
    }elsif ($ARGV[0] eq $LFLAG) {
	shift;
        $lFlagDir = shift;
    }elsif ($ARGV[0] eq $RFLAG){
	shift;
	$rFlagDir = shift;
    }elsif ($ARGV[0] eq $CFLAG){
	shift;
	$cFlagDir = shift;
    }elsif ($ARGV[0] eq "--help"){
	shift;
	USAGE();
    }else {
	# any other flag
	my $key = shift;
	$flags{$key} = shift;
    }
}

# Dump output to stdout if the tar file is not specified.
$tarFlags .= "O" if (! $tarFile );

open (FH, "< $manifestFile")
    or die "Could not open $manifestFile: ($!)\n";

# Create directory structure with symbolic links to files found in the crawl manifest file.
if (! -d $crawlName){
    mkdir($crawlName);
}

my $file;
my $dir;
while (<FH>) {
    chomp;
    my @parts = split;
    if (scalar @parts != 2) {
	warn "Illegal format in $manifestFile ($_)\n";
	next;
    }
    my @chars = split (//,$parts[0]);
    next if ($chars[1] eq "-");
    $dir = $flags{"-$chars[0]"};
    mkdir("$crawlName/$dir") if (! -d "$crawlName/$dir");
    $parts[1] =~ /^.+\/([^\/]+)$/;

    if ($1) {
	$file = $1;
    } else {
	warn "Could not recognize a file in: $_\n";
    }

    symlink($parts[1], "$crawlName/$dir/$file")
	or die "Could not create link to $parts[1]: ($!)\n"; 
} 

# tar up the directory structure.
my $cmd = "tar -$tarFlags $crawlName";
print "Running $cmd\n" if ($v);
!system($cmd)
    or die "Running command $cmd failed: ($!)\n";

# clean up

`rm -rf $crawlName`;

sub USAGE {
    print "heri_manifest_bundle crawl_name manifest_file [-f output_tar_file] [-z] [ -flag directory]\n";
    print "\t -f output tar file. If omitted output to stdout.\n";
    print "\t -z compress tar file with gzip\n";
    print "\t -flag is any upper case letter. Default values C, L, and are R are set to configuration, logs and reports.\n";
    print "Example:\n\theri_manifest_bundle crawl-manifest.txt -f /0/test-crawl/manifest-bundle.tar -z -F filters\n";
}
