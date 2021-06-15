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
package org.hibernate.loader.criteria;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.QueryException;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.persister.entity.Loadable;
import org.hibernate.persister.entity.Lockable;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

/**
 * A <tt>Loader</tt> for <tt>Criteria</tt> queries. Note that criteria queries are
 * more like multi-object <tt>load()</tt>s than like HQL queries.
 *
 * @author Gavin King
 */
public class CriteriaLoader extends OuterJoinLoader {

	//TODO: this class depends directly upon CriteriaImpl, 
	//      in the impl package ... add a CriteriaImplementor 
	//      interface

	//NOTE: unlike all other Loaders, this one is NOT
	//      multithreaded, or cacheable!!

	private final CriteriaQueryTranslator translator;
	private final Set querySpaces;
	private final Type[] resultTypes;
	//the user visible aliases, which are unknown to the superclass,
	//these are not the actual "physical" SQL aliases
	private final String[] userAliases;
	private final boolean[] includeInResultRow;
	private final int resultRowLength;

	public CriteriaLoader(
			final OuterJoinLoadable persister, 
			final SessionFactoryImplementor factory, 
			final CriteriaImpl criteria, 
			final String rootEntityName,
			final LoadQueryInfluencers loadQueryInfluencers) throws HibernateException {
		super( factory, loadQueryInfluencers );

		translator = new CriteriaQueryTranslator(
				factory, 
				criteria, 
				rootEntityName, 
				CriteriaQueryTranslator.ROOT_SQL_ALIAS
			);

		querySpaces = translator.getQuerySpaces();
		
		CriteriaJoinWalker walker = new CriteriaJoinWalker(
				persister, 
				translator,
				factory, 
				criteria, 
				rootEntityName, 
				loadQueryInfluencers
			);

		initFromWalker(walker);
		
		userAliases = walker.getUserAliases();
		resultTypes = walker.getResultTypes();
		includeInResultRow = walker.includeInResultRow();
		resultRowLength = ArrayHelper.countTrue( includeInResultRow );

		postInstantiate();

	}
	
	public ScrollableResults scroll(SessionImplementor session, ScrollMode scrollMode) 
	throws HibernateException {
		QueryParameters qp = translator.getQueryParameters();
		qp.setScrollMode(scrollMode);
		return scroll(qp, resultTypes, null, session);
	}

	public List list(SessionImplementor session) 
	throws HibernateException {
		return list( session, translator.getQueryParameters(), querySpaces, resultTypes );

	}

	protected String[] getResultRowAliases() {
		return userAliases;
	}

	protected ResultTransformer resolveResultTransformer(ResultTransformer resultTransformer) {
		return translator.getRootCriteria().getResultTransformer();
	}

	protected boolean areResultSetRowsTransformedImmediately() {
		return true;
	}

	protected boolean[] includeInResultRow() {
		return includeInResultRow;
	}

	protected Object getResultColumnOrRow(Object[] row, ResultTransformer transformer, ResultSet rs, SessionImplementor session)
	throws SQLException, HibernateException {
		return resolveResultTransformer( transformer ).transformTuple(
				getResultRow( row, rs, session),
				getResultRowAliases()
		);
	}
			
	protected Object[] getResultRow(Object[] row, ResultSet rs, SessionImplementor session)
			throws SQLException, HibernateException {
		final Object[] result;
		if ( translator.hasProjection() ) {
			Type[] types = translator.getProjectedTypes();
			result = new Object[types.length];
			String[] columnAliases = translator.getProjectedColumnAliases();
			for ( int i=0, pos=0; i<result.length; i++ ) {
				int numColumns = types[i].getColumnSpan( session.getFactory() );
				if ( numColumns > 1 ) {
			    	String[] typeColumnAliases = ArrayHelper.slice( columnAliases, pos, numColumns );
					result[i] = types[i].nullSafeGet(rs, typeColumnAliases, session, null);
				}
				else {
					result[i] = types[i].nullSafeGet(rs, columnAliases[pos], session, null);
				}
				pos += numColumns;
			}
		}
		else {
			result = toResultRow( row );
		}
		return result;
	}

