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
package org.archive.crawler.settings;

import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;

/**
 *
 * @author John Erik Halse
 */
public class ModuleAttributeInfo extends MBeanAttributeInfo {

    private static final long serialVersionUID = -4447321338690051514L;

    private String type;
    private boolean isOverrideable;
    private boolean isTransient;
    private final Object defaultValue;
    private final Object legalValueLists[];
    private boolean complexType = false;
    private boolean isExpertSetting;

    /** Construct a new instance of ModuleAttributeInfo.
     *
     * @param type the element to create info for.
     *
     * @throws InvalidAttributeValueException
     * @throws java.lang.IllegalArgumentException
     */
    public ModuleAttributeInfo(Type type)
            throws InvalidAttributeValueException {

        super(type.getName(), type.getClass().getName(), type.getDescription(),
                true, true, false);
        setType(type.getDefaultValue());
        this.isOverrideable = type.isOverrideable();
        this.isTransient = type.isTransient();
        this.legalValueLists = type.getLegalValues();
        this.isExpertSetting = type.isExpertSetting();
        //this.defaultValue = checkValue(type.getValue());
        this.defaultValue = type.getValue();
        if (type.getDefaultValue() instanceof ComplexType) {
            complexType = true;
        }
    }

    public ModuleAttributeInfo(ModuleAttributeInfo attr) {
        super(attr.getName(), attr.getType(), attr.getDescription(),
                true, true, false);
        setType(attr.getDefaultValue());
        this.isOverrideable = attr.isOverrideable();
        this.isTransient = attr.isTransient();
        this.legalValueLists = attr.getLegalValues();
        this.isExpertSetting = attr.isExpertSetting();
        this.defaultValue = attr.getDefaultValue();
        this.complexType = attr.complexType;
    }

    public Object[] getLegalValues() {
        return legalValueLists;
    }

    /** Returns true if this attribute refers to a ComplexType.
     *
     * @return true if this attribute refers to a ComplexType.
     */
    public boolean isComplexType() {
        return complexType;
    }

    /** Returns true if this attribute could be overridden in per settings.
     *
     * @return True if overrideable.
     */
    public boolean isOverrideable() {
        return isOverrideable;
    }

    /** Returns true if this attribute should be hidden from UI and not be
     * serialized to persistent storage.
     *
     * @return True if transient.
     */
    public boolean isTransient() {
        return isTransient;
    }

    /** Returns true if this Type should only show up in expert mode in UI.
     *
     * @return true if this Type should only show up in expert mode in UI.
     */
    public boolean isExpertSetting() {
        return isExpertSetting;
    }

    /**
     * @return Default value.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /* (non-Javadoc)
     * @see javax.management.MBeanAttributeInfo#getType()
     */
    public String getType() {
        return type;
    }

    protected void setType(Object type) {
        this.type = type.getClass().getName();
    }
}
