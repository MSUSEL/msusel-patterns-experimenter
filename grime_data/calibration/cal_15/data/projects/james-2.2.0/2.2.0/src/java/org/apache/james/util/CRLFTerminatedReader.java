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
package org.apache.james.util;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A Reader for use with SMTP or other protocols in which lines
 * must end with CRLF.  Extends BufferedReader and overrides its 
 * readLine() method.  The BufferedReader readLine() method cannot
 * serve for SMTP because it ends lines with either CR or LF alone. 
 */
public class CRLFTerminatedReader extends BufferedReader {

    public class TerminationException extends IOException {
        private int where;
        public TerminationException(int where) {
            super();
            this.where = where;
        }

        public TerminationException(String s, int where) {
            super(s);
            this.where = where;
        }

        public int position() {
            return where;
        }
    }

    /**
     * Constructs this CRLFTerminatedReader.
     * @param in an InputStream
     * @param charsetName the String name of a supported charset.  
     * "ASCII" is common here.
     * @throws UnsupportedEncodingException if the named charset
     * is not supported
     */
    public CRLFTerminatedReader(InputStream in, String charsetName)
            throws UnsupportedEncodingException {
        super(new InputStreamReader(in, charsetName));
    }

    private StringBuffer lineBuffer = new StringBuffer();
    private final int
            EOF = -1,
            CR  = 13,
            LF  = 10;

    private int tainted = -1;

    /**
     * Read a line of text which is terminated by CRLF.  The concluding
     * CRLF characters are not returned with the String, but if either CR
     * or LF appears in the text in any other sequence it is returned
     * in the String like any other character.  Some characters at the 
     * end of the stream may be lost if they are in a "line" not
     * terminated by CRLF.
     * 
     * @return either a String containing the contents of a 
     * line which must end with CRLF, or null if the end of the 
     * stream has been reached, possibly discarding some characters 
     * in a line not terminated with CRLF. 
     * @throws IOException if an I/O error occurs.
     */
    public String readLine() throws IOException{

        //start with the StringBuffer empty
        lineBuffer.delete(0, lineBuffer.length());

        /* This boolean tells which state we are in,
         * depending upon whether or not we got a CR
         * in the preceding read().
         */ 
        boolean cr_just_received = false;

        while (true){
            int inChar = read();

            if (!cr_just_received){
                //the most common case, somewhere before the end of a line
                switch (inChar){
                    case CR  :  cr_just_received = true;
                                break;
                    case EOF :  return null;   // premature EOF -- discards data(?)
                    case LF  :  //the normal ending of a line
                        if (tainted == -1) tainted = lineBuffer.length();
                        // intentional fall-through
                    default  :  lineBuffer.append((char)inChar);
                }
            }else{
                // CR has been received, we may be at end of line
                switch (inChar){
                    case LF  :  // LF without a preceding CR
                        if (tainted != -1) {
                            int pos = tainted;
                            tainted = -1;
                            throw new TerminationException("\"bare\" CR or LF in data stream", pos);
                        }
                        return lineBuffer.toString();
                    case EOF :  return null;   // premature EOF -- discards data(?)
                    case CR  :  //we got two (or more) CRs in a row
                        if (tainted == -1) tainted = lineBuffer.length();
                        lineBuffer.append((char)CR);
                        break;
                    default  :  //we got some other character following a CR
                        if (tainted == -1) tainted = lineBuffer.length();
                        lineBuffer.append((char)CR);
                        lineBuffer.append((char)inChar);
                        cr_just_received = false;
                }
            }
        }//while
    }//method readLine()
}
