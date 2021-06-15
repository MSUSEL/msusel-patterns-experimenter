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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;

import org.hibernate.ejb.test.Cat;
import org.hibernate.ejb.test.Distributor;
import org.hibernate.ejb.test.Item;
import org.hibernate.ejb.test.Kitten;
import org.hibernate.ejb.test.pack.cfgxmlpar.Morito;
import org.hibernate.ejb.test.pack.defaultpar.ApplicationServer;
import org.hibernate.ejb.test.pack.defaultpar.IncrementListener;
import org.hibernate.ejb.test.pack.defaultpar.Lighter;
import org.hibernate.ejb.test.pack.defaultpar.Money;
import org.hibernate.ejb.test.pack.defaultpar.Mouse;
import org.hibernate.ejb.test.pack.defaultpar.OtherIncrementListener;
import org.hibernate.ejb.test.pack.defaultpar.Version;
import org.hibernate.ejb.test.pack.defaultpar_1_0.ApplicationServer1;
import org.hibernate.ejb.test.pack.defaultpar_1_0.IncrementListener1;
import org.hibernate.ejb.test.pack.defaultpar_1_0.Lighter1;
import org.hibernate.ejb.test.pack.defaultpar_1_0.Money1;
import org.hibernate.ejb.test.pack.defaultpar_1_0.Mouse1;
import org.hibernate.ejb.test.pack.defaultpar_1_0.Version1;
import org.hibernate.ejb.test.pack.excludehbmpar.Caipirinha;
import org.hibernate.ejb.test.pack.explodedpar.Carpet;
import org.hibernate.ejb.test.pack.explodedpar.Elephant;
import org.hibernate.ejb.test.pack.externaljar.Scooter;
import org.hibernate.ejb.test.pack.spacepar.Bug;
import org.hibernate.ejb.test.pack.various.Airplane;
import org.hibernate.ejb.test.pack.various.Seat;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.fail;

/**
 * @author Hardy Ferentschik
 * @author Brett Meyer
 */
public abstract class PackagingTestCase extends BaseCoreFunctionalTestCase {
	protected static ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
	protected static ClassLoader bundleClassLoader;
	protected static File packageTargetDir;

	static {
		// get a URL reference to something we now is part of the classpath (us)
		URL myUrl = originalClassLoader.getResource(
				PackagingTestCase.class.getName().replace( '.', '/' ) + ".class"
		);
		// this is assuming that there is a target directory
		int index = myUrl.getFile().lastIndexOf( "target" );
		if ( index == -1 ) {
			fail( "Unable to setup packaging test" );
		}

		String baseDirPath = myUrl.getFile().substring( 0, index );
		File baseDir = new File( baseDirPath );

		File testPackagesDir = new File( baseDir, "target/bundles" );
		try {
			bundleClassLoader = new URLClassLoader( new URL[] { testPackagesDir.toURL() }, originalClassLoader );
		}
		catch ( MalformedURLException e ) {
			fail( "Unable to build custom class loader" );
		}
		packageTargetDir = new File( baseDir, "target/packages" );
		packageTargetDir.mkdirs();
	}

	@Before
	public void prepareTCCL() {
		// add the bundle class loader in order for ShrinkWrap to build the test package
		Thread.currentThread().setContextClassLoader( bundleClassLoader );
	}

	@After
	public void resetTCCL() throws Exception {
		// reset the classloader
		Thread.currentThread().setContextClassLoader( originalClassLoader );
	}

	protected void addPackageToClasspath(File... files) throws MalformedURLException {
		List<URL> urlList = new ArrayList<URL>();
		for ( File file : files ) {
			urlList.add( file.toURL() );
		}
		URLClassLoader classLoader = new URLClassLoader(
				urlList.toArray( new URL[urlList.size()] ), originalClassLoader
		);
		Thread.currentThread().setContextClassLoader( classLoader );
	}

	protected void addPackageToClasspath(URL... urls) throws MalformedURLException {
		List<URL> urlList = new ArrayList<URL>();
		urlList.addAll( Arrays.asList( urls ) );
		URLClassLoader classLoader = new URLClassLoader(
				urlList.toArray( new URL[urlList.size()] ), originalClassLoader
		);
		Thread.currentThread().setContextClassLoader( classLoader );
	}

