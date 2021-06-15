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
package org.hibernate.metamodel.source;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.metamodel.binding.MetaAttribute;

/**
 * @author Steve Ebersole
 */
public class MetaAttributeContext {
	private final MetaAttributeContext parentContext;
	private final ConcurrentHashMap<String, MetaAttribute> metaAttributeMap = new ConcurrentHashMap<String, MetaAttribute>();

	public MetaAttributeContext() {
		this( null );
	}

	public MetaAttributeContext(MetaAttributeContext parentContext) {
		this.parentContext = parentContext;
	}


	// read contract ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Iterable<String> getKeys() {
		HashSet<String> keys = new HashSet<String>();
		addKeys( keys );
		return keys;
	}

	private void addKeys(Set<String> keys) {
		keys.addAll( metaAttributeMap.keySet() );
		if ( parentContext != null ) {
			// recursive call
			parentContext.addKeys( keys );
		}
	}

	public Iterable<String> getLocalKeys() {
		return metaAttributeMap.keySet();
	}

	public MetaAttribute getMetaAttribute(String key) {
		MetaAttribute value = getLocalMetaAttribute( key );
		if ( value == null ) {
			// recursive call
			value = parentContext.getMetaAttribute( key );
		}
		return value;
	}

	public MetaAttribute getLocalMetaAttribute(String key) {
		return metaAttributeMap.get( key );
	}


	// write contract ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void add(MetaAttribute metaAttribute) {
		metaAttributeMap.put( metaAttribute.getName(), metaAttribute );
	}

}
