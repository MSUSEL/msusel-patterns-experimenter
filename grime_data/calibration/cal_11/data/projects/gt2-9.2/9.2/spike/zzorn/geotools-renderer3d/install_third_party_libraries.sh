#!/bin/bash
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


# Install JME libraries to local repository
mvn install:install-file -Dfile=lib/jme-0.11/jme.jar -DgroupId=jme -DartifactId=jme -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-awt.jar -DgroupId=jme -DartifactId=jme-awt -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-collada.jar -DgroupId=jme -DartifactId=jme-collada -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-editors.jar -DgroupId=jme -DartifactId=jme-editors -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-effects.jar -DgroupId=jme -DartifactId=jme-effects -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-font.jar -DgroupId=jme -DartifactId=jme-font -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-gamestates.jar -DgroupId=jme -DartifactId=jme-gamestates -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-model.jar -DgroupId=jme -DartifactId=jme-model -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-scene.jar -DgroupId=jme -DartifactId=jme-scene -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-sound.jar -DgroupId=jme -DartifactId=jme-sound -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/jme-0.11/jme-terrain.jar -DgroupId=jme -DartifactId=jme-terrain -Dversion=0.11 -Dpackaging=jar -DgeneratePom=true

# Install LWJGL libraries to local repository
mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/lwjgl.jar -DgroupId=lwjgl -DartifactId=lwjgl -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/lwjgl_test.jar -DgroupId=lwjgl -DartifactId=lwjgl_test -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/lwjgl_util.jar -DgroupId=lwjgl -DartifactId=lwjgl_util -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/lwjgl_util_applet.jar -DgroupId=lwjgl -DartifactId=lwjgl_util_applet -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=lib/lwjgl-1.0/jar/jinput.jar -DgroupId=lwjgl -DartifactId=jinput -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true

