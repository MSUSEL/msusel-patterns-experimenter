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

# $Id: index.py,v 1.1 2002/06/24 21:13:26 niko_schmuck Exp $
# create an index for the lucene engine to perform search queries with.

from java.io import File
from java.util import Date

from de.nava.informa.impl.basic import ChannelBuilder
from de.nava.informa.utils import ChannelRegistry
from de.nava.informa.search import ItemDocument

from org.apache.lucene.analysis.standard import StandardAnalyzer
from org.apache.lucene.index import IndexWriter


# create channel registry
builder = ChannelBuilder()
reg = ChannelRegistry(builder)

# register some channels
chfiles = ('data/heise.rdf',
           'data/slashdot.rdf',
           'data/xmlhack-0.91.xml',
           'data/xmlhack-1.0.xml')
for filename in chfiles:
  reg.addChannel(File(filename).toURL(), 60, 0)

# create (3rd arg) index writer in directory (first arg)
start_time = Date()
writer = IndexWriter("index", StandardAnalyzer(), 1)

# loop over all channels and their items
for channel in reg.getChannels():
    print
    print "channel:", channel
    for item in channel.getItems():
      print "  add %s to index" % item
      writer.addDocument(ItemDocument.makeDocument(item))

writer.optimize()
writer.close()
end_time = Date()

print "building the index took %d milliseconds in total." \
      % (end_time.getTime() - start_time.getTime())
