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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.designer.data.fieldsproviders.olap;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;

/**
 *
 * @version $Id: IROlapHierarchy.java 0 2010-03-03 19:02:26 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class IROlapHierarchy implements JROlapHierarchy {
    private String dimensionName = null;
    private String hierarchyUniqueName = null;
    private List<JROlapHierarchyLevel> levels = new ArrayList<JROlapHierarchyLevel>();

    public IROlapHierarchy(String dimensionName)
    {
        this.dimensionName = dimensionName;
    }

    public void addHierarchyLevel(JROlapHierarchyLevel a)
    {
        levels.add(a);
    }


    public JROlapHierarchyLevel[] getLevels() {
        return levels.toArray(new JROlapHierarchyLevel[levels.size()]);
    }

    /**
     * @return the dimensionName
     */
    public String getDimensionName() {
        return dimensionName;
    }

    /**
     * @param dimensionName the dimensionName to set
     */
    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    /**
     * @return the hierarchyUniqueName
     */
    public String getHierarchyUniqueName() {
        return hierarchyUniqueName;
    }

    /**
     * @param hierarchyUniqueName the hierarchyUniqueName to set
     */
    public void setHierarchyUniqueName(String hierarchyUniqueName) {
        this.hierarchyUniqueName = hierarchyUniqueName;
    }

}
