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

"""Shows how to access the channels via JDO in the database
and marshal them into a XML instance with the given mapping.
$Id: marshal.py,v 1.3 2002/10/20 15:39:03 niko_schmuck Exp $
"""

from java.io import FileWriter

from org.exolab.castor.jdo import JDO
from org.exolab.castor.mapping import Mapping
from org.exolab.castor.xml import Marshaller

# -- Define the JDO object
jdo = JDO()
jdo.setDatabaseName("jdoinforma")
jdo.setConfiguration("../../src/de/nava/informa/impl/jdo/database.xml")
db = jdo.getDatabase()

# -- Create a File to marshal to
writer = FileWriter("test-channels.xml")

# -- Load the mapping file
mapping = Mapping()
mapping.loadMapping("../../src/de/nava/informa/impl/jdo/mapping.xml")

# -- prepare XML marshalling
marshaller = Marshaller(writer)
marshaller.setMapping(mapping)

# -- write out all channels in the database
db.begin()
oql = db.getOQLQuery("SELECT c FROM de.nava.informa.impl.jdo.Channel c")
results = oql.execute()
while results.hasMore():
    marshaller.marshal(results.next())

db.commit()

db.close()
