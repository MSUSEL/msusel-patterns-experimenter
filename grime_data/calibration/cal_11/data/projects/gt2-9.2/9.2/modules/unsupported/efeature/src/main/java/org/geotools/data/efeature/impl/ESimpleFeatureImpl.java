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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.data.efeature.impl;

import static org.geotools.data.efeature.EFeatureUtils.eGetFeatureValues;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.data.efeature.internal.EFeatureDelegate;
import org.geotools.data.efeature.internal.ESimpleFeatureInternal;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;

/**
 * @author kengu - 24. juni 2011
 *
 *
 * @source $URL$
 */
public class ESimpleFeatureImpl extends SimpleFeatureImpl implements ESimpleFeature {

    /**
     * Strong cached reference to EFeature {@link EObject data object}
     */
    private EObject eObject;
    
    /**
     * Strong cached reference to EFeature {@link EFeatureInfo structure}
     */
    private EFeatureInfo eStructure;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * {@link ESimpleFeature} delegate constructor
     * @param eFeature
     * @param transaction
     */
    public ESimpleFeatureImpl(ESimpleFeature eFeature, Transaction transaction) {
        this(eFeature.eFeature().getStructure(),eFeature.eFeature(),eFeature.getID(),
                eGetFeatureValues(eFeature.eFeature().getStructure(),eFeature.eFeature(),transaction));
    }
    
    /**
     * {@link EFeature} delegate constructor
     * @param eFeature
     * @param transaction
     */
    public ESimpleFeatureImpl(EFeature eFeature, Transaction transaction) {
        this(eFeature.getStructure(),eFeature,eFeature.getID(),
                eGetFeatureValues(eFeature.getStructure(),eFeature,transaction));
    }
    
    /**
     * {@link EFeature} compatible {@link EObject} delegate constructor
     * @param eStructure
     * @param eObject
     * @param transaction
     */
    public ESimpleFeatureImpl(EFeatureInfo eStructure, EObject eObject, Transaction transaction) {
        this(eStructure,eObject,eStructure.toID(eObject),
                eGetFeatureValues(eStructure, eObject, transaction));
    }
    
    
    /**
     * Trusted constructor
     * @param eStructure
     * @param eObject
     * @param id
     * @param values
     */
    public ESimpleFeatureImpl(EFeatureInfo eStructure, EObject eObject, String eID, List<Object> values) {
        //
        // Forward to SimpleFeatureImpl
        //
        super(values.toArray(), eStructure.getFeatureType(), 
                new FeatureIdImpl(eID), false, eStructure.eGetAttributeIndex());
        //
        // Prepare
        //
        this.eObject = eObject;
        this.eStructure = eStructure;
    }

    // ----------------------------------------------------- 
    //  ESimpleFeature implementation
    // -----------------------------------------------------
    
    @Override
    public EObject eObject() {
        //
        // Return implementation, not delegate
        //
        if(eObject instanceof EFeatureDelegate) {
            return ((EFeatureDelegate)eObject).eImpl();
        }
        return eObject;
    }
    
    @Override
    public EFeature eFeature() {
        //
        // Return delegate if not a EFeature implementation  
        //
        if(eObject instanceof EFeature) {
            return (EFeature)eObject();
        }
        return new EFeatureDelegate(eStructure, (InternalEObject)eObject, true, null);
    }   
    
    @Override
    public boolean isDetached() {
        return eStructure.eHints().eValuesDetached();
    }

    @Override
    public boolean isSingleton() {
        return eStructure.eHints().eSingletonFeatures();
    }
    
    @Override
    public List<Object> read() throws IllegalStateException {
        return read(Transaction.AUTO_COMMIT);
    }
    
    @Override
    public List<Object> read(Transaction transaction) throws IllegalStateException {
        //
        // Decide if feature values is allowed to be updated from backing store
        //
        if(!isDetached()) {
            throw new IllegalStateException("ESimpleFeature " 
                    + getType().getTypeName() + " is not detached");
        }
        //
        // Read values from eImpl()
        //
        List<Object> eValues = EFeatureUtils.eGetFeatureValues(eStructure, eObject, transaction);
        //
        // Update values
        //
        values = eValues.toArray();
        //
        // Finished
        //
        return eValues;
    }

    @Override
    public List<Object> write() throws IllegalStateException {
        return write(Transaction.AUTO_COMMIT);
    }
    
    @Override
    public List<Object> write(Transaction transaction) throws IllegalStateException {            
        //
        // Decide if feature values is allowed to be updated from backing store
        //
        if(!isDetached()) {
            throw new IllegalStateException("ESimpleFeature " 
                    + getType().getTypeName() + " is not detached");
        }
        //
        // Get values as list
        //
        List<Object> eValues = getAttributes();
        //
        // Write values to eImpl()
        //
        EFeatureUtils.eSetFeatureValues(eStructure, eObject, eValues, transaction);
        //
        // Finished
        //
        return eValues;            
    }    
    
    @Override
    public boolean isReleased() {
        return eObject == null;
    }

    @Override
    public void release() {
        eObject = null;
    }
    
    // ----------------------------------------------------- 
    //  Object equality implementation
    // -----------------------------------------------------

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getID().hashCode();
        hash = 31 * hash + getFeatureType().hashCode();
        for(Object it : getAttributes()) {
            hash = (null == it ? 0 : it.hashCode());
        }
        return hash;
    }
               
    @Override
    public boolean equals(Object obj) {
        //
        // Sanity checks
        //
        if (obj == null) {
            return false;
        }
        //
        // Same as this?
        //
        if (obj == this) {
            return true;
        }
        //
        // Not same implementation?
        //        
        if (!(obj instanceof ESimpleFeatureInternal)) {
            return false;
        }
        //
        // Cast to ESimpleFeatureInternal
        //
        ESimpleFeatureInternal eFeature = (ESimpleFeatureInternal)obj;
        //
        // Get this feature ID
        //
        String eID = getID();
        //
        // This check shouldn't really be necessary since
        // by contract, all features should have an ID.
        //
        if (getID() == null) {
            if (eFeature.getIdentifier() != null) {
                return false;
            }
        }
        //
        // Is not same feature ID?
        //
        if (!eID.equals(eFeature.getIdentifier())) {
            return false;
        }
        //
        // Is not same feature type. 
        //
        if (!eFeature.getFeatureType().equals(getFeatureType())) {
            return false;
        }
        //
        // Get attribute values
        //
        List<Object> values = getAttributes();
        //
        // Check all values for inequality
        //
        for (int i = 0, count = values.size(); i < count; i++) {
            //
            // Get attribute values
            //
            Object v1 = values.get(i);
            Object v2 = eFeature.getAttribute(i);
            //
            // Do a guarded check
            //
            if (v1 == null) {
                if (v2 != null) {
                    return false;
                }
            } else {
                if (!v1.equals(v2)) {
                    return false;
                }
            }
        }
        //
        // All values are equal
        //
        return true;        
    }    
    

}
