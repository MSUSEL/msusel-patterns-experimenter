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
package org.hibernate.test.mapping;

import org.junit.Test;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Mappings;
import org.hibernate.mapping.Any;
import org.hibernate.mapping.Array;
import org.hibernate.mapping.Bag;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.DependantValue;
import org.hibernate.mapping.IdentifierBag;
import org.hibernate.mapping.List;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.Map;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.OneToOne;
import org.hibernate.mapping.PrimitiveArray;
import org.hibernate.mapping.RootClass;
import org.hibernate.mapping.Set;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.ValueVisitor;
import org.hibernate.testing.junit4.BaseUnitTestCase;

/**
 * @author max
 */
public class ValueVisitorTest extends BaseUnitTestCase {
	@Test
	public void testProperCallbacks() {
		final Mappings mappings = new Configuration().createMappings();
		final Table tbl = new Table();
		final RootClass rootClass = new RootClass();

		ValueVisitor vv = new ValueVisitorValidator();

		new Any( mappings, tbl ).accept(vv);
		new Array( mappings, rootClass ).accept(vv);
		new Bag( mappings, rootClass ).accept(vv);
		new Component( mappings, rootClass ).accept(vv);
		new DependantValue( mappings, tbl, null ).accept(vv);
		new IdentifierBag( mappings, rootClass ).accept(vv);
		new List( mappings, rootClass ).accept(vv);
		new ManyToOne( mappings, tbl ).accept(vv);
		new Map( mappings, rootClass ).accept(vv);
		new OneToMany( mappings, rootClass ).accept(vv);
		new OneToOne( mappings, tbl, rootClass ).accept(vv);
		new PrimitiveArray( mappings, rootClass ).accept(vv);
		new Set( mappings, rootClass ).accept(vv);
		new SimpleValue( mappings ).accept(vv);
	}

	static public class ValueVisitorValidator implements ValueVisitor {

		public Object accept(PrimitiveArray primitiveArray) {
			return validate(PrimitiveArray.class,primitiveArray);
		}

		public Object accept(Bag bag) {
			return validate(Bag.class, bag);
		}

		public Object accept(DependantValue value) {
			return validate(DependantValue.class, value);
		}

		private Object validate(Class expectedClass, Object visitee) {
			if (!visitee.getClass().getName().equals(expectedClass.getName())) {
				throw new IllegalStateException(visitee.getClass().getName()
						+ " did not call proper accept method. Was "
						+ expectedClass.getName());
			}
			return null;
		}

		public Object accept(IdentifierBag bag) {
			return validate(IdentifierBag.class, bag);
		}

		public Object accept(List list) {
			return validate(List.class, list);
		}

		public Object accept(Map map) {
			return validate(Map.class, map);
		}

		public Object accept(Array list) {
			return validate(Array.class, list);
		}

		public Object accept(OneToMany many) {
			return validate(OneToMany.class, many);
		}

		public Object accept(Set set) {
			return validate(Set.class, set);
		}

		public Object accept(Any any) {
			return validate(Any.class, any);
		}

		public Object accept(SimpleValue value) {
			return validate(SimpleValue.class, value);
		}

		public Object accept(Component component) {
			return validate(Component.class, component);
		}

		public Object accept(ManyToOne mto) {
			return validate(ManyToOne.class, mto);
		}

		public Object accept(OneToOne oto) {
			return validate(OneToOne.class, oto);
		}

	}
}
