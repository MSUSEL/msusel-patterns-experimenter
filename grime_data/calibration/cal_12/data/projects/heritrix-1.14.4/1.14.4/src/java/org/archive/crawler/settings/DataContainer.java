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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;

import java.util.concurrent.CopyOnWriteArrayList;

/** This class holds the data for a ComplexType for a settings object.
 *
 * @author John Erik Halse
 */
public class DataContainer extends HashMap<String,Object> {

    private static final long serialVersionUID = 2089160108643429282L;

    /** The ComplexType for which this DataContainer keeps data */
    private ComplexType complexType;

    /** The Settings object for which this data is valid */
    private Reference<CrawlerSettings> settings;

    /** The attributes defined for this DataContainers combination of
     * ComplexType and CrawlerSettings.
     */
    private List<MBeanAttributeInfo> attributes;

    /** All attributes that have their value set for this DataContainers
     * combination of ComplexType and CrawlerSettings. This includes overrides.
     */
    private Map<String,MBeanAttributeInfo> attributeNames;

    /** Create a data container for a module.
     *
     * @param settings Settings to use.
     * @param module the module to create the data container for.
     */
    public DataContainer(CrawlerSettings settings, ComplexType module) {
        super();
        this.settings = new WeakReference<CrawlerSettings>(settings);
        this.complexType = module;
        attributes =
            new CopyOnWriteArrayList<MBeanAttributeInfo>();
        attributeNames = new HashMap<String,MBeanAttributeInfo>();
    }

    /** Add a new element to the data container.
     *
     * @param type the element to add.
     * @param index index at which the specified element is to be inserted.
     * @throws InvalidAttributeValueException
     */
    public void addElementType(Type type, int index)
            throws InvalidAttributeValueException {

        if (attributeNames.containsKey(type.getName())) {
            throw new IllegalArgumentException(
                    "Duplicate field: " + type.getName());
        }
        if (type.getDefaultValue() == null) {
            throw new InvalidAttributeValueException(
                    "null is not allowed as default value for attribute '"
                            + type.getName() + "' in class '"
                            + complexType.getClass().getName() + "'");
        }
        MBeanAttributeInfo attribute = new ModuleAttributeInfo(type);
        attributes.add(index, attribute);
        //attributeNames.put(type.getName(), attribute);
        try {
            put(type.getName(), attribute, type.getDefaultValue());
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Appends the specified element to the end of this data container.
     *
     * @param type the element to add.
     * @throws InvalidAttributeValueException
     */
    public void addElementType(Type type) throws InvalidAttributeValueException {

        addElementType(type, attributes.size());
    }

    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo attrs[] = (MBeanAttributeInfo[]) attributes
                .toArray(new MBeanAttributeInfo[0]);
        MBeanInfo info = new MBeanInfo(complexType.getClass().getName(),
                complexType.getDescription(), attrs, null, null, null);
        return info;
    }

    protected List<MBeanAttributeInfo> getLocalAttributeInfoList() {
        return attributes;
    }

    protected boolean hasAttributes() {
        return !attributes.isEmpty();
    }

    public int size() {
        return attributes.size();
    }

    protected MBeanAttributeInfo getAttributeInfo(String name) {
        return (MBeanAttributeInfo) attributeNames.get(name);
    }

    protected void copyAttributeInfo(String name, DataContainer destination) {
        if (this != destination) {
            ModuleAttributeInfo attribute = (ModuleAttributeInfo) attributeNames.get(name);
            destination.attributeNames.put(name, new ModuleAttributeInfo(attribute));
        }
    }

    protected boolean copyAttribute(String name, DataContainer destination)
            throws InvalidAttributeValueException, AttributeNotFoundException {
        if (this != destination) {
            ModuleAttributeInfo attribute = (ModuleAttributeInfo) attributeNames
                    .get(name);

            if (attribute == null) {
                return false;
            } else {
                int index = attributes.indexOf(attribute);
                if (index != -1 && !destination.attributes.contains(attribute)) {
                    destination.attributes.add(index, attribute);
                }
                destination.put(attribute.getName(), attribute, get(attribute
                        .getName()));
            }
        }
        return true;
    }

    public Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    protected Object put(String key, MBeanAttributeInfo info, Object value)
        throws InvalidAttributeValueException, AttributeNotFoundException {
        attributeNames.put(key, info);
        return super.put(key, value);
    }

    /* (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(String key) throws AttributeNotFoundException {
        Object res = super.get(key);
        if (res == null && complexType.definitionMap.get(key) == null) {
            throw new AttributeNotFoundException(key);
        }
        return res;
    }

    /** Move an attribute up one place in the list.
     *
     * @param key name of attribute to move.
     * @return true if attribute was moved, false if attribute was already
     *              at the top.
     * @throws AttributeNotFoundException is thrown if there is no attribute
     *         with the submitted key.
     */
    protected boolean moveElementUp(String key)
            throws AttributeNotFoundException {
        MBeanAttributeInfo element = getAttributeInfo(key);
        if (element == null) {
            throw new AttributeNotFoundException(key);
        }

        int prevIndex = attributes.indexOf(element);
        if (prevIndex == 0) {
            return false;
        }

        attributes.remove(prevIndex);
        attributes.add(prevIndex-1, element);

        return true;
    }

    /** Move an attribute down one place in the list.
     *
     * @param key name of attribute to move.
     * @return true if attribute was moved, false if attribute was already
     *              at bottom.
     * @throws AttributeNotFoundException is thrown if there is no attribute
     *         with the submitted key.
     */
    protected boolean moveElementDown(String key)
            throws AttributeNotFoundException {
        MBeanAttributeInfo element = getAttributeInfo(key);
        if (element == null) { throw new AttributeNotFoundException(key); }

        int prevIndex = attributes.indexOf(element);
        if (prevIndex == attributes.size() - 1) { return false; }

        attributes.remove(prevIndex);
        attributes.add(prevIndex + 1, element);

        return true;
    }

    /**
     * Remove an attribute from the DataContainer.
     *
     * @param key name of the attribute to remove.
     * @return the element that was removed.
     * @throws AttributeNotFoundException is thrown if there is no attribute
     *             with the submitted key.
     */
    protected Object removeElement(String key) throws AttributeNotFoundException {
        MBeanAttributeInfo element = getAttributeInfo(key);
        if (element == null) {
            throw new AttributeNotFoundException(key);
        }

        attributes.remove(element);
        attributeNames.remove(element.getName());
        return super.remove(element.getName());
    }

    /** Get the ComplexType for which this DataContainer keeps data.
     *
     * @return the ComplexType for which this DataContainer keeps data.
     */
    protected ComplexType getComplexType() {
        return complexType;
    }

    /** Get the settings object for which this DataContainers data are valid.
     *
     * @return the settings object for which this DataContainers data are valid.
     */
    protected CrawlerSettings getSettings() {
        return (CrawlerSettings) settings.get();
    }

}
