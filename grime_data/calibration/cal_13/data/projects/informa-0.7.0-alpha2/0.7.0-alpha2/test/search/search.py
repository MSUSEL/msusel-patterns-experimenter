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

# $Id: search.py,v 1.3 2002/06/26 08:03:29 niko_schmuck Exp $
# search specified value in lucene index of news items.

import sys

from org.apache.lucene.analysis.standard import StandardAnalyzer
from org.apache.lucene.search import IndexSearcher
from org.apache.lucene.queryParser import QueryParser

searcher = IndexSearcher("index")
analyzer = StandardAnalyzer()

if len(sys.argv) < 2:
    print "Usage: %s query" % sys.argv[0]
    sys.exit(0)

q_str = sys.argv[1]
print "\nSearching for <%s>." % q_str

query = QueryParser.parse(q_str, "titledesc", analyzer)
print "query:", query
hits = searcher.search(query)

print "%d total matching item(s).\n" % hits.length()

for i in xrange(hits.length()):
    doc = hits.doc(i)
    score = hits.score(i)
    print "- [Id: %s] Score %.4f" % (doc.get("id"), score)
    print "  Title: %s" % doc.get("title")
    print "  Description: %s" % doc.get("description")
    print

searcher.close()
print
