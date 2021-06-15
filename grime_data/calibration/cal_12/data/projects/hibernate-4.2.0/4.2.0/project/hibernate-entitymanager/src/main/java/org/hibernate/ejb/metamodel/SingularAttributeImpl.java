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
package org.hibernate.ejb.metamodel;
import java.io.Serializable;
import java.lang.reflect.Member;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

/**
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 */
public class SingularAttributeImpl<X, Y>
		extends AbstractAttribute<X,Y>
		implements SingularAttribute<X, Y>, Serializable {
	private final boolean isIdentifier;
	private final boolean isVersion;
	private final boolean isOptional;
	private final Type<Y> attributeType;

	public SingularAttributeImpl(
			String name,
			Class<Y> javaType,
			AbstractManagedType<X> declaringType,
			Member member,
			boolean isIdentifier,
			boolean isVersion,
			boolean isOptional,
			Type<Y> attributeType,
			PersistentAttributeType persistentAttributeType) {
		super( name, javaType, declaringType, member, persistentAttributeType );
		this.isIdentifier = isIdentifier;
		this.isVersion = isVersion;
		this.isOptional = isOptional;
		this.attributeType = attributeType;
	}

	/**
	 * Subclass used to simply instantiation of singular attributes representing an entity's
	 * identifier.
	 */
	public static class Identifier<X,Y> extends SingularAttributeImpl<X,Y> {
		public Identifier(
				String name,
				Class<Y> javaType,
				AbstractManagedType<X> declaringType,
				Member member,
				Type<Y> attributeType,
				PersistentAttributeType persistentAttributeType) {
			super( name, javaType, declaringType, member, true, false, false, attributeType, persistentAttributeType );
		}
	}

	/**
	 * Subclass used to simply instantiation of singular attributes representing an entity's
	 * version.
	 */
	public static class Version<X,Y> extends SingularAttributeImpl<X,Y> {
		public Version(
				String name,
				Class<Y> javaType,
				AbstractManagedType<X> declaringType,
				Member member,
				Type<Y> attributeType,
				PersistentAttributeType persistentAttributeType) {
			super( name, javaType, declaringType, member, false, true, false, attributeType, persistentAttributeType );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isId() {
		return isIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isVersion() {
		return isVersion;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOptional() {
		return isOptional;
	}

	/**
	 * {@inheritDoc}
	 */
	public Type<Y> getType() {
		return attributeType;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAssociation() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isCollection() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public BindableType getBindableType() {
		return BindableType.SINGULAR_ATTRIBUTE;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<Y> getBindableJavaType() {
		return attributeType.getJavaType();
	}
}
