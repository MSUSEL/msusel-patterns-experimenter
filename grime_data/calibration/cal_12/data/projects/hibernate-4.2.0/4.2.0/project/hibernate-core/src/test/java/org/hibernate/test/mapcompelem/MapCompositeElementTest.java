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
package org.hibernate.test.mapcompelem;

import java.util.List;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class MapCompositeElementTest extends BaseCoreFunctionalTestCase {
	public String[] getMappings() {
		return new String[] { "mapcompelem/ProductPart.hbm.xml" };
	}

	@SuppressWarnings( {"unchecked"})
	@Test
	public void testMapCompositeElementWithFormula() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Part top = new Part("top", "The top part");
		Part bottom = new Part("bottom", "The bottom part");
		Product prod = new Product("Some Thing");
		prod.getParts().put("Top", top);
		prod.getParts().put("Bottom", bottom);
		s.persist(prod);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		prod = (Product) s.get(Product.class, "Some Thing");
		assertEquals( prod.getParts().size(), 2 );
		prod.getParts().remove("Bottom");
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		prod = (Product) s.get(Product.class, "Some Thing");
		assertEquals( prod.getParts().size(), 1 );
		prod.getParts().put("Top", new Part("top", "The brand new top part"));
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		prod = (Product) s.get(Product.class, "Some Thing");
		assertEquals( prod.getParts().size(), 1 );
		assertEquals(  ( (Part) prod.getParts().get("Top") ).getDescription(), "The brand new top part");
		s.delete(prod);
		t.commit();
		s.close();
	}

	@SuppressWarnings( {"unchecked"})
	@Test
	public void testQueryMapCompositeElement() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		Part top = new Part("top", "The top part");
		Part bottom = new Part("bottom", "The bottom part");
		Product prod = new Product("Some Thing");
		prod.getParts().put("Top", top);
		prod.getParts().put("Bottom", bottom);
		s.persist(prod);
		
		Item item = new Item("123456", prod);
		s.persist(item);

		List list = s.createQuery("select new Part( part.name, part.description ) from Product prod join prod.parts part order by part.name desc").list();
		assertEquals( list.size(), 2 );
		assertTrue( list.get(0) instanceof Part );
		assertTrue( list.get(1) instanceof Part );
		Part part = (Part) list.get(0);
		assertEquals( part.getName(), "top" );
		assertEquals( part.getDescription(), "The top part" );
		
		list = s.createQuery("select new Part( part.name, part.description ) from Product prod join prod.parts part where index(part) = 'Top'").list();
		assertEquals( list.size(), 1 );
		assertTrue( list.get(0) instanceof Part );
		part = (Part) list.get(0);
		assertEquals( part.getName(), "top" );
		assertEquals( part.getDescription(), "The top part" );
		
		list = s.createQuery("from Product p where 'Top' in indices(p.parts)").list();
		assertEquals( list.size(), 1 );
		assertSame( list.get(0), prod );
		
		list = s.createQuery("select i from Item i join i.product p where 'Top' in indices(p.parts)").list();
		assertEquals( list.size(), 1 );
		assertSame( list.get(0), item );
		
		list = s.createQuery("from Item i where 'Top' in indices(i.product.parts)").list();
		assertEquals( list.size(), 1 );
		assertSame( list.get(0), item );
		
		s.delete(item);
		s.delete(prod);
		t.commit();
		s.close();
	}

}

