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
package org.apache.james.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A builder which generates a ProtocolSession from a test file.
 *
 *
 * @version $Revision: 1.4.2.3 $
 */
public class FileProtocolSessionBuilder
{
    private static final String CLIENT_TAG = "C:";
    private static final String SERVER_TAG = "S:";
    private static final String OPEN_UNORDERED_BLOCK_TAG = "SUB {";
    private static final String CLOSE_UNORDERED_BLOCK_TAG = "}";
    private static final String COMMENT_TAG = "#";

    /**
     * Builds a ProtocolSession by reading lines from the test file
     * with the supplied name.
     * @param fileName The name of the protocol session file.
     * @return The ProtocolSession
     */
    public ProtocolSession buildProtocolSession( String fileName )
            throws Exception
    {
        ProtocolSession session = new ProtocolSession();
        addTestFile( fileName, session );
        return session;
    }

    /**
     * Adds all protocol elements from a test file to the ProtocolSession supplied.
     * @param fileName The name of the protocol session file.
     * @param session The ProtocolSession to add the elements to.
     */
    public void addTestFile( String fileName, ProtocolSession session )
            throws Exception
    {
        // Need to find local resource.
        InputStream is = this.getClass().getResourceAsStream( fileName );
        if ( is == null ) {
            throw new Exception( "Test Resource '" + fileName + "' not found." );
        }

        addProtocolLinesFromStream( is, session, fileName );
    }

    /**
     * Reads ProtocolElements from the supplied InputStream and adds
     * them to the ProtocolSession.
     * @param is The input stream containing the protocol definition.
     * @param session The ProtocolSession to add elements to.
     * @param fileName The name of the source file, for error messages.
     */ 
    public void addProtocolLinesFromStream( InputStream is,
                                             ProtocolSession session,
                                             String fileName )
            throws Exception
    {
        BufferedReader reader = new BufferedReader( new InputStreamReader( is ) );
        String next;
        int lineNumber = 1;
        while ( ( next = reader.readLine() ) != null ) {
            String location = fileName + ":" + lineNumber;
            if ( next.startsWith( CLIENT_TAG ) ) {
                String clientMsg = "";
                if ( next.length() > 3 ) {
                    clientMsg = next.substring( 3 );
                }
                session.CL( clientMsg );
            }
            else if ( next.startsWith( SERVER_TAG ) ) {
                String serverMsg = "";
                if ( next.length() > 3 ) {
                    serverMsg = next.substring( 3 );
                }
                session.SL( serverMsg, location );
            }
            else if ( next.startsWith( OPEN_UNORDERED_BLOCK_TAG ) ) {
                List unorderedLines = new ArrayList( 5 );
                next = reader.readLine();

                while ( !next.startsWith( CLOSE_UNORDERED_BLOCK_TAG ) ) {
                    if (! next.startsWith( SERVER_TAG ) ) {
                        throw new Exception( "Only 'S: ' lines are permitted inside a 'SUB {' block.");
                    }
                    String serverMsg = next.substring( 3 );
                    unorderedLines.add( serverMsg );
                    next = reader.readLine();
                    lineNumber++;
                }

                session.SUB( unorderedLines, location );
            }
            else if ( next.startsWith( COMMENT_TAG )
                    || next.trim().length() == 0 ) {
                // ignore these lines.
            }
            else {
                String prefix = next;
                if ( next.length() > 3 ) {
                    prefix = next.substring( 0, 3 );
                }
                throw new Exception( "Invalid line prefix: " + prefix );
            }
            lineNumber++;
        }
    }

}
