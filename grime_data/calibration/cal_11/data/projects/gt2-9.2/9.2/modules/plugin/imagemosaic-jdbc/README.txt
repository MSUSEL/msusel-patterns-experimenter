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

Imagemosaic Jdbc  Implementation for Geotools

**  Build Issues  **

None 

** Tests **

The standard test is running against an H2 Database. In src/test/resources you find

resources.zip		:	a zip file containing image material needed for the tests

						The sample data is downloaded from www.openstreemap.com
						WGS84 Coordinates (lon/lat)
						lower left:		9/46
						upper right:	17/49
						
db2.properties		 :	a template for connect info for db2 online tests
postgis.properties	 : 	a template for connect info for postgis online tests
mysql.properties	 :   a template for connect info for mysql online tests
oracle.properties	 : 	a template for connect info for oracle online tests
georaster.properties : 	a template for connect info for oracle georaster online tests

Prior to starting the test, resources.zip  will automatically be unzipped into target/resources/.

The results of the tests are located in 

target/h2
target/db2
target/postgis
target/mysql
target/oracle
target/georaster


**  Running Online Tests **

Depending on the db to test, copy the correct property file to 

~/.geotools/imagemosaic/.  (unix)
c:\Document and Settings\userid\.geotools\imagemosaic\.	(windows)

and adjust the connect params in the file.


Requirements:

DB2:


	1) db2 spatial extender installed
	2) created a database
	3) have a correct property file (fixture) in your home dir
	4) have db2jcc.jar and db2jcc_license.jar in your classpath
	   https://www14.software.ibm.com/webapp/iwm/web/preLogin.do?lang=en_US&source=swg-idsjs11

MYSQL:	   


	1) running mysql
	2) created a database
	3) have a correct property file (fixture) in your home dir
	   
PostGis

	1) running postgres
	2) created a database and enable this db for postgis
	3) have a correct property file (fixture) in your home dir

Oracle

	1) running an oracle instance
	2) have a correct property file (fixture) in your home dir
	3) have ojdbc14.jar in your classpath
	http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/htdocs/jdbc9201.html
	
Oracle Georaster
	1) like Oracle, Oracle Spatial must be available


Run  Maven with:
mvn -P online test

If an online test is missing its property file (fixture) or cannot load its jdbc driver class, 
the test is skipped showing a message.


**  Further Help **
If you are still having problems getting the tests
to work, ask a question via geotools-devel@lists.sourceforge.net
