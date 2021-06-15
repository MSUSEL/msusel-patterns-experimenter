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
package org.hibernate.envers.entities.mapper.relation.query;

/**
 * Constants used in JPQL queries.
 * 
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class QueryConstants {
    public static final String REFERENCED_ENTITY_ALIAS = "e__";
    public static final String REFERENCED_ENTITY_ALIAS_DEF_AUD_STR = "e2__";

    public static final String INDEX_ENTITY_ALIAS = "f__";
    public static final String INDEX_ENTITY_ALIAS_DEF_AUD_STR = "f2__";

    public static final String MIDDLE_ENTITY_ALIAS = "ee__";
    public static final String MIDDLE_ENTITY_ALIAS_DEF_AUD_STR = "ee2__";

    public static final String REVISION_PARAMETER = "revision";
    public static final String DEL_REVISION_TYPE_PARAMETER = "delrevisiontype";
}
