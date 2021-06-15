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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.geotools.arcsde.data.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;

import org.geotools.arcsde.session.ISession;

/**
 * Seems to visit a list and update the entries and fill in the blanks qualifying them.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/view/ItemsListQualifier.java $
 * @since 2.3.x
 */
class ItemsListQualifier implements ItemsListVisitor {

    ItemsList _qualifiedList;

    private ISession session;

    private Map<String, Object> tableAliases;

    /**
     * Creates a new ItemsListQualifier object.
     * 
     * @param session
     */
    public ItemsListQualifier(ISession session, Map<String, Object> tableAliases) {
        this.session = session;
        this.tableAliases = tableAliases;
    }

    public static ItemsList qualify(ISession session, Map<String, Object> tableAliases,
            ItemsList items) {
        if (items == null) {
            return null;
        }

        ItemsListQualifier q = new ItemsListQualifier(session, tableAliases);
        items.accept(q);

        return q._qualifiedList;
    }

    public void visit(SubSelect subSelect) {
        SubSelect qualified = SubSelectQualifier.qualify(session, subSelect);
        this._qualifiedList = qualified;
    }

    @SuppressWarnings("unchecked")
    public void visit(ExpressionList expressionList) {
        List<Expression> expressions = expressionList.getExpressions();
        List<Expression> qualifiedList = new ArrayList<Expression>(expressions.size());

        for (Iterator<Expression> it = expressions.iterator(); it.hasNext();) {
            Expression exp = (Expression) it.next();
            Expression qExp = ExpressionQualifier.qualify(session, tableAliases, exp);

            qualifiedList.add(qExp);
        }

        ExpressionList qExpList = new ExpressionList();
        qExpList.setExpressions(qualifiedList);
        this._qualifiedList = qExpList;
    }
}
