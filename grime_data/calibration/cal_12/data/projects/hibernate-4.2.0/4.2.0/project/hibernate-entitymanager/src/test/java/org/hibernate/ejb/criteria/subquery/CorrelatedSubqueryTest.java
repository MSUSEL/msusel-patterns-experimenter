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
package org.hibernate.ejb.criteria.subquery;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.Test;

import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.ejb.metamodel.AbstractMetamodelSpecificTest;
import org.hibernate.ejb.metamodel.Customer;
import org.hibernate.ejb.metamodel.Customer_;
import org.hibernate.ejb.metamodel.LineItem;
import org.hibernate.ejb.metamodel.LineItem_;
import org.hibernate.ejb.metamodel.Order;
import org.hibernate.ejb.metamodel.Order_;
import org.hibernate.testing.SkipForDialect;

/**
 * @author Steve Ebersole
 */
public class CorrelatedSubqueryTest extends AbstractMetamodelSpecificTest {
	@Test
	public void testBasicCorrelation() {
		CriteriaBuilder builder = entityManagerFactory().getCriteriaBuilder();
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();

		CriteriaQuery<Customer> criteria = builder.createQuery( Customer.class );
		Root<Customer> customer = criteria.from( Customer.class );
		criteria.select( customer );
		Subquery<Order> orderSubquery = criteria.subquery( Order.class );
		Root<Customer> customerCorrelationRoot = orderSubquery.correlate( customer );
		Join<Customer, Order> customerOrderCorrelationJoin = customerCorrelationRoot.join( Customer_.orders );
		orderSubquery.select( customerOrderCorrelationJoin );
		criteria.where( builder.not( builder.exists( orderSubquery ) ) );
		em.createQuery( criteria ).getResultList();

		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testRestrictedCorrelation() {
		CriteriaBuilder builder = entityManagerFactory().getCriteriaBuilder();
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();

		CriteriaQuery<Order> criteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = criteria.from( Order.class );
		criteria.select( orderRoot );
		// create correlated subquery
		Subquery<Customer> customerSubquery = criteria.subquery( Customer.class );
		Root<Order> orderRootCorrelation = customerSubquery.correlate( orderRoot );
		Join<Order, Customer> orderCustomerJoin = orderRootCorrelation.join( Order_.customer );
		customerSubquery.where( builder.like( orderCustomerJoin.get( Customer_.name ), "%Caruso" ) )
				.select( orderCustomerJoin );
		criteria.where( builder.exists( customerSubquery ) );
		em.createQuery( criteria ).getResultList();

		em.getTransaction().commit();
		em.close();
	}

	@Test
	@SkipForDialect(value=SybaseASE15Dialect.class, jiraKey="HHH-3032")
	public void testCorrelationExplicitSelectionCorrelation() {
		CriteriaBuilder builder = entityManagerFactory().getCriteriaBuilder();
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();

		CriteriaQuery<Customer> customerCriteria = builder.createQuery( Customer.class );
		Root<Customer> customer = customerCriteria.from( Customer.class );
		Join<Customer, Order> o = customer.join( Customer_.orders );
		Subquery<Order> sq = customerCriteria.subquery(Order.class);
		Join<Customer, Order> sqo = sq.correlate(o);
		Join<Order, LineItem> sql = sqo.join(Order_.lineItems);
		sq.where( builder.gt(sql.get( LineItem_.quantity), 3) );
		// use the correlation itself as the subquery selection (initially caused problems wrt aliases)
		sq.select(sqo);
		customerCriteria.select(customer).distinct(true);
		customerCriteria.where(builder.exists(sq));
		em.createQuery( customerCriteria ).getResultList();

		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testRestrictedCorrelationNoExplicitSelection() {
		CriteriaBuilder builder = entityManagerFactory().getCriteriaBuilder();
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();

		CriteriaQuery<Order> criteria = builder.createQuery( Order.class );
		Root<Order> orderRoot = criteria.from( Order.class );
		criteria.select( orderRoot );
		// create correlated subquery
		Subquery<Customer> customerSubquery = criteria.subquery( Customer.class );
		Root<Order> orderRootCorrelation = customerSubquery.correlate( orderRoot );
		Join<Order, Customer> orderCustomerJoin = orderRootCorrelation.join( "customer" );
		customerSubquery.where( builder.like( orderCustomerJoin.<String>get( "name" ), "%Caruso" ) );
		criteria.where( builder.exists( customerSubquery ) );
		em.createQuery( criteria ).getResultList();

		em.getTransaction().commit();
		em.close();
	}
}
