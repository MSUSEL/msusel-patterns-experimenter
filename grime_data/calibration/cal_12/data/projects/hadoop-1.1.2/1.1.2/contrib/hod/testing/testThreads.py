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

# module specific imports
import os, tempfile, random

excludes = []

import getpass
from hodlib.Common.threads import simpleCommand
from testing.helper import sampleText

# All test-case classes should have the naming convention test_.*
class test_SimpleCommand(unittest.TestCase):
  def setUp(self):
    self.rootDir = '/tmp/hod-%s' % getpass.getuser()
    if not os.path.exists(self.rootDir):
      os.mkdir(self.rootDir)
    self.prefix= 'ThreadsTestSuite.test_SimpleCommand'
    self.testFile = None
    pass

  def testRedirectedStdout(self):
    self.testFile= tempfile.NamedTemporaryFile(dir=self.rootDir, \
                                               prefix=self.prefix)
    cmd=simpleCommand('helper','%s %s 1 1>%s' % \
                      (sys.executable, \
                      os.path.join(rootDirectory, "testing", "helper.py"), \
                      self.testFile.name))

    cmd.start()
    cmd.join()
    
    self.testFile.seek(0)
    stdout = self.testFile.read()
    # print stdout, sampleText
    assert(stdout == sampleText)
    pass

  def testRedirectedStderr(self):
    self.testFile= tempfile.NamedTemporaryFile(dir=self.rootDir, \
                                                prefix=self.prefix)
    cmd=simpleCommand('helper','%s %s 2 2>%s' % \
                      (sys.executable, \
                      os.path.join(rootDirectory, "testing", "helper.py"), \
                      self.testFile.name))
    cmd.start()
    cmd.join()
     
    self.testFile.seek(0)
    stderror = self.testFile.read()
    # print stderror, sampleText
    assert(stderror == sampleText)
    pass

  def tearDown(self):
    if self.testFile: self.testFile.close()
    pass

class ThreadsTestSuite(BaseTestSuite):
  def __init__(self):
    # suite setup
    BaseTestSuite.__init__(self, __name__, excludes)
    pass
  
  def cleanUp(self):
    # suite tearDown
    pass

def RunThreadsTests():
  # modulename_suite
  suite = ThreadsTestSuite()
  testResult = suite.runTests()
  suite.cleanUp()
  return testResult

if __name__ == "__main__":
  RunThreadsTests()
