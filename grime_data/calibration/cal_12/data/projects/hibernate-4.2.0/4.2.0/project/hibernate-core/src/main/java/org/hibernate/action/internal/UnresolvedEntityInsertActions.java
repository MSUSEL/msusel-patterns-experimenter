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
package org.hibernate.action.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.logging.Logger;

import org.hibernate.PropertyValueException;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.engine.internal.NonNullableTransientDependencies;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.Status;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.collections.IdentitySet;
import org.hibernate.pretty.MessageHelper;

/**
 * Tracks unresolved entity insert actions.
 *
 * An entity insert action is unresolved if the entity
 * to be inserted has at least one non-nullable association with
 * an unsaved transient entity, and the foreign key points to that
 * unsaved transient entity.
 *
 * These references must be resolved before an insert action can be
 * executed.
 *
 * @author Gail Badner
 */
public class UnresolvedEntityInsertActions {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
				CoreMessageLogger.class,
				UnresolvedEntityInsertActions.class.getName()
		);
	private static final int INIT_SIZE = 5;

	private final Map<AbstractEntityInsertAction,NonNullableTransientDependencies> dependenciesByAction =
			new IdentityHashMap<AbstractEntityInsertAction,NonNullableTransientDependencies>( INIT_SIZE );
	private final Map<Object,Set<AbstractEntityInsertAction>> dependentActionsByTransientEntity =
			new IdentityHashMap<Object,Set<AbstractEntityInsertAction>>( INIT_SIZE );

	/**
	 * Add an unresolved insert action.
	 *
	 * @param insert - unresolved insert action.
	 * @param dependencies - non-nullable transient dependencies
	 *                       (must be non-null and non-empty).
	 *
	 * @throws IllegalArgumentException if {@code dependencies is null or empty}.
	 */
	public void addUnresolvedEntityInsertAction(AbstractEntityInsertAction insert, NonNullableTransientDependencies dependencies) {
		if ( dependencies == null || dependencies.isEmpty() ) {
			throw new IllegalArgumentException(
					"Attempt to add an unresolved insert action that has no non-nullable transient entities."
			);
		}
		if ( LOG.isTraceEnabled() ) {
			LOG.tracev(
					"Adding insert with non-nullable, transient entities; insert=[{0}], dependencies=[{1}]",
					insert,
					dependencies.toLoggableString( insert.getSession() )
			);
		}
		dependenciesByAction.put( insert, dependencies );
		addDependenciesByTransientEntity( insert, dependencies );
	}

	/**
	 * Returns the unresolved insert actions.
	 * @return the unresolved insert actions.
	 */
	public Iterable<AbstractEntityInsertAction> getDependentEntityInsertActions() {
		return dependenciesByAction.keySet();
	}

	/**
	 * Throws {@link org.hibernate.PropertyValueException} if there are any unresolved
	 * entity insert actions that depend on non-nullable associations with
	 * a transient entity. This method should be called on completion of
	 * an operation (after all cascades are completed) that saves an entity.
	 *
	 * @throws org.hibernate.PropertyValueException if there are any unresolved entity
	 * insert actions; {@link org.hibernate.PropertyValueException#getEntityName()}
	 * and {@link org.hibernate.PropertyValueException#getPropertyName()} will
	 * return the entity name and property value for the first unresolved
	 * entity insert action.
	 */
	public void checkNoUnresolvedActionsAfterOperation() throws PropertyValueException {
		if ( isEmpty() ) {
			LOG.trace( "No entity insert actions have non-nullable, transient entity dependencies." );
		}
		else {
			AbstractEntityInsertAction firstDependentAction =
					dependenciesByAction.keySet().iterator().next();

			logCannotResolveNonNullableTransientDependencies( firstDependentAction.getSession() );

			NonNullableTransientDependencies nonNullableTransientDependencies =
					dependenciesByAction.get( firstDependentAction );
			Object firstTransientDependency =
					nonNullableTransientDependencies.getNonNullableTransientEntities().iterator().next();
			String firstPropertyPath =
					nonNullableTransientDependencies.getNonNullableTransientPropertyPaths( firstTransientDependency ).iterator().next();
			throw new TransientPropertyValueException(
					"Not-null property references a transient value - transient instance must be saved before current operation",
					firstDependentAction.getSession().guessEntityName( firstTransientDependency ),
					firstDependentAction.getEntityName(),
					firstPropertyPath
			);
		}
	}

	private void logCannotResolveNonNullableTransientDependencies(SessionImplementor session) {
		for ( Map.Entry<Object,Set<AbstractEntityInsertAction>> entry : dependentActionsByTransientEntity.entrySet() ) {
			Object transientEntity = entry.getKey();
			String transientEntityName = session.guessEntityName( transientEntity );
			Serializable transientEntityId = session.getFactory().getEntityPersister( transientEntityName ).getIdentifier( transientEntity, session );
			String transientEntityString = MessageHelper.infoString( transientEntityName, transientEntityId );
			Set<String> dependentEntityStrings = new TreeSet<String>();
			Set<String> nonNullableTransientPropertyPaths = new TreeSet<String>();
			for ( AbstractEntityInsertAction dependentAction : entry.getValue() ) {
				dependentEntityStrings.add( MessageHelper.infoString( dependentAction.getEntityName(), dependentAction.getId() ) );
				for ( String path : dependenciesByAction.get( dependentAction ).getNonNullableTransientPropertyPaths( transientEntity ) ) {
					String fullPath = new StringBuilder( dependentAction.getEntityName().length() + path.length() + 1 )
							.append( dependentAction.getEntityName() )
							.append( '.' )
							.append( path )
							.toString();
					nonNullableTransientPropertyPaths.add( fullPath );
				}
			}
			LOG.cannotResolveNonNullableTransientDependencies(
					transientEntityString,
					dependentEntityStrings,
					nonNullableTransientPropertyPaths
			);
		}
	}

	/**
	 * Returns true if there are no unresolved entity insert actions.
	 * @return true, if there are no unresolved entity insert actions; false, otherwise.
	 */
	public boolean isEmpty() {
		return dependenciesByAction.isEmpty();
	}

	@SuppressWarnings({ "unchecked" })
	private void addDependenciesByTransientEntity(AbstractEntityInsertAction insert, NonNullableTransientDependencies dependencies) {
		for ( Object transientEntity : dependencies.getNonNullableTransientEntities() ) {
			Set<AbstractEntityInsertAction> dependentActions = dependentActionsByTransientEntity.get( transientEntity );
			if ( dependentActions == null ) {
				dependentActions = new IdentitySet();
				dependentActionsByTransientEntity.put( transientEntity, dependentActions );
			}
			dependentActions.add( insert );
		}
	}

	/**
	 * Resolve any dependencies on {@code managedEntity}.
	 *
	 * @param managedEntity - the managed entity name
	 * @param session - the session
	 *
	 * @return the insert actions that depended only on the specified entity.
	 *
	 * @throws IllegalArgumentException if {@code managedEntity} did not have managed or read-only status.
	 */
	@SuppressWarnings({ "unchecked" })
	public Set<AbstractEntityInsertAction> resolveDependentActions(Object managedEntity, SessionImplementor session) {
		EntityEntry entityEntry = session.getPersistenceContext().getEntry( managedEntity );
		if ( entityEntry.getStatus() != Status.MANAGED && entityEntry.getStatus() != Status.READ_ONLY ) {
			throw new IllegalArgumentException( "EntityEntry did not have status MANAGED or READ_ONLY: " + entityEntry );
		}
		// Find out if there are any unresolved insertions that are waiting for the
		// specified entity to be resolved.
		Set<AbstractEntityInsertAction> dependentActions = dependentActionsByTransientEntity.remove( managedEntity );
		if ( dependentActions == null ) {
			if ( LOG.isTraceEnabled() ) {
				LOG.tracev(
						"No unresolved entity inserts that depended on [{0}]",
						MessageHelper.infoString( entityEntry.getEntityName(), entityEntry.getId() )
				);
			}
			return Collections.emptySet();  //NOTE EARLY EXIT!
		}
		Set<AbstractEntityInsertAction> resolvedActions = new IdentitySet(  );
		if ( LOG.isTraceEnabled()  ) {
			LOG.tracev(
					"Unresolved inserts before resolving [{0}]: [{1}]",
					MessageHelper.infoString( entityEntry.getEntityName(), entityEntry.getId() ),
					toString()
			);
		}
		for ( AbstractEntityInsertAction dependentAction : dependentActions ) {
			if ( LOG.isTraceEnabled() ) {
				LOG.tracev(
						"Resolving insert [{0}] dependency on [{1}]",
						MessageHelper.infoString( dependentAction.getEntityName(), dependentAction.getId() ),
						MessageHelper.infoString( entityEntry.getEntityName(), entityEntry.getId() )
				);
			}
			NonNullableTransientDependencies dependencies = dependenciesByAction.get( dependentAction );
			dependencies.resolveNonNullableTransientEntity( managedEntity );
			if ( dependencies.isEmpty() ) {
				if ( LOG.isTraceEnabled() ) {
					LOG.tracev(
							"Resolving insert [{0}] (only depended on [{1}])",
							dependentAction,
							MessageHelper.infoString( entityEntry.getEntityName(), entityEntry.getId() )
					);
				}
				// dependentAction only depended on managedEntity..
				dependenciesByAction.remove( dependentAction );
				resolvedActions.add( dependentAction );
			}
		}
		if ( LOG.isTraceEnabled()  ) {
			LOG.tracev(
					"Unresolved inserts after resolving [{0}]: [{1}]",
					MessageHelper.infoString( entityEntry.getEntityName(), entityEntry.getId() ),
					toString()
			);
		}
		return resolvedActions;
	}

	/**
	 * Clear this {@link UnresolvedEntityInsertActions}.
	 */
	public void clear() {
		dependenciesByAction.clear();
		dependentActionsByTransientEntity.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( getClass().getSimpleName() )
				.append( '[' );
		for ( Map.Entry<AbstractEntityInsertAction,NonNullableTransientDependencies> entry : dependenciesByAction.entrySet() ) {
			AbstractEntityInsertAction insert = entry.getKey();
			NonNullableTransientDependencies dependencies = entry.getValue();
			sb.append( "[insert=" )
					.append( insert )
					.append( " dependencies=[" )
					.append( dependencies.toLoggableString( insert.getSession() ) )
					.append( "]" );
		}
		sb.append( ']');
		return sb.toString();
	}

	/**
	 * Serialize this {@link UnresolvedEntityInsertActions} object.
	 * @param oos - the output stream
	 * @throws IOException if there is an error writing this object to the output stream.
	 */
	public void serialize(ObjectOutputStream oos) throws IOException {
		int queueSize = dependenciesByAction.size();
		LOG.tracev( "Starting serialization of [{0}] unresolved insert entries", queueSize );
		oos.writeInt( queueSize );
		for ( AbstractEntityInsertAction unresolvedAction : dependenciesByAction.keySet() ) {
			oos.writeObject( unresolvedAction );
		}
	}

	/**
	 * Deerialize a {@link UnresolvedEntityInsertActions} object.
	 *
	 * @param ois - the input stream.
	 * @param session - the session.
	 *
	 * @return the deserialized  {@link UnresolvedEntityInsertActions} object
	 * @throws IOException if there is an error writing this object to the output stream.
	 * @throws ClassNotFoundException if there is a class that cannot be loaded.
	 */
	public static UnresolvedEntityInsertActions deserialize(
			ObjectInputStream ois,
			SessionImplementor session) throws IOException, ClassNotFoundException {

		UnresolvedEntityInsertActions rtn = new UnresolvedEntityInsertActions();

		int queueSize = ois.readInt();
		LOG.tracev( "Starting deserialization of [{0}] unresolved insert entries", queueSize );
		for ( int i = 0; i < queueSize; i++ ) {
			AbstractEntityInsertAction unresolvedAction = ( AbstractEntityInsertAction ) ois.readObject();
			unresolvedAction.afterDeserialize( session );
			rtn.addUnresolvedEntityInsertAction(
					unresolvedAction,
					unresolvedAction.findNonNullableTransientEntities()
			);
		}
		return rtn;
	}
}
