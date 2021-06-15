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
package org.hibernate.ejb.packaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import org.jboss.logging.Logger;

import org.hibernate.ejb.internal.EntityManagerMessageLogger;


/**
 * Work on a JAR that can only be accessed through a inputstream
 * This is less efficient than the {@link FileZippedJarVisitor}
 *
 * @author Emmanuel Bernard
 */
public class InputStreamZippedJarVisitor extends AbstractJarVisitor {

    private static final EntityManagerMessageLogger LOG = Logger.getMessageLogger(EntityManagerMessageLogger.class,
                                                                           InputStreamZippedJarVisitor.class.getName());

    private String entry;

	public InputStreamZippedJarVisitor(URL url, Filter[] filters, String entry) {
		super( url, filters );
		this.entry = entry;
	}

	public InputStreamZippedJarVisitor(String fileName, Filter[] filters) {
		super( fileName, filters );
	}

	@Override
    protected void doProcessElements() throws IOException {
		JarInputStream jis;
		try {
			jis = new JarInputStream( jarUrl.openStream() );
		}
		catch (Exception ze) {
			//really should catch IOException but Eclipse is buggy and raise NPE...
            LOG.unableToFindFile(jarUrl, ze);
			return;
		}
		if ( entry != null && entry.length() == 1 ) entry = null; //no entry
		if ( entry != null && entry.startsWith( "/" ) ) entry = entry.substring( 1 ); //remove '/' header

		JarEntry jarEntry;
		while ( ( jarEntry = jis.getNextJarEntry() ) != null ) {
			String name = jarEntry.getName();
			if ( entry != null && ! name.startsWith( entry ) ) continue; //filter it out
			if ( !jarEntry.isDirectory() ) {
				if ( name.equals( entry ) ) {
					//exact match, might be a nested jar entry (ie from jar:file:..../foo.ear!/bar.jar)
					/*
					 * This algorithm assumes that the zipped file is only the URL root (including entry), not just any random entry
					 */
					JarInputStream subJis = null;
					try {
						subJis = new JarInputStream( jis );
						ZipEntry subZipEntry = jis.getNextEntry();
						while (subZipEntry != null) {
							if ( ! subZipEntry.isDirectory() ) {
								//FIXME copy sucks
								byte[] entryBytes = JarVisitorFactory.getBytesFromInputStream( jis );
								String subname = subZipEntry.getName();
								if ( subname.startsWith( "/" ) ) subname = subname.substring( 1 );
								addElement(
										subname,
										new ByteArrayInputStream(entryBytes),
										new ByteArrayInputStream(entryBytes)
								);
							}
							subZipEntry = jis.getNextJarEntry();
						}
					}
					finally {
						if (subJis != null) subJis.close();
					}
				}
				else {
					byte[] entryBytes = JarVisitorFactory.getBytesFromInputStream( jis );
					//build relative name
					if (entry != null) name = name.substring( entry.length() );
					if ( name.startsWith( "/" ) ) name = name.substring( 1 );
					//this is bad cause we actually read everything instead of walking it lazily
					addElement(
							name,
							new ByteArrayInputStream( entryBytes ),
							new ByteArrayInputStream( entryBytes )
					);
				}
			}
		}
		jis.close();
	}
}
