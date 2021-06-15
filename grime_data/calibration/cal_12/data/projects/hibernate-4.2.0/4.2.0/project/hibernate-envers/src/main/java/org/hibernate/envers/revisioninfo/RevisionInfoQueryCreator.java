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
package org.hibernate.envers.revisioninfo;

import java.util.Date;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class RevisionInfoQueryCreator {
    private final String revisionInfoEntityName;
    private final String revisionInfoIdName;
    private final String revisionInfoTimestampName;
    private final boolean timestampAsDate;

    public RevisionInfoQueryCreator(String revisionInfoEntityName, String revisionInfoIdName,
                                    String revisionInfoTimestampName, boolean timestampAsDate) {
        this.revisionInfoEntityName = revisionInfoEntityName;
        this.revisionInfoIdName = revisionInfoIdName;
        this.revisionInfoTimestampName = revisionInfoTimestampName;
        this.timestampAsDate = timestampAsDate;
    }

    public Criteria getRevisionDateQuery(Session session, Number revision) {
        return session.createCriteria(revisionInfoEntityName).setProjection(Projections.property(revisionInfoTimestampName))
                                                             .add(Restrictions.eq(revisionInfoIdName, revision));
    }

    public Criteria getRevisionNumberForDateQuery(Session session, Date date) {
        return session.createCriteria(revisionInfoEntityName).setProjection(Projections.max(revisionInfoIdName))
                                                             .add(Restrictions.le(revisionInfoTimestampName, timestampAsDate ? date : date.getTime()));
    }

    public Criteria getRevisionsQuery(Session session, Set<Number> revisions) {
        return session.createCriteria(revisionInfoEntityName).add(Restrictions.in(revisionInfoIdName, revisions));
    }
}
