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
//$Id: ComponentNotNullMaster.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.util.List;

/**
 * Entity containing components for not-null testing
 * 
 * @author Emmanuel Bernard
 */
public class ComponentNotNullMaster {
	
	private int id;
	private String test;
	private ComponentNotNull nullable;
	private ComponentNotNull supercomp;
	private List components;
	private List componentsImplicit;

	public int getId() {
		return id;
	}

	public ComponentNotNull getNullable() {
		return nullable;
	}

	public void setId(int i) {
		id = i;
	}

	public void setNullable(ComponentNotNull component) {
		nullable = component;
	}

	public static final class ContainerInnerClass {
		private Simple simple;
		private String name;
		private One one;
		private Many many;
		private int count;
		private ContainerInnerClass nested;
		private String nestedproperty;
		
		public void setSimple(Simple simple) {
			this.simple = simple;
		}
		
		public Simple getSimple() {
			return simple;
		}

		public String getName() {
			return name;
		}
		

		public void setName(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name +  " = " + simple.getCount() +
			 "/"  + ( one==null ? "nil" : one.getKey().toString() ) +
			 "/"  + ( many==null ? "nil" : many.getKey().toString() );
		}
		
		public One getOne() {
			return one;
		}
		
		public void setOne(One one) {
			this.one = one;
		}
		
		public Many getMany() {
			return many;
		}

		public void setMany(Many many) {
			this.many = many;
		}
		
		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public ContainerInnerClass getNested() {
			return nested;
		}

		public void setNested(ContainerInnerClass class1) {
			nested = class1;
		}

		public String getNestedproperty() {
			return nestedproperty;
		}

		public void setNestedproperty(String string) {
			nestedproperty = string;
		}

	}

	public List getComponents() {
		return components;
	}

	public void setComponents(List list) {
		components = list;
	}

	public List getComponentsImplicit() {
		return componentsImplicit;
	}

	public void setComponentsImplicit(List list) {
		componentsImplicit = list;
	}

	public ComponentNotNull getSupercomp() {
		return supercomp;
	}

	public void setSupercomp(ComponentNotNull component) {
		supercomp = component;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String string) {
		test = string;
	}

}
