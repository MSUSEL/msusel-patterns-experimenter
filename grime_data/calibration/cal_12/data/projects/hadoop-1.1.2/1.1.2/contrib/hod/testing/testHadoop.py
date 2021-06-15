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

excludes = []

import tempfile, getpass
from xml.dom import minidom

from hodlib.Hod.hadoop import hadoopConfig

# All test-case classes should have the naming convention test_.*
class test_hadoopConfig(unittest.TestCase):
  def setUp(self):
    self.__hadoopConfig = hadoopConfig()
    self.rootDir = '/tmp/hod-%s' % getpass.getuser()
    if not os.path.exists(self.rootDir):
      os.mkdir(self.rootDir)
    self.testingDir = tempfile.mkdtemp( dir=self.rootDir,
                                  prefix='HadoopTestSuite.test_hadoopConfig')
    self.confDir = tempfile.mkdtemp(dir=self.rootDir,
                                  prefix='HadoopTestSuite.test_hadoopConfig')
    self.tempDir = '/tmp/hod-%s/something' % getpass.getuser()
    self.hadoopSite = os.path.join(self.confDir,'hadoop-site.xml')
    self.numNodes = 4
    self.hdfsAddr = 'nosuchhost1.apache.org:50505'
    self.mapredAddr = 'nosuchhost2.apache.org:50506'
    self.finalServerParams = {
                                'mapred.child.java.opts' : '-Xmx1024m',
                                'mapred.compress.map.output' : 'false',
                             }
    self.serverParams = {
                          'mapred.userlog.limit' : '200',
                          'mapred.userlog.retain.hours' : '10',
                          'mapred.reduce.parallel.copies' : '20',
                        }
    self.clientParams = {
                          'mapred.tasktracker.tasks.maximum' : '2',
                          'io.sort.factor' : '100',
                          'io.sort.mb' : '200',
                          'mapred.userlog.limit.kb' : '1024',
                          'io.file.buffer.size' : '262144',
                        }
    self.clusterFactor = 1.9
    self.mySysDir = '/user/' + getpass.getuser() + '/mapredsystem'
    pass

  def testSuccess(self):
    self.__hadoopConfig.gen_site_conf(
                  confDir = self.confDir,\
                  tempDir = self.tempDir,\
                  numNodes = self.numNodes,\
                  hdfsAddr = self.hdfsAddr,\
                  mrSysDir = self.mySysDir,\
                  mapredAddr = self.mapredAddr,\
                  clientParams = self.clientParams,\
                  serverParams = self.serverParams,\
                  finalServerParams = self.finalServerParams,\
                  clusterFactor = self.clusterFactor

    )
    xmldoc = minidom.parse(self.hadoopSite)
    xmldoc = xmldoc.childNodes[0] # leave out xml spec
    properties = xmldoc.childNodes # children of tag configuration
    keyvals = {}
    for prop in properties:
      if not isinstance(prop,minidom.Comment):
        #      ---------- tag -------------------- -value elem-- data -- 
        name = prop.getElementsByTagName('name')[0].childNodes[0].data
        value = prop.getElementsByTagName('value')[0].childNodes[0].data
        keyvals[name] = value

    # fs.default.name should start with hdfs://
    assert(keyvals['fs.default.name'].startswith('hdfs://'))
    assert(keyvals['hadoop.tmp.dir'] == self.tempDir)

    # TODO other tests
    pass
    
  def tearDown(self):
    if os.path.exists(self.hadoopSite): os.unlink(self.hadoopSite)
    if os.path.exists(self.confDir) : os.rmdir(self.confDir)
    if os.path.exists(self.testingDir) : os.rmdir(self.testingDir)
    pass

class HadoopTestSuite(BaseTestSuite):
  def __init__(self):
    # suite setup
    BaseTestSuite.__init__(self, __name__, excludes)
    pass
  
  def cleanUp(self):
    # suite tearDown
    pass

def RunHadoopTests():
  suite = HadoopTestSuite()
  testResult = suite.runTests()
  suite.cleanUp()
  return testResult

if __name__ == "__main__":
  RunHadoopTests()
