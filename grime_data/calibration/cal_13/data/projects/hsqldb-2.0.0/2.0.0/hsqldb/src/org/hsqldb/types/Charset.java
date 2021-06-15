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

package org.hsqldb.types;

import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.SchemaObject;
import org.hsqldb.Session;
import org.hsqldb.SqlInvariants;
import org.hsqldb.Tokens;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.rights.Grantee;

/**
 * Implementation of CHARACTER SET objects.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class Charset implements SchemaObject {

    public static final int[][] uppercaseLetters   = new int[][] {
        {
            'A', 'Z'
        }
    };
    public static final int[][] unquotedIdentifier = new int[][] {
        {
            '0', '9'
        }, {
            'A', 'Z'
        }, {
            '_', '_'
        }
    };
    public static final int[][] basicIdentifier    = new int[][] {
        {
            '0', '9'
        }, {
            'A', 'Z'
        }, {
            '_', '_'
        }, {
            'a', 'z'
        }
    };
    HsqlName                    name;
    public HsqlName             base;

    //
    int[][] ranges;

    public Charset(HsqlName name) {
        this.name = name;
    }

    public int getType() {
        return SchemaObject.CHARSET;
    }

    public HsqlName getName() {
        return name;
    }

    public HsqlName getCatalogName() {
        return name.schema.schema;
    }

    public HsqlName getSchemaName() {
        return name.schema;
    }

    public Grantee getOwner() {
        return name.schema.owner;
    }

    public OrderedHashSet getReferences() {

        OrderedHashSet set = new OrderedHashSet();

        set.add(base);

        return set;
    }

    public OrderedHashSet getComponents() {
        return null;
    }

    public void compile(Session session, SchemaObject parentObject) {}

    public String getSQL() {

        StringBuffer sb = new StringBuffer();

        sb.append(Tokens.T_CREATE).append(' ').append(
            Tokens.T_CHARACTER).append(' ').append(Tokens.T_SET).append(' ');
        sb.append(name.getSchemaQualifiedStatementName());
        sb.append(' ').append(Tokens.T_AS).append(' ').append(Tokens.T_GET);
        sb.append(' ').append(base.getSchemaQualifiedStatementName());

        return sb.toString();
    }

    public long getChangeTimestamp() {
        return 0;
    }

    public static boolean isInSet(String value, int[][] ranges) {

        int length = value.length();

        mainLoop:
        for (int index = 0; index < length; index++) {
            int ch = value.charAt(index);

            for (int i = 0; i < ranges.length; i++) {
                if (ch > ranges[i][1]) {
                    continue;
                }

                if (ch < ranges[i][0]) {
                    return false;
                }

                continue mainLoop;
            }

            return false;
        }

        return true;
    }

    public static boolean startsWith(String value, int[][] ranges) {

        int ch = value.charAt(0);

        for (int i = 0; i < ranges.length; i++) {
            if (ch > ranges[i][1]) {
                continue;
            }

            if (ch < ranges[i][0]) {
                return false;
            }

            return true;
        }

        return false;
    }

    public static Charset getDefaultInstance() {
        return SqlInvariants.UTF16;
    }
}
