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

#rsync -e /usr/local/bin/ssh-C -av --exclude=WEB-INF/web.xml --exclude=WEB-INF/tag --exclude=WEB-INF/logs --exclude=WEB-INF/classes --exclude=WEB-INF/work --exclude=WEB-INF/tmp target/ivatagroupware-war/* root@ivata.com:/home/sites/groupware.ivata.org/demo
#ssh -C root@ivata.com /bin/chmod -R a+rX /home/sites/groupware.ivata.org/demo
rsync -e /usr/local/bin/ssh-C -av --delete --exclude=WEB-INF/classes/hibernate.cfg.xml --exclude=WEB-INF/web.xml --exclude=WEB-INF/logs --exclude=WEB-INF/work --exclude=WEB-INF/tmp target/ivatagroupware-war/* root@ivata.com:/usr/local/resin3/webapps/demo
ssh -C root@ivata.com /bin/chmod -R a+rX /usr/local/resin3/webapps/demo
################################################################################

