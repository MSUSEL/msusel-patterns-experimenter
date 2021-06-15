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
package org.apache.james.imapserver;

import java.util.StringTokenizer;

/**
 * An single client request to an IMAP server, with necessary details for
 * command processing
 *
 * @version 0.2 on 04 Aug 2002
 */
public class ImapRequestImpl implements ImapRequest
{

    private String _command;
    private StringTokenizer commandLine;
    private boolean useUIDs;
    private ACLMailbox currentMailbox;
    private String commandRaw;
    private String tag;
    private SingleThreadedConnectionHandler caller;
    private String currentFolder;

    public ImapRequestImpl(SingleThreadedConnectionHandler handler,
                           String command ) {
        caller = handler;
        _command = command;
    }
    
    public String getCommand()
    {
        return _command;
    }
    
    public void setCommand( String command )
    {
        _command = command;
    }

    public SingleThreadedConnectionHandler getCaller() {
        return caller;
    }

    public void setCommandLine(StringTokenizer st) {
        commandLine = st;
    }

    public StringTokenizer getCommandLine() {
        //return new java.util.StringTokenizer(this.getCommandRaw());
        return commandLine;
    }

    public int arguments()
    {
        return commandLine.countTokens();
    }

    public void setUseUIDs(boolean state) {
        useUIDs = state;
    }

    public boolean useUIDs() {
        return useUIDs;
    }

    public void setCurrentMailbox(ACLMailbox mbox) {
        currentMailbox = mbox;
    }

    public ACLMailbox getCurrentMailbox() {
        return currentMailbox;
    }

    public void setCommandRaw(String raw) {
        commandRaw = raw;
    }

    public String getCommandRaw() {
        return commandRaw;
    }

    public void setTag(String t) {
        tag = t;
    }

    public String getTag() {
        return tag;
    }

    public void setCurrentFolder(String f) {
        currentFolder = f;
    }

    public String getCurrentFolder() {
        return currentFolder;
    }
}
