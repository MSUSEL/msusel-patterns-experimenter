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
package org.hibernate.envers.test.integration.hashcode;

import java.util.Arrays;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class ChangingHashcode extends BaseEnversJPAFunctionalTestCase {
	private Long pageId;
	private Long imageId;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(WikiPage.class);
        cfg.addAnnotatedClass(WikiImage.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        // Revision 1
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

		WikiPage page = new WikiPage("title", "content");
        em.persist(page);

        em.getTransaction().commit();

        // Revision 2
        em = getEntityManager();
        em.getTransaction().begin();

        WikiImage image = new WikiImage("name1");
		em.persist(image);

		page = em.find(WikiPage.class, page.getId());
		page.getImages().add(image);

        em.getTransaction().commit();

        // Revision 3
        em = getEntityManager();
        em.getTransaction().begin();

        image = em.find(WikiImage.class, image.getId());
		image.setName("name2");

        em.getTransaction().commit();

		pageId = page.getId();
		imageId = image.getId();
    }

    @Test
    public void testRevisionsCounts() {
        assert Arrays.asList(1, 2).equals(getAuditReader().getRevisions(WikiPage.class, pageId));
        assert Arrays.asList(2, 3).equals(getAuditReader().getRevisions(WikiImage.class, imageId));
    }

    @Test
    public void testHistoryOfImage() {
		assert getAuditReader().find(WikiImage.class, imageId, 1) == null;
        assert getAuditReader().find(WikiImage.class, imageId, 2).equals(new WikiImage("name1"));
        assert getAuditReader().find(WikiImage.class, imageId, 3).equals(new WikiImage("name2"));
    }

    @Test
    public void testHistoryOfPage() {
        assert getAuditReader().find(WikiPage.class, pageId, 1).getImages().size() == 0;
        assert getAuditReader().find(WikiPage.class, pageId, 2).getImages().equals(TestTools.makeSet(new WikiImage("name1")));
        assert getAuditReader().find(WikiPage.class, pageId, 3).getImages().equals(TestTools.makeSet(new WikiImage("name2")));
    }
}