/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.archive.net;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.LineIterator;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Client for FTP operations. Saves the commands sent to the server and replies
 * received, which can be retrieved with {@link #getControlConversation()}.
 * 
 * @author pjack
 * @author nlevitt
 */
public class ClientFTP extends FTPClient implements ProtocolCommandListener {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    // Records the conversation on the ftp control channel. The format is based on "curl -v".
    protected StringBuilder controlConversation;
    protected Socket dataSocket;

    /**
     * Constructs a new <code>ClientFTP</code>.
     */
    public ClientFTP() {
        controlConversation = new StringBuilder();
        addProtocolCommandListener(this);
    }

    /**
     * Opens a data connection.
     * 
     * @param command
     *            the data command (eg, RETR or LIST)
     * @param path
     *            the path of the file to retrieve
     * @return the socket to read data from, or null if server says not found,
     *         permission denied, etc
     * @throws IOException
     *             if a network error occurs
     */
    public Socket openDataConnection(int command, String path)
    throws IOException {
        try {
            dataSocket = _openDataConnection_(command, path);
            if (dataSocket != null) {
                recordAdditionalInfo("Opened data connection to "
                        + dataSocket.getInetAddress().getHostAddress() + ":"
                        + dataSocket.getPort());
            }
            return dataSocket;
        } catch (IOException e) {
            if (getPassiveHost() != null) {
                recordAdditionalInfo("Failed to open data connection to "
                        + getPassiveHost() + ":" + getPassivePort() + ": "
                        + e.getMessage());
            } else {
                recordAdditionalInfo("Failed to open data connection: "
                        + e.getMessage());
            }
            throw e;
        }
    }

    public void closeDataConnection() {
        if (dataSocket != null) {
            String dataHostPort = dataSocket.getInetAddress().getHostAddress()
                    + ":" + dataSocket.getPort();
            try {
                dataSocket.close();
                recordAdditionalInfo("Closed data connection to "
                        + dataHostPort);
            } catch (IOException e) {
                recordAdditionalInfo("Problem closing data connection to "
                        + dataHostPort + ": " + e.getMessage());
            }
        }
    }

    protected void _connectAction_() throws IOException {
        try {
            recordAdditionalInfo("Opening control connection to "
                    + getRemoteAddress().getHostAddress() + ":"
                    + getRemotePort());
            super._connectAction_();
        } catch (IOException e) {
            recordAdditionalInfo("Failed to open control connection to "
                    + getRemoteAddress().getHostAddress() + ":"
                    + getRemotePort() + ": " + e.getMessage());
            throw e;
        }
    }
    
    public void disconnect() throws IOException {
        String remoteHostPort = getRemoteAddress().getHostAddress() + ":"
                + getRemotePort();
        super.disconnect();
        recordAdditionalInfo("Closed control connection to " + remoteHostPort);
    }

    public String getControlConversation() {
        return controlConversation.toString();
    }    

    private class IterableLineIterator extends LineIterator implements Iterable<String> {
        public IterableLineIterator(final Reader reader) throws IllegalArgumentException {
            super(reader);
        }
        @SuppressWarnings("unchecked")
        public Iterator<String> iterator() {
            return this;
        }
    }
    
    protected void recordControlMessage(String linePrefix, String message) {
        for (String line: new IterableLineIterator(new BufferedReader(new StringReader(message)))) {
            controlConversation.append(linePrefix);
            controlConversation.append(line);
            controlConversation.append(NETASCII_EOL);
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest(linePrefix + line);
            }
        }
    }

    public void protocolCommandSent(ProtocolCommandEvent event) {
        recordControlMessage("> ", event.getMessage());
    }

    public void protocolReplyReceived(ProtocolCommandEvent event) {
        recordControlMessage("< ", event.getMessage());
    }

    // for noting things like successful/unsuccessful connection to data port
    private void recordAdditionalInfo(String message) {
        recordControlMessage("* ", message);
    }

    // XXX see https://issues.apache.org/jira/browse/NET-257
    @Override
    public String[] getReplyStrings() {
        return _replyLines.toArray(new String[0]);
    }
}
