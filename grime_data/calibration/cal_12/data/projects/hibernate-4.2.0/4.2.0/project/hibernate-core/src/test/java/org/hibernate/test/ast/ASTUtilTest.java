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
package org.hibernate.test.ast;

import antlr.ASTFactory;
import antlr.collections.AST;
import org.junit.Test;

import org.hibernate.hql.internal.ast.util.ASTUtil;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit test for ASTUtil.
 */
public class ASTUtilTest extends BaseUnitTestCase {
	private ASTFactory factory = new ASTFactory();

	@Test
	public void testCreate() throws Exception {
		AST n = ASTUtil.create( factory, 1, "one");
		assertNull( n.getFirstChild() );
		assertEquals( "one", n.getText() );
		assertEquals( 1, n.getType() );
	}

	@Test
	public void testCreateTree() throws Exception {
		AST[] tree = new AST[4];
		AST grandparent = tree[0] = ASTUtil.create(factory, 1, "grandparent");
		AST parent = tree[1] = ASTUtil.create(factory,2,"parent");
		AST child = tree[2] = ASTUtil.create(factory,3,"child");
		AST baby = tree[3] = ASTUtil.create(factory,4,"baby");
		AST t = ASTUtil.createTree( factory, tree);
		assertSame(t,grandparent);
		assertSame(parent,t.getFirstChild());
		assertSame(child,t.getFirstChild().getFirstChild());
		assertSame( baby, t.getFirstChild().getFirstChild().getFirstChild() );
	}

	@Test
	public void testFindPreviousSibling() throws Exception {
		AST child1 = ASTUtil.create(factory,2, "child1");
		AST child2 = ASTUtil.create(factory,3, "child2");
		AST n = factory.make( new AST[] {
			ASTUtil.create(factory, 1, "parent"),
			child1,
			child2,
		});
		assertSame(child1,ASTUtil.findPreviousSibling( n,child2));
		Exception e = null;
		try {
			ASTUtil.findPreviousSibling(child1,null);
		}
		catch (Exception x) {
			e = x;
		}
		assertNotNull(e);
	}
}
