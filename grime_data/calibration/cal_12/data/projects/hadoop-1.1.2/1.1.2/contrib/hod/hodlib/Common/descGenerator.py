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
"""manage hod configuration"""
# -*- python -*-

import sys, csv, os
from optparse import Option, OptionParser
from xml.dom import minidom
from sets import Set
from select import select, poll, POLLIN

from hodlib.Common.desc import *

class DescGenerator:
  """Contains the conversion to descriptors and other method calls
  to config"""  
  def __init__(self, hodConfig):
    """parse all the descriptors"""
    
    self.hodConfig = hodConfig
    
  def initializeDesc(self):
    self.hodConfig['nodepooldesc'] = self.createNodePoolDesc()
    self.hodConfig['servicedesc'] = self.createServiceDescDict()
    
    return self.hodConfig
  
  def getServices(self):
    """get all the services from the config"""
    
    sdd = {}
    for keys in self.hodConfig:
      if keys.startswith('gridservice-'):
        str = keys.split('-')
        dict = self.hodConfig[keys]
        if 'server-params' in dict: dict['attrs'] = dict['server-params']
        if 'final-server-params' in dict: dict['final-attrs'] = dict['final-server-params']
        dict['id'] = str[1]
        desc = ServiceDesc(dict)
        sdd[desc.getName()] = desc 
        
    return sdd
  
  def createNodePoolDesc(self):
    """ create a node pool descriptor and store
    it in hodconfig"""
    
    desc = NodePoolDesc(self.hodConfig['resource_manager'])
    return desc
  
  def createServiceDescDict(self):
    """create a service descriptor for 
    all the services and store it in the 
    hodconfig"""
    
    sdd = self.getServices()
    return sdd
  
  