	private Object[] toResultRow(Object[] row) {
		if ( resultRowLength == row.length ) {
			return row;
		}
		else {
			Object[] result = new Object[ resultRowLength ];
			int j = 0;
			for ( int i = 0; i < row.length; i++ ) {
				if ( includeInResultRow[i] ) result[j++] = row[i];
			}
			return result;
		}
	}

	public Set getQuerySpaces() {
		return querySpaces;
	}

	@Override
	protected String applyLocks(
			String sql,
			QueryParameters parameters,
			Dialect dialect,
			List<AfterLoadAction> afterLoadActions) throws QueryException {
		final LockOptions lockOptions = parameters.getLockOptions();
		if ( lockOptions == null ||
			( lockOptions.getLockMode() == LockMode.NONE && lockOptions.getAliasLockCount() == 0 ) ) {
			return sql;
		}

		if ( dialect.useFollowOnLocking() ) {
			// Dialect prefers to perform locking in a separate step
			LOG.usingFollowOnLocking();

			final LockMode lockMode = determineFollowOnLockMode( lockOptions );
			final LockOptions lockOptionsToUse = new LockOptions( lockMode );
			lockOptionsToUse.setTimeOut( lockOptions.getTimeOut() );
			lockOptionsToUse.setScope( lockOptions.getScope() );

			afterLoadActions.add(
					new AfterLoadAction() {
						@Override
						public void afterLoad(SessionImplementor session, Object entity, Loadable persister) {
							( (Session) session ).buildLockRequest( lockOptionsToUse )
									.lock( persister.getEntityName(), entity );
						}
					}
			);
			parameters.setLockOptions( new LockOptions() );
			return sql;
		}

		final LockOptions locks = new LockOptions(lockOptions.getLockMode());
		locks.setScope( lockOptions.getScope());
		locks.setTimeOut( lockOptions.getTimeOut());

		final Map keyColumnNames = dialect.forUpdateOfColumns() ? new HashMap() : null;
		final String[] drivingSqlAliases = getAliases();
		for ( int i = 0; i < drivingSqlAliases.length; i++ ) {
			final LockMode lockMode = lockOptions.getAliasSpecificLockMode( drivingSqlAliases[i] );
			if ( lockMode != null ) {
				final Lockable drivingPersister = ( Lockable ) getEntityPersisters()[i];
				final String rootSqlAlias = drivingPersister.getRootTableAlias( drivingSqlAliases[i] );
				locks.setAliasSpecificLockMode( rootSqlAlias, lockMode );
				if ( keyColumnNames != null ) {
					keyColumnNames.put( rootSqlAlias, drivingPersister.getRootTableIdentifierColumnNames() );
				}
			}
		}
		return dialect.applyLocksToSql( sql, locks, keyColumnNames );
	}



	protected LockMode determineFollowOnLockMode(LockOptions lockOptions) {
		final LockMode lockModeToUse = lockOptions.findGreatestLockMode();

		if ( lockOptions.getAliasLockCount() > 1 ) {
			// > 1 here because criteria always uses alias map for the root lock mode (under 'this_')
			LOG.aliasSpecificLockingWithFollowOnLocking( lockModeToUse );
		}

		return lockModeToUse;
	}

	protected LockMode[] getLockModes(LockOptions lockOptions) {
		final String[] entityAliases = getAliases();
		if ( entityAliases == null ) {
			return null;
		}
		final int size = entityAliases.length;
		LockMode[] lockModesArray = new LockMode[size];
		for ( int i=0; i<size; i++ ) {
			LockMode lockMode = lockOptions.getAliasSpecificLockMode( entityAliases[i] );
			lockModesArray[i] = lockMode==null ? lockOptions.getLockMode() : lockMode;
		}
		return lockModesArray;
	}

	protected boolean isSubselectLoadingEnabled() {
		return hasSubselectLoadableCollections();
	}

	protected List getResultList(List results, ResultTransformer resultTransformer) {
		return resolveResultTransformer( resultTransformer ).transformList( results );
	}

}
