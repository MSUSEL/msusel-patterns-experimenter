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
package org.hibernate.ejb.criteria.basic;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;

import org.hibernate.ejb.metamodel.AbstractMetamodelSpecificTest;
import org.hibernate.ejb.metamodel.Customer_;
import org.hibernate.ejb.metamodel.Order;
import org.hibernate.ejb.metamodel.Order_;
import org.hibernate.testing.TestForIssue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the various predicates.
 *
 * @author Steve Ebersole
 * @author Hardy Ferentschik
 */
public class PredicateTest extends AbstractMetamodelSpecificTest {
	private CriteriaBuilder builder;

	@Before
	public void prepareTestData() {
		builder = entityManagerFactory().getCriteriaBuilder();

		EntityManager em = entityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist( new Order( "order-1", 1.0d ) );
		em.persist( new Order( "order-2", 10.0d ) );
		em.persist( new Order( "order-3", new char[]{'r','u'} ) );
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testEmptyConjunction() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		// yes this is a retarded case, but explicitly allowed in the JPA spec
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );
		orderCriteria.select( orderRoot );
		orderCriteria.where( builder.isTrue( builder.conjunction() ) );
		em.createQuery( orderCriteria ).getResultList();

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 3 );
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testEmptyDisjunction() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		// yes this is a retarded case, but explicitly allowed in the JPA spec
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );
		orderCriteria.select( orderRoot );
		orderCriteria.where( builder.isFalse( builder.disjunction() ) );
		em.createQuery( orderCriteria ).getResultList();

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 3 );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Check simple not.
	 */
	@Test
	public void testSimpleNot() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );

		orderCriteria.select( orderRoot );
		orderCriteria.where( builder.not( builder.equal( orderRoot.get( "id" ), "order-1" ) ) );

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 2 );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Check complicated not.
	 */
	@Test
	public void testComplicatedNotOr() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );

		orderCriteria.select( orderRoot );
		Predicate p1 = builder.equal( orderRoot.get( "id" ), "order-1" );
		Predicate p2 = builder.equal( orderRoot.get( "id" ), "order-2" );
		orderCriteria.where( builder.not( builder.or( p1, p2 ) ) );

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 1 );
		Order order = orders.get( 0 );
		assertEquals( "order-3", order.getId() );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Check complicated not.
	 */
	@Test
	public void testNotMultipleOr() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );

		orderCriteria.select( orderRoot );
		Predicate p1 = builder.equal( orderRoot.get( "id" ), "order-1" );
		Predicate p2 = builder.equal( orderRoot.get( "id" ), "order-2" );
		Predicate p3 = builder.equal( orderRoot.get( "id" ), "order-3" );
		orderCriteria.where( builder.not( builder.or( p1, p2, p3 ) ) );

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 0 );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Check complicated not.
	 */
	@Test
	public void testComplicatedNotAnd() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );

		orderCriteria.select( orderRoot );
		Predicate p1 = builder.equal( orderRoot.get( "id" ), "order-1" );
		Predicate p2 = builder.equal( orderRoot.get( "id" ), "order-2" );
		orderCriteria.where( builder.not( builder.and( p1, p2 ) ) );

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 3 );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Check predicate for field which has simple char array type (char[]).
	 */
	@Test
	public void testCharArray() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );
		
		orderCriteria.select( orderRoot );
		Predicate p = builder.equal( orderRoot.get( "domen" ), new char[]{'r','u'} );
		orderCriteria.where( p );

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 1 );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Check predicate for field which has simple char array type (byte[]).
	 */
	@Test
	public void testByteArray() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );
		
		orderCriteria.select( orderRoot );
		Predicate p = builder.equal( orderRoot.get( "number" ), new byte[]{'1','2'} );
		orderCriteria.where( p );

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 0 );
		em.getTransaction().commit();
		em.close();
	}

	@Test
	@TestForIssue( jiraKey = "HHH-5803" )
	public void testQuotientConversion() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Order> orderCriteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = orderCriteria.from( Order.class );

		Long longValue = 999999999L;
		Path<Double> doublePath = orderRoot.get( Order_.totalPrice );
		Path<Integer> integerPath = orderRoot.get( Order_.customer ).get( Customer_.age );

		orderCriteria.select( orderRoot );
		Predicate p = builder.ge(
				builder.quot( integerPath, doublePath ),
				longValue
		);
		orderCriteria.where( p );

		List<Order> orders = em.createQuery( orderCriteria ).getResultList();
		assertTrue( orders.size() == 0 );
		em.getTransaction().commit();
		em.close();
	}

}
