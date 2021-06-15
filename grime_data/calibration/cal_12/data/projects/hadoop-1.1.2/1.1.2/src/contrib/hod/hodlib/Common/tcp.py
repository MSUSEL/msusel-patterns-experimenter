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
# $Id:tcp.py 6172 2007-05-22 20:26:54Z zim $
#
#------------------------------------------------------------------------------

""" TCP related classes. """

import socket, re, string
reAddress    = re.compile(":")
reMayBeIp = re.compile("^\d+\.\d+\.\d+\.\d+$")
reValidPort = re.compile("^\d+$")

class Error(Exception):
    def __init__(self, msg=''):
        self.message = msg
        Exception.__init__(self, msg)

    def __repr__(self):
        return self.message

class tcpError(Error):
    def __init__(self, message):
        Error.__init__(self, message)

class tcpSocket:
    def __init__(self, address, timeout=30, autoflush=0):
        """Constructs a tcpSocket object.

           address - standard tcp address (HOST:PORT)
           timeout - socket timeout"""

        self.address = address
        self.__autoFlush = autoflush
        self.__remoteSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.__remoteSock.settimeout(timeout)
        self.host = None
        self.port = None
        splitAddress = address
        if isinstance(address, (tuple, list)):
            self.host = address[0]
            self.port = int(address[1])
        else:
            splitAddress = get_address_tuple(address)
            if not splitAddress[0]:
                self.host = 'localhost'
            else:
                self.host = splitAddress[0]

            self.port = int(splitAddress[1])

        self.__fileObjectOut = ''
        self.__fileObjectIn = ''

    def __repr__(self):
        return self.address

    def __iter__(self):
        return self

    def next(self):
        sockLine = self.read()
        if not sockLine:
            raise StopIteration

        return sockLine

    def open(self):
        """Attempts to open a socket to the specified address."""

        socketAddress = (self.host, self.port)

        try:
            self.__remoteSock.connect(socketAddress)
            if self.__autoFlush:
                self.__fileObjectOut = self.__remoteSock.makefile('wb', 0)
            else:
                self.__fileObjectOut = self.__remoteSock.makefile('wb')

            self.__fileObjectIn  = self.__remoteSock.makefile('rb', 0)
        except:
            raise tcpError, "connection failure: %s" % self.address

    def flush(self):
        """Flushes write buffer."""
        self.__fileObjectOut.flush()

    def close(self):
        """Attempts to close and open socket connection"""

        try:
            self.__remoteSock.close()
            self.__fileObjectOut.close()
            self.__fileObjectIn.close()
        except socket.error, exceptionObject:
            exceptionMessage = "close failure %s %s" % (self.address,
                exceptionObject.__str__())
            raise tcpError, exceptionMessage

    def verify(self):
        """Verifies that a given IP address/host and port are valid. This
           method will not attempt to open a socket to the specified address.
        """

        isValidAddress = False
        if reMayBeIp.match(self.host):
            if check_ip_address(self.host):
                if reValidPort.match(str(self.port)):
                    isValidAddress = True
        else:
            if reValidPort.match(str(self.port)):
                isValidAddress = True

        return(isValidAddress)

    def read(self):
        """Reads a line off of the active socket."""

        return self.__fileObjectIn.readline()

    def write(self, string):
        """Writes a string to the active socket."""

        print >> self.__fileObjectOut, string

def check_net_address(address):
    valid = True
    pieces = string.split(address, '.')
    if len(pieces) != 4:
        valid = False
    else:
        for piece in pieces:
            if int(piece) < 0 or int(piece) > 255:
                valid = False

    return valid

def check_ip_address(address):
    valid = True
    pieces = string.split(address, '.')
    if len(pieces) != 4:
        valid = False
    else:
        if int(pieces[0]) < 1 or int(pieces[0]) > 254:
            valid = False
        for i in range(1,4):
            if int(pieces[i]) < 0 or int(pieces[i]) > 255:
                valid = False

    return valid

def get_address_tuple(address):
    """ Returns an address tuple for TCP address.

        address - TCP address of the form host:port

        returns address tuple (host, port)
    """

    addressList = reAddress.split(address)
    addressTuple = (addressList[0], int(addressList[1]))

    return addressTuple
