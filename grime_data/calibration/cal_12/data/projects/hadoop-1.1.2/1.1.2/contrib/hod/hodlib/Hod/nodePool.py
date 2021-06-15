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
"""defines nodepool and nodeset as abstract interface for batch system"""
# -*- python -*-

from hodlib.GridServices.service import *

class NodeSet:
  """a set of nodes as one allocation unit"""

  PENDING, COMMITTED, COMPLETE = range(3)

  def __init__(self, id, numNodes, preferredList, isPreemptee):
    self.id = id
    self.numNodes = numNodes
    self.isPreemptee = isPreemptee
    self.preferredList = preferredList
    self.cmdDescSet = []

  def getId(self):
    """returns a unique id of the nodeset"""
    return self.id

  def registerCommand(self, cmdDesc):
    """register a command to the nodeset"""
    self.cmdDescSet.append(cmdDesc)

  def getAddrList(self):
    """get list of node host names
    May return empty list if node set is not allocated yet"""
    raise NotImplementedError

  def _getNumNodes(self):
    return self.numNodes

  def _isPreemptee(self):
    return self.isPreemptee

  def _getPreferredList(self):
    return self.preferredList

  def _getCmdSet(self):
    return self.cmdDescSet

class NodePool:
  """maintains a collection of node sets as they get allocated.
  Also the base class for all kinds of nodepools. """

  def __init__(self, nodePoolDesc, cfg, log):
    self.nodePoolDesc = nodePoolDesc
    self.nodeSetDict = {}
    self._cfg = cfg
    self.nextNodeSetId = 0
    self._log = log
    

  def newNodeSet(self, numNodes, preferred=[], isPreemptee=True, id=None):
    """create a nodeset possibly with asked properties"""
    raise NotImplementedError

  def submitNodeSet(self, nodeSet, walltime = None, qosLevel = None, 
                    account = None, resourcelist = None):
    """submit the nodeset request to nodepool
    return False if error happened"""
    raise NotImplementedError

  def pollNodeSet(self, nodeSet):
    """return status of node set"""
    raise NotImplementedError

  def getWorkers(self):
    """return the hosts that comprise this nodepool"""
    raise NotImplementedError

  def runWorkers(self, nodeSet = None, args = []):
    """Run node set workers."""
    
    raise NotImplementedError
  
  def freeNodeSet(self, nodeset):
    """free a node set"""
    raise NotImplementedError

  def finalize(self):
    """cleans up all nodesets"""
    raise NotImplementedError

  def getServiceId(self):
    raise NotImplementedError
 
  def getJobInfo(self, jobId=None):
    raise NotImplementedError

  def deleteJob(self, jobId):
    """Delete a job, given it's id"""
    raise NotImplementedError

  def isJobFeasible(self):
    """Check if job can run by looking at any user/job limits"""
    raise NotImplementedError

  def updateWorkerInfo(self, workerInfoMap, jobId):
    """Update information about the workers started by this NodePool."""
    raise NotImplementedError

  def getAccountString(self):
    """Return the account string for this job"""
    raise NotImplementedError

  def getNextNodeSetId(self):
    id = self.nextNodeSetId
    self.nextNodeSetId += 1
    
    return id  
  
