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

package org.hsqldb.jdbc;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

// boucherb@users 20040411 - doc 1.7.2 - javadoc updates toward 1.7.2 final

/**
 * A JNDI ObjectFactory for creating {@link JDBCDataSource JDBCDataSource}
 * object instances.
 *
 * @author deforest@users
 * @version 1.7.2
 */
public class JDBCDataSourceFactory implements ObjectFactory {

    /**
     * Creates a JDBCDataSource object using the location or reference
     * information specified.<p>
     *
     * The Reference object should support the properties, database, user,
     * password.
     *
     * @param obj The reference information used in creating a
     *      JDBCDatasource object.
     * @param name ignored
     * @param nameCtx ignored
     * @param environment ignored
     * @return A newly created JDBCDataSource object; null if an object
     *      cannot be created.
     * @exception Exception never
     */
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
                                    Hashtable environment) throws Exception {

        String    dsClass = "org.hsqldb.jdbc.JDBCDataSource";
        Reference ref     = (Reference) obj;

        if (ref.getClassName().equals(dsClass)) {
            JDBCDataSource ds = new JDBCDataSource();

            ds.setDatabase((String) ref.get("database").getContent());
            ds.setUser((String) ref.get("user").getContent());
            ds.setPassword((String) ref.get("password").getContent());

            return ds;
        } else {
            return null;
        }
    }
}
