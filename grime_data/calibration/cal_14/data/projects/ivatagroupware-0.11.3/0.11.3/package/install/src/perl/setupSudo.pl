#!/usr/bin/perl -w
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

################################################################################
# INCLUDES: please don't change these
use Cwd;
use File::Basename  qw(dirname basename);
use File::Copy      qw(copy move);
use File::Path      qw(mkpath);
use locale;                     # needed in 5.004 or above
use strict;
use Text::Wrap      qw(&wrap $columns);
use Term::ReadKey   qw(GetTerminalSize ReadKey ReadLine ReadMode);
################################################################################
# $Id: setupSudo.pl,v 1.1 2005/04/27 14:55:57 colinmacleod Exp $
#
# ivata groupware sudo setup program
# ----------------------------------
#
# Installs sudo scripts to add remove users from an Exim/Postfix/Sendmail style
# system. This script has been tested and works on the following operating
# systems:
#        ----> Debian Woody
#        ----> Fedora Core 3
#        ----> Fedora Core 2
#        ----> SuSE 9.2 (FTP edition)
#        ----> SuSE 9.1 (FTP edition)
#
# You should run this setup program from the console, and it must be run from
# the directory where you want ivata groupware to be installed.
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
# $Log: setupSudo.pl,v $
# Revision 1.1  2005/04/27 14:55:57  colinmacleod
# Stripped out everything from the setup script
# apart from the sudo setup.
#
# Revision 1.5  2005/04/09 17:19:52  colinmacleod
# Changed copyright text to GPL v2 explicitly.
#
# Revision 1.4  2005/03/22 18:54:07  colinmacleod
# Expanded contents of ivatagroupware-hibernate.
#
# Revision 1.3  2005/03/18 08:11:21  colinmacleod
# Surrounded war file name with quotes.
#
# Revision 1.2  2005/03/17 18:45:19  colinmacleod
# Changed non-supported OS message to warning.
#
# Revision 1.1.1.1  2005/03/10 17:49:14  colinmacleod
# Restructured ivata op around Hibernate/PicoContainer.
# Renamed ivata groupware.
#
# Revision 1.3  2004/12/31 19:07:31  colinmacleod
# Cosmetic indentation improvements.
#
# Revision 1.2  2004/11/03 18:36:13  colinmacleod
# Split local.pl from os.pl.
#
# Revision 1.1  2004/11/03 17:54:26  colinmacleod
# First version of setup program.
#
################################################################################
#
# Since: ivata groupware 0.9 (2004-10-18)
# Author: Colin MacLeod <colin.macleod@ivata.com>
# $Revision: 1.1 $
#
################################################################################

################################################################################
# --- EVERYTHING BELOW THIS POINT SHOULD BE OK AS IS ---
# Normally you can just leave the following settings alone. If you've done
# something funky on your system (like installed an MTA), you might want to
# glance thro' and see if the values make sense.
################################################################################
# first get the configuration values from the config file
my $CONF_DIR = dirname($0);
my %CONF = ();
open (CONF, "<${CONF_DIR}/setupSudo.config")
    || die "Cannot open setup.config for reading: $!\n";
while (<CONF>) {
    chomp;                  # no newline
    s/#.*//;                # no comments
    s/^\s+//;               # no leading white
    s/\s+$//;               # no trailing white
    next unless length;     # anything left?
    my ($var, $value) = split(/\s*=\s*/, $_, 2);
    $CONF{uc($var)} = $value;
}
close (CONF)
    || die "Cannot close setup.config: $!\n";


################################################################################
# first useful programs
my $PERL = $CONF{"PERL"};
my $SH = $CONF{"SH"};
my $SU = $CONF{"SU"};

# this is where you will be installing ivata groupware from
my $IVATAGROUPWARE_DIR = $CONF_DIR;
if ($CONF{"IVATAGROUPWARE_DIR"}) {
    $IVATAGROUPWARE_DIR = $CONF{"IVATAGROUPWARE_DIR"};
}
# lock directory - IF YOU CHANGE THIS YOU WILL ALSO NEED TO OVERRIDE THE
# SETTING IN mailserver/exim/Site.pm BY CREATING YOUR OWN
# mailserver/exim/local.pl FILE!!!!! - definitely safer to leave the default
my $IVATAGROUPWARE_LOCK_DIR = "/var/lock/ivatagroupware";

