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
package org.hibernate.cfg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.jboss.logging.Logger;

import org.hibernate.MappingException;
import org.hibernate.engine.ResultSetMappingDefinition;
import org.hibernate.engine.spi.NamedSQLQueryDefinition;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;

/**
 * @author Emmanuel Bernard
 */
public class NamedSQLQuerySecondPass extends ResultSetMappingBinder implements QuerySecondPass {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
                                                                       NamedSQLQuerySecondPass.class.getName());

	private Element queryElem;
	private String path;
	private Mappings mappings;

	public NamedSQLQuerySecondPass(Element queryElem, String path, Mappings mappings) {
		this.queryElem = queryElem;
		this.path = path;
		this.mappings = mappings;
	}

	public void doSecondPass(Map persistentClasses) throws MappingException {
		String queryName = queryElem.attribute( "name" ).getValue();
		if (path!=null) queryName = path + '.' + queryName;

		boolean cacheable = "true".equals( queryElem.attributeValue( "cacheable" ) );
		String region = queryElem.attributeValue( "cache-region" );
		Attribute tAtt = queryElem.attribute( "timeout" );
		Integer timeout = tAtt == null ? null : Integer.valueOf( tAtt.getValue() );
		Attribute fsAtt = queryElem.attribute( "fetch-size" );
		Integer fetchSize = fsAtt == null ? null : Integer.valueOf( fsAtt.getValue() );
		Attribute roAttr = queryElem.attribute( "read-only" );
		boolean readOnly = roAttr != null && "true".equals( roAttr.getValue() );
		Attribute cacheModeAtt = queryElem.attribute( "cache-mode" );
		String cacheMode = cacheModeAtt == null ? null : cacheModeAtt.getValue();
		Attribute cmAtt = queryElem.attribute( "comment" );
		String comment = cmAtt == null ? null : cmAtt.getValue();

		java.util.List<String> synchronizedTables = new ArrayList<String>();
		Iterator tables = queryElem.elementIterator( "synchronize" );
		while ( tables.hasNext() ) {
			synchronizedTables.add( ( (Element) tables.next() ).attributeValue( "table" ) );
		}
		boolean callable = "true".equals( queryElem.attributeValue( "callable" ) );

		NamedSQLQueryDefinition namedQuery;
		Attribute ref = queryElem.attribute( "resultset-ref" );
		String resultSetRef = ref == null ? null : ref.getValue();
		if ( StringHelper.isNotEmpty( resultSetRef ) ) {
			namedQuery = new NamedSQLQueryDefinition(
					queryName,
					queryElem.getText(),
					resultSetRef,
					synchronizedTables,
					cacheable,
					region,
					timeout,
					fetchSize,
					HbmBinder.getFlushMode( queryElem.attributeValue( "flush-mode" ) ),
					HbmBinder.getCacheMode( cacheMode ),
					readOnly,
					comment,
					HbmBinder.getParameterTypes( queryElem ),
					callable
			);
			//TODO check there is no actual definition elemnents when a ref is defined
		}
		else {
			ResultSetMappingDefinition definition = buildResultSetMappingDefinition( queryElem, path, mappings );
			namedQuery = new NamedSQLQueryDefinition(
					queryName,
					queryElem.getText(),
					definition.getQueryReturns(),
					synchronizedTables,
					cacheable,
					region,
					timeout,
					fetchSize,
					HbmBinder.getFlushMode( queryElem.attributeValue( "flush-mode" ) ),
					HbmBinder.getCacheMode( cacheMode ),
					readOnly,
					comment,
					HbmBinder.getParameterTypes( queryElem ),
					callable
			);
		}

		if ( LOG.isDebugEnabled() ) {
			LOG.debugf( "Named SQL query: %s -> %s", namedQuery.getName(), namedQuery.getQueryString() );
		}
		mappings.addSQLQuery( queryName, namedQuery );
	}
}
