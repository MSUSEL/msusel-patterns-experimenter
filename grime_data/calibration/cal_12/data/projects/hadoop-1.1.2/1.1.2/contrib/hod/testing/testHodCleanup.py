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

myDirectory = os.path.realpath(sys.argv[0])
rootDirectory   = re.sub("/testing/.*", "", myDirectory)

sys.path.append(rootDirectory)

from testing.lib import BaseTestSuite
from hodlib.HodRing.hodRing import MRSystemDirectoryManager, createMRSystemDirectoryManager
from hodlib.Common.threads import simpleCommand

excludes = []

# duplicating temporarily until HADOOP-2848 is committed.
class MyMockLogger:
  def __init__(self):
    self.__logLines = {}

  def info(self, message):
    self.__logLines[message] = 'info'

  def critical(self, message):
    self.__logLines[message] = 'critical'

  def warn(self, message):
    self.__logLines[message] = 'warn'

  def debug(self, message):
    # don't track debug lines.
    pass

  # verify a certain message has been logged at the defined level of severity.
  def hasMessage(self, message, level):
    if not self.__logLines.has_key(message):
      return False
    return self.__logLines[message] == level

class test_MRSystemDirectoryManager(unittest.TestCase):

  def setUp(self):
    self.log = MyMockLogger()

  def testCleanupArgsString(self):
    sysDirMgr = MRSystemDirectoryManager(1234, '/user/hod/mapredsystem/hoduser.123.abc.com', \
                                          'def.com:5678', '/usr/bin/hadoop', self.log)
    str = sysDirMgr.toCleanupArgs()
    self.assertTrue(" --jt-pid 1234 --mr-sys-dir /user/hod/mapredsystem/hoduser.123.abc.com --fs-name def.com:5678 --hadoop-path /usr/bin/hadoop ", str) 

  def testCreateMRSysDirInvalidParams(self):
    # test that no mr system directory manager is created if required keys are not present
    # this case will test scenarios of non jobtracker daemons.
    keys = [ 'jt-pid', 'mr-sys-dir', 'fs-name', 'hadoop-path' ]
    map = { 'jt-pid' : 1234,
            'mr-sys-dir' : '/user/hod/mapredsystem/hoduser.def.com',
            'fs-name' : 'ghi.com:1234',
            'hadoop-path' : '/usr/bin/hadoop'
          }
    for key in keys:
      val = map[key]
      map[key] = None
      self.assertEquals(createMRSystemDirectoryManager(map, self.log), None)
      map[key] = val

  def testUnresponsiveJobTracker(self):
    # simulate an unresponsive job tracker, by giving a command that runs longer than the retries
    # verify that the program returns with the right error message.
    sc = simpleCommand("sleep", "sleep 300")
    sc.start()
    pid = sc.getPid()
    while pid is None:
      pid = sc.getPid()
    sysDirMgr = MRSystemDirectoryManager(pid, '/user/yhemanth/mapredsystem/hoduser.123.abc.com', \
                                                'def.com:5678', '/usr/bin/hadoop', self.log, retries=3)
    sysDirMgr.removeMRSystemDirectory()
    self.log.hasMessage("Job Tracker did not exit even after a minute. Not going to try and cleanup the system directory", 'warn')
    sc.kill()
    sc.wait()
    sc.join()

class HodCleanupTestSuite(BaseTestSuite):
  def __init__(self):
    # suite setup
    BaseTestSuite.__init__(self, __name__, excludes)
    pass
  
  def cleanUp(self):
    # suite tearDown
    pass

def RunHodCleanupTests():
  # modulename_suite
  suite = HodCleanupTestSuite()
  testResult = suite.runTests()
  suite.cleanUp()
  return testResult

if __name__ == "__main__":
  RunHodCleanupTests()
