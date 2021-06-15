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
package org.hibernate.metamodel.source.annotations.util;

import java.util.Iterator;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.junit.Ignore;
import org.junit.Test;

import org.hibernate.AssertionFailure;
import org.hibernate.metamodel.source.annotations.entity.EmbeddableClass;
import org.hibernate.metamodel.source.annotations.entity.EmbeddableHierarchy;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

/**
 * @author Hardy Ferentschik
 */
@Ignore("fails on openjdk")
public class EmbeddableHierarchyTest extends BaseAnnotationIndexTestCase {
	@Test
    @Ignore("HHH-6606 ignore for now")
	public void testEmbeddableHierarchy() {
		@Embeddable
		class A {
			String foo;
		}

		class B extends A {
		}

		@Embeddable
		class C extends B {
			String bar;
		}

		EmbeddableHierarchy hierarchy = createEmbeddableHierarchy(
				AccessType.FIELD,
				C.class,
				A.class,
				B.class
		);
		Iterator<EmbeddableClass> iter = hierarchy.iterator();
		ClassInfo info = iter.next().getClassInfo();
		assertEquals( "wrong class", DotName.createSimple( A.class.getName() ), info.name() );
		info = iter.next().getClassInfo();
		assertEquals( "wrong class", DotName.createSimple( B.class.getName() ), info.name() );
		info = iter.next().getClassInfo();
		assertEquals( "wrong class", DotName.createSimple( C.class.getName() ), info.name() );
		assertFalse( iter.hasNext() );
		assertNotNull( hierarchy );
	}

	@Test(expected = AssertionFailure.class)
	public void testEmbeddableHierarchyWithNotAnnotatedEntity() {
		class NonAnnotatedEmbeddable {
		}

		createEmbeddableHierarchy( AccessType.FIELD, NonAnnotatedEmbeddable.class );
	}

	@Entity
	public class Foo {
		@Id
		@GeneratedValue
		private int id;
	}

	@Entity
	public class A {
		@Id
		@GeneratedValue
		private int id;
	}

	@Entity
	public class B extends A {
		private String name;
	}
}


