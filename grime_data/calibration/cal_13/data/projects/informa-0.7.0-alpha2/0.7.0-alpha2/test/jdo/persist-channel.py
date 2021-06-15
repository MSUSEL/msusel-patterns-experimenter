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


# Test Castor XML marshalling with channel read in from file
# $Id: persist-channel.py,v 1.2 2002/10/20 15:39:03 niko_schmuck Exp $

from java.io import File
from java.io import FileWriter
from java.lang import System
from de.nava.informa.impl.basic import ChannelBuilder
from de.nava.informa.parsers import RSS_0_91_Parser
from de.nava.informa.parsers import RSS_1_0_Parser

from org.exolab.castor.mapping import Mapping
from org.exolab.castor.xml import Marshaller

import sys

if len(sys.argv) < 3:
    print "Usage: %s filename format(0.91|1.0)" % sys.argv[0]
    sys.exit(0)

filename = sys.argv[1]
print "Try to parse %s" % filename

# Create a Parser of specified format
format = sys.argv[2]
builder = ChannelBuilder()
if format == "0.91":
    parser = RSS_0_91_Parser(builder)
else:
    parser = RSS_1_0_Parser(builder)

# Read in Channel from File
channel = parser.parse(File(filename).toURL())    
    
# Create a File to marshal to
writer = FileWriter("test-persist-channel.xml")

# Load the mapping information from the file
mapping = Mapping()
mapping.loadMapping("../../src/de/nava/informa/impl/jdo/mapping.xml");

# Marshal the channel object
marshaller = Marshaller(writer)
marshaller.setMapping(mapping)
marshaller.marshal(channel)

# ---- Unmarshalling 

# Create a Reader to the file to unmarshal from
# reader = FileReader("test-persist-channel.xml")

# Unmarshal the channel object
# channel = Unmarshaller.unmarshal(Channel.class, reader);
