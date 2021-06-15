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

GeoTools SQL Server DataStore README
====================================

JDBC Driver
-----------

To use the sql server datastore the jdbc 3.0 driver must be downloaded from 
Microsoft:

  http://www.microsoft.com/download/en/details.aspx?displaylang=en&id=19847

The current tested driver version is 3.0 for SQL Server and SQL Azure. According to Microsoft this
driver provides access to SQL Azure, SQL Server 2008 R2, SQL Server 2008, SQL Server 2005 and 
SQL Server 2000 from any Java application, application server, or Java-enabled applet.

Make sure to unpack the sqljdbc4.jar file, as that's the one providing support for the Java
version in this series.
If you are using Java 5 you should downgrade to GeoTools 2.7.x, while Java 7 is, at the time
of writing, not officially supported   

Maven
-----

If developing with the datastore and the rest of GeoTools: 

  1. install the JDBC driver into the location maven repository:

     mvn install:install-file  -Dfile=<path to sqljdbc4.jar>
         -DartifactId=sqljdbc4 \ 
         -DgroupId=com.microsoft \ 
         -Dversion=3.0 \
         -Dpackaging=jar \ 
         -DgeneratePom=true

  2. Build the jdbc-sqlserver module using the *sqlServerDriver* profile:

     mvn clean install -P sqlServerDriver
  
