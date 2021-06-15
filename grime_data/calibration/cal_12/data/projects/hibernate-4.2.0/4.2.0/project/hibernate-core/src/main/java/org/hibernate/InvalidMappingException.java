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
package org.hibernate;

import org.hibernate.internal.jaxb.Origin;
import org.hibernate.internal.util.xml.XmlDocument;

/**
 * Thrown when a mapping is found to be invalid.
 * Similar to MappingException, but this contains more info about the path and type of mapping (e.g. file, resource or url)
 * 
 * @author Max Rydahl Andersen
 * @author Steve Ebersole
 */
public class InvalidMappingException extends MappingException {
	private final String path;
	private final String type;

	public InvalidMappingException(String customMessage, String type, String path, Throwable cause) {
		super(customMessage, cause);
		this.type=type;
		this.path=path;
	}

	public InvalidMappingException(String customMessage, String type, String path) {
		super(customMessage);
		this.type=type;
		this.path=path;
	}

	public InvalidMappingException(String customMessage, XmlDocument xmlDocument, Throwable cause) {
		this( customMessage, xmlDocument.getOrigin().getType(), xmlDocument.getOrigin().getName(), cause );
	}

	public InvalidMappingException(String customMessage, XmlDocument xmlDocument) {
		this( customMessage, xmlDocument.getOrigin().getType(), xmlDocument.getOrigin().getName() );
	}

	public InvalidMappingException(String customMessage, Origin origin) {
		this( customMessage, origin.getType().toString(), origin.getName() );
	}

	public InvalidMappingException(String type, String path) {
		this("Could not parse mapping document from " + type + (path==null?"":" " + path), type, path);
	}

	public InvalidMappingException(String type, String path, Throwable cause) {
		this("Could not parse mapping document from " + type + (path==null?"":" " + path), type, path, cause);		
	}

	public String getType() {
		return type;
	}
	
	public String getPath() {
		return path;
	}
}
