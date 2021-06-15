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
package net.sourceforge.ganttproject.task;

/**
 * This class describes a single custom column.
 * @author bbaranne(Benoit Baranne) Mar 2, 2005
 * 
 */
/**
 * @author bbaranne
 * Mar 8, 2005
 */
/**
 * @author bbaranne Mar 8, 2005
 */
public class CustomColumn {
    private String id = null;

    /**
     * The column name.
     */
    private String name = null;

    /**
     * The enclosing value class.
     */
    private Class type = null;

    /**
     * Teh default value.
     */
    private Object defaultValue = null;

    /**
     * Creates an instance of CustomColmn.
     */
    public CustomColumn() {
    }

    /**
     * Creates an instance of CustomColmn with a name, a type (class) and a
     * default value.
     * 
     * @param colName
     *            The column name.
     * @param colType
     *            The colmun type.
     * @param colDefaultValue
     *            The default value.
     */
    public CustomColumn(String colName, Class colType, Object colDefaultValue) {
        name = colName;
        type = colType;
        defaultValue = colDefaultValue;
    }

    /**
     * Creates an instance of CustomColmn. The type is String and the default
     * value is en empty String.
     * 
     * @param colName
     *            The column name.
     */
    public CustomColumn(String colName) {
        name = colName;
        type = String.class;
        defaultValue = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String newId) {
        id = newId;
    }

    /**
     * Returns the default value.
     * 
     * @return The defautl value.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value.
     * 
     * @param defaultValue
     *            The default value to be set.
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the column name.
     * 
     * @return The column name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the column name.
     * 
     * @param name
     *            The column name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the column type.
     * 
     * @return The column type.
     */
    public Class getType() {
        return type;
    }

    /**
     * Sets the column type.
     * 
     * @param type
     *            The column type to be set.
     */
    public void setType(Class type) {
        this.type = type;
    }

    /**
     * Returns a string representation of the CustomColumn.
     * 
     * @return A string representation of the CustomColumn.
     */
    public String toString() {
        return this.name + " [" + this.type + "] <" + this.defaultValue + ">";
    }
}
