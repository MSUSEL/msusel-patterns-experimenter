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

import tempfile, getpass, logging
from xml.dom import minidom

from hodlib.Hod.hadoop import hadoopConfig
from hodlib.HodRing.hodRing import CommandDesc, HadoopCommand

# All test-case classes should have the naming convention test_.*
class test_HadoopCommand(unittest.TestCase):
  def setUp(self):
    self.rootDir = '/tmp/hod-%s' % getpass.getuser()
    self.id = 0
    self.desc = None
    self.tempDir = os.path.join(self.rootDir,'test_HadoopCommand_tempDir')
    self.pkgDir = os.path.join(self.rootDir,'test_HadoopCommand_pkgDir')
    self.log = logging.getLogger() # TODO Use MockLogger
    self.javaHome = '/usr/java/bin/'
    self.mrSysDir = '/user/' + getpass.getuser() + '/mapredsystem'
    
    self.attrs = {}
    self.finalAttrs = {
                        'fs.default.name': 'nohost.apache.com:56366',
                        'mapred.child.java.opts' : '-Xmx1024m',
                        'mapred.compress.map.output' : 'false',
                      }
    self.attrs = {
                    'mapred.userlog.limit' : '200',
                    'mapred.userlog.retain.hours' : '10',
                    'mapred.reduce.parallel.copies' : '20',
                 }
    self.desc = CommandDesc(
                              {
                                'name' : 'dummyHadoop',
                                'program' : 'bin/hadoop',
                                'pkgdirs' : self.pkgDir,
                                'final-attrs' : self.finalAttrs,
                                'attrs' : self.attrs,
                              }, self.log
                            )
    # TODO
    #   4th arg to HadoopCommand 'tardir' is not used at all. Instead pkgdir is
    #   specified through HadoopCommand.run(pkgdir). This could be changed so
    #   that pkgdir is specified at the time of object creation.
    # END OF TODO
    self.hadoopCommand = HadoopCommand(self.id, self.desc, self.tempDir,
                          self.pkgDir, (50000, 60000), self.log, self.javaHome,
                          self.mrSysDir, restart=True)
    self.hadoopSite = os.path.join( self.hadoopCommand.confdir,
                                    'hadoop-site.xml')
    pass

  def test_createHadoopSiteXml(self):
    self.hadoopCommand._createHadoopSiteXml()
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

    # TODO other tests
    pass
    
  def tearDown(self):
    pass

class HodRingTestSuite(BaseTestSuite):
  def __init__(self):
    # suite setup
    BaseTestSuite.__init__(self, __name__, excludes)
    pass
  
  def cleanUp(self):
    # suite tearDown
    pass

def RunHodRingTests():
  # modulename_suite
  suite = HodRingTestSuite()
  testResult = suite.runTests()
  suite.cleanUp()
  return testResult

if __name__ == "__main__":
  RunHodRingTests()
