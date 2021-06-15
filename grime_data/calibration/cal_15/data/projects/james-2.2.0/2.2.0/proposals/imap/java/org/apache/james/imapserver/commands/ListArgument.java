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

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

final class ListArgument implements ImapArgument
{
    private String type;

    public ListArgument( String type )
    {
        this.type = type;
    }

    public Object parse( StringTokenizer tokens ) throws Exception
    {
        // TODO: implement this properly.
        String attr = tokens.nextToken();
        List dataNames = new ArrayList();

        if ( !attr.startsWith( "(" ) ) {
            throw new Exception( "Missing '(': " );
        }
        else if ( attr.endsWith( ")" ) ) { //single attr in paranthesis
            dataNames.add( attr.substring( 1, attr.length() - 1 ) );
        }
        else { // multiple attrs
            dataNames.add( attr.substring( 1 ).trim() );
            while ( tokens.hasMoreTokens() ) {
                attr = tokens.nextToken();
                if ( attr.endsWith( ")" ) ) {
                    dataNames.add( attr.substring( 0, attr.length() - 1 ) );
                }
                else {
                    dataNames.add( attr );
                }
            }
        }

        return dataNames;
    }

    public String format()
    {
        return "( <" + this.type + ">+ )";
    }
}
