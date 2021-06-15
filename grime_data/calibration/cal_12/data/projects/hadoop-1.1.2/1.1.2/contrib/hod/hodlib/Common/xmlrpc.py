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
import xmlrpclib, time, random, signal
from hodlib.Common.util import hodInterrupt, HodInterruptException

class hodXRClient(xmlrpclib.ServerProxy):
    def __init__(self, uri, transport=None, encoding=None, verbose=0,
                 allow_none=0, installSignalHandlers=1, retryRequests=True, timeOut=15):
        xmlrpclib.ServerProxy.__init__(self, uri, transport, encoding, verbose, 
                                       allow_none)
        self.__retryRequests = retryRequests
        self.__timeOut = timeOut
        if (installSignalHandlers!=0):
          self.__set_alarm()
    
    def __set_alarm(self):
        def alarm_handler(sigNum, sigHandler):
            raise Exception("XML-RPC socket timeout.")
          
        signal.signal(signal.SIGALRM, alarm_handler)
      
    def __request(self, methodname, params):
        response = None
        retryWaitTime = 5 + random.randint(0, 5)
        for i in range(0, 30):
            signal.alarm(self.__timeOut)
            try:
                response = self._ServerProxy__request(methodname, params)
                signal.alarm(0)
                break
            except Exception:
                if self.__retryRequests:
                  if hodInterrupt.isSet():
                    raise HodInterruptException()
                  time.sleep(retryWaitTime)
                else:
                  raise Exception("hodXRClientTimeout")

        return response
                
    def __getattr__(self, name):
        # magic method dispatcher
        return xmlrpclib._Method(self.__request, name)
                           
