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


from java.lang import Thread
from java.net import URL
from java.util import Date
from de.nava.informa.impl.basic import ChannelBuilder
from de.nava.informa.utils import ChannelRegistry

builder = ChannelBuilder()
reg = ChannelRegistry(builder)

# url_xmlhack = "http://xmlhack.com/rss10.php"
# url_xmlhack = URL("http://localhost/rss/xmlhack-1.0.xml")
url_xmlhack = URL("http://localhost/rss/xmlhack-0.91.xml")
# update channel which should be updated every 30 seconds
reg.addChannel(url_xmlhack, 30, 1)
print "first time parsed in at ", Date()
for channel in reg.getChannels():
    print "channel:", channel
    for item in channel.getItems():
	print "  -", item

# sleep 100 seconds
Thread.currentThread().sleep(100 * 1000)

print "--- after updating"
for channel in reg.getChannels():
    print "channel:", channel
    for item in channel.getItems():
	print "  -", item

# offically deregister
# reg.removeChannel(channel)