	protected File buildDefaultPar() {
		String fileName = "defaultpar.par";
		JavaArchive archive = ShrinkWrap.create(  JavaArchive.class, fileName );
		archive.addClasses(
				ApplicationServer.class,
				Lighter.class,
				Money.class,
				Mouse.class,
				OtherIncrementListener.class,
				IncrementListener.class,
				Version.class
		);
		ArchivePath path = ArchivePaths.create( "META-INF/orm.xml" );
		archive.addAsResource( "defaultpar/META-INF/orm.xml", path );

		path = ArchivePaths.create( "META-INF/persistence.xml" );
		archive.addAsResource( "defaultpar/META-INF/persistence.xml", path );

		path = ArchivePaths.create( "org/hibernate/ejb/test/pack/defaultpar/Mouse.hbm.xml" );
		archive.addAsResource( "defaultpar/org/hibernate/ejb/test/pack/defaultpar/Mouse.hbm.xml", path );

		path = ArchivePaths.create( "org/hibernate/ejb/test/pack/defaultpar/package-info.class" );
		archive.addAsResource( "org/hibernate/ejb/test/pack/defaultpar/package-info.class", path );


		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo ( testPackage, true );
		return testPackage;
	}

	protected File buildDefaultPar_1_0() {
		String fileName = "defaultpar_1_0.par";
		JavaArchive archive = ShrinkWrap.create(  JavaArchive.class,fileName );
		archive.addClasses(
				ApplicationServer1.class,
				Lighter1.class,
				Money1.class,
				Mouse1.class,
				IncrementListener1.class,
				Version1.class
		);
		ArchivePath path = ArchivePaths.create( "META-INF/orm.xml" );
		archive.addAsResource( "defaultpar_1_0/META-INF/orm.xml", path );

		path = ArchivePaths.create( "META-INF/persistence.xml" );
		archive.addAsResource( "defaultpar_1_0/META-INF/persistence.xml", path );

		path = ArchivePaths.create( "org/hibernate/ejb/test/pack/defaultpar_1_0/Mouse.hbm.xml" );
		archive.addAsResource( "defaultpar_1_0/org/hibernate/ejb/test/pack/defaultpar_1_0/Mouse1.hbm.xml", path );

		path = ArchivePaths.create( "org/hibernate/ejb/test/pack/defaultpar_1_0/package-info.class" );
		archive.addAsResource( "org/hibernate/ejb/test/pack/defaultpar_1_0/package-info.class", path );


		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildExplicitPar() {
		String fileName = "explicitpar.par";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class, fileName );
		archive.addClasses(
				Airplane.class,
				Seat.class,
				Cat.class,
				Kitten.class,
				Distributor.class,
				Item.class
		);

		ArchivePath path = ArchivePaths.create( "META-INF/orm.xml" );
		archive.addAsResource( "explicitpar/META-INF/orm.xml", path );

		path = ArchivePaths.create( "META-INF/persistence.xml" );
		archive.addAsResource( "explicitpar/META-INF/persistence.xml", path );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildExplodedPar() {
		String fileName = "explodedpar";
		JavaArchive archive = ShrinkWrap.create(  JavaArchive.class,fileName );
		archive.addClasses(
				Elephant.class,
				Carpet.class
		);

		ArchivePath path = ArchivePaths.create( "META-INF/persistence.xml" );
		archive.addAsResource( "explodedpar/META-INF/persistence.xml", path );

		path = ArchivePaths.create( "org/hibernate/ejb/test/pack/explodedpar/Elephant.hbm.xml" );
		archive.addAsResource( "explodedpar/org/hibernate/ejb/test/pack/explodedpar/Elephant.hbm.xml", path );

		path = ArchivePaths.create( "org/hibernate/ejb/test/pack/explodedpar/package-info.class" );
		archive.addAsResource( "org/hibernate/ejb/test/pack/explodedpar/package-info.class", path );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ExplodedExporter.class ).exportExploded( packageTargetDir );
		return testPackage;
	}

	protected File buildExcludeHbmPar() {
		String fileName = "excludehbmpar.par";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class,fileName );
		archive.addClasses(
				Caipirinha.class
		);

		ArchivePath path = ArchivePaths.create( "META-INF/orm2.xml" );
		archive.addAsResource( "excludehbmpar/META-INF/orm2.xml", path );

		path = ArchivePaths.create( "META-INF/persistence.xml" );
		archive.addAsResource( "excludehbmpar/META-INF/persistence.xml", path );

