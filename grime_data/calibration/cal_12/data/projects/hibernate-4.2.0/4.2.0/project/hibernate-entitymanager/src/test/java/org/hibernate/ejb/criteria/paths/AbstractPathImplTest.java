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
package org.hibernate.ejb.criteria.paths;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.ejb.metamodel.AbstractMetamodelSpecificTest;
import org.hibernate.ejb.metamodel.Order;
import org.hibernate.ejb.metamodel.Thing;
import org.hibernate.ejb.metamodel.ThingWithQuantity;

import static org.junit.Assert.assertEquals;

/**
 * @author Michael Rudolf
 * @author James Gilbertson
 */
public class AbstractPathImplTest extends AbstractMetamodelSpecificTest {
    @Before
    public void prepareTestData() {
        EntityManager em = getOrCreateEntityManager();
        em.getTransaction().begin();

        Thing thing = new Thing();
        thing.setId( "thing1" );
        thing.setName( "A Thing" );
        em.persist( thing );

        thing = new Thing();
        thing.setId( "thing2" );
        thing.setName( "Another Thing" );
        em.persist( thing );

        ThingWithQuantity thingWithQuantity = new ThingWithQuantity();
        thingWithQuantity.setId( "thingWithQuantity3" );
        thingWithQuantity.setName( "3 Things" );
        thingWithQuantity.setQuantity( 3 );
        em.persist( thingWithQuantity );

        em.getTransaction().commit();
        em.close();
    }

    @After
    public void cleanupTestData() {
        EntityManager em = getOrCreateEntityManager();
        em.getTransaction().begin();
        em.remove( em.find( Thing.class, "thing1" ) );
        em.remove( em.find( Thing.class, "thing2" ) );
        em.remove( em.find( ThingWithQuantity.class, "thingWithQuantity3" ) );
        em.getTransaction().commit();
        em.close();
    }

	@Test(expected = IllegalArgumentException.class)
	public void testGetNonExistingAttributeViaName() {
		EntityManager em = getOrCreateEntityManager();
		try {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<Order> criteria = criteriaBuilder.createQuery( Order.class );
			Root<Order> orderRoot = criteria.from( Order.class );
			orderRoot.get( "nonExistingAttribute" );
		}
		finally {
			em.close();
		}
	}

	@Test
	public void testTypeExpression() {
		EntityManager em = getOrCreateEntityManager();
		try {
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<Thing> criteria = criteriaBuilder.createQuery( Thing.class );
			Root<Thing> thingRoot = criteria.from( Thing.class );

			criteria.select( thingRoot );
			assertEquals( em.createQuery( criteria ).getResultList().size(), 3);

			criteria.where( criteriaBuilder.equal( thingRoot.type(), criteriaBuilder.literal( Thing.class ) ) );
			assertEquals( em.createQuery( criteria ).getResultList().size(), 2 );
		}
		finally {
			em.close();
		}
	}
}
