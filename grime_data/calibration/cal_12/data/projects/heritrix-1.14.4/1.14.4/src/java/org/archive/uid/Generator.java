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
 * A <code>record-id</code> generator.
 * {@link GeneratorFactory} assumes implementations have a no-arg Constructor.
 * @see GeneratorFactory
 * @author stack
 * @version $Revision: 4495 $ $Date: 2006-08-15 00:25:03 +0000 (Tue, 15 Aug 2006) $
 */
public interface Generator {
	/**
	 * @return A URI that can serve as a record-id.
	 * @throws URISyntaxException
	 */
	public URI getRecordID() throws URISyntaxException;
	
	/**
	 * @param qualifiers Qualifiers to add.
	 * @return A URI qualified with passed <code>qualifiers</code> that can
	 * serve as a record-id, or, a new, unique record-id without qualifiers
	 * (if qualifiers not easily implemented using passed URI scheme).
	 * @throws URISyntaxException
	 */
	public URI getQualifiedRecordID(final Map<String, String> qualifiers)
	throws URISyntaxException;
	
	/**
	 * @param key Name of qualifier
	 * @param value Value of qualifier
	 * @return A URI qualified with passed <code>qualifiers</code> that can
	 * serve as a record-id, or, a new, unique record-id without qualifiers
	 * (if qualifiers not easily implemented using passed URI scheme).
	 * @throws URISyntaxException
	 */
	public URI getQualifiedRecordID(final String key, final String value)
	throws URISyntaxException;
	
	/**
	 * Append (or if already present, update) qualifiers to passed
	 * <code>recordId</code>.  Use with caution. Guard against turning up a
	 * result that already exists.  Use when writing a group of records inside
	 * a single transaction. 
	 * 
	 * How qualifiers are appended/updated varies with URI scheme. Its allowed
	 * that an invocation of this method does nought but call
	 * {@link #getRecordID()}, returning a new URI unrelated to the passed
	 * recordId and passed qualifier.  
	 * @param recordId URI to append qualifier to.
	 * @param qualifiers Map of qualifier values keyed by qualifier name.
	 * @return New URI based off passed <code>uri</code> and passed qualifier.
	 * @throws URISyntaxException if probably constructing URI OR if the
	 * resultant UUID does not differ from the one passed.
	 */
	public URI qualifyRecordID(final URI recordId,
	    final Map<String, String>  qualifiers)
	throws URISyntaxException;
}
