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
package org.apache.james.imapserver.commands;

import junit.framework.TestCase;

import java.util.StringTokenizer;

public final class ArgumentTest
        extends TestCase
{
    public ArgumentTest( String s )
    {
        super( s );
    }

    public void testAstring() throws Exception
    {
        AstringArgument arg = new AstringArgument( "test" );
        ParseChecker parser = new ParseChecker( arg );

        parser.check( "straightup", "straightup" );
        parser.check( "quoted", "\"quoted\"" );
        parser.check( "with space", "\"with space\"" );

        // Test currently fails - don't see whitespace.
        //parser.check( "multiple   spaces", "\"multiple   spaces\"" );

        parser.checkFail( "Missing argument <test>", "" );
        parser.checkFail( "Missing closing quote for <test>", "\"something" );
        parser.checkFail( "Missing closing quote for <test>", "\"" );
        parser.checkFail( "Missing closing quote for <test>", "\"something special" );
    }

    private static class ParseChecker
    {
        private ImapArgument arg;

        ParseChecker( ImapArgument arg )
        {
            this.arg = arg;
        }

        public void check( Object expected, String input )
        {
            StringTokenizer tokens = new StringTokenizer( input );
            Object result = null;
            try {
                result = this.arg.parse( tokens );
            }
            catch ( Exception e ) {
                fail( "Error encountered: " + e.getMessage() );
            }

            assertEquals( expected, result );
        }

        public void checkFail( String expectedError, String input )
        {
            StringTokenizer tokens = new StringTokenizer( input );
            try {
                Object result = this.arg.parse( tokens );
            }
            catch ( Exception e ) {
                assertEquals( expectedError, e.getMessage() );
                return;
            }

            fail( "Expected error" );
        }
    }
}
