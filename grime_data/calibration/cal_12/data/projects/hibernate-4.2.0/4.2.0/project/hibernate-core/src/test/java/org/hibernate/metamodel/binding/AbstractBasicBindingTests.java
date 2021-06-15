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
package org.hibernate.metamodel.binding;

import java.sql.Types;
import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.metamodel.MetadataSources;
import org.hibernate.metamodel.domain.BasicType;
import org.hibernate.metamodel.domain.SingularAttribute;
import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.Datatype;
import org.hibernate.metamodel.relational.SimpleValue;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.metamodel.source.internal.MetadataImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Basic tests of {@code hbm.xml} and annotation binding code
 *
 * @author Steve Ebersole
 */
public abstract class AbstractBasicBindingTests extends BaseUnitTestCase {

	private StandardServiceRegistryImpl serviceRegistry;

	@Before
	public void setUp() {
		serviceRegistry = (StandardServiceRegistryImpl) new ServiceRegistryBuilder().buildServiceRegistry();
	}

	@After
	public void tearDown() {
		serviceRegistry.destroy();
	}

	protected ServiceRegistry basicServiceRegistry() {
		return serviceRegistry;
	}

	@Test
	public void testSimpleEntityMapping() {
		MetadataSources sources = new MetadataSources( serviceRegistry );
		addSourcesForSimpleEntityBinding( sources );
		MetadataImpl metadata = (MetadataImpl) sources.buildMetadata();
		EntityBinding entityBinding = metadata.getEntityBinding( SimpleEntity.class.getName() );
		assertRoot( metadata, entityBinding );
		assertIdAndSimpleProperty( entityBinding );

		assertNull( entityBinding.getHierarchyDetails().getVersioningAttributeBinding() );
	}

	@Test
	public void testSimpleVersionedEntityMapping() {
		MetadataSources sources = new MetadataSources( serviceRegistry );
		addSourcesForSimpleVersionedEntityBinding( sources );
		MetadataImpl metadata = (MetadataImpl) sources.buildMetadata();
		EntityBinding entityBinding = metadata.getEntityBinding( SimpleVersionedEntity.class.getName() );
		assertIdAndSimpleProperty( entityBinding );

		assertNotNull( entityBinding.getHierarchyDetails().getVersioningAttributeBinding() );
		assertNotNull( entityBinding.getHierarchyDetails().getVersioningAttributeBinding().getAttribute() );
	}

	@Test
	public void testEntityWithManyToOneMapping() {
		MetadataSources sources = new MetadataSources( serviceRegistry );
		addSourcesForSimpleEntityBinding( sources );
		addSourcesForManyToOne( sources );
		MetadataImpl metadata = (MetadataImpl) sources.buildMetadata();

		EntityBinding simpleEntityBinding = metadata.getEntityBinding( SimpleEntity.class.getName() );
		assertIdAndSimpleProperty( simpleEntityBinding );

		Set<SingularAssociationAttributeBinding> referenceBindings = simpleEntityBinding.locateAttributeBinding( "id" )
				.getEntityReferencingAttributeBindings();
		assertEquals( "There should be only one reference binding", 1, referenceBindings.size() );

		SingularAssociationAttributeBinding referenceBinding = referenceBindings.iterator().next();
		EntityBinding referencedEntityBinding = referenceBinding.getReferencedEntityBinding();
		// TODO - Is this assertion correct (HF)?
		assertEquals( "Should be the same entity binding", referencedEntityBinding, simpleEntityBinding );

		EntityBinding entityWithManyToOneBinding = metadata.getEntityBinding( ManyToOneEntity.class.getName() );
		Iterator<SingularAssociationAttributeBinding> it = entityWithManyToOneBinding.getEntityReferencingAttributeBindings()
				.iterator();
		assertTrue( it.hasNext() );
		assertSame( entityWithManyToOneBinding.locateAttributeBinding( "simpleEntity" ), it.next() );
		assertFalse( it.hasNext() );
	}

