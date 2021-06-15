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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import org.jboss.logging.Logger;

import org.hibernate.ejb.internal.EntityManagerMessageLogger;

/**
 * Work on a JAR that can be accessed through a File
 *
 * @author Emmanuel Bernard
 */
public class FileZippedJarVisitor extends AbstractJarVisitor {

    private static final EntityManagerMessageLogger LOG = Logger.getMessageLogger(EntityManagerMessageLogger.class,
                                                                           FileZippedJarVisitor.class.getName());

    private String entry;

	public FileZippedJarVisitor(String fileName, Filter[] filters) {
		super( fileName, filters );
	}

	public FileZippedJarVisitor(URL url, Filter[] filters, String entry) {
		super( url, filters );
		this.entry = entry;
	}

	@Override
    protected void doProcessElements() throws IOException {
		JarFile jarFile;
		try {
			String filePart = jarUrl.getFile();
			if ( filePart != null && filePart.indexOf( ' ' ) != -1 ) {
				//unescaped (from the container), keep as is
				jarFile = new JarFile( jarUrl.getFile() );
			}
			else {
				jarFile = new JarFile( jarUrl.toURI().getSchemeSpecificPart() );
			}
		}
		catch (IOException ze) {
            LOG.unableToFindFile(jarUrl, ze);
			return;
		}
		catch (URISyntaxException e) {
            LOG.malformedUrlWarning(jarUrl, e);
			return;
		}

		if ( entry != null && entry.length() == 1 ) entry = null; //no entry
		if ( entry != null && entry.startsWith( "/" ) ) entry = entry.substring( 1 ); //remove '/' header

		Enumeration<? extends ZipEntry> entries = jarFile.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry zipEntry = entries.nextElement();
			String name = zipEntry.getName();
			if ( entry != null && ! name.startsWith( entry ) ) continue; //filter it out
			if ( !zipEntry.isDirectory() ) {
				if ( name.equals( entry ) ) {
					//exact match, might be a nested jar entry (ie from jar:file:..../foo.ear!/bar.jar)
					/*
					 * This algorithm assumes that the zipped file is only the URL root (including entry), not just any random entry
					 */
					InputStream is = null;
					try {
						is = new BufferedInputStream( jarFile.getInputStream( zipEntry ) );
						JarInputStream jis = new JarInputStream( is );
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
							subZipEntry = jis.getNextEntry();
						}
					}
					finally {
						if ( is != null) is.close();
					}
				}
				else {
					//build relative name
					if (entry != null) name = name.substring( entry.length() );
					if ( name.startsWith( "/" ) ) name = name.substring( 1 );
					addElement(
							name,
							new BufferedInputStream( jarFile.getInputStream( zipEntry ) ),
							new BufferedInputStream( jarFile.getInputStream( zipEntry ) )
					);
				}
			}
		}
	}
}
