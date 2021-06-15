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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import org.dom4j.Node;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.metamodel.relational.Size;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.LoggableUserType;
import org.hibernate.usertype.Sized;
import org.hibernate.usertype.UserType;
import org.hibernate.usertype.UserVersionType;

/**
 * Adapts {@link UserType} to the generic {@link Type} interface, in order
 * to isolate user code from changes in the internal Type contracts.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class CustomType
		extends AbstractType
		implements IdentifierType, DiscriminatorType, VersionType, BasicType, StringRepresentableType {

	private final UserType userType;
	private final String name;
	private final int[] types;
	private final Size[] dictatedSizes;
	private final Size[] defaultSizes;
	private final boolean customLogging;
	private final String[] registrationKeys;

	public CustomType(UserType userType) throws MappingException {
		this( userType, ArrayHelper.EMPTY_STRING_ARRAY );
	}

	public CustomType(UserType userType, String[] registrationKeys) throws MappingException {
		this.userType = userType;
		this.name = userType.getClass().getName();
		this.types = userType.sqlTypes();
		this.dictatedSizes = Sized.class.isInstance( userType )
				? ( (Sized) userType ).dictatedSizes()
				: new Size[ types.length ];
		this.defaultSizes = Sized.class.isInstance( userType )
				? ( (Sized) userType ).defaultSizes()
				: new Size[ types.length ];
		this.customLogging = LoggableUserType.class.isInstance( userType );
		this.registrationKeys = registrationKeys;
	}

	public UserType getUserType() {
		return userType;
	}

	public String[] getRegistrationKeys() {
		return registrationKeys;
	}

	public int[] sqlTypes(Mapping pi) {
		return types;
	}

	@Override
	public Size[] dictatedSizes(Mapping mapping) throws MappingException {
		return dictatedSizes;
	}

	@Override
	public Size[] defaultSizes(Mapping mapping) throws MappingException {
		return defaultSizes;
	}

	public int getColumnSpan(Mapping session) {
		return types.length;
	}

	public Class getReturnedClass() {
		return userType.returnedClass();
	}

	public boolean isEqual(Object x, Object y) throws HibernateException {
		return userType.equals( x, y );
	}

	public int getHashCode(Object x) {
		return userType.hashCode(x);
	}

	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		return userType.nullSafeGet(rs, names, session, owner);
	}

	public Object nullSafeGet(ResultSet rs, String columnName, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		return nullSafeGet(rs, new String[] { columnName }, session, owner);
	}


	public Object assemble(Serializable cached, SessionImplementor session, Object owner)
			throws HibernateException {
		return userType.assemble(cached, owner);
	}

	public Serializable disassemble(Object value, SessionImplementor session, Object owner)
			throws HibernateException {
		return userType.disassemble(value);
	}

	public Object replace(
			Object original,
			Object target,
			SessionImplementor session,
			Object owner,
			Map copyCache) throws HibernateException {
		return userType.replace( original, target, owner );
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, boolean[] settable, SessionImplementor session)
			throws HibernateException, SQLException {
		if ( settable[0] ) {
			userType.nullSafeSet( st, value, index, session );
		}
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		userType.nullSafeSet( st, value, index, session );
	}

	@SuppressWarnings({ "UnusedDeclaration" })
	public String toXMLString(Object value, SessionFactoryImplementor factory) {
		return toString( value );
	}

	@SuppressWarnings({ "UnusedDeclaration" })
	public Object fromXMLString(String xml, Mapping factory) {
		return fromStringValue( xml );
	}

	public String getName() {
		return name;
	}

	public Object deepCopy(Object value, SessionFactoryImplementor factory)
			throws HibernateException {
		return userType.deepCopy(value);
	}

	public boolean isMutable() {
		return userType.isMutable();
	}

	public Object stringToObject(String xml) {
		return fromStringValue( xml );
	}

	public String objectToSQLString(Object value, Dialect dialect) throws Exception {
		return ( (EnhancedUserType) userType ).objectToSQLString(value);
	}

	public Comparator getComparator() {
		return (Comparator) userType;
	}

	public Object next(Object current, SessionImplementor session) {
		return ( (UserVersionType) userType ).next( current, session );
	}

	public Object seed(SessionImplementor session) {
		return ( (UserVersionType) userType ).seed( session );
	}

	public Object fromXMLNode(Node xml, Mapping factory) throws HibernateException {
		return fromXMLString( xml.getText(), factory );
	}

	public void setToXMLNode(Node node, Object value, SessionFactoryImplementor factory)
			throws HibernateException {
		node.setText( toXMLString(value, factory) );
	}

	public String toLoggableString(Object value, SessionFactoryImplementor factory)
			throws HibernateException {
		if ( value == null ) {
			return "null";
		}
		else if ( customLogging ) {
			return ( ( LoggableUserType ) userType ).toLoggableString( value, factory );
		}
		else {
			return toXMLString( value, factory );
		}
	}

	public boolean[] toColumnNullness(Object value, Mapping mapping) {
		boolean[] result = new boolean[ getColumnSpan(mapping) ];
		if ( value != null ) {
			Arrays.fill(result, true);
		}
		return result;
	}

	public boolean isDirty(Object old, Object current, boolean[] checkable, SessionImplementor session)
			throws HibernateException {
		return checkable[0] && isDirty(old, current, session);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String toString(Object value) throws HibernateException {
		if ( StringRepresentableType.class.isInstance( userType ) ) {
			return ( (StringRepresentableType) userType ).toString( value );
		}
		if ( value == null ) {
			return null;
		}
		if ( EnhancedUserType.class.isInstance( userType ) ) {
			//noinspection deprecation
			return ( (EnhancedUserType) userType ).toXMLString( value );
		}
		return value.toString();
	}

	@Override
	public Object fromStringValue(String string) throws HibernateException {
		if ( StringRepresentableType.class.isInstance( userType ) ) {
			return ( (StringRepresentableType) userType ).fromStringValue( string );
		}
		if ( EnhancedUserType.class.isInstance( userType ) ) {
			//noinspection deprecation
			return ( (EnhancedUserType) userType ).fromXMLString( string );
		}
		throw new HibernateException(
				String.format(
						"Could not process #fromStringValue, UserType class [%s] did not implement %s or %s",
						name,
						StringRepresentableType.class.getName(),
						EnhancedUserType.class.getName()
				)
		);
	}
}