# stores the version - calculated from the war file
my $IVATAGROUPWARE_VERSION;

# path to the sudoers file
my $SUDOERS = $CONF{"SUDOERS"};

# get the terminal width, and use it for the wrap columns
my ($TERM_WIDTH) = GetTerminalSize();
die "Terminal only $TERM_WIDTH wide.\n"
    . "You must have at least 40 characters.\n" unless $TERM_WIDTH >= 40;
($columns) = $TERM_WIDTH;

# tmp directory
my $TMP = "/tmp";

################################################################################

################################################################################
# askOrDie: Asks the user to choose from a list
# otherwise dies.
# params:
#   statement: The situation which needs to be asked about.
#   chooseText: string displayed on the choice line
#	list: all choices available to the user.
# returns:
#        user input.
sub askOrDie($$@) {
    my $statement = shift();
    my $chooseText = shift();
    my @choices = @_;
    my $user_input;
    print wrap("", "", "$statement\n");
    my $choice;
    foreach $choice (@choices) {
        print wrap("  ", "               ", "$choice\n");
    }
    print wrap ("", "", "\n\n$chooseText: ");
    $user_input = <STDIN>;

    chomp ($user_input);
    return $user_input;
}
################################################################################
# changeDirectory: change directory to the ivata groupware directory.
# params:
#        NONE
# returns:
#        NONE
sub changeDirectory() {
    chdir($IVATAGROUPWARE_DIR)
        || dieNow("Cannot change directory to $IVATAGROUPWARE_DIR: $!");
    $IVATAGROUPWARE_DIR = cwd();
}

################################################################################
# changeOwner: change ownership of a file in an OS-friendly way
# params:
#   filePath: path of the file whose ownership will be changed
#   user: user name of the user who will own it after
# returns:
#   non-zero for success
sub changeOwner($$) {
    my ($filePath, $user) = @_;
    return chown(((getpwnam($user))[2,3], $filePath));
}

################################################################################
# checkApps: check that all mandatory apps are present and correct
# params:
#        NONE
# returns:
#        NONE
sub checkApps() {
    my $output;

    # check sudoers exists
    printCheck("sudo");
    unless (-e $SUDOERS) {
        dieNow ("You don't appear to have a valid sudo installation: "
            . "'$SUDOERS' does not exist. If sudo is not installed, then "
            . "install it now, otherwise change the value for SUDOERS in "
            . "setupSudo.config.");
    }
    printOK("Mandatory programs");
}
################################################################################
# checkPostres: checks that the perl installation is valid
# params:
#        NONE
# returns:
#        non-zero if the perl installation is valid, otherwise zero.
sub checkPerl() {
    # check perl
    printCheck("perl Expect module is installed");
    unless (eval "require Expect") {
        dieNow ("perl module Expect.pm does not appear to be installed. Please "
            . "install it for your OS - refer to the instructions at "
            . "http://groupware.ivata.org/install.html for help.");
    }
}
################################################################################
# checkRoot: to run this application, you need to be root. This sub dies if
# you don't have the necessary permissions.
# params:
#        NONE
# returns:
#        NONE
sub checkRoot() {
    # on Linux, root is always 0 AFAIK
    unless ($<==  0) {
        dieNow ("You have to be root to run this setup program.");
    }
}
################################################################################
# dieNow: Just dies in a consistent way.
# params:
# 	dyingGasp: message to display on dying.
# returns:
#        NONE
sub dieNow($) {
    my $dyingGasp = shift();

    $dyingGasp = "\nFATAL:\n$dyingGasp\n\nExamine the messages above, then "
        . "please try to fix the problem, and run the setup program again :-)\n"
        . "If you can't fix the problem, get help at "
        . "http://www.ivata.com/contact.jsp. Send us the setup program output, "
        . "as well as your operating system, PostgreSQL version and "
        . "application server type and version.\n";

    die wrap("", "", $dyingGasp);
}

