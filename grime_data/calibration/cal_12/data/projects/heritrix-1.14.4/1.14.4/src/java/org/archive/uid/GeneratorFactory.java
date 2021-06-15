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
package org.archive.uid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Factory that generates uids.
 * Singleton.  Default implementation is {@link UUIDGenerator}. To
 * change, specify alternate implementation of {@link Generator} with
 * {@link #SYSTEM_PROPERTY_GENERATOR_KEY} system property.
 * @author stack
 * @version $Revision: 4417 $ $Date: 2006-08-02 01:12:00 +0000 (Wed, 02 Aug 2006) $
 */
public class GeneratorFactory implements Generator {
	public final String SYSTEM_PROPERTY_GENERATOR_KEY =
		this.getClass().toString() + ".generator";
	private static final String DEFAULT_GENERATOR =
		"org.archive.uid.UUIDGenerator";
	private static final GeneratorFactory factory = new GeneratorFactory();
	private final Generator generator;
	
	private GeneratorFactory() {
		super();
		String className = System.getProperty(SYSTEM_PROPERTY_GENERATOR_KEY,
			DEFAULT_GENERATOR);
		Generator ridg = null;
		try {
			Class c = Class.forName(className);
			ridg = (Generator) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.generator = ridg;
	}
	
	public URI getRecordID() throws URISyntaxException {
		return this.generator.getRecordID();
	}
	
	public URI getQualifiedRecordID(Map<String, String> qualifiers)
	throws URISyntaxException {
		return this.generator.getQualifiedRecordID(qualifiers);
	}
	
	public URI getQualifiedRecordID(String key, String value)
	throws URISyntaxException {
		return this.generator.getQualifiedRecordID(key, value);
	}
	
	public URI qualifyRecordID(final URI uri,
			final Map<String, String> qualifiers)
	throws URISyntaxException {
		return this.generator.qualifyRecordID(uri, qualifiers);
	}

	public static GeneratorFactory getFactory() {
		return factory;
	}
}
