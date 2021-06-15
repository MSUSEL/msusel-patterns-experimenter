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

import java.io.InputStream;

/**
 * A Protocol test which reads the test protocol session from a file. The
 * file read is taken as "<test-name>.test", where <test-name> is the value
 * passed into the constructor.
 * Subclasses of this test can set up {@link #preElements} and {@link #postElements}
 * for extra elements not defined in the protocol session file.
 */
public class SimpleFileProtocolTest
        extends AbstractProtocolTest
{
    private FileProtocolSessionBuilder builder =
            new FileProtocolSessionBuilder();

    /**
     * Sets up a SimpleFileProtocolTest which reads the protocol session from
     * a file of name "<fileName>.test". This file should be available in the classloader
     * in the same location as this test class.
     * @param fileName The name of the file to read protocol elements from.
     */
    public SimpleFileProtocolTest( String fileName )
    {
        super( fileName );
    }

    /**
     * Reads test elements from the protocol session file and adds them to the
     * {@link #testElements} ProtocolSession. Then calls {@link #runSessions}.
     */
    protected void runTest() throws Throwable
    {
        String fileName = getName() + ".test";
        addTestFile( fileName, testElements );

        runSessions();
    }

    /**
     * Finds the protocol session file identified by the test name, and builds
     * protocol elements from it. All elements from the definition file are added
     * to the supplied ProtocolSession.
     * @param fileName The name of the file to read
     * @param session The ProtocolSession to add elements to.
     */ 
    protected void addTestFile( String fileName, ProtocolSession session) throws Exception
    {
        // Need to find local resource.
        InputStream is = this.getClass().getResourceAsStream( fileName );
        if ( is == null ) {
            throw new Exception( "Test Resource '" + fileName + "' not found." );
        }

        builder.addProtocolLinesFromStream( is, session, fileName );
    }
}
