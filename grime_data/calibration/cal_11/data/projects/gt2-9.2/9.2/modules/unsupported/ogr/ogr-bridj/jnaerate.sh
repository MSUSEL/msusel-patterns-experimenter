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

export JNAERATOR=/home/aaime/devel/gdal/jnaerator-0.9.9-SNAPSHOT-shaded.jar
export GDAL_BASE=/home/aaime/devel/gdal/gdal-1.8.0
java -jar $JNAERATOR -I. -I$GDAL_BASE/gcore -I$GDAL_BASEport -I$GDAL_BASEogr -package org.geotools.data.ogr.bridj -library osr $GDAL_BASE/ogr/ogr_srs_api.h -library ogr $GDAL_BASE/ogr/ogr_core.h $GDAL_BASE/ogr/ogr_api.h -library cplError $GDAL_BASE/port/cpl_error.h -o src/main/java  -v -runtime BridJ -reification  -nocpp -DCPL_DLL= -DCPL_STDCALL= -DCPL_C_START= -DCPL_C_END= -noComp -parseInOneChunk 
