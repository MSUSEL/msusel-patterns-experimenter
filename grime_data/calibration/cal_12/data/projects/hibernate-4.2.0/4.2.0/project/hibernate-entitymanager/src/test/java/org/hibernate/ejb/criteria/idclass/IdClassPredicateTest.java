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
package org.hibernate.ejb.criteria.idclass;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.ejb.metamodel.AbstractMetamodelSpecificTest;

/**
 * @author Erich Heard
 */
public class IdClassPredicateTest extends AbstractMetamodelSpecificTest {

	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[] {
				Widget.class,
				Tool.class
		};
	}

	@Before
	public void prepareTestData() {
		EntityManager em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();

		Widget w = new Widget();
		w.setCode( "AAA" );
		w.setDivision( "NA" );
		w.setCost( 10.00 );
		em.persist( w );

		w = new Widget();
		w.setCode( "AAA" );
		w.setDivision( "EU" );
		w.setCost( 12.50 );
		em.persist( w );

		w = new Widget();
		w.setCode( "AAA" );
		w.setDivision( "ASIA" );
		w.setCost( 110.00 );
		em.persist( w );

		w = new Widget();
		w.setCode( "BBB" );
		w.setDivision( "NA" );
		w.setCost( 14.00 );
		em.persist( w );

		w = new Widget();
		w.setCode( "BBB" );
		w.setDivision( "EU" );
		w.setCost( 8.75 );
		em.persist( w );

		w = new Widget();
		w.setCode( "BBB" );
		w.setDivision( "ASIA" );
		w.setCost( 86.22 );
		em.persist( w );

		Tool t = new Tool();
		t.setName( "AAA" );
		t.setType( "NA" );
		t.setCost( 10.00 );
		em.persist( t );

		t = new Tool();
		t.setName( "AAA" );
		t.setType( "EU" );
		t.setCost( 12.50 );
		em.persist( t );

		t = new Tool();
		t.setName( "AAA" );
		t.setType( "ASIA" );
		t.setCost( 110.00 );
		em.persist( t );

		t = new Tool();
		t.setName( "BBB" );
		t.setType( "NA" );
		t.setCost( 14.00 );
		em.persist( t );

		t = new Tool();
		t.setName( "BBB" );
		t.setType( "EU" );
		t.setCost( 8.75 );
		em.persist( t );

		t = new Tool();
		t.setName( "BBB" );
		t.setType( "ASIA" );
		t.setCost( 86.22 );
		em.persist( t );

		em.getTransaction().commit();
		em.close();
	}


	@After
	public void cleanupTestData() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.createQuery( "delete Widget" ).executeUpdate();
		em.createQuery( "delete Tool" ).executeUpdate();
		em.getTransaction().commit();
		em.close();
	}


	@Test
	public void testDeclaredIdClassAttributes( ) {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();

		// Packaging arguments for use in query.
		List<String> divisions = new ArrayList<String>( );
		divisions.add( "NA" );
		divisions.add( "EU" );
			
		// Building the query.
		CriteriaBuilder criteria = em.getCriteriaBuilder( );
		CriteriaQuery<Widget> query = criteria.createQuery( Widget.class );
		Root<Widget> root = query.from( Widget.class );
			
		Predicate predicate = root.get( "division" ).in( divisions );
		query.where( predicate );

		// Retrieving query.;
		List<Widget> widgets = em.createQuery( query ).getResultList( );
		Assert.assertEquals( 4, widgets.size() );

		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testSupertypeIdClassAttributes( ) {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();

		// Packaging arguments for use in query.
		List<String> types = new ArrayList<String>( );
		types.add( "NA" );
		types.add( "EU" );

		// Building the query.
		CriteriaBuilder criteria = em.getCriteriaBuilder( );
		CriteriaQuery<Tool> query = criteria.createQuery( Tool.class );
		Root<Tool> root = query.from( Tool.class );

		Predicate predicate = root.get( "type" ).in( types );
		query.where( predicate );

		// Retrieving query.
		List<Tool> tools = em.createQuery( query ).getResultList( );
		Assert.assertEquals( 4, tools.size() );

		em.getTransaction().commit();
		em.close();
	}
}
