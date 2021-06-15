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
import org.geotools.data.efeature.EFeatureGeometry;
import org.geotools.data.efeature.EFeatureGeometryInfo;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.Identifier;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Geometry;

/**
 * An abstract implementation of the model data type {@link EFeatureGeometry}.
 * 
 * @author kengu, 22. apr. 2011
 *
 * @source $URL$
 */
public class EFeatureGeometryDelegate<V extends Geometry> extends
        EFeaturePropertyDelegate<V, GeometryAttribute, EAttribute> implements EFeatureGeometry<V> {

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
    public EFeatureGeometryDelegate(EFeatureInternal eInternal,
            String eName, Class<V> valueType) {
        super(eInternal, eName, GeometryAttribute.class, valueType);
    }

    // ----------------------------------------------------- 
    //  EFeatureGeometry implementation
    // -----------------------------------------------------

    @Override
    public boolean isEmpty() {
        Geometry geom = getValue();
        return (geom == null || geom.isEmpty());
    }

    @Override
    public boolean isDefault() {
        return getStructure().isDefaultGeometry();
    }

    // ----------------------------------------------------- 
    //  EFeaturePropertyDelegate implementation
    // -----------------------------------------------------

    @Override
    public String getName() {
        return getStructure().eName();
    }

    @Override
    public EFeatureGeometryInfo getStructure() {
        return (EFeatureGeometryInfo) super.getStructure();
    }

    @Override
    protected GeometryAttribute create() {
        return new GeometryAttributeDelegate();
    }

    @Override
    protected V validate(GeometryAttribute data) {
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
    //  GeometryAttribute delegate implementation
    // -----------------------------------------------------


    private class GeometryAttributeDelegate implements GeometryAttribute {

        /**
         * bounds, derived
         */
        private BoundingBox bounds;

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
        public GeometryType getType() {
            return getDescriptor().getType();
        }

        @Override
        public GeometryDescriptor getDescriptor() {
            return getStructure().getDescriptor();
        }

        @Override
        public Object getValue() {
            return EFeatureGeometryDelegate.this.getValue();
        }

        @Override
        public void setValue(Object newValue) {
            // Cast to value type
            //
            V value = eValueType.cast(newValue);
            //
            // Update delegate
            //
            EFeatureGeometryDelegate.this.setValue(value);
            // Geometry has changed, hence bounds must be recalculated
            //
            bounds = null;
        }

        @Override
        public BoundingBox getBounds() {
            if (bounds == null) {
                ReferencedEnvelope bbox = new ReferencedEnvelope(getType()
                        .getCoordinateReferenceSystem());
                V geom = EFeatureGeometryDelegate.this.getValue();
                if (geom != null) {
                    bbox.expandToInclude(geom.getEnvelopeInternal());
                } else {
                    bbox.setToNull();
                }
                bounds = bbox;
            }
            return bounds;
        }

        @Override
        public void setBounds(BoundingBox bounds) {
            this.bounds = bounds;
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
            EFeatureGeometryDelegate.this.validate(getValue());
        }

    }

} // EFeatureGeometryDelegate