	@Test
	public void testSimpleEntityWithSimpleComponentMapping() {
		MetadataSources sources = new MetadataSources( serviceRegistry );
		addSourcesForComponentBinding( sources );
		MetadataImpl metadata = (MetadataImpl) sources.buildMetadata();
		EntityBinding entityBinding = metadata.getEntityBinding( SimpleEntityWithSimpleComponent.class.getName() );
		assertRoot( metadata, entityBinding );
		assertIdAndSimpleProperty( entityBinding );

		ComponentAttributeBinding componentAttributeBinding = (ComponentAttributeBinding) entityBinding.locateAttributeBinding( "simpleComponent" );
		assertNotNull( componentAttributeBinding );
		assertSame( componentAttributeBinding.getAttribute().getSingularAttributeType(), componentAttributeBinding.getAttributeContainer() );
		assertEquals( SimpleEntityWithSimpleComponent.class.getName() + ".simpleComponent", componentAttributeBinding.getPathBase() );
		assertSame( entityBinding, componentAttributeBinding.seekEntityBinding() );
		assertNotNull( componentAttributeBinding.getComponent() );
	}

	public abstract void addSourcesForSimpleVersionedEntityBinding(MetadataSources sources);

	public abstract void addSourcesForSimpleEntityBinding(MetadataSources sources);

	public abstract void addSourcesForManyToOne(MetadataSources sources);

	public abstract void addSourcesForComponentBinding(MetadataSources sources);

	protected void assertIdAndSimpleProperty(EntityBinding entityBinding) {
		assertNotNull( entityBinding );
		assertNotNull( entityBinding.getHierarchyDetails().getEntityIdentifier() );
		assertNotNull( entityBinding.getHierarchyDetails().getEntityIdentifier().getValueBinding() );

		AttributeBinding idAttributeBinding = entityBinding.locateAttributeBinding( "id" );
		assertNotNull( idAttributeBinding );
		assertSame( idAttributeBinding, entityBinding.getHierarchyDetails().getEntityIdentifier().getValueBinding() );
		assertSame( LongType.INSTANCE, idAttributeBinding.getHibernateTypeDescriptor().getResolvedTypeMapping() );

		assertTrue( idAttributeBinding.getAttribute().isSingular() );
		assertNotNull( idAttributeBinding.getAttribute() );
		SingularAttributeBinding singularIdAttributeBinding = (SingularAttributeBinding) idAttributeBinding;
		assertFalse( singularIdAttributeBinding.isNullable() );
		SingularAttribute singularIdAttribute =  ( SingularAttribute ) idAttributeBinding.getAttribute();
		BasicType basicIdAttributeType = ( BasicType ) singularIdAttribute.getSingularAttributeType();
		assertSame( Long.class, basicIdAttributeType.getClassReference() );

		assertNotNull( singularIdAttributeBinding.getValue() );
		assertTrue( singularIdAttributeBinding.getValue() instanceof Column );
		Datatype idDataType = ( (Column) singularIdAttributeBinding.getValue() ).getDatatype();
		assertSame( Long.class, idDataType.getJavaType() );
		assertSame( Types.BIGINT, idDataType.getTypeCode() );
		assertSame( LongType.INSTANCE.getName(), idDataType.getTypeName() );

		assertNotNull( entityBinding.locateAttributeBinding( "name" ) );
		assertNotNull( entityBinding.locateAttributeBinding( "name" ).getAttribute() );
		assertTrue( entityBinding.locateAttributeBinding( "name" ).getAttribute().isSingular() );

		SingularAttributeBinding nameBinding = (SingularAttributeBinding) entityBinding.locateAttributeBinding( "name" );
		assertTrue( nameBinding.isNullable() );
		assertSame( StringType.INSTANCE, nameBinding.getHibernateTypeDescriptor().getResolvedTypeMapping() );
		assertNotNull( nameBinding.getAttribute() );
		assertNotNull( nameBinding.getValue() );
		SingularAttribute singularNameAttribute =  ( SingularAttribute ) nameBinding.getAttribute();
		BasicType basicNameAttributeType = ( BasicType ) singularNameAttribute.getSingularAttributeType();
		assertSame( String.class, basicNameAttributeType.getClassReference() );

		assertNotNull( nameBinding.getValue() );
		SimpleValue nameValue = (SimpleValue) nameBinding.getValue();
		assertTrue( nameValue instanceof Column );
		Datatype nameDataType = nameValue.getDatatype();
		assertSame( String.class, nameDataType.getJavaType() );
		assertSame( Types.VARCHAR, nameDataType.getTypeCode() );
		assertSame( StringType.INSTANCE.getName(), nameDataType.getTypeName() );
	}

	protected void assertRoot(MetadataImplementor metadata, EntityBinding entityBinding) {
		assertTrue( entityBinding.isRoot() );
		assertSame( entityBinding, metadata.getRootEntityBinding( entityBinding.getEntity().getName() ) );
	}
}
