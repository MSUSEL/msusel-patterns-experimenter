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

#     http://www.apache.org/licenses/LICENSE-2.0

#Unless required by applicable law or agreed to in writing, software
#distributed under the License is distributed on an "AS IS" BASIS,
#WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#See the License for the specific language governing permissions and
#limitations under the License.
"""HOD Service Proxy Implementation"""
# -*- python -*-

import sys, time, signal, httplib, socket, threading
import sha, base64, hmac
import xml.dom.minidom

from hodlib.Common.socketServers import hodHTTPServer
from hodlib.Common.hodsvc import hodBaseService
from hodlib.Common.threads import loop
from hodlib.Common.tcp import tcpSocket
from hodlib.Common.util import get_exception_string
from hodlib.Common.AllocationManagerUtil import *

class svcpxy(hodBaseService):
    def __init__(self, config):
        hodBaseService.__init__(self, 'serviceProxy', config['service_proxy'],
                                xrtype='twisted')
        self.amcfg=config['allocation_manager']

    def _xr_method_isProjectUserValid(self, userid, project, ignoreErrors = False, timeOut = 15):
       return self.isProjectUserValid(userid, project, ignoreErrors, timeOut)
    
    def isProjectUserValid(self, userid, project, ignoreErrors, timeOut):
        """Method thats called upon by
        the hodshell to verify if the 
        specified (user, project) combination 
        is valid"""
        self.logs['main'].info("Begin isProjectUserValid()")
        am = AllocationManagerUtil.getAllocationManager(self.amcfg['id'], 
                                                        self.amcfg,
                                                        self.logs['main'])
        self.logs['main'].info("End isProjectUserValid()")
        return am.getQuote(userid, project)
