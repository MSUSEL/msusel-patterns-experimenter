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
package org.hibernate.metamodel.source.annotations.xml.mocker;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.junit.Test;

import org.hibernate.internal.jaxb.mapping.orm.JaxbAttributes;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEntity;
import org.hibernate.internal.jaxb.mapping.orm.JaxbGeneratedValue;
import org.hibernate.internal.jaxb.mapping.orm.JaxbId;
import org.hibernate.metamodel.source.annotations.JPADotNames;

import static org.junit.Assert.assertEquals;

/**
 * @author Strong Liu
 */
public class BasicMockerTest extends AbstractMockerTest {
	@Test
	public void testEntity() {
		JaxbEntity entity = createEntity();
		IndexBuilder indexBuilder = getIndexBuilder();
		EntityMocker entityMocker = new EntityMocker( indexBuilder, entity, new EntityMappingsMocker.Default() );
		entityMocker.preProcess();
		entityMocker.process();

		Index index = indexBuilder.build( new EntityMappingsMocker.Default() );
		assertEquals( 1, index.getKnownClasses().size() );
		DotName itemName = DotName.createSimple( Item.class.getName() );
		assertHasAnnotation( index, itemName, JPADotNames.ENTITY );
		assertHasAnnotation( index, itemName, JPADotNames.ID );
		assertHasAnnotation( index, itemName, JPADotNames.GENERATED_VALUE );
	}

	@Test
	public void testEntityWithEntityMappingsConfiguration() {
		JaxbEntity entity = new JaxbEntity();
		entity.setName( "Item" );
		entity.setClazz( "Item" );
		IndexBuilder indexBuilder = getIndexBuilder();
		EntityMappingsMocker.Default defaults = new EntityMappingsMocker.Default();
		defaults.setPackageName( getClass().getPackage().getName() );
		defaults.setSchema( "HIBERNATE_SCHEMA" );
		defaults.setCatalog( "HIBERNATE_CATALOG" );
		EntityMocker entityMocker = new EntityMocker( indexBuilder, entity, defaults );
		entityMocker.preProcess();
		entityMocker.process();

		Index index = indexBuilder.build( new EntityMappingsMocker.Default() );
		assertEquals( 1, index.getKnownClasses().size() );
		DotName itemName = DotName.createSimple( Item.class.getName() );
		assertHasAnnotation( index, itemName, JPADotNames.ENTITY );
		assertHasAnnotation( index, itemName, JPADotNames.TABLE );
		assertAnnotationValue(
				index, itemName, JPADotNames.TABLE, new AnnotationValueChecker() {
					@Override
					public void check(AnnotationInstance annotationInstance) {
						AnnotationValue schemaValue = annotationInstance.value( "schema" );
						AnnotationValue catalogValue = annotationInstance.value( "catalog" );
						assertStringAnnotationValue( "HIBERNATE_SCHEMA", schemaValue );
						assertStringAnnotationValue( "HIBERNATE_CATALOG", catalogValue );
					}
				}
		);

	}


	private JaxbEntity createEntity() {
		JaxbEntity entity = new JaxbEntity();
		entity.setName( "Item" );
		entity.setClazz( Item.class.getName() );
		JaxbAttributes attributes = new JaxbAttributes();
		JaxbId id = new JaxbId();
		id.setName( "id" );
		id.setGeneratedValue( new JaxbGeneratedValue() );
		attributes.getId().add( id );
		entity.setAttributes( attributes );
		return entity;
	}


}
