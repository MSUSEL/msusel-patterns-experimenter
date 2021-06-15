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

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.SQLUpdate;
import org.hibernate.engine.spi.ExecuteUpdateResultCheckStyle;
import org.hibernate.metamodel.binding.CustomSQL;
import org.hibernate.metamodel.binding.EntityBinding;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Tests for {@code o.h.a.SQLInsert}, {@code o.h.a.SQLUpdate}, {@code o.h.a.Delete} and {@code o.h.a.SQLDeleteAll}.
 *
 * @author Hardy Ferentschik
 */
public class CustomSQLBindingTest extends BaseAnnotationBindingTestCase {
	@Test
	@Resources(annotatedClasses = NoCustomSQLEntity.class)
	public void testNoCustomSqlAnnotations() {
		EntityBinding binding = getEntityBinding( NoCustomSQLEntity.class );
		assertNull( binding.getCustomDelete() );
		assertNull( binding.getCustomInsert() );
		assertNull( binding.getCustomUpdate() );
	}

	@Test
	@Resources(annotatedClasses = CustomSQLEntity.class)
	public void testCustomSqlAnnotations() {
		EntityBinding binding = getEntityBinding( CustomSQLEntity.class );

		CustomSQL customSql = binding.getCustomInsert();
		assertCustomSql( customSql, "INSERT INTO FOO", true, ExecuteUpdateResultCheckStyle.NONE );

		customSql = binding.getCustomDelete();
		assertCustomSql( customSql, "DELETE FROM FOO", false, ExecuteUpdateResultCheckStyle.COUNT );

		customSql = binding.getCustomUpdate();
		assertCustomSql( customSql, "UPDATE FOO", false, ExecuteUpdateResultCheckStyle.PARAM );
	}

// not so sure about the validity of this one
//	@Test
//	public void testDeleteAllWins() {
//		buildMetadataSources( CustomDeleteAllEntity.class );
//		EntityBinding binding = getEntityBinding( CustomDeleteAllEntity.class );
//		assertEquals( "Wrong sql", "DELETE ALL", binding.getCustomDelete().getSql() );
//	}

	private void assertCustomSql(CustomSQL customSql, String sql, boolean isCallable, ExecuteUpdateResultCheckStyle style) {
		assertNotNull( customSql );
		assertEquals( "Wrong sql", sql, customSql.getSql() );
		assertEquals( isCallable, customSql.isCallable() );
		assertEquals( style, customSql.getCheckStyle() );
	}

	@Entity
	class NoCustomSQLEntity {
		@Id
		private int id;
	}

	@Entity
	@SQLInsert(sql = "INSERT INTO FOO", callable = true)
	@SQLDelete(sql = "DELETE FROM FOO", check = ResultCheckStyle.COUNT)
	@SQLUpdate(sql = "UPDATE FOO", check = ResultCheckStyle.PARAM)
	class CustomSQLEntity {
		@Id
		private int id;
	}

	@Entity
	@SQLDelete(sql = "DELETE")
	@SQLDeleteAll(sql = "DELETE ALL")
	class CustomDeleteAllEntity {
		@Id
		private int id;
	}
}


