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

/**
 * Raised whenever a duplicate for a certain type occurs.
 * Duplicate class, table, property name etc.
 * 
 * @author Max Rydahl Andersen
 * @author Steve Ebersole
 */
public class DuplicateMappingException extends MappingException {
	public static enum Type {
		ENTITY,
		TABLE,
		PROPERTY,
		COLUMN
	}

	private final String name;
	private final String type;

	public DuplicateMappingException(Type type, String name) {
		this( type.name(), name );
	}

	@Deprecated
	public DuplicateMappingException(String type, String name) {
		this( "Duplicate " + type + " mapping " + name, type, name );
	}

	public DuplicateMappingException(String customMessage, Type type, String name) {
		this( customMessage, type.name(), name );
	}

	@Deprecated
	public DuplicateMappingException(String customMessage, String type, String name) {
		super( customMessage );
		this.type=type;
		this.name=name;
	}

	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
}
