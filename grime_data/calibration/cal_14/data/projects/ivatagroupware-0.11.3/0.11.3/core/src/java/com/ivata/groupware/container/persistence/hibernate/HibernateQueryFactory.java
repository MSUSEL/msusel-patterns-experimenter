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
package com.ivata.groupware.container.persistence.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since Mar 28, 2004
 * @version $Revision: 1.3 $
 */
public class HibernateQueryFactory implements Serializable {
    /**
     * Map of query names to the string queries which will be passed to
     * hibernate.
     */
    private Map queries;
    /**
     * Stores the order of arguments when passed as an object array.
     */
    private Map queryArguments;

    /**
     * Construct a new hibernate query factory with the given queries and
     * default argument order.
     */
    public HibernateQueryFactory(Map queries, Map queryArguments) {
        super();
        this.queries = queries;
        this.queryArguments = queryArguments;
    }

    /**
     * @see com.ivata.groupware.container.persistence.PersistenceQueryFactory#generateQuery(String, java.util.List)
     */
    public HibernateQuery generateQuery(final String name,
            final Map arguments) {
        String query = (String) queries.get(name);
        if (query == null) {
            throw new NullPointerException("Error in HibernateQueryFactory: no query called '"
                + name
                +"'");
        }
        return new HibernateQuery(query, arguments);
    }

    /**
     * @see com.ivata.groupware.container.persistence.PersistenceQueryFactory#generateQuery(String, Object[])
     */
    public HibernateQuery generateQuery(final String name,
            final Object[] arguments) {
        Object argumentNamesObject = queryArguments.get(name);
        String[] argumentNames;
        if (argumentNamesObject instanceof List) {
            argumentNames = (String[]) ((List) argumentNamesObject)
                .toArray(new String[] {});
        } else {
            assert (argumentNamesObject instanceof String[]);
            argumentNames = (String[]) argumentNamesObject;
        }
        if (argumentNames == null) {
            throw new NullPointerException("Error in HibernateQueryFactory: no query called '"
                + name
                +"'");
        }
        if(arguments.length > argumentNames.length) {
            throw new RuntimeException("Error in HibernateQueryFactory: too many arguments ("
                + arguments.length
                + ") provided - expected "
                + argumentNames.length);
        }
        Map argumentMap = new HashMap();
        for (int i = 0; i < arguments.length; i++) {
            String argumentName = argumentNames[i];
            Object argument = arguments[i];
            argumentMap.put(argumentName, argument);
        }
        return generateQuery(name, argumentMap);
    }
}
