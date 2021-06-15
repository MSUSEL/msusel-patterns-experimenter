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
package org.geotools.data.wms.xml;

/**
 * Property class for holding and handling of property values declared in Dimension-element of a
 * layer. In WMS 1.3.0 this is expanded to include Extent information documenting the valid
 * data values for this range.
 * 
 * http://schemas.opengis.net/wms/1.1.1/WMS_MS_Capabilities.dtd <!-- The Dimension element declares
 * the _existence_ of a dimension. --> <!ELEMENT Dimension EMPTY > <!ATTLIST Dimension name CDATA
 * #REQUIRED units CDATA #REQUIRED unitSymbol CDATA #IMPLIED>
 * 
 * http://schemas.opengis.net/wms/1.3.0/capabilities_1_3_0.xsd <element name="Dimension">
 * <complexType> <simpleContent> <extension base="string"> <attribute name="name" type="string"
 * use="required"/> <attribute name="units" type="string" use="required"/> <attribute
 * name="unitSymbol" type="string"/> <attribute name="default" type="string"/> <attribute
 * name="multipleValues" type="boolean"/> <attribute name="nearestValue" type="boolean"/> <attribute
 * name="current" type="boolean"/> </extension> </simpleContent> </complexType> </element>
 * 
 *
 *
 * @source $URL$
 * @version SVN $Id$
 * @author Per Engstrom, Curalia AB, pereng@gmail.com
 * 
 */
public class Dimension {
    /** This name is often used as a lookup key */
    protected String name;
    
    protected String units;

    protected String unitSymbol;

    protected boolean current;

    /** Optional Extent as supplied by WMS 1.3.0 */
    protected Extent extent = null;

    public Dimension(String name, String units, String unitSymbol) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(
                    "Error creating Extent: parameter name must not be null!");
        }
        if (units == null || units.length() == 0) {
            throw new IllegalArgumentException(
                    "Error creating Extent: parameter units must not be null!");
        }

        this.name = name;
        this.units = units;
        this.unitSymbol = unitSymbol;
    }

    public Dimension(String name, String units) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(
                    "Error creating Extent: parameter name must not be null!");
        }
        if (units == null || units.length() == 0) {
            throw new IllegalArgumentException(
                    "Error creating Extent: parameter units must not be null!");
        }

        this.name = name;
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public Extent getExtent() {
        return extent;
    }

    public void setExtent(Extent extent) {
        this.extent = extent;
    }

    public String toString() {
        return name + ", " + units + "(" + unitSymbol + ")";
    }

}
