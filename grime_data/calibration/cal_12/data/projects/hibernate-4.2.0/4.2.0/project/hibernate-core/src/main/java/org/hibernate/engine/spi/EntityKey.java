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
package org.hibernate.engine.spi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.hibernate.AssertionFailure;
import org.hibernate.internal.util.compare.EqualsHelper;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.pretty.MessageHelper;
import org.hibernate.type.Type;

/**
 * Uniquely identifies of an entity instance in a particular session by identifier.
 * <p/>
 * Information used to determine uniqueness consists of the entity-name and the identifier value (see {@link #equals}).
 *
 * @author Gavin King
 */
public final class EntityKey implements Serializable {
	private final Serializable identifier;
	private final String entityName;
	private final String rootEntityName;
	private final String tenantId;

	private final int hashCode;

	private final Type identifierType;
	private final boolean isBatchLoadable;
	private final SessionFactoryImplementor factory;

	/**
	 * Construct a unique identifier for an entity class instance.
	 * <p>
	 * NOTE : This signature has changed to accommodate both entity mode and multi-tenancy, both of which relate to
	 * the Session to which this key belongs.  To help minimize the impact of these changes in the future, the
	 * {@link SessionImplementor#generateEntityKey} method was added to hide the session-specific changes.
	 *
	 * @param id The entity id
	 * @param persister The entity persister
	 * @param tenantId The tenant identifier of the session to which this key belongs
	 */
	public EntityKey(Serializable id, EntityPersister persister, String tenantId) {
		if ( id == null ) {
			throw new AssertionFailure( "null identifier" );
		}
		this.identifier = id; 
		this.rootEntityName = persister.getRootEntityName();
		this.entityName = persister.getEntityName();
		this.tenantId = tenantId;

		this.identifierType = persister.getIdentifierType();
		this.isBatchLoadable = persister.isBatchLoadable();
		this.factory = persister.getFactory();
		this.hashCode = generateHashCode();
	}

	/**
	 * Used to reconstruct an EntityKey during deserialization.
	 *
	 * @param identifier The identifier value
	 * @param rootEntityName The root entity name
	 * @param entityName The specific entity name
	 * @param identifierType The type of the identifier value
	 * @param batchLoadable Whether represented entity is eligible for batch loading
	 * @param factory The session factory
	 * @param tenantId The entity's tenant id (from the session that loaded it).
	 */
	private EntityKey(
			Serializable identifier,
	        String rootEntityName,
	        String entityName,
	        Type identifierType,
	        boolean batchLoadable,
	        SessionFactoryImplementor factory,
			String tenantId) {
		this.identifier = identifier;
		this.rootEntityName = rootEntityName;
		this.entityName = entityName;
		this.identifierType = identifierType;
		this.isBatchLoadable = batchLoadable;
		this.factory = factory;
		this.tenantId = tenantId;
		this.hashCode = generateHashCode();
	}

	private int generateHashCode() {
		int result = 17;
		result = 37 * result + rootEntityName.hashCode();
		result = 37 * result + identifierType.getHashCode( identifier, factory );
		return result;
	}

	public boolean isBatchLoadable() {
		return isBatchLoadable;
	}

	public Serializable getIdentifier() {
		return identifier;
	}

	public String getEntityName() {
		return entityName;
	}

	@Override
	public boolean equals(Object other) {
		EntityKey otherKey = (EntityKey) other;
		return otherKey.rootEntityName.equals(this.rootEntityName) &&
				identifierType.isEqual(otherKey.identifier, this.identifier, factory) &&
				EqualsHelper.equals( tenantId, otherKey.tenantId );
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return "EntityKey" + 
			MessageHelper.infoString( factory.getEntityPersister( entityName ), identifier, factory );
	}

	/**
	 * Custom serialization routine used during serialization of a
	 * Session/PersistenceContext for increased performance.
	 *
	 * @param oos The stream to which we should write the serial data.
	 *
	 * @throws IOException Thrown by Java I/O
	 */
	public void serialize(ObjectOutputStream oos) throws IOException {
		oos.writeObject( identifier );
		oos.writeObject( rootEntityName );
		oos.writeObject( entityName );
		oos.writeObject( identifierType );
		oos.writeBoolean( isBatchLoadable );
		oos.writeObject( tenantId );
	}

	/**
	 * Custom deserialization routine used during deserialization of a
	 * Session/PersistenceContext for increased performance.
	 *
	 * @param ois The stream from which to read the entry.
	 * @param session The session being deserialized.
	 *
	 * @return The deserialized EntityEntry
	 *
	 * @throws IOException Thrown by Java I/O
	 * @throws ClassNotFoundException Thrown by Java I/O
	 */
	public static EntityKey deserialize(
			ObjectInputStream ois,
	        SessionImplementor session) throws IOException, ClassNotFoundException {
		return new EntityKey(
				( Serializable ) ois.readObject(),
		        (String) ois.readObject(),
				(String) ois.readObject(),
		        ( Type ) ois.readObject(),
		        ois.readBoolean(),
		        ( session == null ? null : session.getFactory() ),
				(String) ois.readObject()
		);
	}
}
