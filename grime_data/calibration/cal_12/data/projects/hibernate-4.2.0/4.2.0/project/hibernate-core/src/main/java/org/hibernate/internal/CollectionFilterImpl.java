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
package org.hibernate.internal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.engine.query.spi.ParameterMetadata;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.Type;

/**
 * implementation of the <tt>Query</tt> interface for collection filters
 * @author Gavin King
 */
public class CollectionFilterImpl extends QueryImpl {

	private Object collection;

	public CollectionFilterImpl(
			String queryString,
	        Object collection,
	        SessionImplementor session,
	        ParameterMetadata parameterMetadata) {
		super( queryString, session, parameterMetadata );
		this.collection = collection;
	}


	/**
	 * @see org.hibernate.Query#iterate()
	 */
	public Iterator iterate() throws HibernateException {
		verifyParameters();
		Map namedParams = getNamedParams();
		return getSession().iterateFilter( 
				collection, 
				expandParameterLists(namedParams),
				getQueryParameters(namedParams) 
		);
	}

	/**
	 * @see org.hibernate.Query#list()
	 */
	public List list() throws HibernateException {
		verifyParameters();
		Map namedParams = getNamedParams();
		return getSession().listFilter( 
				collection, 
				expandParameterLists(namedParams),
				getQueryParameters(namedParams) 
		);
	}

	/**
	 * @see org.hibernate.Query#scroll()
	 */
	public ScrollableResults scroll() throws HibernateException {
		throw new UnsupportedOperationException("Can't scroll filters");
	}

	public Type[] typeArray() {
		List typeList = getTypes();
		int size = typeList.size();
		Type[] result = new Type[size+1];
		for (int i=0; i<size; i++) result[i+1] = (Type) typeList.get(i);
		return result;
	}

	public Object[] valueArray() {
		List valueList = getValues();
		int size = valueList.size();
		Object[] result = new Object[size+1];
		for (int i=0; i<size; i++) result[i+1] = valueList.get(i);
		return result;
	}

}
