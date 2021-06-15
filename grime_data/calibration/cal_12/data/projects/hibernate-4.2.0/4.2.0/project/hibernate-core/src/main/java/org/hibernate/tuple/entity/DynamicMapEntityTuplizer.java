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
package org.hibernate.tuple.entity;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.EntityMode;
import org.hibernate.EntityNameResolver;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metamodel.binding.AttributeBinding;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.proxy.map.MapProxyFactory;
import org.hibernate.tuple.DynamicMapInstantiator;
import org.hibernate.tuple.Instantiator;

/**
 * An {@link EntityTuplizer} specific to the dynamic-map entity mode.
 *
 * @author Steve Ebersole
 * @author Gavin King
 */
public class DynamicMapEntityTuplizer extends AbstractEntityTuplizer {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
                                                                       DynamicMapEntityTuplizer.class.getName());

	DynamicMapEntityTuplizer(EntityMetamodel entityMetamodel, PersistentClass mappedEntity) {
		super(entityMetamodel, mappedEntity);
	}

	DynamicMapEntityTuplizer(EntityMetamodel entityMetamodel, EntityBinding mappedEntity) {
		super(entityMetamodel, mappedEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	public EntityMode getEntityMode() {
		return EntityMode.MAP;
	}

	private PropertyAccessor buildPropertyAccessor(Property mappedProperty) {
		if ( mappedProperty.isBackRef() ) {
			return mappedProperty.getPropertyAccessor(null);
		}
		else {
			return PropertyAccessorFactory.getDynamicMapPropertyAccessor();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    protected Getter buildPropertyGetter(Property mappedProperty, PersistentClass mappedEntity) {
		return buildPropertyAccessor(mappedProperty).getGetter( null, mappedProperty.getName() );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    protected Setter buildPropertySetter(Property mappedProperty, PersistentClass mappedEntity) {
		return buildPropertyAccessor(mappedProperty).getSetter( null, mappedProperty.getName() );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    protected Instantiator buildInstantiator(PersistentClass mappingInfo) {
        return new DynamicMapInstantiator( mappingInfo );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    protected ProxyFactory buildProxyFactory(PersistentClass mappingInfo, Getter idGetter, Setter idSetter) {

		ProxyFactory pf = new MapProxyFactory();
		try {
			//TODO: design new lifecycle for ProxyFactory
			pf.postInstantiate(
					getEntityName(),
					null,
					null,
					null,
					null,
					null
			);
		}
		catch ( HibernateException he ) {
			LOG.unableToCreateProxyFactory( getEntityName(), he );
			pf = null;
		}
		return pf;
	}

	private PropertyAccessor buildPropertyAccessor(AttributeBinding mappedProperty) {
		// TODO: fix when backrefs are working in new metamodel
		//if ( mappedProperty.isBackRef() ) {
		//	return mappedProperty.getPropertyAccessor( null );
		//}
		//else {
			return PropertyAccessorFactory.getDynamicMapPropertyAccessor();
		//}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Getter buildPropertyGetter(AttributeBinding mappedProperty) {
		return buildPropertyAccessor( mappedProperty ).getGetter( null, mappedProperty.getAttribute().getName() );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Setter buildPropertySetter(AttributeBinding mappedProperty) {
		return buildPropertyAccessor( mappedProperty ).getSetter( null, mappedProperty.getAttribute().getName() );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Instantiator buildInstantiator(EntityBinding mappingInfo) {
		return new DynamicMapInstantiator( mappingInfo );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ProxyFactory buildProxyFactory(EntityBinding mappingInfo, Getter idGetter, Setter idSetter) {

		ProxyFactory pf = new MapProxyFactory();
		try {
			//TODO: design new lifecycle for ProxyFactory
			pf.postInstantiate(
					getEntityName(),
					null,
					null,
					null,
					null,
					null
			);
		}
		catch ( HibernateException he ) {
			LOG.unableToCreateProxyFactory(getEntityName(), he);
			pf = null;
		}
		return pf;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class getMappedClass() {
		return Map.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class getConcreteProxyClass() {
		return Map.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInstrumented() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public EntityNameResolver[] getEntityNameResolvers() {
		return new EntityNameResolver[] { BasicEntityNameResolver.INSTANCE };
	}

	/**
	 * {@inheritDoc}
	 */
	public String determineConcreteSubclassEntityName(Object entityInstance, SessionFactoryImplementor factory) {
		return extractEmbeddedEntityName( ( Map ) entityInstance );
	}

	public static String extractEmbeddedEntityName(Map entity) {
		return ( String ) entity.get( DynamicMapInstantiator.KEY );
	}

	public static class BasicEntityNameResolver implements EntityNameResolver {
		public static final BasicEntityNameResolver INSTANCE = new BasicEntityNameResolver();

		/**
		 * {@inheritDoc}
		 */
		public String resolveEntityName(Object entity) {
			if ( ! Map.class.isInstance( entity ) ) {
				return null;
			}
			final String entityName = extractEmbeddedEntityName( ( Map ) entity );
			if ( entityName == null ) {
				throw new HibernateException( "Could not determine type of dynamic map entity" );
			}
			return entityName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
        public boolean equals(Object obj) {
			return getClass().equals( obj.getClass() );
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
        public int hashCode() {
			return getClass().hashCode();
		}
	}
}
