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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

/**
 * Defines commonality for the JPA {@link IdentifiableType} types.  JPA defines
 * identifiable types as entities or mapped-superclasses.  Basically things to which an
 * identifier can be attached.
 * <p/>
 * NOTE : Currently we only really have support for direct entities in the Hibernate metamodel
 * as the information for them is consumed into the closest actual entity subclass(es) in the
 * internal Hibernate mapping-metamodel.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractIdentifiableType<X>
		extends AbstractManagedType<X>
		implements IdentifiableType<X>, Serializable {

	private final boolean hasIdentifierProperty;
	private final boolean isVersioned;

	private SingularAttributeImpl<X, ?> id;
	private SingularAttributeImpl<X, ?> version;
	private Set<SingularAttribute<? super X,?>> idClassAttributes;

	public AbstractIdentifiableType(
			Class<X> javaType,
			AbstractIdentifiableType<? super X> superType,
			boolean hasIdentifierProperty,
			boolean versioned) {
		super( javaType, superType );
		this.hasIdentifierProperty = hasIdentifierProperty;
		isVersioned = versioned;
	}

	/**
	 * {@inheritDoc}
	 */
	public AbstractIdentifiableType<? super X> getSupertype() {
		return ( AbstractIdentifiableType<? super X> ) super.getSupertype();
	}

	/**
	 * Indicates if a non-null super type is required to provide the
	 * identifier attribute(s) if this object does not have a declared
	 * identifier.
	 * .
	 * @return true, if a non-null super type is required to provide
	 * the identifier attribute(s) if this object does not have a
	 * declared identifier; false, otherwise.
	 */
	protected abstract boolean requiresSupertypeForNonDeclaredIdentifier();

	protected AbstractIdentifiableType<? super X> requireSupertype() {
		if ( getSupertype() == null ) {
			throw new IllegalStateException( "No supertype found" );
		}
		return getSupertype();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasSingleIdAttribute() {
		return hasIdentifierProperty;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<? super X, Y> getId(Class<Y> javaType) {
		final SingularAttribute<? super X, Y> id_;
		if ( id != null ) {
			checkSimpleId();
			id_ = ( SingularAttribute<? super X, Y> ) id;
			if ( javaType != id.getJavaType() ) {
				throw new IllegalArgumentException( "Id attribute was not of specified type : " + javaType.getName() );
			}
		}
		else {
			//yuk yuk bad me
			if ( ! requiresSupertypeForNonDeclaredIdentifier()) {
				final AbstractIdentifiableType<? super X> supertype = getSupertype();
				if (supertype != null) {
					id_ = supertype.getId( javaType );
				}
				else {
					id_ = null;
				}
			}
			else {
				id_ = requireSupertype().getId( javaType );
			}
		}
		return id_;
	}

	/**
	 * Centralized check to ensure the id for this hierarchy is a simple one (i.e., does not use
	 * an id-class).
	 *
	 * @see #checkIdClass()
	 */
	protected void checkSimpleId() {
		if ( ! hasIdentifierProperty ) {
			throw new IllegalStateException( "This class uses an @IdClass" );
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<X, Y> getDeclaredId(Class<Y> javaType) {
		checkDeclaredId();
		checkSimpleId();
		if ( javaType != id.getJavaType() ) {
			throw new IllegalArgumentException( "Id attribute was not of specified type : " + javaType.getName() );
		}
		return (SingularAttribute<X, Y>) id;
	}

	/**
	 * Centralized check to ensure the id is actually declared on the class mapped here, as opposed to a
	 * super class.
	 */
	protected void checkDeclaredId() {
		if ( id == null ) {
			throw new IllegalArgumentException( "The id attribute is not declared on this type" );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Type<?> getIdType() {
		if ( id != null ) {
			checkSimpleId();
			return id.getType();
		}
		else {
			return requireSupertype().getIdType();
		}
	}

	private boolean hasIdClassAttributesDefined() {
		return idClassAttributes != null ||
				( getSupertype() != null && getSupertype().hasIdClassAttributesDefined() );
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<SingularAttribute<? super X, ?>> getIdClassAttributes() {
		if ( idClassAttributes != null ) {
			checkIdClass();
		}
		else {
			// Java does not allow casting requireSupertype().getIdClassAttributes()
			// to Set<SingularAttribute<? super X, ?>> because the
			// superclass X is a different Java type from this X
			// (i.e, getSupertype().getJavaType() != getJavaType()).
			// It will, however, allow a Set<SingularAttribute<? super X, ?>>
			// to be initialized with requireSupertype().getIdClassAttributes(),
			// since getSupertype().getJavaType() is a superclass of getJavaType()
			if ( requiresSupertypeForNonDeclaredIdentifier() ) {
				idClassAttributes = new HashSet<SingularAttribute<? super X, ?>>( requireSupertype().getIdClassAttributes() );
			}
			else if ( getSupertype() != null && hasIdClassAttributesDefined() ) {
				idClassAttributes = new HashSet<SingularAttribute<? super X, ?>>( getSupertype().getIdClassAttributes() );
			}
		}
		return idClassAttributes;
	}

	/**
	 * Centralized check to ensure the id for this hierarchy uses an id-class.
	 *
	 * @see #checkSimpleId()
	 */
	private void checkIdClass() {
		if ( hasIdentifierProperty ) {
			throw new IllegalArgumentException( "This class does not use @IdClass" );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasVersionAttribute() {
		return isVersioned;
	}

	public boolean hasDeclaredVersionAttribute() {
		return isVersioned && version != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<? super X, Y> getVersion(Class<Y> javaType) {
		if ( ! hasVersionAttribute() ) {
			return null;
		}
		final SingularAttribute<? super X, Y> version_;
		if ( version != null ) {
			version_ = ( SingularAttribute<? super X, Y> ) version;
			if ( javaType != version.getJavaType() ) {
				throw new IllegalArgumentException( "Version attribute was not of specified type : " + javaType.getName() );
			}
		}
		else {
			version_ = requireSupertype().getVersion( javaType );
		}
		return version_;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<X, Y> getDeclaredVersion(Class<Y> javaType) {
		checkDeclaredVersion();
		if ( javaType != version.getJavaType() ) {
			throw new IllegalArgumentException( "Version attribute was not of specified type : " + javaType.getName() );
		}
		return ( SingularAttribute<X, Y> ) version;
	}

	/**
	 * For used to retrieve the declared version when populating the static metamodel.
	 *
	 * @return The declared
	 */
	public SingularAttribute<X, ?> getDeclaredVersion() {
		checkDeclaredVersion();
		return version;
	}

	/**
	 * Centralized check to ensure the version (if one) is actually declared on the class mapped here, as opposed to a
	 * super class.
	 */
	protected void checkDeclaredVersion() {
		if ( version == null || ( getSupertype() != null && getSupertype().hasVersionAttribute() )) {
			throw new IllegalArgumentException( "The version attribute is not declared on this type" );
		}
	}

	public Builder<X> getBuilder() {
		final AbstractManagedType.Builder<X> managedBuilder = super.getBuilder();
		return new Builder<X>() {
			public void applyIdAttribute(SingularAttributeImpl<X, ?> idAttribute) {
				AbstractIdentifiableType.this.id = idAttribute;
				managedBuilder.addAttribute( idAttribute );
			}

			public void applyIdClassAttributes(Set<SingularAttribute<? super X,?>> idClassAttributes) {
				for ( SingularAttribute<? super X,?> idClassAttribute : idClassAttributes ) {
					if ( AbstractIdentifiableType.this == idClassAttribute.getDeclaringType() ) {
						@SuppressWarnings({ "unchecked" })
						SingularAttribute<X,?> declaredAttribute = ( SingularAttribute<X,?> ) idClassAttribute;
						addAttribute( declaredAttribute );
					}
				}
				AbstractIdentifiableType.this.idClassAttributes = idClassAttributes;
			}

			public void applyVersionAttribute(SingularAttributeImpl<X, ?> versionAttribute) {
				AbstractIdentifiableType.this.version = versionAttribute;
				managedBuilder.addAttribute( versionAttribute );
			}

			public void addAttribute(Attribute<X, ?> attribute) {
				managedBuilder.addAttribute( attribute );
			}
		};
	}

	public static interface Builder<X> extends AbstractManagedType.Builder<X> {
		public void applyIdAttribute(SingularAttributeImpl<X,?> idAttribute);
		public void applyIdClassAttributes(Set<SingularAttribute<? super X,?>> idClassAttributes);
		public void applyVersionAttribute(SingularAttributeImpl<X,?> versionAttribute);
	}
}
