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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;

import org.hibernate.AnnotationException;
import org.hibernate.HibernateException;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEntityMappings;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.testing.ServiceRegistryBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Strong Liu
 */
public abstract class AbstractMockerTest {
	private static final String ORM1_MAPPING_XSD = "org/hibernate/ejb/orm_1_0.xsd";
	private static final String ORM2_MAPPING_XSD = "org/hibernate/ejb/orm_2_0.xsd";
	private IndexBuilder indexBuilder;
	private Index index;
	private ServiceRegistry serviceRegistry;
	protected String packagePrefix = getClass().getPackage().getName().replace( '.', '/' ) + '/';

	protected IndexBuilder getIndexBuilder() {
		if ( indexBuilder == null ) {
			indexBuilder = new IndexBuilder( getIndex(), getServiceRegistry() );
		}
		return indexBuilder;

	}

	protected EntityMappingsMocker getEntityMappingsMocker(String... mappingFiles) {
		ClassLoaderService classLoaderService = getServiceRegistry().getService( ClassLoaderService.class );
		List<JaxbEntityMappings> xmlEntityMappingsList = new ArrayList<JaxbEntityMappings>();
		for ( String fileName : mappingFiles ) {
			JaxbEntityMappings entityMappings;
			try {
				entityMappings = XmlHelper.unmarshallXml(
						packagePrefix + fileName, ORM2_MAPPING_XSD, JaxbEntityMappings.class, classLoaderService
				).getRoot();
			}
			catch ( JAXBException orm2Exception ) {
				// if we cannot parse against orm_2_0.xsd we try orm_1_0.xsd for backwards compatibility
				try {
					entityMappings = XmlHelper.unmarshallXml(
							packagePrefix + fileName, ORM1_MAPPING_XSD, JaxbEntityMappings.class, classLoaderService
					).getRoot();
				}
				catch ( JAXBException orm1Exception ) {
					throw new AnnotationException( "Unable to parse xml configuration.", orm1Exception );
				}
			}
			xmlEntityMappingsList.add( entityMappings );
		}
		return new EntityMappingsMocker( xmlEntityMappingsList, getIndex(), getServiceRegistry() );
	}

	protected Index getIndex() {
		if ( index == null ) {
			Indexer indexer = new Indexer();
			for ( Class<?> clazz : getAnnotatedClasses() ) {
				indexClass( indexer, clazz.getName().replace( '.', '/' ) + ".class" );
			}

			// add package-info from the configured packages
			for ( String packageName : getAnnotatedPackages() ) {
				indexClass( indexer, packageName.replace( '.', '/' ) + "/package-info.class" );
			}
			index = indexer.complete();
		}
		return index;

	}

	protected Index getMockedIndex(String ormFileName) {
		EntityMappingsMocker mocker = getEntityMappingsMocker( ormFileName );
		return mocker.mockNewIndex();
	}

	private void indexClass(Indexer indexer, String className) {
		ClassLoaderService classLoaderService = getServiceRegistry().getService( ClassLoaderService.class );
		InputStream stream = classLoaderService.locateResourceStream( className );
		try {
			indexer.index( stream );
		}
		catch ( IOException e ) {
			throw new HibernateException( "Unable to open input stream for class " + className, e );
		}
	}

	protected Class[] getAnnotatedClasses() {
		return new Class[0];
	}

	protected String[] getAnnotatedPackages() {
		return new String[0];
	}

	protected ServiceRegistry getServiceRegistry() {
		if ( serviceRegistry == null ) {
			serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry();
		}
		return serviceRegistry;
	}

	protected void assertHasNoAnnotation(Index index, DotName className, DotName annName) {
		List<AnnotationInstance> annotationInstanceList = getAnnotationInstances( index, className, annName );
		if ( annotationInstanceList != null ) {
			if ( !annotationInstanceList.isEmpty() ) {
				fail( className + " has Annotation " + annName );
			}
		}
	}
	protected void assertHasAnnotation(Index index,  DotName annName) {
		assertHasAnnotation( index, null, annName, 1 );
	}
	protected void assertHasAnnotation(Index index, DotName className, DotName annName) {
		assertHasAnnotation( index, className, annName, 1 );
	}

	protected void assertHasAnnotation(Index index, DotName className, DotName annName, int size) {
		List<AnnotationInstance> annotationInstanceList = getAnnotationInstances( index, className, annName );
		if ( annotationInstanceList == null || annotationInstanceList.isEmpty() ) {
			fail( "Expected annotation " + annName + " size is " + size + ", but no one can be found in Index" );
		}
		assertEquals(
				"Expected annotation " + annName + " size is " + size + ", but it actually is " + annotationInstanceList
						.size(), size, annotationInstanceList.size()
		);
	}

	protected void assertStringAnnotationValue(String expected, AnnotationValue annotationValue) {
		if ( annotationValue == null ) {
			fail( "Annotation Value is null." );
		}
		assertEquals( expected, annotationValue.asString() );
	}

	protected void assertAnnotationValue(Index index, DotName className, DotName annName, AnnotationValueChecker checker) {
		assertAnnotationValue( index, className, annName, 1, checker );
	}

	protected void assertAnnotationValue(Index index, DotName className, DotName annName, int size, AnnotationValueChecker checker) {
		assertHasAnnotation( index, className, annName, size );
		List<AnnotationInstance> annotationInstanceList = getAnnotationInstances( index,className,annName );
		for ( AnnotationInstance annotationInstance : annotationInstanceList ) {
			checker.check( annotationInstance );
		}
	}

	private List<AnnotationInstance> getAnnotationInstances(Index index, DotName className, DotName annName) {
		if ( className != null ) {
			ClassInfo classInfo = index.getClassByName( className );
			if ( classInfo == null ) {
				fail( "Can't find " + className + " from Index" );
			}
			if ( classInfo.annotations() == null ) {
				fail( classInfo + " doesn't have any annotations defined" );
			}
			return classInfo.annotations().get( annName );
		}
		else {
			return index.getAnnotations( annName );
		}
	}

	static interface AnnotationValueChecker {
		void check(AnnotationInstance annotationInstance);
	}
}
