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
package org.hibernate.ejb.test.util;

import javax.persistence.spi.LoadState;

import org.junit.Test;

import org.hibernate.ejb.util.PersistenceUtilHelper;

import static org.junit.Assert.assertEquals;
/**
 * Tests for HHH-5094 and HHH-5334
 *
 * @author Hardy Ferentschik
 */
public class PersistenceUtilHelperTest{
	private final PersistenceUtilHelper.MetadataCache cache = new PersistenceUtilHelper.MetadataCache();

	public static class FieldAccessBean extends FieldAccessBeanBase {
		protected String protectedAccessProperty;
		private String privateAccessProperty;
	}

	public static class FieldAccessBeanBase {
		public String publicAccessProperty;
	}

	public static class MethodAccessBean extends MethodAccessBeanBase {
		private String protectedAccessProperty;
		private String privateAccessProperty;

		protected String getProtectedAccessPropertyValue() {
			return protectedAccessProperty;
		}

		private String getPrivateAccessPropertyValue() {
			return privateAccessProperty;
		}
	}

	public static class MethodAccessBeanBase {
		private String publicAccessProperty;

		public String getPublicAccessPropertyValue() {
			return publicAccessProperty;
		}
	}
    @Test
	public void testIsLoadedWithReferencePublicField() {
		assertEquals(
				LoadState.UNKNOWN,
				PersistenceUtilHelper.isLoadedWithReference( new FieldAccessBean(), "publicAccessProperty", cache )
		);
	}
    @Test
	public void testIsLoadedWithReferencePublicMethod() {
		assertEquals(
				LoadState.UNKNOWN,
				PersistenceUtilHelper.isLoadedWithReference(
						new MethodAccessBean(), "publicAccessPropertyValue", cache
				)
		);
	}
   @Test
	public void testIsLoadedWithReferenceProtectedField() {
		assertEquals(
				LoadState.UNKNOWN,
				PersistenceUtilHelper.isLoadedWithReference( new FieldAccessBean(), "protectedAccessProperty", cache )
		);
	}
    @Test
	public void testIsLoadedWithReferenceProtectedMethod() {
		assertEquals(
				LoadState.UNKNOWN,
				PersistenceUtilHelper.isLoadedWithReference(
						new MethodAccessBean(), "protectedAccessPropertyValue", cache
				)
		);
	}
    @Test
	public void testIsLoadedWithReferencePrivateField() {
		assertEquals(
				LoadState.UNKNOWN,
				PersistenceUtilHelper.isLoadedWithReference( new FieldAccessBean(), "privateAccessProperty", cache )
		);
	}
    @Test
	public void testIsLoadedWithReferencePrivateMethod() {
		assertEquals(
				LoadState.UNKNOWN,
				PersistenceUtilHelper.isLoadedWithReference(
						new MethodAccessBean(), "privateAccessPropertyValue", cache
				)
		);
	}
}