################################################################################
# getDate: simple routine to get the current date
# params:
#        NONE
# returns:
#        current date in the form YYYY-MM-DD.
sub getDate() {
    my ($sec,$min,$hour,$mday,$mon,$year) =
        localtime(time);
    $year += 1900;
    $mon ++;
    if ($mon < 10) {
        $mon = "0$mon";
    }
    if ($mday < 10) {
        $mday = "0$mday";
    }
    return "$year-$mon-$mday";
}
################################################################################
# getWARVersion: identify the ivata groupware war file and derive the ivata
# groupware version from the name
# params:
#        NONE
# returns:
#        NONE
sub getWARVersion() {
    my $search = "ivatagroupware-war-*.war";
    my @glob = glob($search);
    my $totalResults = @glob;
    # sanity check...
    if ($totalResults == 0) {
        dieNow("Could not find a WAR file matching '$search'!!");
    } elsif ($totalResults > 1) {
        dieNow("Found more than one WAR file matching '$search'!!");
    }
    my $war = shift(@glob);
    # current ivata groupware version - derived from the war file name by
    # default
    $war =~ /ivatagroupware-war-(.*).war/;
    $IVATAGROUPWARE_VERSION = $1;
    unless ($IVATAGROUPWARE_VERSION) {
        identifyOS();
        dieNow("Could not identify the ivata groupware version from "
            . "'$war'.");
    }
}
################################################################################
# makeLink: make a symbolic link. Override any existing link.
# params:
#   source: source script to be linked against.
#   link: the target of the link
# returns:
#        NONE
sub makeLink($$) {
    my ($source, $link) = @_;
    if (-e $link) {
        if (-l $link) {
            printInfo("Replacing existing link '$link'...");
            unlink($link);
        } else {
            dieNow ("Cannot create link \"$link\": file exists already and is "
                . "not a link!");
        }
    } else {
        printInfo("Creating link '$link'...");
    }
    symlink($source, $link);
}
################################################################################
# moveWithBackup: useful when we're writing out a new version of a config file,
# and a backup of the existing one should be made
# params:
#   newFile: the file name of a new file
#   configFile: the existing config file to be backed up
# returns:
#        NONE
sub moveWithBackup($$) {
    my ($newFile, $configFile) = @_;

    my $fileCounter = 0;
    my $bakFile;
    do {
        ++$fileCounter;
        $bakFile = "${configFile}_ivatagroupware$fileCounter.bak";
    } while (-e $bakFile);
    printInfo("Creating backup of current '$configFile' to '$bakFile'...");

    # get previous user/group to make the user rights the same

    my $mode;
    my @stat = stat($configFile);
    $mode = $stat[2];
    my ($userName) = getpwuid($stat[4]);

    copy($configFile, $bakFile)
        || dieNow "Could not copy $configFile to $bakFile: $!";
    move($newFile, $configFile)
        || dieNow "Could not move $newFile to $configFile: $!";
    changeOwner($configFile, $userName)
        || dieNow ("Cannot change ownership of $configFile to user $userName.");
    changeOwner($bakFile, $userName)
        || dieNow ("Cannot change ownership of $bakFile to user $userName.");
    chmod($mode, $configFile, $bakFile);
}
################################################################################
# printCongratulations: print a happy message to say ivata groupware installed OK
# params:
#        NONE
# returns:
#        NONE
sub printCongratulations() {
    print "\n\n";
    print wrap("", "", "   === CONGRATULATIONS ===\n");
    print wrap("", "", "ivata groupware sudo scripts are installed on your "
        . "system and should be available for you right now at:\n");
    print wrap("    ", "    ", "$IVATAGROUPWARE_DIR\n\n");

    print wrap("", "", "If you wish to change any of the settings you made in "
        . "this install process, change the settings in setupSudo.config "
        . "and run this program again.\n");
    print "\n";
    print wrap ("", "", "If you like ivata groupware (or if you don't!), "
        . "please get in touch and let us know via the website "
        . "http://groupware.ivata.org.\n");
    print "\n";
    print wrap ("", "", "Have fun!\n");
    print wrap ("    ", "", "- the ivata coders\n");
    print "\n";
}
################################################################################
# printCheck: message printed at the start of a routine checking for the
# existance of a necessary system function or service.
# params:
#        service: name of the service which will be checked.
# returns:
#        NONE
sub printCheck($) {
    my $service = shift();
    print wrap ("  ", "  ", "Checking $service...\n");
}

