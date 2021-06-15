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

This is the readme file for the ArcSDE DataStore of the geotools2 project.

This module is out of the normal build since we can't provide the required
libraries from ESRI to access an ArcSDE database through Java. 
If you intend to use this module and have access to the 
ESRI's ArcSDE Java API libraries (for instance jpe90_sdk.jar and
jsde90_sdk.jar) the easiest way of getting the module building is adding
them to your JRE's ext/lib directory and uncomment this module from the 
list of excludes in the root project maven pom.xml



Unit testing the ArcSDE DataSource over a live database is disabled
due to the lack of a publicly available one.

If you have a live ArcSDE database and want to run the
unit tests over it, remove or comment the line
<exclude>**/SdeDataStoreTest.java</exclude>
on project.xml and see tests/unit/testData/testparams.properties
for instructions on setting up testing and connection options.

For any comment, suggestion or contribution to this code,
email to the geotools-devel@sourceforge.net mailing list
or contact me directly at groldan@users.sourceforge.net

Gabriel Rold�n.
