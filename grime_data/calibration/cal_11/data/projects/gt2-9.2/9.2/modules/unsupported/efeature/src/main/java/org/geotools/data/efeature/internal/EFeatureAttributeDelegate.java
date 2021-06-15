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
package org.geotools.data.efeature.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.geotools.data.efeature.EFeatureAttribute;
import org.geotools.data.efeature.EFeatureAttributeInfo;
import org.geotools.data.efeature.EFeatureStatus;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.Identifier;

/**
 * An abstract implementation of the model data type {@link EFeatureAttribute}.
 * 
 * @author kengu, 22. apr. 2011
 *
 * @source $URL$
 */
public class EFeatureAttributeDelegate<V> extends
        EFeaturePropertyDelegate<V, Attribute, EAttribute> implements EFeatureAttribute<V> {
    
    /**
     * Cached value when detached
     */
    protected V eValue;
    

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------


    /**
     * Default constructor.
     * <p>
     * @param eInternal - 
     * @param eName - 
     * @param valueType - {@link #getValue() property value} type.
     */
    public EFeatureAttributeDelegate(EFeatureInternal eInternal, String eName, Class<V> valueType) {
        super(eInternal, eName, Attribute.class, valueType);
    }

    // ----------------------------------------------------- 
    //  EFeaturePropertyDelegate implementation
    // -----------------------------------------------------

    @Override
    public EFeatureAttributeInfo getStructure() {
        return getStructure();
    }

    @Override
    protected Attribute create() {
        return new AttributeDelegate();
    }

    @Override
    protected V validate(Attribute data) {
        EFeatureStatus s;
        if (!(s = getStructure().validate(data)).isSuccess()) {
            throw new IllegalArgumentException(s.getMessage());
        }
        return eValueType.cast(data.getValue());
    }

    @Override
    protected V validate(Object value) {
        EFeatureStatus s;
        if (!(s = getStructure().validate(value)).isSuccess()) {
            throw new IllegalArgumentException(s.getMessage(), s.getCause());
        }

        // Is valid, cast to value type
        //
        return eValueType.cast(eData.getValue());
    }

    // ----------------------------------------------------- 
    //  Attribute delegate implementation
    // -----------------------------------------------------

    private class AttributeDelegate implements Attribute {

        /**
         * Cached User data
         */
        private Map<Object, Object> userData;

        @Override
        public Identifier getIdentifier() {
            return null;
        }

        @Override
        public Name getName() {
            return getType().getName();
        }

        @Override
        public boolean isNillable() {
            return getDescriptor().isNillable();
        }

        @Override
        public AttributeType getType() {
            return getDescriptor().getType();
        }

        @Override
        public AttributeDescriptor getDescriptor() {
            return getStructure().getDescriptor();
        }

        @Override
        public Object getValue() {
            return EFeatureAttributeDelegate.this.getValue();
        }

        @Override
        public void setValue(Object newValue) {
            // Cast to value type
            //
            V value = eValueType.cast(newValue);
            //
            // Update delegate
            //
            EFeatureAttributeDelegate.this.setValue(value);
        }

        @Override
        public Map<Object, Object> getUserData() {
            if (userData == null) {
                userData = new HashMap<Object, Object>();
            }
            return userData;
        }

        @Override
        public void validate() throws IllegalArgumentException {
            EFeatureAttributeDelegate.this.validate(getValue());
        }

    }

} // EFeatureAttributeDelegate
