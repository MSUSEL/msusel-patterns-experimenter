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

public interface ImapConstants
{
    // Basic response types
    String OK = "OK";
    String NO = "NO";
    String BAD = "BAD";
    String UNTAGGED = "*";

    String SP = " ";
    String VERSION = "IMAP4rev1";

    String AUTH_FAIL_MSG
            = "NO Command not authorized on this mailbox";
    String BAD_LISTRIGHTS_MSG
            = "BAD Command should be <tag> <LISTRIGHTS> <mailbox> <identifier>";
    String NO_NOTLOCAL_MSG
            = "NO Mailbox does not exist on this server";

    //mainly to switch on stack traces and catch responses;
    boolean DEEP_DEBUG = true;

    // Connection termination options
    int NORMAL_CLOSE = 0;
    int OK_BYE = 1;
    int UNTAGGED_BYE = 2;
    int TAGGED_NO = 3;
    int NO_BYE = 4;

    String LIST_WILD = "*";
    String LIST_WILD_FLAT = "%";
    char[] CTL = {};
    String[] ATOM_SPECIALS
            = {"(", ")", "{", " ", LIST_WILD, LIST_WILD_FLAT, };

    
}
