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
import unittest, os, sys, re, threading, time

myDirectory    = os.path.realpath(sys.argv[0])
rootDirectory   = re.sub("/testing/.*", "", myDirectory)

sys.path.append(rootDirectory)

from hodlib.Common.xmlrpc import hodXRClient
from hodlib.Common.socketServers import hodXMLRPCServer
from hodlib.GridServices.service import ServiceUtil
from hodlib.Common.util import hodInterrupt, HodInterruptException

from testing.lib import BaseTestSuite

excludes = []

global serverPort
serverPort = None

class test_HodXRClient(unittest.TestCase):
  def setUp(self):
    pass

  # All testMethods have to have their names start with 'test'
  def testSuccess(self):
    global serverPort
    client = hodXRClient('http://localhost:' + str(serverPort), retryRequests=False)
    self.assertEqual(client.testing(), True)
    pass
    
  def testFailure(self):
    """HOD should raise Exception when unregistered rpc is called"""
    global serverPort
    client = hodXRClient('http://localhost:' + str(serverPort), retryRequests=False)
    self.assertRaises(Exception, client.noMethod)
    pass

  def testTimeout(self):
    """HOD should raise Exception when rpc call times out"""
    # Give client some random nonexistent url
    serverPort = ServiceUtil.getUniqRandomPort(h='localhost',low=40000,high=50000)
    client = hodXRClient('http://localhost:' + str(serverPort), retryRequests=False)
    self.assertRaises(Exception, client.testing)
    pass

  def testInterrupt(self):
    """ HOD should raise HodInterruptException when interrupted"""

    def interrupt(testClass):
      testClass.assertRaises(HodInterruptException, client.testing)
      
    serverPort = ServiceUtil.getUniqRandomPort(h='localhost',low=40000,high=50000)
    client = hodXRClient('http://localhost:' + str(serverPort))
    myThread = threading.Thread(name='testinterrupt', target=interrupt,args=(self,))
    # Set the global interrupt
    hodInterrupt.setFlag()
    myThread.start()
    myThread.join()
    pass

  def tearDown(self):
    pass

class XmlrpcTestSuite(BaseTestSuite):
  def __init__(self):
    # suite setup
    BaseTestSuite.__init__(self, __name__, excludes)

    def rpcCall():
      return True
    
    global serverPort
    serverPort = ServiceUtil.getUniqRandomPort(h='localhost',low=40000,high=50000)
    self.server = hodXMLRPCServer('localhost', [serverPort])
    self.server.register_function(rpcCall, 'testing')
    self.thread = threading.Thread(name="server", 
                                   target=self.server._serve_forever)
    self.thread.start()
    time.sleep(1) # give some time to start server
  
  def cleanUp(self):
    # suite tearDown
    self.server.stop()
    self.thread.join()

def RunXmlrpcTests():
  # modulename_suite
  suite = XmlrpcTestSuite()
  testResult = suite.runTests()
  suite.cleanUp()
  return testResult

if __name__ == "__main__":
  RunXmlrpcTests()
