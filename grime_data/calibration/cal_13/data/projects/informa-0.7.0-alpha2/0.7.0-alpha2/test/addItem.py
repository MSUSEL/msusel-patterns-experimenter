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

from jarray import array

from java.lang import Object
from java.net import URL
from java.util import Date

from de.nava.informa.impl.basic import Channel
from de.nava.informa.impl.basic import Item


def addItem(channel, title, descr, link):
    item = Item(title, descr, URL(link))
    item.setFound(Date())
    channel.addItem(item)
    


# create demo channel
channel = Channel("The Great Demo Channel")
addItem(channel, "Bugo", "All about it!", "http://nava.de/huhu2002")
addItem(channel, "Python", "Instant Python updated", "http://nava.de/I9")

# convert to array
items = channel.getItems().toArray()
print "items", items

new_item = Item("NN", "Fresh", URL("http://new.com"))

new_items = []
new_items.append(new_item)
for item in items:
    new_items.append(item)

items = array(new_items, Object)
print "now items is", items

