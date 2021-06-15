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
package org.geotools.data.efeature.query;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.geotools.data.efeature.EFeatureUtils;
import org.opengis.feature.Feature;
import org.opengis.filter.identity.Identifier;

/**
 * 
 *
 * @source $URL$
 */
public class EAttributeValueIsID extends EObjectAttributeValueCondition {

    public EAttributeValueIsID(EAttribute eAttribute, Object eIDs) throws EFeatureEncoderException {        
        this(eAttribute, toEIDs(eIDs));
    }

    public EAttributeValueIsID(EAttribute eAttribute, String... eIDs) throws EFeatureEncoderException {
        this(eAttribute, EFeatureUtils.toEIDs((Object[])eIDs));
    }
    
    public EAttributeValueIsID(EAttribute eAttribute, Set<Identifier> eIDs) throws EFeatureEncoderException {
        super(eAttribute, eq(eIDs));
    }
    
    public static final Set<Identifier> toEIDs(Object eIDs) throws IllegalArgumentException {
        if( !(eIDs instanceof Set)) {
            throw new IllegalArgumentException("eIDs must be an instance of java.util.Set");
        }
        Set<Identifier> eIDSet = new HashSet<Identifier>();
        for(Object it : (Set<?>)eIDs) {
            if( !(it instanceof Identifier)) {
                throw new IllegalArgumentException("Items in set must implement " +
                		"org.opengis.filter.identity.Identifier");
            }
            eIDSet.add((Identifier)it);
        }
        return eIDSet;
    }
    
    public static final Condition eq(final Set<Identifier> eIDs) {
        return new Condition() {
            
            @Override
            public boolean isSatisfied(Object value) {
                if(value instanceof Feature) {
                    for(Identifier it : eIDs) {
                        if(it.matches(value)) 
                            return true;
                    }
                } else if(value instanceof String) {
                    for(Identifier it : eIDs) {
                        if(it.getID().equals(value)) 
                            return true;
                    }
                }

                return false;
            }
        };
    }
    
}
