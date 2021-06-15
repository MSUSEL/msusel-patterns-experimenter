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

                             ---- ---- ---- ----
                    ---- WELCOME TO ivata groupware ----
                             ---- ---- ---- ----
Welcome to ivata groupware - a great tool to communicate as a team.

ivata groupware is released as open source for everyone to share - see the file
LICENSE.txt for details.  It was been developed by the consulting team at ivata
- see http://www.ivata.net.

                             ---- ---- ---- ----
                             ---- BUILDING  ----
                             ---- ---- ---- ----
To build all subprojects in order of dependency, first make sure you have a
recent version of Java and Maven installed.

Of course you'll have to download and install Java and Maven first :-)
- see http://maven.apache.org/start/install.html
It has been tested with JDK 1.4.2 and Maven 1.0.2. (for Java JDK - go to
java.sun.com).

Then simply enter:
  <source>
    maven
  </source>

in the root directory (where this file is located).

You'll find the web application here:
  <source>
    package/war/target/ivatagroupware-war-{version}.war
  </source>


                             ---- ---- ---- ----
                   ---- GENERATING MORE DOCUMENTATION ----
                             ---- ---- ---- ----
ivata groupware uses Maven to generate documentation.
Follow these steps carefully:

  * install Maven v1.0.2 or higher
     - see http://maven.apache.org/start/install.html
  * go to the ivata groupware root directory (where this file is)

Then build the documentation, execute:
  <source>
    maven multiproject:site
  </source>

Now point your browser at ivatagroupware/target/docs.