		path = ArchivePaths.create( "org/hibernate/ejb/test/pack/excludehbmpar/Mouse.hbm.xml" );
		archive.addAsResource( "excludehbmpar/org/hibernate/ejb/test/pack/excludehbmpar/Mouse.hbm.xml", path );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildCfgXmlPar() {
		String fileName = "cfgxmlpar.par";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class,fileName );
		archive.addClasses(
				Morito.class,
				Item.class
		);

		ArchivePath path = ArchivePaths.create( "META-INF/persistence.xml" );
		archive.addAsResource( "cfgxmlpar/META-INF/persistence.xml", path );

		path = ArchivePaths.create( "org/hibernate/ejb/test/pack/cfgxmlpar/hibernate.cfg.xml" );
		archive.addAsResource( "cfgxmlpar/org/hibernate/ejb/test/pack/cfgxmlpar/hibernate.cfg.xml", path );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildSpacePar() {
		String fileName = "space par.par";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class, fileName );
		archive.addClasses(
				Bug.class
		);

		ArchivePath path = ArchivePaths.create( "META-INF/persistence.xml" );
		archive.addAsResource( "space par/META-INF/persistence.xml", path );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildOverridenPar() {
		String fileName = "overridenpar.jar";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class, fileName );
		archive.addClasses(
				org.hibernate.ejb.test.pack.overridenpar.Bug.class
		);

		ArchivePath path = ArchivePaths.create( "META-INF/persistence.xml" );
		archive.addAsResource( "overridenpar/META-INF/persistence.xml", path );

		path = ArchivePaths.create( "overridenpar.properties" );
		archive.addAsResource( "overridenpar/overridenpar.properties", path );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildExternalJar() {
		String fileName = "externaljar.jar";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class, fileName );
		archive.addClasses(
				Scooter.class
		);

		ArchivePath path = ArchivePaths.create( "META-INF/orm.xml" );
		archive.addAsResource( "externaljar/META-INF/orm.xml", path );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildLargeJar() {
		String fileName = "large.jar";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class, fileName );
		// Build a large jar by adding a lorem ipsum file repeatedly.
		for ( int i = 0; i < 100; i++ ) {
			ArchivePath path = ArchivePaths.create( "META-INF/file" + i );
			archive.addAsResource( new File( "src/test/resources/org/hibernate/jpa/test/packaging/loremipsum.txt" ),
					path );
		}

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildWar() {
		String fileName = "war.war";
		WebArchive archive = ShrinkWrap.create( WebArchive.class, fileName );
		archive.addClasses(
				org.hibernate.ejb.test.pack.war.ApplicationServer.class,
				org.hibernate.ejb.test.pack.war.IncrementListener.class,
				org.hibernate.ejb.test.pack.war.Lighter.class,
				org.hibernate.ejb.test.pack.war.Money.class,
				org.hibernate.ejb.test.pack.war.Mouse.class,
				org.hibernate.ejb.test.pack.war.OtherIncrementListener.class,
				org.hibernate.ejb.test.pack.war.Version.class
		);

		ArchivePath path = ArchivePaths.create( "WEB-INF/classes/META-INF/orm.xml" );
		archive.addAsResource( "war/WEB-INF/classes/META-INF/orm.xml", path );

		path = ArchivePaths.create( "WEB-INF/classes/META-INF/persistence.xml" );
		archive.addAsResource( "war/WEB-INF/classes/META-INF/persistence.xml", path );

		path = ArchivePaths.create( "WEB-INF/classes/org/hibernate/ejb/test/pack/war/Mouse.hbm.xml" );
		archive.addAsResource( "war/WEB-INF/classes/org/hibernate/ejb/test/pack/war/Mouse.hbm.xml", path );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildNestedEar(File includeFile) {
		String fileName = "nestedjar.ear";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class, fileName );
		archive.addAsResource( includeFile );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ZipExporter.class ).exportTo( testPackage, true );
		return testPackage;
	}

	protected File buildNestedEarDir(File includeFile) {
		String fileName = "nesteddir.ear";
		JavaArchive archive = ShrinkWrap.create( JavaArchive.class, fileName );
		archive.addAsResource( includeFile );

		File testPackage = new File( packageTargetDir, fileName );
		archive.as( ExplodedExporter.class ).exportExploded( packageTargetDir );
		return testPackage;
	}

}


