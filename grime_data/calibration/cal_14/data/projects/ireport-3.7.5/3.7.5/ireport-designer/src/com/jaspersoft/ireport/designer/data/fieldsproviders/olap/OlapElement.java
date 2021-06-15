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

/**
 *
 * @version $Id: OlapElement.java 0 2010-03-03 16:56:20 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class OlapElement {

    public static final int TYPE_MEASURE = 0;
    public static final int TYPE_LEVEL = 1;
    public static final int TYPE_HIERARCHY = 2;
    public static final int TYPE_AXIS = 3;
    private int type = 0;
    private int axis = 0;
    private String hierarchyName = "";
    private String uName = "";
    private String displayName = "";
    private OlapElement parent = null;
    
    
    public String toString()
    {
        return getDisplayName();
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the axis
     */
    public int getAxis() {
        return axis;
    }

    /**
     * @param axis the axis to set
     */
    public void setAxis(int axis) {
        this.axis = axis;
    }

    /**
     * @return the uName
     */
    public String getuName() {
        return uName;
    }

    /**
     * @param uName the uName to set
     */
    public void setuName(String uName) {
        this.uName = uName;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {

        if (displayName == null || displayName.length() == 0)
        {
            if (getType() == TYPE_AXIS)
            {
                switch (getAxis())
                {
                            case 0: return "Columns";
                            case 1: return "Rows";
                            case 2: return "Pages";
                            case 3: return "Chapters";
                            case 4: return "Sections";
                            default: return "Axis(" + getAxis() +")";
                }
            }
        }
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the parent
     */
    public OlapElement getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(OlapElement parent) {
        this.parent = parent;
    }

    /**
     * @return the hierarchyName
     */
    public String getHierarchyName() {
        return hierarchyName;
    }

    /**
     * @param hierarchyName the hierarchyName to set
     */
    public void setHierarchyName(String hierarchyName) {
        this.hierarchyName = hierarchyName;
    }

}
