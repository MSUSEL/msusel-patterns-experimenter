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
package org.hibernate.metamodel.binding;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.type.Type;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class HibernateTypeDescriptor {
	private String explicitTypeName;
	private String javaTypeName;
	private boolean isToOne;
	private Map<String, String> typeParameters = new HashMap<String, String>(  );

	private Type resolvedTypeMapping;

	public String getExplicitTypeName() {
		return explicitTypeName;
	}

	public void setExplicitTypeName(String explicitTypeName) {
		this.explicitTypeName = explicitTypeName;
	}

	public String getJavaTypeName() {
		return javaTypeName;
	}

	public void setJavaTypeName(String javaTypeName) {
		this.javaTypeName = javaTypeName;
	}

	public boolean isToOne() {
		return isToOne;
	}

	public void setToOne(boolean toOne) {
		isToOne = toOne;
	}

	public Map<String, String> getTypeParameters() {
		return typeParameters;
	}

	public void setTypeParameters(Map<String, String> typeParameters) {
		this.typeParameters = typeParameters;
	}

	public Type getResolvedTypeMapping() {
		return resolvedTypeMapping;
	}

	public void setResolvedTypeMapping(Type resolvedTypeMapping) {
		this.resolvedTypeMapping = resolvedTypeMapping;
	}
}
