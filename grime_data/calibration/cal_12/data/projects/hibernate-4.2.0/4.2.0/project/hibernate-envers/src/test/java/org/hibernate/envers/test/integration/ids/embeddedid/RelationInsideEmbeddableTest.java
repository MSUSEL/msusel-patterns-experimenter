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
package org.hibernate.envers.test.integration.ids.embeddedid;

import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.testing.TestForIssue;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Arrays;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-7690")
public class RelationInsideEmbeddableTest extends BaseEnversJPAFunctionalTestCase {
    private Integer orderId = null;
    private ItemId itemId = null;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{PurchaseOrder.class, Item.class, ItemId.class, Producer.class};
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1
        em.getTransaction().begin();
        Producer producer = new Producer(1, "Sony");
        ItemId sonyId = new ItemId("TV", 1, producer);
        Item item = new Item(sonyId, 100.50);
        PurchaseOrder order = new PurchaseOrder(item, null);
        em.persist(producer);
        em.persist(item);
        em.persist(order);
        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();
        order = em.find(PurchaseOrder.class, order.getId());
        order.setComment("fragile");
        order = em.merge(order);
        em.getTransaction().commit();

        // Revision 3
        em.getTransaction().begin();
        item = em.find(Item.class, sonyId);
        item.setPrice(110.00);
        em.getTransaction().commit();

        orderId = order.getId();
        itemId = sonyId;

        em.close();
    }

    @Test
    public void testRevisionsCounts() throws Exception {
        Assert.assertEquals(Arrays.asList(1, 2), getAuditReader().getRevisions(PurchaseOrder.class, orderId));
        Assert.assertEquals(Arrays.asList(1, 3), getAuditReader().getRevisions(Item.class, itemId));
    }

    @Test
    public void testHistoryOfPurchaseOrder() {
        PurchaseOrder ver1 = new PurchaseOrder(orderId, new Item(new ItemId("TV", 1, new Producer(1, "Sony")), 100.50), null);
        PurchaseOrder ver2 = new PurchaseOrder(orderId, new Item(new ItemId("TV", 1, new Producer(1, "Sony")), 100.50), "fragile");

        Assert.assertEquals(ver1, getAuditReader().find(PurchaseOrder.class, orderId, 1));
        Assert.assertEquals(ver2, getAuditReader().find(PurchaseOrder.class, orderId, 2));
    }

    @Test
    public void testHistoryOfItem() {
        Item ver1 = new Item(itemId, 100.50);
        Item ver2 = new Item(itemId, 110.00);

        Assert.assertEquals(ver1, getAuditReader().find(Item.class, itemId, 1));
        Assert.assertEquals(ver2, getAuditReader().find(Item.class, itemId, 3));
    }
}
