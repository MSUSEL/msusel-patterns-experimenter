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
package org.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.metamodel.relational.Size;
import org.hibernate.persister.entity.EntityPersister;

/**
 * A one-to-one association to an entity
 * @author Gavin King
 */
public class OneToOneType extends EntityType {

	private final ForeignKeyDirection foreignKeyType;
	private final String propertyName;
	private final String entityName;

	/**
	 * @deprecated Use {@link #OneToOneType(TypeFactory.TypeScope, String, ForeignKeyDirection, String, boolean, boolean, String, String)}
	 *  instead.
	 * See Jira issue: <a href="https://hibernate.onjira.com/browse/HHH-7771">HHH-7771</a>
	 */
	@Deprecated
	public OneToOneType(
			TypeFactory.TypeScope scope,
			String referencedEntityName,
			ForeignKeyDirection foreignKeyType,
			String uniqueKeyPropertyName,
			boolean lazy,
			boolean unwrapProxy,
			boolean isEmbeddedInXML,
			String entityName,
			String propertyName) {
		super( scope, referencedEntityName, uniqueKeyPropertyName, !lazy, isEmbeddedInXML, unwrapProxy );
		this.foreignKeyType = foreignKeyType;
		this.propertyName = propertyName;
		this.entityName = entityName;
	}

	public OneToOneType(
			TypeFactory.TypeScope scope,
			String referencedEntityName,
			ForeignKeyDirection foreignKeyType,
			String uniqueKeyPropertyName,
			boolean lazy,
			boolean unwrapProxy,
			String entityName,
			String propertyName) {
		super( scope, referencedEntityName, uniqueKeyPropertyName, !lazy, unwrapProxy );
		this.foreignKeyType = foreignKeyType;
		this.propertyName = propertyName;
		this.entityName = entityName;
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	public boolean isNull(Object owner, SessionImplementor session) {
		if ( propertyName != null ) {
			final EntityPersister ownerPersister = session.getFactory().getEntityPersister( entityName );
			final Serializable id = session.getContextEntityIdentifier( owner );
			final EntityKey entityKey = session.generateEntityKey( id, ownerPersister );
			return session.getPersistenceContext().isPropertyNull( entityKey, getPropertyName() );
		}
		else {
			return false;
		}
	}

	public int getColumnSpan(Mapping session) throws MappingException {
		return 0;
	}

	public int[] sqlTypes(Mapping session) throws MappingException {
		return ArrayHelper.EMPTY_INT_ARRAY;
	}

	private static final Size[] SIZES = new Size[0];

	@Override
	public Size[] dictatedSizes(Mapping mapping) throws MappingException {
		return SIZES;
	}

	@Override
	public Size[] defaultSizes(Mapping mapping) throws MappingException {
		return SIZES;
	}

	public boolean[] toColumnNullness(Object value, Mapping mapping) {
		return ArrayHelper.EMPTY_BOOLEAN_ARRAY;
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, boolean[] settable, SessionImplementor session) {
		//nothing to do
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) {
		//nothing to do
	}

	public boolean isOneToOne() {
		return true;
	}

	public boolean isDirty(Object old, Object current, SessionImplementor session) {
		return false;
	}

	public boolean isDirty(Object old, Object current, boolean[] checkable, SessionImplementor session) {
		return false;
	}

	public boolean isModified(Object old, Object current, boolean[] checkable, SessionImplementor session) {
		return false;
	}

	public ForeignKeyDirection getForeignKeyDirection() {
		return foreignKeyType;
	}

	public Object hydrate(
		ResultSet rs,
		String[] names,
		SessionImplementor session,
		Object owner)
	throws HibernateException, SQLException {

		return session.getContextEntityIdentifier(owner);
	}

	protected boolean isNullable() {
		return foreignKeyType==ForeignKeyDirection.FOREIGN_KEY_TO_PARENT;
	}

	public boolean useLHSPrimaryKey() {
		return true;
	}

	public Serializable disassemble(Object value, SessionImplementor session, Object owner)
	throws HibernateException {
		return null;
	}

	public Object assemble(Serializable oid, SessionImplementor session, Object owner)
	throws HibernateException {
		//this should be a call to resolve(), not resolveIdentifier(), 
		//'cos it might be a property-ref, and we did not cache the
		//referenced value
		return resolve( session.getContextEntityIdentifier(owner), session, owner );
	}
	
	/**
	 * We don't need to dirty check one-to-one because of how 
	 * assemble/disassemble is implemented and because a one-to-one 
	 * association is never dirty
	 */
	public boolean isAlwaysDirtyChecked() {
		//TODO: this is kinda inconsistent with CollectionType
		return false; 
	}
	
}

