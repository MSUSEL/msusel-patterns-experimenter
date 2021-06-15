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


from java.net import URL
from java.util import Date

from de.nava.informa.impl.jdo import ChannelGroup
from de.nava.informa.impl.jdo import Channel
from de.nava.informa.impl.jdo import Item

from org.exolab.castor.jdo import JDO

# -- Define the JDO object
jdo = JDO()
jdo.setDatabaseName("jdoinforma")
jdo.setConfiguration("../../src/de/nava/informa/impl/jdo/database.xml")
# jdo.setClassLoader( self.getClass().getClassLoader() )

# -- Obtain a new database
db = jdo.getDatabase()

# -- Begin a transaction
db.begin()

# -- Create a new Object and Make it persistent
print "creating channel"

group = ChannelGroup("Default")

channel = Channel()
channel.setTitle("ChannelOne")
channel.setLocation("http://nava.de")
channel.setDescription("Test description for a channel.")
group.add(channel)

itemA = Item()
itemA.setTitle("Bugo")
itemA.setDescription("All about it!")
itemA.setLink(URL("http://nava.de/huhu2002"))
itemA.setFound(Date())
channel.addItem(itemA)

db.create( group )
print "persisted", group

# because there is no 'depends' declared
db.create( channel )
print "persisted", channel

channel_id = channel.getId()

# -- Commit the transaction
db.commit()

# -- Look up
db.begin()
print "Try to lookup channel with id", channel_id
oql = db.getOQLQuery("SELECT c FROM de.nava.informa.impl.jdo.Channel c" \
                     "         WHERE id = $1")
oql.bind( channel_id )
results = oql.execute()
if results.hasMore():
    r = results.next()
    print "Retrieved with OQL:", r
else:
    print "Nothing found :-("
db.commit()

# -- Close the database
db.close()
