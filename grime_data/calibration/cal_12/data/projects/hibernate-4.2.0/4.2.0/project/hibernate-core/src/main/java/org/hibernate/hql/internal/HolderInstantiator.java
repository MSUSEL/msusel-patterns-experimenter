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
package org.hibernate.hql.internal;
import java.lang.reflect.Constructor;

import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

/**
 * @author Gavin King
 */
public final class HolderInstantiator {
		
	public static final HolderInstantiator NOOP_INSTANTIATOR = new HolderInstantiator(null,null);
	
	private final ResultTransformer transformer;
	private final String[] queryReturnAliases;
	
	public static HolderInstantiator getHolderInstantiator(ResultTransformer selectNewTransformer, ResultTransformer customTransformer, String[] queryReturnAliases) {
		return new HolderInstantiator(
				resolveResultTransformer( selectNewTransformer, customTransformer ),
				queryReturnAliases
		);
	}

	public static ResultTransformer resolveResultTransformer(ResultTransformer selectNewTransformer, ResultTransformer customTransformer) {
		return selectNewTransformer != null ? selectNewTransformer : customTransformer;
	}	

	public static ResultTransformer createSelectNewTransformer(Constructor constructor, boolean returnMaps, boolean returnLists) {
		if ( constructor != null ) {
			return new AliasToBeanConstructorResultTransformer(constructor);
		}
		else if ( returnMaps ) {
			return Transformers.ALIAS_TO_ENTITY_MAP;			
		}
		else if ( returnLists ) {
			return Transformers.TO_LIST;
		}		
		else {
			return null;
		}
	}
	
	static public HolderInstantiator createClassicHolderInstantiator(Constructor constructor, 
			ResultTransformer transformer) {
		return new HolderInstantiator( resolveClassicResultTransformer( constructor, transformer ), null );
	}

	static public ResultTransformer resolveClassicResultTransformer(
			Constructor constructor,
			ResultTransformer transformer) {
		return constructor != null ? new AliasToBeanConstructorResultTransformer( constructor ) : transformer;
	}	

	public HolderInstantiator( 
			ResultTransformer transformer,
			String[] queryReturnAliases
	) {
		this.transformer = transformer;		
		this.queryReturnAliases = queryReturnAliases;
	}
	
	public boolean isRequired() {
		return transformer!=null;
	}
	
	public Object instantiate(Object[] row) {
		if(transformer==null) {
			return row;
		} else {
			return transformer.transformTuple(row, queryReturnAliases);
		}
	}	
	
	public String[] getQueryReturnAliases() {
		return queryReturnAliases;
	}

	public ResultTransformer getResultTransformer() {
		return transformer;
	}

}
