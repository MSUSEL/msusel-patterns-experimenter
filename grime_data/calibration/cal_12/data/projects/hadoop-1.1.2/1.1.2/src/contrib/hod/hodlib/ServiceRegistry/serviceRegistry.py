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
import sys, time, socket, threading, copy, pprint

from hodlib.Common.hodsvc import hodBaseService
from hodlib.Common.threads import loop
from hodlib.Common.tcp import tcpSocket
from hodlib.Common.util import get_exception_string
import logging

class svcrgy(hodBaseService):
    def __init__(self, config, log=None):
        hodBaseService.__init__(self, 'serviceRegistry', config)
        
        self.__serviceDict = {}
        self.__failCount = {}
        self.__released = {}
        self.__locked = {}
        
        self.__serviceDictLock = threading.Lock()
        self.RMErrorMsgs = None # Ringmaster error messages
        self.log = log
        if self.log is None:
          self.log = logging.getLogger()
    
    def __get_job_key(self, userid, job):
        return "%s-%s" % (userid, job)
    
    def _xr_method_registerService(self, userid, job, host, name, type, dict):
       return self.registerService(userid, job, host, name, type, dict)
    
    def _xr_method_getServiceInfo(self, userid=None, job=None, name=None, 
                                  type=None):
        return self.getServiceInfo(userid, job, name, type)

    def _xr_method_setRMError(self, args):
        self.log.debug("setRMError called with %s" % args)
        self.RMErrorMsgs = args
        return True

    def _xr_method_getRMError(self):
        self.log.debug("getRMError called")
        if self.RMErrorMsgs is not None:
          return self.RMErrorMsgs
        else:
          self.log.debug("no Ringmaster error messages")
          return False

    def registerService(self, userid, job, host, name, type, dict):
        """Method thats called upon by
        the ringmaster to register to the
        the service registry"""
        lock = self.__serviceDictLock
        lock.acquire()
        try:
            self.logs['main'].debug("Registering %s.%s.%s.%s.%s..." % (
                                    userid, job, host, name, type))    
            id = "%s.%s" % (name, type) 
   
            if userid in self.__serviceDict:
                if job in self.__serviceDict[userid]:
                     if host in self.__serviceDict[userid][job]:
                          self.__serviceDict[userid][job][host].append(
                              {id : dict,})
                     else:
                        self.__serviceDict[userid][job][host] = [
                            {id : dict,},] 
                else:
                    self.__serviceDict[userid][job] = {host : [
                                                       { id : dict,},]}
            else:    
                self.__serviceDict[userid] = {job : {host : [
                                                     { id : dict,},]}}

        finally:
            lock.release()
            
        return True
    
    def getXMLRPCAddr(self):
      """return the xml rpc server address"""
      return self._xrc.server_address
    
    def getServiceInfo(self, userid=None, job=None, name=None, type=None):
        """This method is called upon by others
        to query for a particular service returns
        a dictionary of elements"""
        
        self.logs['main'].debug("inside getServiceInfo: %s.%s.%s" % (userid, job, name))
        retdict = {}
        lock = self.__serviceDictLock
        lock.acquire()
        try:
            if userid in self.__serviceDict:
                if job in self.__serviceDict[userid]:
                    if name and type:
                        retdict = []
                        id = "%s.%s" % (name, type)
                        for host in self.__serviceDict[userid][job]:
                            for dict in self.__serviceDict[userid][job][host]:
                                [loopID, ] = dict.keys()
                                if loopID == id:
                                    retdict.append(dict[id])
                    else:
                        retdict = copy.deepcopy(
                            self.__serviceDict[userid][job])
                elif not job:
                    retdict = copy.deepcopy(self.__serviceDict[userid])
            elif not userid:
                retdict = copy.deepcopy(self.__serviceDict)
        finally:
            lock.release()
        
        return retdict
