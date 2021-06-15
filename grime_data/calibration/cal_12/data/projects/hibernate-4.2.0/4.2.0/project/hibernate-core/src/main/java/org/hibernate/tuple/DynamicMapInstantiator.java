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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.binding.EntityBinding;


public class DynamicMapInstantiator implements Instantiator {
	public static final String KEY = "$type$";

	private String entityName;
	private Set isInstanceEntityNames = new HashSet();

	public DynamicMapInstantiator() {
		this.entityName = null;
	}

	public DynamicMapInstantiator(PersistentClass mappingInfo) {
		this.entityName = mappingInfo.getEntityName();
		isInstanceEntityNames.add( entityName );
		if ( mappingInfo.hasSubclasses() ) {
			Iterator itr = mappingInfo.getSubclassClosureIterator();
			while ( itr.hasNext() ) {
				final PersistentClass subclassInfo = ( PersistentClass ) itr.next();
				isInstanceEntityNames.add( subclassInfo.getEntityName() );
			}
		}
	}

	public DynamicMapInstantiator(EntityBinding mappingInfo) {
		this.entityName = mappingInfo.getEntity().getName();
		isInstanceEntityNames.add( entityName );
		for ( EntityBinding subEntityBinding : mappingInfo.getPostOrderSubEntityBindingClosure() ) {
			isInstanceEntityNames.add( subEntityBinding.getEntity().getName() );
		}
	}

	public final Object instantiate(Serializable id) {
		return instantiate();
	}

	public final Object instantiate() {
		Map map = generateMap();
		if ( entityName!=null ) {
			map.put( KEY, entityName );
		}
		return map;
	}

	public final boolean isInstance(Object object) {
		if ( object instanceof Map ) {
			if ( entityName == null ) {
				return true;
			}
			String type = ( String ) ( ( Map ) object ).get( KEY );
			return type == null || isInstanceEntityNames.contains( type );
		}
		else {
			return false;
		}
	}

	protected Map generateMap() {
		return new HashMap();
	}
}