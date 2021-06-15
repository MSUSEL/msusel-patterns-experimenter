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

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.ejb.metamodel.AbstractMetamodelSpecificTest;
import org.hibernate.ejb.metamodel.Product;
import org.hibernate.ejb.metamodel.Product_;

/**
 * @author Steve Ebersole
 */
public class AggregationResultTest extends AbstractMetamodelSpecificTest {
	private CriteriaBuilder builder;

	@Before
	public void createTestData() {
		builder = entityManagerFactory().getCriteriaBuilder();

		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		Product product = new Product();
		product.setId( "product1" );
		product.setPrice( 1.23d );
		product.setQuantity( 1000 );
		product.setPartNumber( ( (long) Integer.MAX_VALUE ) + 1 );
		product.setRating( 1.999f );
		product.setSomeBigInteger( BigInteger.valueOf( 987654321 ) );
		product.setSomeBigDecimal( BigDecimal.valueOf( 987654.32 ) );
		em.persist( product );
		em.getTransaction().commit();
		em.close();
	}

	@After
	public void cleanUpTestData() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		em.createQuery( "delete Product" ).executeUpdate();
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Sum of Longs should return a Long
	 */
	@Test
	public void testSumOfLongs() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Long> criteria = builder.createQuery( Long.class );
		Root<Product> productRoot = criteria.from( Product.class );
		criteria.select( builder.sum( productRoot.get( Product_.partNumber ) ) );
		Object sumResult = em.createQuery( criteria ).getSingleResult();
		assertReturnType( Long.class, sumResult );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Sum of Integers should return an Integer; note that this is distinctly different than JPAQL
	 */
	@Test
	public void testSumOfIntegers() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Integer> criteria = builder.createQuery( Integer.class );
		Root<Product> productRoot = criteria.from( Product.class );
		criteria.select( builder.sum( productRoot.get( Product_.quantity ) ) );
		Object sumResult = em.createQuery( criteria ).getSingleResult();
		assertReturnType( Integer.class, sumResult );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Sum of Doubles should return a Double
	 */
	@Test
	public void testSumOfDoubles() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Double> criteria = builder.createQuery( Double.class );
		Root<Product> productRoot = criteria.from( Product.class );
		criteria.select( builder.sum( productRoot.get( Product_.price ) ) );
		Object sumResult = em.createQuery( criteria ).getSingleResult();
		assertReturnType( Double.class, sumResult );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Sum of Floats should return a Float; note that this is distinctly different than JPAQL
	 */
	@Test
	public void testSumOfFloats() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<Float> criteria = builder.createQuery( Float.class );
		Root<Product> productRoot = criteria.from( Product.class );
		criteria.select( builder.sum( productRoot.get( Product_.rating ) ) );
		Object sumResult = em.createQuery( criteria ).getSingleResult();
		assertReturnType( Float.class, sumResult );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Sum of BigInteger should return a BigInteger
	 */
	@Test
	public void testSumOfBigIntegers() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<BigInteger> criteria = builder.createQuery( BigInteger.class );
		Root<Product> productRoot = criteria.from( Product.class );
		criteria.select( builder.sum( productRoot.get( Product_.someBigInteger ) ) );
		Object sumResult = em.createQuery( criteria ).getSingleResult();
		assertReturnType( BigInteger.class, sumResult );
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Sum of BigDecimal should return a BigDecimal
	 */
	@Test
	public void testSumOfBigDecimals() {
		EntityManager em = getOrCreateEntityManager();
		em.getTransaction().begin();
		CriteriaQuery<BigDecimal> criteria = builder.createQuery( BigDecimal.class );
		Root<Product> productRoot = criteria.from( Product.class );
		criteria.select( builder.sum( productRoot.get( Product_.someBigDecimal ) ) );
		Object sumResult = em.createQuery( criteria ).getSingleResult();
		assertReturnType( BigDecimal.class, sumResult );
		em.getTransaction().commit();
		em.close();
	}

	private void assertReturnType(Class expectedType, Object value) {
		if ( value != null && ! expectedType.isInstance( value ) ) {
			throw new AssertionFailedError(
					"Result value was not of expected type: expected [" + expectedType.getName()
							+ "] but found [" + value.getClass().getName() + "]"
			);
		}
	}
}
