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

import org.hibernate.annotations.RowId;
import org.hibernate.metamodel.binding.EntityBinding;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for {@code o.h.a.RowId}.
 *
 * @author Hardy Ferentschik
 */
public class RowIdBindingTests extends BaseAnnotationBindingTestCase {
	@Test
	@Resources(annotatedClasses = NoRowIdEntity.class)
	public void testNoRowId() {
		EntityBinding binding = getEntityBinding( NoRowIdEntity.class );
		assertEquals( "Wrong row id", null, binding.getRowId() );
	}

	@Test
	@Resources(annotatedClasses = RowIdEntity.class)
	public void testRowId() {
		EntityBinding binding = getEntityBinding( RowIdEntity.class );
		assertEquals( "Wrong row id", "rowid", binding.getRowId() );
	}

	@Entity
	class NoRowIdEntity {
		@Id
		private int id;
	}

	@Entity
	@RowId("rowid")
	class RowIdEntity {
		@Id
		private int id;
	}
}


