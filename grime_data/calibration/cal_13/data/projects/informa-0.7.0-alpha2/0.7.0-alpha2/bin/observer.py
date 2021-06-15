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

"""
$Id: observer.py,v 1.3 2002/07/17 08:15:51 niko_schmuck Exp $
test the observer while new items pop up over a period
"""

from java.lang import Thread
from java.net import URL
from de.nava.informa.impl.basic import ChannelBuilder
from de.nava.informa.utils import ChannelRegistry
from de.nava.informa.utils import SimpleChannelObserver

# create channel registry
builder = ChannelBuilder()
reg = ChannelRegistry(builder)

# register channels (which should be updated every 15-35 minutes)
c = {}
c[1]=reg.addChannel(URL("http://www.newsforge.com/newsforge.rss"), 15*60, 1);
c[2]=reg.addChannel(URL("http://slashdot.org/slashdot.rdf"), 20*60, 1);
c[3]=reg.addChannel(URL("http://www.heise.de/newsticker/heise.rdf"), 25*60, 1);
c[4]=reg.addChannel(URL("http://xmlhack.com/rss10.php"), 30*60, 1)
c[5]=reg.addChannel(URL("http://freshmeat.net/backend/fm.rdf"), 35*60, 1);

# create a simple observer which watches out for new items
o = SimpleChannelObserver()
for idx in c.keys():
    c[idx].addObserver(o)

# sleep 4 hours (let the updating threads work)
Thread.currentThread().sleep(4 * 60 * 60 * 1000)

# the end.
print "--- finished observing"
for channel in reg.getChannels():
    print
    print "channel:", channel
    for item in channel.getItems():
      print "  -", item
