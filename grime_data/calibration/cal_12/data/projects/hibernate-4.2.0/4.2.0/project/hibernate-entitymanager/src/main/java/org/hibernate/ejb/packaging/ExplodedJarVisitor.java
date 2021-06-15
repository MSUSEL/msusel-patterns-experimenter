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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.jboss.logging.Logger;

import org.hibernate.ejb.internal.EntityManagerMessageLogger;


/**
 * @author Emmanuel Bernard
 */
public class ExplodedJarVisitor extends AbstractJarVisitor {

    private static final EntityManagerMessageLogger LOG = Logger.getMessageLogger(EntityManagerMessageLogger.class,
                                                                           ExplodedJarVisitor.class.getName());

    private String entry;

	public ExplodedJarVisitor(URL url, Filter[] filters, String entry) {
		super( url, filters );
		this.entry = entry;
	}

	public ExplodedJarVisitor(String fileName, Filter[] filters) {
		super( fileName, filters );
	}

	@Override
    protected void doProcessElements() throws IOException {
		File jarFile;
		try {
			String filePart = jarUrl.getFile();
			if ( filePart != null && filePart.indexOf( ' ' ) != -1 ) {
				//unescaped (from the container), keep as is
				jarFile = new File( jarUrl.getFile() );
			}
			else {
				jarFile = new File( jarUrl.toURI().getSchemeSpecificPart() );
			}
		}
		catch (URISyntaxException e) {
            LOG.malformedUrl(jarUrl, e);
			return;
		}

		if ( !jarFile.exists() ) {
            LOG.explodedJarDoesNotExist(jarUrl);
			return;
		}
		if ( !jarFile.isDirectory() ) {
            LOG.explodedJarNotDirectory(jarUrl);
			return;
		}
		File rootFile;
		if (entry != null && entry.length() > 0 && ! "/".equals( entry ) ) {
			rootFile = new File(jarFile, entry);
		}
		else {
			rootFile = jarFile;
		}
		if ( rootFile.isDirectory() ) {
			getClassNamesInTree( rootFile, null );
		}
		else {
			//assume zipped file
			processZippedRoot(rootFile);
		}
	}

	//FIXME shameful copy of FileZippedJarVisitor.doProcess()
	//TODO long term fix is to introduce a process interface (closure like) to addElements and then share the code
	private void processZippedRoot(File rootFile) throws IOException {
		JarFile jarFile = new JarFile(rootFile);
		Enumeration<? extends ZipEntry> entries = jarFile.entries();
		while ( entries.hasMoreElements() ) {
			ZipEntry zipEntry = entries.nextElement();
			String name = zipEntry.getName();
			if ( !zipEntry.isDirectory() ) {
				//build relative name
				if ( name.startsWith( "/" ) ) name = name.substring( 1 );
				addElement(
						name,
						new BufferedInputStream( jarFile.getInputStream( zipEntry ) ),
						new BufferedInputStream( jarFile.getInputStream( zipEntry ) )
				);
			}
		}
	}

	private void getClassNamesInTree(File jarFile, String header) throws IOException {
		File[] files = jarFile.listFiles();
		header = header == null ? "" : header + "/";
		for ( File localFile : files ) {
			if ( !localFile.isDirectory() ) {
				String entryName = localFile.getName();
				addElement(
						header + entryName,
						new BufferedInputStream( new FileInputStream( localFile ) ),
						new BufferedInputStream( new FileInputStream( localFile ) )
				);

			}
			else {
				getClassNamesInTree( localFile, header + localFile.getName() );
			}
		}
	}
}
