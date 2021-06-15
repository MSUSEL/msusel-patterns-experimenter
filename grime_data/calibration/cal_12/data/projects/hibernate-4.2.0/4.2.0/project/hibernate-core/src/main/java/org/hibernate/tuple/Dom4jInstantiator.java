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
package org.hibernate.tuple;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import org.dom4j.Element;

import org.hibernate.internal.util.xml.XMLHelper;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;

/**
 * Performs "instantiation" based on DOM4J elements.
 */
public class Dom4jInstantiator implements Instantiator {
	private final String nodeName;
	private final HashSet isInstanceNodeNames = new HashSet();

	public Dom4jInstantiator(Component component) {
		this.nodeName = component.getNodeName();
		isInstanceNodeNames.add( nodeName );
	}

	public Dom4jInstantiator(PersistentClass mappingInfo) {
		this.nodeName = mappingInfo.getNodeName();
		isInstanceNodeNames.add( nodeName );

		if ( mappingInfo.hasSubclasses() ) {
			Iterator itr = mappingInfo.getSubclassClosureIterator();
			while ( itr.hasNext() ) {
				final PersistentClass subclassInfo = ( PersistentClass ) itr.next();
				isInstanceNodeNames.add( subclassInfo.getNodeName() );
			}
		}
	}
	
	public Object instantiate(Serializable id) {
		return instantiate();
	}
	
	public Object instantiate() {
		return XMLHelper.generateDom4jElement( nodeName );
	}

	public boolean isInstance(Object object) {
		if ( object instanceof Element ) {
			return isInstanceNodeNames.contains( ( ( Element ) object ).getName() );
		}
		else {
			return false;
		}
	}
}