################################################################################
# printInfo: print out a standard (secondary) information line, wrapped to the
# terminal width
# params:
#        message: the message to be warpped and printed
# returns:
#        NONE
sub printInfo($) {
    my $message = shift();
    print wrap ("  ", "    ", "$message\n");
}
################################################################################
# printOK: final message, after task completed successfully.
# params:
#        task: name of the task which completed.
# returns:
#        NONE
sub printOK($) {
    my $task = shift();
    print wrap ("  ", "  ", "$task OK\n\n");
}

################################################################################
# printStep: print something to tell the user where she is in the install
# process
# params:
# 	thisStep: the number of this step
#   totalSteps: total number of steps to perform
#   title: the title of this step
# returns:
#        the number for the next step
sub printStep($$$) {
    my $thisStep = shift();
    my $totalSteps = shift();
    my $title = shift();
    print wrap("\n  ", "", "=== STEP $thisStep of $totalSteps: $title ===\n");
    return ++$thisStep;
}
################################################################################
# printWelcome: print a happy message at the start
# params:
#        NONE
# returns:
#        NONE
sub printWelcome() {
    print wrap("  ", "  ", "=== IVATA GROUPWARE X-PLATFORM SETUP PROGRAM ===\n");
    print wrap("  ", "  ", "ivata groupware version......: $IVATAGROUPWARE_VERSION\n");
    print wrap("  ", "  ", "Install directory............: $IVATAGROUPWARE_DIR\n\n");
}
################################################################################
# setupScripts: set up the environment for the ivata groupware scripts
# params:
#        NONE
# returns:
#        NONE
sub setupScripts() {
    # make sure there is a lock directory
    if (-e $IVATAGROUPWARE_LOCK_DIR) {
        unless (-d $IVATAGROUPWARE_LOCK_DIR) {
            dieNow ("Lock dir location for ivata groupware exists, but is not "
                . "a directory: $IVATAGROUPWARE_LOCK_DIR");
        }
        printInfo("Lock dir $IVATAGROUPWARE_LOCK_DIR already exists.");
    } else {
        printInfo("Creating lock dir $IVATAGROUPWARE_LOCK_DIR...");
        mkpath($IVATAGROUPWARE_LOCK_DIR, 1, 0700);
    }
    changeOwner($IVATAGROUPWARE_LOCK_DIR, $CONF{"USER_APP_SERVER"})
        || dieNow ("Cannot change ownership of $IVATAGROUPWARE_LOCK_DIR to "
            . $CONF{"USER_APP_SERVER"});

    my $localConfig = "mailserver/exim/os.pl";
    if (-e $localConfig) {
        printInfo("Leaving local config file $localConfig...");
    } elsif (-e "/etc/redhat-release") {
        printInfo("Linking config for Red Hat...");
        makeLink("os_redhat.pl", $localConfig);
    } elsif (-e "/etc/SuSEconfig") {
        printInfo("Linking config for SuSE...");
        makeLink("os_suse.pl", $localConfig);
    } elsif (-e "/etc/debian_version") {
        printInfo("No config link required; default settings based on Debian.");
    } else {
        printInfo(
            "Your OS version was not identified. Please examine the contents "
             . "of the file $IVATAGROUPWARE_DIR/exim/Site.pm for site script "
             . "settings.\n"
             . "If you need to override these settings, create a new file "
             . "called $IVATAGROUPWARE_DIR/exim/local.pl and put your own "
             . "settings there (see $IVATAGROUPWARE_DIR/exim/local_suse.pl for "
             . "an example).");
        askOrDie("\n", "Press ENTER/Return to continue");
    }

    printOK("Set up ivata groupware scripts");
}
################################################################################
# setupSudo: set up the sudo command using visudo.
# params:
#        NONE
# returns:
#        NONE
sub setupSudo() {
    printInfo("Creating links to all the scripts...");
    `"$SH" mailserver/clearLinks.sh`;
    system ("\"$SH\" mailserver/makeLinks.sh > /dev/null")
        && dieNow ("Cannot create script links: $!.\nPlease run script "
            . "$IVATAGROUPWARE_DIR/mailserver/makeLinks.sh manually and try "
            . "again.");

    open (SUDOERS, "<$SUDOERS")
        || dieNow ("Cannot open $SUDOERS for reading: $!");
    my @sudoers = <SUDOERS>;
    close (SUDOERS) || dieNow ("Cannot close $SUDOERS: $!");

    # if the sudoers file already contains ivata groupware, then remove old
    # setup
    if (grep (/IVATAGROUPWARE/, @sudoers)) {
        printInfo("It looks like sudo has already been set up for ivata "
            . "groupware. Removing previous setup...");
        my $tmpOutFile = "$SUDOERS.new.ivatagroupware";
        open (SUDOERS, ">$tmpOutFile")
            || dieNow ("Cannot open $tmpOutFile for writing: $!");
        my ($line, $inOldConfig, $endOldConfig);
        foreach $line (@sudoers) {
            # if the last line was the end of the old config, see if this line
            # is blank, and ignore it if so
            if ($endOldConfig) {
                $inOldConfig = 0;
                # don't print a blank line directly after the last old config
                if ($line eq "\n") {
                    next;
                }
            }

            # ignore all the lines of the old config
            if ($inOldConfig) {
                $endOldConfig = ($line =~ /^\#\#\#\#\sEND\sIVATAGROUPWARE/);
            } elsif ($line =~ /^\#\#\#\#\sSTART\sIVATAGROUPWARE\sSETUP/) {
                $inOldConfig = -1;
            } else {
                print SUDOERS $line;
            }
        }
        close (SUDOERS) || dieNow ("Cannot close $tmpOutFile: $!");
        # if we never found the end of the old ivata groupware config...
        if ($inOldConfig) {
            unlink($tmpOutFile);
                dieNow(
                "Could not find the end of the old ivata groupware sudo "
                . "config.\n"
                . "Please run visudo now to manually delete the old ivata "
                . "groupware config and then restart this setup program. "
                . "(You need to remove the section beginning with '#### "
                . "START IVATAGROUPWARE INSTALL'.)");
        }
        # if it gets here, we successfully removed the old config
        moveWithBackup($tmpOutFile, $SUDOERS);
    }
    my $date = getDate();
    printInfo("Writing new ivata groupware sudo configuration...");
    open (SUDOERS, ">>$SUDOERS")
        || dieNow ("Cannot open $SUDOERS for writing: $!");
    print SUDOERS "#### START IVATAGROUPWARE SETUP PROGRAM ADDED "
        . "AUTOMATICALLY $date\n";
    print SUDOERS "# ANY CHANGES TO THIS SECTION MAY BE OVER-WRITTEN BY IVATA "
        . "GROUPWARE SETUP PROGRAM\n";
    print SUDOERS "Cmnd_Alias IVATAGROUPWARE_CMD=";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/addUser, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/checkPassword, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/getFolderModified, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/getUserAliases, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/getVacationMessage, "
        . "\\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/isUser, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/makeMaildir, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/removeList, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/removeUser, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/setList, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/setPassword, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/setUserAliases, \\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/setVacationMessage, "
        . "\\\n";
    print SUDOERS "$IVATAGROUPWARE_DIR/mailserver/exim/userExists\n";
    print SUDOERS "\n";
    print SUDOERS $CONF{"USER_APP_SERVER"}
        . " ALL = (root)NOPASSWD: IVATAGROUPWARE_CMD\n";
    print SUDOERS "#### END IVATAGROUPWARE SETUP PROGRAM ADDED AUTOMATICALLY "
        . "$date\n";
    print SUDOERS "\n";
    close (SUDOERS) || dieNow ("Cannot close $SUDOERS: $!");

    printInfo("If prompted for a password now, enter the root password.");

    changeOwner("$IVATAGROUPWARE_DIR", $CONF{"USER_APP_SERVER"})
        || dieNow ("Cannot change ownership of "
            . "'$IVATAGROUPWARE_DIR' to "
            . $CONF{"USER_APP_SERVER"});
    changeOwner("$IVATAGROUPWARE_DIR/mailserver", $CONF{"USER_APP_SERVER"})
        || dieNow ("Cannot change ownership of "
            . "'$IVATAGROUPWARE_DIR/mailserver' to "
            . $CONF{"USER_APP_SERVER"});
    changeOwner("$IVATAGROUPWARE_DIR/mailserver/sudo", $CONF{"USER_APP_SERVER"})
        || dieNow ("Cannot change ownership of "
            . "'$IVATAGROUPWARE_DIR/mailserver/sudo' to "
            . $CONF{"USER_APP_SERVER"});
    opendir (SUDO_DIR, "$IVATAGROUPWARE_DIR/mailserver/sudo")
        || dieNow ("Cannot open the directory listing of "
            . "'$IVATAGROUPWARE_DIR/mailserver/sudo':$!");
    my @allSudoFiles = readdir SUDO_DIR;
    closedir SUDO_DIR;
    my $sudoFile;
    foreach $sudoFile (@allSudoFiles) {
        changeOwner("$IVATAGROUPWARE_DIR/mailserver/sudo/$sudoFile",
                $CONF{"USER_APP_SERVER"})
            || dieNow ("Cannot change ownership of '"
                . "$IVATAGROUPWARE_DIR/mailserver/sudo/$sudoFile' to "
                . $CONF{"USER_APP_SERVER"});
    }

    changeOwner("$IVATAGROUPWARE_DIR/mailserver/exim", "root")
        || dieNow ("Cannot change ownership of "
            . "'$IVATAGROUPWARE_DIR/mailserver/exim' to root");
    chmod(0755, "$IVATAGROUPWARE_DIR/mailserver/exim");
    opendir (SUDO_DIR, "$IVATAGROUPWARE_DIR/mailserver/exim")
        || dieNow ("Cannot open the directory listing of "
            . "'$IVATAGROUPWARE_DIR/mailserver/exim':$!");
    my @allEximFiles = readdir SUDO_DIR;
    closedir SUDO_DIR;
    my $eximFile;
    foreach $eximFile (@allEximFiles) {
        changeOwner("$IVATAGROUPWARE_DIR/mailserver/exim/$eximFile",
                "root")
            || dieNow ("Cannot change ownership of '"
                . "$IVATAGROUPWARE_DIR/mailserver/exim/$eximFile' to "
                . "root");
        chmod(0711, $eximFile);
    }


    $ENV{"SITE_ID"} = "www";
    $ENV{"SUDO_USER"} = "root";
    $ENV{"SUDO_PATH"} = "$IVATAGROUPWARE_DIR/mailserver/exim";

    my $sudoCommand = "\"$SU\" "
        . $CONF{"USER_APP_SERVER"}
        . " -c \"$IVATAGROUPWARE_DIR/mailserver/sudo/isUser root \" "
        . "> /dev/null";
    system ($sudoCommand)
        && dieNow ("Looks like there is a problem running sudo. The test "
            . "which failed was:\n"
            . $sudoCommand);
    printOK("Set up sudo");
}
################################################################################
# MAIN ROUTINE: calls the other subs/functions in turn

my $thisStep = 1;
# change this value if you add more install steps
my $totalSteps = 3;

# preparation - go to the directory, and print the welcome text
changeDirectory();
getWARVersion();
printWelcome();

$thisStep = printStep($thisStep, $totalSteps, "CHECK ENVIRONMENT");
checkRoot();
checkApps();
checkPerl();
$thisStep = printStep($thisStep, $totalSteps, "SETUP SCRIPTS");
setupScripts();
$thisStep = printStep($thisStep, $totalSteps, "SETUP SUDO");
setupSudo();

### if it gets here, everything went OK ###
printCongratulations();
################################################################################

