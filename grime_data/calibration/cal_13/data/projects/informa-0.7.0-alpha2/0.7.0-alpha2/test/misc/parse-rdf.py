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


from org.jdom.input import SAXBuilder
from org.jdom import Namespace

import sys

if len(sys.argv) < 2:
    print "Usage: %s filename" % sys.argv[0]
    sys.exit(0)
    
filename = sys.argv[1]

builder = SAXBuilder(0)
print "Read in document from", filename
doc = builder.build(filename)

root = doc.getRootElement()
# print "root", root
print "Root element name: %s, namespace prefix: %s." \
      % (root.getName(), root.getNamespacePrefix())

# --- retrieve default namespace from root element
ns_root = root.getNamespace()
ns_root_others = root.getAdditionalNamespaces()
# print "additional namespaces", ns_root_others

dns = None
for ns in ns_root_others:
    if ns.getPrefix() == "":
        print "default namespace", ns.getURI()
        dns = ns
    else:
        print "namespace", ns.getPrefix(), "->", ns.getURI()

# --- retrieve 1 channel element
if dns == None:
    # --- old way:
    # dns = Namespace.getNamespace("", "http://purl.org/rss/1.0/")
    # does not work (retrieve anything) when leaving out the URI
    # print "No default namespace found, using self constructed one."
    # --- using no namespace
    channel = root.getChild("channel")
else:
    channel = root.getChild("channel", dns)
    
print "channel", channel

# --- retrieve n item elements
if dns == None:
    items = channel.getChildren("item")
else:
    items = root.getChildren("item", dns)
    
print "Found %i item(s)." % items.size()
for item in items:
    if dns == None:
        title = item.getChild("title").getTextTrim()
    else:
        title = item.getChild("title", dns).getTextTrim()    
    print "  *", title
