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
package org.hibernate.envers.entities;
import org.hibernate.envers.ModificationStore;

/**
 * Holds information on a property that is audited.
 * @author Adam Warski (adam at warski dot org)
 */
public class PropertyData {
    private final String name;
	/**
	 * Name of the property in the bean.
	 */
	private final String beanName;
    private final String accessType;
    private final ModificationStore store;
	private boolean usingModifiedFlag;
	private String modifiedFlagName;

    /**
     * Copies the given property data, except the name.
     * @param newName New name.
     * @param propertyData Property data to copy the rest of properties from.
     */
    public PropertyData(String newName, PropertyData propertyData) {
        this.name = newName;
		this.beanName = propertyData.beanName;
        this.accessType = propertyData.accessType;
        this.store = propertyData.store;
    }

    /**
     * @param name Name of the property.
	 * @param beanName Name of the property in the bean.
     * @param accessType Accessor type for this property.
     * @param store How this property should be stored.
     */
    public PropertyData(String name, String beanName, String accessType, ModificationStore store) {
        this.name = name;
		this.beanName = beanName;
        this.accessType = accessType;
        this.store = store;
    }

	/**
     * @param name Name of the property.
	 * @param beanName Name of the property in the bean.
     * @param accessType Accessor type for this property.
     * @param store How this property should be stored.
     * @param usingModifiedFlag Defines if field changes should be tracked
     */
	public PropertyData(String name, String beanName, String accessType, ModificationStore store, boolean usingModifiedFlag, String modifiedFlagName) {
		this(name, beanName, accessType, store);
		this.usingModifiedFlag = usingModifiedFlag;
		this.modifiedFlagName = modifiedFlagName;
	}

    public String getName() {
        return name;
    }

	public String getBeanName() {
		return beanName;
	}

	public String getAccessType() {
        return accessType;
    }

    public ModificationStore getStore() {
        return store;
    }

	public boolean isUsingModifiedFlag() {
		return usingModifiedFlag;
	}

	public String getModifiedFlagPropertyName() {
		return modifiedFlagName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PropertyData that = (PropertyData) o;

		if (accessType != null ? !accessType.equals(that.accessType) : that.accessType != null) return false;
		if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (store != that.store) return false;
		if (usingModifiedFlag != that.usingModifiedFlag) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (beanName != null ? beanName.hashCode() : 0);
		result = 31 * result + (accessType != null ? accessType.hashCode() : 0);
		result = 31 * result + (store != null ? store.hashCode() : 0);
		result = 31 * result + (usingModifiedFlag ? 1 : 0);
		return result;
	}
}
