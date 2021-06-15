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
package org.hibernate.cfg;

import java.io.InputStream;

import org.jboss.logging.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.xml.DTDEntityResolver;

/**
 * Resolve JPA xsd files locally
 * Hibernate OGM uses this class, consider this some kind of exposed service at the SPI level
 *
 * @author Emmanuel Bernard
 */
public class EJB3DTDEntityResolver extends DTDEntityResolver {
	public static final EntityResolver INSTANCE = new EJB3DTDEntityResolver();

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, EJB3DTDEntityResolver.class.getName() );

	boolean resolved = false;

	/**
	 * Persistence.xml has been resolved locally
	 * @return true if it has
	 */
	public boolean isResolved() {
		return resolved;
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId) {
		LOG.tracev( "Resolving XML entity {0} : {1}", publicId, systemId );
		InputSource is = super.resolveEntity( publicId, systemId );
		if ( is == null ) {
			if ( systemId != null ) {
				if ( systemId.endsWith( "orm_1_0.xsd" ) ) {
					InputStream dtdStream = getStreamFromClasspath( "orm_1_0.xsd" );
					final InputSource source = buildInputSource( publicId, systemId, dtdStream, false );
					if (source != null) return source;
				}
				else if ( systemId.endsWith( "orm_2_0.xsd" ) ) {
					InputStream dtdStream = getStreamFromClasspath( "orm_2_0.xsd" );
					final InputSource source = buildInputSource( publicId, systemId, dtdStream, false );
					if (source != null) return source;
				}
				else if ( systemId.endsWith( "persistence_1_0.xsd" ) ) {
					InputStream dtdStream = getStreamFromClasspath( "persistence_1_0.xsd" );
					final InputSource source = buildInputSource( publicId, systemId, dtdStream, true );
					if (source != null) return source;
				}
				else if ( systemId.endsWith( "persistence_2_0.xsd" ) ) {
					InputStream dtdStream = getStreamFromClasspath( "persistence_2_0.xsd" );
					final InputSource source = buildInputSource( publicId, systemId, dtdStream, true );
					if (source != null) return source;
				}
			}
		}
		else {
			resolved = true;
			return is;
		}
		//use the default behavior
		return null;
	}

	private InputSource buildInputSource(String publicId, String systemId, InputStream dtdStream, boolean resolved) {
		if ( dtdStream == null ) {
			LOG.tracev( "Unable to locate [{0}] on classpath", systemId );
			return null;
		}
		LOG.tracev( "Located [{0}] in classpath", systemId );
		InputSource source = new InputSource( dtdStream );
		source.setPublicId( publicId );
		source.setSystemId( systemId );
		this.resolved = resolved;
		return source;
	}

	private InputStream getStreamFromClasspath(String fileName) {
		LOG.trace( "Recognized JPA ORM namespace; attempting to resolve on classpath under org/hibernate/ejb" );
		String path = "org/hibernate/ejb/" + fileName;
		InputStream dtdStream = resolveInHibernateNamespace( path );
		return dtdStream;
	}
}
