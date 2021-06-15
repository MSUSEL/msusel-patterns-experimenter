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
import urllib, urlparse, re

from HTMLParser import HTMLParser

class miniHTMLParser( HTMLParser ):

  viewedQueue = []
  instQueue = []

  def setBaseUrl(self, url):
    self.baseUrl = url

  def getNextLink( self ):
    if self.instQueue == []:
      return None
    else:
      return self.instQueue.pop(0)

  def handle_starttag( self, tag, attrs ):
    if tag == 'a':
      newstr = urlparse.urljoin(self.baseUrl, str(attrs[0][1]))
      if re.search('mailto', newstr) != None:
        return

      if (newstr in self.viewedQueue) == False:
        self.instQueue.append( newstr )
        self.viewedQueue.append( newstr )



