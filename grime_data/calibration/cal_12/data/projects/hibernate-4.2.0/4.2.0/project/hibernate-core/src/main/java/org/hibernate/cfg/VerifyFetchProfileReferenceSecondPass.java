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
import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.mapping.MetadataSource;
import org.hibernate.mapping.PersistentClass;

/**
 * @author Hardy Ferentschik
 */
public class VerifyFetchProfileReferenceSecondPass implements SecondPass {
	private String fetchProfileName;
	private FetchProfile.FetchOverride fetch;
	private Mappings mappings;

	public VerifyFetchProfileReferenceSecondPass(
			String fetchProfileName,
			FetchProfile.FetchOverride fetch,
			Mappings mappings) {
		this.fetchProfileName = fetchProfileName;
		this.fetch = fetch;
		this.mappings = mappings;
	}

	public void doSecondPass(Map persistentClasses) throws MappingException {
		org.hibernate.mapping.FetchProfile profile = mappings.findOrCreateFetchProfile(
				fetchProfileName,
				MetadataSource.ANNOTATIONS
		);
		if ( MetadataSource.ANNOTATIONS != profile.getSource() ) {
			return;
		}

		PersistentClass clazz = mappings.getClass( fetch.entity().getName() );
		// throws MappingException in case the property does not exist
		clazz.getProperty( fetch.association() );

		profile.addFetch(
				fetch.entity().getName(), fetch.association(), fetch.mode().toString().toLowerCase()
		);
	}
}


