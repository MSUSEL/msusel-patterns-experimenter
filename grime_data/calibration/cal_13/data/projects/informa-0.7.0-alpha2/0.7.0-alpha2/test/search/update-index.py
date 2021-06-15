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

# $Id: update-index.py,v 1.1 2002/06/25 08:18:23 niko_schmuck Exp $
# update an existing index (adding a new document).

from java.net import URL
from java.util import Date

# from de.nava.informa.impl.basic import ChannelBuilder
# from de.nava.informa.utils import ChannelRegistry
from de.nava.informa.impl.basic import Item
from de.nava.informa.search import ItemDocument

from org.apache.lucene.analysis.standard import StandardAnalyzer
from org.apache.lucene.index import IndexWriter


# update (3rd arg) index writer in directory (first arg)
start_time = Date()
writer = IndexWriter("index", StandardAnalyzer(), 0)

# create new (dummy) item
item = Item("Informa released", "blubb",
            URL("http://nava.de/news/2002/06/25"))
item.setFound(Date())
	    
# add new item to index
writer.addDocument(ItemDocument.makeDocument(item))

writer.optimize()
writer.close()
end_time = Date()

print "updating the index took %d milliseconds in total." \
      % (end_time.getTime() - start_time.getTime())
