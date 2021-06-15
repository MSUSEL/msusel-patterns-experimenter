====
    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
====

OVERVIEW
----------------------

Informa is an open source Java API for building applications which want to
access, modify and persist news channels information to allow content
syndication.

PLEASE NOTE: That although the Informa API is in a pretty stable state the
whole distribution is not complete and still considered in beta stadium.
Please report any problems to the informa-developers mailing list. Thanks
for your patience.

Additional information and documentation can be obtained from the Informa
web site: http://informa.sourceforge.net/


GETTING STARTED
----------------------

Requirements: it is assumed that you have a running Java 2 SDK (v. >= 1.3) 
environment and a SAX Parser (like Xerces-J) available (not needed if you
already use J2SDK 1.4 and higher).

Append the necessary JARs in the lib directory (especially informa.jar)
to your CLASSPATH. Together with the JavaDoc you should be able to get up to
speed in relative short time, one main goal in the design of the Informa
API was to be as much intuitive as possible.

This distribution will also includes an example application at some point in
the future. In the meantime please see the test directory in the src
package or in CVS to obtain samples and a minigui test application.


To verify that parsing is working properly you might want to execute for a
local feed:

  ant parse -Dargs='-f /path/to/my-weblog.xml'

If the feed is available from an URI please specify:

  ant parse -Dargs='-u http://url.to/my-weblog.xml'

Happy news channels parsing.


NOTES
----------------------

Please note that this release is a beta release.  Although Informa should be
fairly stable at this point, there is the possibility for changes before the
final release.

If you like you could have a look into the FAQ which is at:
http://informa.sourceforge.net/faq.html


CONTACT
----------------------

Please send all questions to niko_schmuck@users.sourceforge.net or join the
Informa developers list by visiting the web site
http://lists.sourceforge.net/mailman/listinfo/informa-developer .


Have fun,
Niko Schmuck
