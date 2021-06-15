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
package org.geotools.wcs.bindings;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.gml.DirectPositionType;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.Gml4wcsFactory;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.Wcs10Factory;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:SpatialSubsetType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;SpatialSubsetType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Definition of a subset of a coverage spatial domain. Currently, only a grid subset of a coverage domain. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base=&quot;wcs:SpatialDomainType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref=&quot;gml:Envelope&quot;/&gt;
 *                  &lt;element ref=&quot;gml:Grid&quot;/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/restriction&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class SpatialSubsetTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return WCS.SpatialSubsetType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return SpatialSubsetType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        SpatialSubsetType spatialSubset = Wcs10Factory.eINSTANCE.createSpatialSubsetType();
        
        List<Node> envelopes = node.getChildren("Envelope");
        for (Node envelopeNode : envelopes) {
            ReferencedEnvelope envelope = (ReferencedEnvelope) envelopeNode.getValue();
            EnvelopeType env = Gml4wcsFactory.eINSTANCE.createEnvelopeType();
            
            env.setSrsName(CRS.lookupIdentifier(envelope.getCoordinateReferenceSystem(), true));
            
            DirectPositionType pos1 = Gml4wcsFactory.eINSTANCE.createDirectPositionType();
            DirectPositionType pos2 = Gml4wcsFactory.eINSTANCE.createDirectPositionType();
            
            pos1.setDimension(BigInteger.valueOf(2));
            pos2.setDimension(BigInteger.valueOf(2));
            
            pos1.setValue(Arrays.asList(envelope.getMinX(), envelope.getMinY()));
            pos2.setValue(Arrays.asList(envelope.getMaxX(), envelope.getMaxY()));

            env.getPos().add(pos1);
            env.getPos().add(pos2);
            
            spatialSubset.getEnvelope().add(envelope);
        }
        
        List<Node> gridsNode = node.getChildren("Grid");
        for (Node grid : gridsNode)
            spatialSubset.getGrid().add(grid.getValue());
        
        return spatialSubset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.xml.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

}
