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

-------------------------------------------------------------------------------
 README file for Geotools developers - Do NOT copy this file to the web server
-------------------------------------------------------------------------------

The HTML pages in this directory provide the content for the following site:

    http://maven.geotools.org/  (TODO: not yet enabled)
    http://maven.geotools.fr/

Changes in those files will be automatically reflected to those web sites after
some server-dependent delay. It can also be reflected to any mirrors configured
as below:

  - A local SVN repository updated on a regular basis.

  - Soft links created as below (the base directory names provided below
    are just examples and will change for different server installation):

    cd /var/www/geotools.org/maven/
    ln -s /home/localsvn/geotools/trunk/gt/maven/www/index.html index.html

    cd /var/www/geotools.org/maven/repository
    ln -s /home/localsvn/geotools/trunk/gt/maven/www/repository/HEADER.html HEADER.html

    cd /var/www/geotools.org/maven/resources
    ln -s /home/localsvn/geotools/trunk/gt/maven/www/resources/banner.gif banner.gif
