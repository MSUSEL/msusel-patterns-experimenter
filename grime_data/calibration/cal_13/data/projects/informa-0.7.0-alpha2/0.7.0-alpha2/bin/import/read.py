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


# Simple test script for displaying channel content
# $Id: read.py,v 1.6 2002/06/24 21:13:26 niko_schmuck Exp $

from java.io import File
from java.lang import System
from org.apache.log4j import BasicConfigurator
from de.nava.informa.impl.basic import ChannelBuilder
from de.nava.informa.parsers import RSS_0_91_Parser
from de.nava.informa.parsers import RSS_1_0_Parser

import sys

if len(sys.argv) < 3:
    print "Usage: %s filename format(0.91|1.0)" % sys.argv[0]
    sys.exit(0)

filename = sys.argv[1]
print "Try to parse %s" % filename

# Set up a simple configuration that logs on the console.
BasicConfigurator.configure()

format = sys.argv[2]
if format == "0.91":
    parser = RSS_0_91_Parser()
else:
    parser = RSS_1_0_Parser()

builder = ChannelBuilder()
parser.setBuilder(builder)

# time has come to actually start working
start_time = System.currentTimeMillis()
channel = parser.parse(File(filename).toURL())
end_time = System.currentTimeMillis()
print "Parsing took %d milliseconds." % (end_time-start_time)

# display a bit
print
print "Channel: %s (%s)" % (channel.getTitle(), channel.getDescription())
print "         %s" % channel.getSite()
print

for item in channel.getItems():
    print "  - %s" % item.getTitle()
    print "    [Id: %s]" % item.getId()
    print "    link to %s" % item.getLink()
    if item.getDescription():
        print '    "%s ..."' % item.getDescription()[:50]
    print
