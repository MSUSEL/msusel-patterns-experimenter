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
package org.hibernate.sql.ordering.antlr;

import java.io.StringReader;
import java.util.Set;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.hql.internal.ast.util.ASTPrinter;

/**
 * A translator for order-by mappings, whether specified by hbm.xml files, Hibernate
 * {@link org.hibernate.annotations.OrderBy} annotation or JPA {@link javax.persistence.OrderBy} annotation.
 *
 * @author Steve Ebersole
 */
public class OrderByFragmentTranslator {
	private static final Logger LOG = Logger.getLogger( OrderByFragmentTranslator.class.getName() );

	/**
	 * Perform the translation of the user-supplied fragment, returning the translation.
	 * <p/>
	 * The important distinction to this split between (1) translating and (2) resolving aliases is that
	 * both happen at different times
	 *
	 *
	 * @param context Context giving access to delegates needed during translation.
	 * @param fragment The user-supplied order-by fragment
	 *
	 * @return The translation.
	 */
	public static OrderByTranslation translate(TranslationContext context, String fragment) {
		GeneratedOrderByLexer lexer = new GeneratedOrderByLexer( new StringReader( fragment ) );

		// Perform the parsing (and some analysis/resolution).  Another important aspect is the collection
		// of "column references" which are important later to seek out replacement points in the
		// translated fragment.
		OrderByFragmentParser parser = new OrderByFragmentParser( lexer, context );
		try {
			parser.orderByFragment();
		}
		catch ( HibernateException e ) {
			throw e;
		}
		catch ( Throwable t ) {
			throw new HibernateException( "Unable to parse order-by fragment", t );
		}

		if ( LOG.isTraceEnabled() ) {
			ASTPrinter printer = new ASTPrinter( OrderByTemplateTokenTypes.class );
			LOG.trace( printer.showAsString( parser.getAST(), "--- {order-by fragment} ---" ) );
		}

		// Render the parsed tree to text.
		OrderByFragmentRenderer renderer = new OrderByFragmentRenderer( context.getSessionFactory() );
		try {
			renderer.orderByFragment( parser.getAST() );
		}
		catch ( HibernateException e ) {
			throw e;
		}
		catch ( Throwable t ) {
			throw new HibernateException( "Unable to render parsed order-by fragment", t );
		}

		return new StandardOrderByTranslationImpl( renderer.getRenderedFragment(), parser.getColumnReferences() );
	}

	public static class StandardOrderByTranslationImpl implements OrderByTranslation {
		private final String sqlTemplate;
		private final Set<String> columnReferences;

		public StandardOrderByTranslationImpl(String sqlTemplate, Set<String> columnReferences) {
			this.sqlTemplate = sqlTemplate;
			this.columnReferences = columnReferences;
		}

		@Override
		public String injectAliases(OrderByAliasResolver aliasResolver) {
			String sql = sqlTemplate;
			for ( String columnReference : columnReferences ) {
				final String replacementToken = "{" + columnReference + "}";
				sql = sql.replace(
						replacementToken,
						aliasResolver.resolveTableAlias( columnReference ) + '.' + columnReference
				);
			}
			return sql;
		}
	}
}
