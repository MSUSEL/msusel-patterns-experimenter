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
package org.hibernate.envers.test.integration.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.onetomany.CollectionRefEdEntity;
import org.hibernate.envers.test.entities.onetomany.CollectionRefIngEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class SerializingCollection extends BaseEnversJPAFunctionalTestCase {
    private Integer ed1_id;
    private Integer ing1_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(CollectionRefEdEntity.class);
        cfg.addAnnotatedClass(CollectionRefIngEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        CollectionRefEdEntity ed1 = new CollectionRefEdEntity(1, "data_ed_1");

        CollectionRefIngEntity ing1 = new CollectionRefIngEntity(3, "data_ing_1", ed1);

        // Revision 1
        em.getTransaction().begin();

        em.persist(ed1);
        em.persist(ing1);

        em.getTransaction().commit();

        //

        ed1_id = ed1.getId();
        ing1_id = ing1.getId();
    }

    @Test
    public void testDetach()throws Exception  {
        CollectionRefIngEntity ing1 = getEntityManager().find(CollectionRefIngEntity.class, ing1_id);
        CollectionRefEdEntity rev1 = getAuditReader().find(CollectionRefEdEntity.class, ed1_id, 1);

		// First forcing loading of the collection
		assert rev1.getReffering().size() == 1;

		// Now serializing and de-serializing the
		rev1 = serializeDeserialize(rev1);

		// And checking the colleciton again
        assert rev1.getReffering().contains(ing1);
        assert rev1.getReffering().size() == 1;

    }

	@SuppressWarnings({"unchecked"})
	public static <T> T serializeDeserialize(T o) throws Exception {
		if (o == null) return null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		byte[] buffer = baos.toByteArray();
		baos.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (T) ois.readObject();		
	}
}
