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

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.opengis.filter.expression.Literal;

/**
 * 
 *
 * @source $URL$
 */
public class EAttributeValueIsOutside extends EObjectAttributeValueCondition {

    public EAttributeValueIsOutside(EAttribute eAttribute, Literal lower, Literal upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(eAttribute.getEAttributeType(), lower, upper));
    }
        
    public EAttributeValueIsOutside(EAttribute eAttribute, Object lower, Object upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(eAttribute.getEAttributeType(), lower, upper));
    }
   
    public EAttributeValueIsOutside(EAttribute eAttribute, Number lower, Number upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(lower, upper));
    }
    
    public EAttributeValueIsOutside(EAttribute eAttribute, Date lower, Date upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(lower, upper));
    }
    
    public EAttributeValueIsOutside(EAttribute eAttribute, String lower, String upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(lower, upper));
    }
    
    public EAttributeValueIsOutside(EAttribute eAttribute, Character lower, Character upper)
            throws EFeatureEncoderException {
        super(eAttribute, ConditionEncoder.outside(lower, upper));
    }    

}
