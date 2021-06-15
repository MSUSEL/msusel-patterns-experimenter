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
package org.hibernate.envers.test.integration.tools;

import org.hibernate.Session;
import org.hibernate.envers.test.BaseEnversFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.testing.TestForIssue;
import org.hibernate.tool.EnversSchemaGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-7106")
public class SchemaExportTest extends BaseEnversFunctionalTestCase  {
    private Integer id = null;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[] {StrTestEntity.class};
    }

	protected boolean createSchema() {
		// Disable schema auto generation.
		return false;
	}
    @Test
    @Priority(10)
    public void testSchemaCreation() {
        // Generate complete schema.
        new EnversSchemaGenerator(configuration()).export().create( true, true );

        // Populate database with test data.
        Session session = getSession();
        session.getTransaction().begin();
        StrTestEntity entity = new StrTestEntity("data");
        session.save(entity);
        session.getTransaction().commit();

        id = entity.getId();
    }

	@Test
    @Priority(9)
    public void testAuditDataRetrieval() {
        Assert.assertEquals(Arrays.asList(1), getAuditReader().getRevisions(StrTestEntity.class, id));
        Assert.assertEquals(new StrTestEntity("data", id), getAuditReader().find(StrTestEntity.class, id, 1));
    }

    @Test
    @Priority(8)
    public void testSchemaDrop() {
        new EnversSchemaGenerator(configuration()).export().drop( true, true );
    }
}
