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
package org.hibernate.ejb.test.packaging;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.MappedSuperclass;
import javax.persistence.Persistence;

import org.junit.Test;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.packaging.NamedInputStream;
import org.hibernate.ejb.packaging.NativeScanner;
import org.hibernate.ejb.packaging.Scanner;
import org.hibernate.ejb.test.pack.defaultpar.ApplicationServer;
import org.hibernate.testing.RequiresDialect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@RequiresDialect(H2Dialect.class)
public class ScannerTest extends PackagingTestCase {
	@Test
	public void testNativeScanner() throws Exception {
		File defaultPar = buildDefaultPar();
		addPackageToClasspath( defaultPar );

		Scanner scanner = new NativeScanner();
		assertEquals( "defaultpar", scanner.getUnqualifiedJarName( defaultPar.toURL() ) );

		Set<Class<? extends Annotation>> annotationsToLookFor = new HashSet<Class<? extends Annotation>>( 3 );
		annotationsToLookFor.add( Entity.class );
		annotationsToLookFor.add( MappedSuperclass.class );
		annotationsToLookFor.add( Embeddable.class );
		final Set<Class<?>> classes = scanner.getClassesInJar( defaultPar.toURL(), annotationsToLookFor );

		assertEquals( 3, classes.size() );
		assertTrue( classes.contains( ApplicationServer.class ) );
		assertTrue( classes.contains( org.hibernate.ejb.test.pack.defaultpar.Version.class ) );

		Set<String> filePatterns = new HashSet<String>( 2 );
		filePatterns.add( "**/*.hbm.xml" );
		filePatterns.add( "META-INF/orm.xml" );
		final Set<NamedInputStream> files = scanner.getFilesInJar( defaultPar.toURL(), filePatterns );

		assertEquals( 2, files.size() );
		for ( NamedInputStream file : files ) {
			assertNotNull( file.getStream() );
			file.getStream().close();
		}
	}

	@Test
	public void testCustomScanner() throws Exception {
		File defaultPar = buildDefaultPar();
		File explicitPar = buildExplicitPar();
		addPackageToClasspath( defaultPar, explicitPar );
		
		EntityManagerFactory emf;
		CustomScanner.resetUsed();
		final HashMap integration = new HashMap();
		emf = Persistence.createEntityManagerFactory( "defaultpar", integration );
		assertTrue( ! CustomScanner.isUsed() );
		emf.close();

		CustomScanner.resetUsed();
		emf = Persistence.createEntityManagerFactory( "manager1", integration );
		assertTrue( CustomScanner.isUsed() );
		emf.close();

		CustomScanner.resetUsed();
		integration.put( AvailableSettings.SCANNER, new CustomScanner() );
		emf = Persistence.createEntityManagerFactory( "defaultpar", integration );
		assertTrue( CustomScanner.isUsed() );
		emf.close();

		CustomScanner.resetUsed();
		emf = Persistence.createEntityManagerFactory( "defaultpar", null );
		assertTrue( ! CustomScanner.isUsed() );
		emf.close();
	}
}
