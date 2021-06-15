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
package org.hibernate.envers.test.integration.reventity;

import java.util.Iterator;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.integration.inheritance.joined.ChildEntity;
import org.hibernate.envers.test.integration.inheritance.joined.ParentEntity;
import org.hibernate.mapping.Column;

import static org.junit.Assert.assertEquals;

/**
 * A join-inheritance test using a custom revision entity where the revision number is a long, mapped in the database
 * as an int.
 * @author Adam Warski (adam at warski dot org)
 */
public class LongRevEntityInheritanceChildAuditing extends BaseEnversJPAFunctionalTestCase {
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ChildEntity.class);
        cfg.addAnnotatedClass(ParentEntity.class);
        cfg.addAnnotatedClass(LongRevNumberRevEntity.class);
    }

    @Test
    public void testChildRevColumnType() {
        // We need the second column
        Iterator childEntityKeyColumnsIterator = getCfg()
                .getClassMapping("org.hibernate.envers.test.integration.inheritance.joined.ChildEntity_AUD")
                .getKey()
                .getColumnIterator();
        childEntityKeyColumnsIterator.next();
        Column second = (Column) childEntityKeyColumnsIterator.next();

        assertEquals(second.getSqlType(), "int");
    }
}