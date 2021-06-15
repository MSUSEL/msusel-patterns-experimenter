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
package org.hibernate.test.legacy;

import java.util.List;

import org.junit.Before;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.DefaultNamingStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.hql.internal.classic.ClassicQueryTranslatorFactory;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.type.Type;

/**
 * @author Steve Ebersole
 */
public abstract class LegacyTestCase extends BaseCoreFunctionalTestCase {
	public static final String USE_ANTLR_PARSER_PROP = "legacy.use_antlr_hql_parser";

	private boolean useAntlrParser;

	@Before
	@SuppressWarnings( {"UnnecessaryUnboxing"})
	public void checkAntlrParserSetting() {
		useAntlrParser = Boolean.valueOf( extractFromSystem( USE_ANTLR_PARSER_PROP ) );
	}

	protected boolean supportsLockingNullableSideOfJoin(Dialect dialect) {
		// db2 and pgsql do *NOT*
		return ! ( DB2Dialect.class.isInstance( dialect ) || PostgreSQL81Dialect.class.isInstance( dialect ) || PostgreSQLDialect.class.isInstance( dialect ));
	}

	protected static String extractFromSystem(String systemPropertyName) {
		try {
			return System.getProperty( systemPropertyName );
		}
		catch( Throwable t ) {
			return null;
		}
	}

	@Override
	protected void cleanupTestData() throws Exception {
		Session s = openSession();
		s.beginTransaction();
		List list = s.createQuery( "from java.lang.Object" ).list();
		for ( Object obj : list ) {
			s.delete( obj );
		}
		s.getTransaction().commit();
		s.close();
	}

	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		if ( !useAntlrParser ) {
			cfg.setProperty( Environment.QUERY_TRANSLATOR, ClassicQueryTranslatorFactory.class.getName() );
			try {
				String dialectTrueRepresentation = Dialect.getDialect().toBooleanValueString( true );
				// if this call succeeds, then the dialect is saying to represent true/false as int values...
				Integer.parseInt( dialectTrueRepresentation );
				String subs = cfg.getProperties().getProperty( Environment.QUERY_SUBSTITUTIONS );
				if ( subs == null ) {
					subs = "";
				}
				if ( StringHelper.isEmpty( subs ) ) {
					subs = "true=1, false=0";
				}
				else {
					subs += ", true=1, false=0";
				}
				cfg.getProperties().setProperty( Environment.QUERY_SUBSTITUTIONS, subs );
				cfg.setNamingStrategy( DefaultNamingStrategy.INSTANCE );
			}
			catch( NumberFormatException nfe ) {
				// the Integer#parseInt call failed...
			}
		}
	}

	protected int doDelete(Session session, String queryString) {
		return doDelete( session, session.createQuery( queryString ) );
	}

	protected int doDelete(Session session, String queryString, Object param, Type paramType) {
		Query query = session.createQuery( queryString )
				.setParameter( 0, param, paramType );
		return doDelete( session, query );
	}

	protected int doDelete(Session session, Query selectQuery) {
		int count = 0;
		for ( Object o : selectQuery.list() ) {
			session.delete( o );
			count++;
		}
		return count;
	}
}
