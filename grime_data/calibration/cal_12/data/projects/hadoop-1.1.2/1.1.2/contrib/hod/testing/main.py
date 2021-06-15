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
import unittest, os, sys, re

myPath = os.path.realpath(sys.argv[0])
rootDirectory   = re.sub("/testing/.*", "", myPath)
testingDir = os.path.join(rootDirectory, "testing")

sys.path.append(rootDirectory)

from testing.lib import printSeparator, printLine

moduleList = []
allList = []
excludes = [
           ]

# Build a module list by scanning through all files in testingDir
for file in os.listdir(testingDir):
  if(re.search(r".py$", file) and re.search(r"^test", file)):
    # All .py files with names starting in 'test'
    module = re.sub(r"^test","",file)
    module = re.sub(r".py$","",module)
    allList.append(module)
    if module not in excludes:
      moduleList.append(module)

printLine("All testcases - %s" % allList)
printLine("Excluding the testcases - %s" % excludes)
printLine("Executing the testcases - %s" % moduleList)

testsResult = 0
# Now import each of these modules and start calling the corresponding
#testSuite methods
for moduleBaseName in moduleList:
  try:
    module = "testing.test" + moduleBaseName
    suiteCaller = "Run" + moduleBaseName + "Tests"
    printSeparator()
    printLine("Running %s" % suiteCaller)

    # Import the corresponding test cases module
    imported_module = __import__(module , fromlist=[suiteCaller] )
    
    # Call the corresponding suite method now
    testRes = getattr(imported_module, suiteCaller)()
    testsResult = testsResult + testRes
    printLine("Finished %s. TestSuite Result : %s\n" % \
                                              (suiteCaller, testRes))
  except ImportError, i:
    # Failed to import a test module
    printLine(i)
    testsResult = testsResult + 1
    pass
  except AttributeError, n:
    # Failed to get suiteCaller from a test module
    printLine(n)
    testsResult = testsResult + 1
    pass
  except Exception, e:
    # Test module suiteCaller threw some exception
    printLine("%s failed. \nReason : %s" % (suiteCaller, e))
    printLine("Skipping %s" % suiteCaller)
    testsResult = testsResult + 1
    pass

if testsResult != 0:
  printSeparator()
  printLine("Total testcases with failure or error : %s" % testsResult)
sys.exit(testsResult)
