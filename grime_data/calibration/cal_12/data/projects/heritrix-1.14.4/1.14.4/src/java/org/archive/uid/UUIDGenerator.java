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
import java.util.UUID;

/**
 * Generates <a href="http://en.wikipedia.org/wiki/UUID">UUID</a>s, using
 * {@link java.util.UUID java.util.UUID}, formatted as URNs from the UUID
 * namespace [See <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC4122</a>].
 * Here is an examples of the type of ID it makes: 
 * <code>urn:uuid:0161811f-5da6-4c6e-9808-a2fab97114cf</code>. Always makes a
 * new identifier even when passed qualifiers.
 *
 * @author stack
 * @version $Revision: 4607 $ $Date: 2006-09-07 20:23:51 +0000 (Thu, 07 Sep 2006) $
 * @see <a href="http://ietf.org/rfc/rfc4122.txt">RFC4122</a>
 */
class UUIDGenerator implements Generator {
	private static final String SCHEME = "urn:uuid";
	private static final String SCHEME_COLON = SCHEME + ":";
	
	UUIDGenerator() {
		super();
	}

	public synchronized URI qualifyRecordID(URI recordId,
			final Map<String, String> qualifiers)
	throws URISyntaxException {
		return getRecordID();
	}

	private String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	public URI getRecordID() throws URISyntaxException {
		return new URI(SCHEME_COLON + getUUID());
	}
	
	public URI getQualifiedRecordID(
			final String key, final String value)
	throws URISyntaxException {
		return getRecordID();
	}

	public URI getQualifiedRecordID(Map<String, String> qualifiers)
	throws URISyntaxException {
		return getRecordID();
	}
}