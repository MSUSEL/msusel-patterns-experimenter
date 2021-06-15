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
package org.hibernate.test.legacy;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import static org.junit.Assert.assertTrue;

public class MapTest extends LegacyTestCase {
	@Override
	public String[] getMappings() {
		return new String[] { "legacy/Map.hbm.xml", "legacy/Commento.hbm.xml", "legacy/Marelo.hbm.xml" };
	}

	@Override
	public void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.DEFAULT_ENTITY_MODE, EntityMode.MAP.toString() );
	}

	@Test
	public void testMap() throws Exception {
		Session s = openSession();
		s.beginTransaction();
		Map map = new HashMap();
		map.put("$type$", "TestMap");
		map.put("name", "foo");
		map.put("address", "bar");
		Map cmp = new HashMap();
		cmp.put( "a", new Integer(1) );
		cmp.put( "b", new Float(1.0) );
		map.put("cmp", cmp);
		s.save(map);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		map = (Map) s.get( "TestMap", (Serializable) map.get("id") );
		assertTrue( map!=null && "foo".equals( map.get("name") ) );
		assertTrue( map.get("$type$").equals("TestMap") );

		int size = s.createCriteria("TestMap").add( Example.create(map) ).list().size();
		assertTrue(size==1);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		List list = s.createQuery("from TestMap").list();
		map = (Map) list.get(0);
		assertTrue( "foo".equals( map.get("name") ) );
		assertTrue( "bar".equals( map.get("address") ) );
		cmp = (Map) map.get("cmp");
		assertTrue( new Integer(1).equals( cmp.get("a") ) && new Float(1.0).equals( cmp.get("b") ) );
		assertTrue( null==map.get("parent") );
		map.put("name", "foobar");
		map.put("parent", map);
		List bag = (List) map.get("children");
		bag.add(map);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		list = s.createQuery("from TestMap tm where tm.address = 'bar'").list();
		map = (Map) list.get(0);
		assertTrue( "foobar".equals( map.get("name") ) );
		assertTrue( "bar".equals( map.get("address") ) );
		assertTrue( map==map.get("parent") );
		bag = (List) map.get("children");
		assertTrue( bag.size()==1 );

		size = s.createCriteria("TestMap")
			.add( Restrictions.eq("address", "bar") )
				.createCriteria("parent")
				.add( Restrictions.eq("name", "foobar") )
			.list()
			.size();
		assertTrue(size==1);

		s.delete(map);
		s.getTransaction().commit();
		s.close();

	}

	@Test
	public void testMapOneToOne() throws Exception {
		Map child = new HashMap();
		Map parent = new HashMap();
		Session s = openSession();
		s.beginTransaction();
		child.put("parent", parent);
		child.put("$type$", "ChildMap");
		parent.put("child", child);
		parent.put("$type$", "ParentMap");
		s.save(parent);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		Map cm = (Map) s.createQuery("from ChildMap cm where cm.parent is not null").uniqueResult();
		s.delete(cm);
		s.delete( cm.get("parent") );
		s.getTransaction().commit();
		s.close();

		child = new HashMap();
		parent = new HashMap();
		s = openSession();
		s.beginTransaction();
		child.put("parent", parent);
		child.put("$type$", "ChildMap");
		parent.put("child", child);
		parent.put("$type$", "ParentMap");
		s.save(child);
		s.getTransaction().commit();
		s.close();

		s = openSession();
		s.beginTransaction();
		Map pm = (Map) s.createQuery("from ParentMap cm where cm.child is not null").uniqueResult();
		s.delete(pm);
		s.delete( pm.get("child") );
		s.getTransaction().commit();
		s.close();

	}

	@Test
	public void testOneToOnePropertyRef() throws Exception {
		Session s = openSession();
		s.beginTransaction();
		s.createQuery("from Commento c where c.marelo.mlmag = 0").list();
		s.createQuery("from Commento c where c.marelo.commento.mcompr is null").list();
		s.createQuery("from Commento c where c.marelo.mlink = 0").list();
		s.createQuery("from Commento c where c.marelo.commento = c").list();
		s.createQuery("from Commento c where c.marelo.id.mlmag = 0").list();
		s.createQuery("from Commento c where c.marelo.commento.id = c.id").list();
		s.createQuery("from Commento c where c.marelo.commento.mclink = c.mclink").list();
		s.createQuery("from Marelo m where m.commento.id > 0").list();
		s.createQuery("from Marelo m where m.commento.marelo.commento.marelo.mlmag is not null").list();
		s.getTransaction().commit();
		s.close();
	}

}

