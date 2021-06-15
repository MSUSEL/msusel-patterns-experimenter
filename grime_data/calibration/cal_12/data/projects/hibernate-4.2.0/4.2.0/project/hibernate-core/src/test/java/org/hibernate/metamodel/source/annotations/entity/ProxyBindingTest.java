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
package org.hibernate.metamodel.source.annotations.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Test;

import org.hibernate.annotations.Proxy;
import org.hibernate.metamodel.binding.EntityBinding;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for {@code o.h.a.Cache}.
 *
 * @author Hardy Ferentschik
 */
public class ProxyBindingTest extends BaseAnnotationBindingTestCase {
	@Test
	@Resources(annotatedClasses = ProxiedEntity.class)
	public void testProxyNoAttributes() {
		EntityBinding binding = getEntityBinding( ProxiedEntity.class );
		assertTrue( "Wrong laziness", binding.isLazy() );
		assertEquals( "Wrong proxy interface", ProxiedEntity.class, binding.getProxyInterfaceType().getValue() );
	}

	@Test
	@Resources(annotatedClasses = NoProxyEntity.class)
	public void testNoProxy() {
		EntityBinding binding = getEntityBinding( NoProxyEntity.class );
		assertTrue( "Wrong laziness", binding.isLazy() );
		assertEquals( "Wrong proxy interface", NoProxyEntity.class, binding.getProxyInterfaceType().getValue() );
	}

	@Test
	@Resources(annotatedClasses = ProxyDisabledEntity.class)
	public void testProxyDisabled() {
		EntityBinding binding = getEntityBinding( ProxyDisabledEntity.class );
		assertFalse( "Wrong laziness", binding.isLazy() );
		assertEquals( "Wrong proxy interface", null, binding.getProxyInterfaceType() );
	}

	@Test
	@Resources(annotatedClasses = ProxyInterfaceEntity.class)
	public void testProxyInterface() {
		EntityBinding binding = getEntityBinding( ProxyInterfaceEntity.class );
		assertTrue( "Wrong laziness", binding.isLazy() );
		assertEquals(
				"Wrong proxy interface",
				"org.hibernate.metamodel.source.annotations.entity.ProxyBindingTest$ProxyInterfaceEntity",
				binding.getProxyInterfaceType().getValue().getName()
		);
	}

	@Entity
	class NoProxyEntity {
		@Id
		private int id;
	}

	@Entity
	@Proxy
	class ProxiedEntity {
		@Id
		private int id;
	}

	@Entity
	@Proxy(lazy = false)
	class ProxyDisabledEntity {
		@Id
		private int id;
	}

	@Entity
	@Proxy(proxyClass = ProxyInterfaceEntity.class)
	class ProxyInterfaceEntity {
		@Id
		private int id;
	}
}


