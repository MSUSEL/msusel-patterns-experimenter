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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

final class SetArgument implements ImapArgument
{
    public Object parse( StringTokenizer tokens ) throws Exception
    {
        if ( !tokens.hasMoreTokens() ) {
            throw new Exception( "Missing argument " + format() );
        }

        return tokens.nextToken();
    }

    private List parseSet( String rawSet )
    {
        List response = new ArrayList();

        int checkComma = rawSet.indexOf( "," );
        if ( checkComma == -1 ) {
            // No comma present
            int checkColon = rawSet.indexOf( ":" );
            if ( checkColon == -1 ) {
                // No colon present (single integer)
                Integer seqNum;
                if ( rawSet.equals( "*" ) ) {
                    seqNum = new Integer( -1 );
                }
                else {
                    seqNum = new Integer( rawSet.trim() );
                    if ( seqNum.intValue() < 1 ) {
                        throw new IllegalArgumentException( "Not a positive integer" );
                    }
                }
                response.add( seqNum );
            }
            else {
                // Simple sequence

                // Add the first number in the range.
                Integer firstNum = new Integer( rawSet.substring( 0, checkColon ) );
                int first = firstNum.intValue();
                if ( first < 1 ) {
                    throw new IllegalArgumentException( "Not a positive integer" );
                }
                response.add( firstNum );

                Integer lastNum;
                int last;
                if ( rawSet.indexOf( "*" ) != -1 ) {
                    // Range from firstNum to '*'
                    // Add -1, to indicate unended range.
                    lastNum = new Integer( -1 );
                }
                else {
                    // Get the final num, and add all numbers in range.
                    lastNum = new Integer( rawSet.substring( checkColon + 1 ) );
                    last = lastNum.intValue();
                    if ( last < 1 ) {
                        throw new IllegalArgumentException( "Not a positive integer" );
                    }
                    if ( last < first ) {
                        throw new IllegalArgumentException( "Not an increasing range" );
                    }

                    for ( int i = (first + 1); i <= last; i++ ) {
                        response.add( new Integer( i ) );
                    }
                }
            }

        }
        else {
            // Comma present, compound range.
            try {
                String firstRawSet = rawSet.substring( 0, checkComma );
                String secondRawSet = rawSet.substring( checkComma + 1 );
                response.addAll( parseSet( firstRawSet ) );
                response.addAll( parseSet( secondRawSet ) );
            }
            catch ( IllegalArgumentException e ) {
                throw e;
            }
        }
        return response;

    }

    public String format()
    {
        return "<set>";
    }

}
