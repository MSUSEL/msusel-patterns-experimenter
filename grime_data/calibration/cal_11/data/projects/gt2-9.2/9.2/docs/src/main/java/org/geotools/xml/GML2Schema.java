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
package org.geotools.xml;

import java.util.List;

import org.opengis.feature.Feature;

import com.vividsolutions.jts.geom.Envelope;

/**
 * The following is a placeholder simply to make a UML Diagram.
 * @author jody
 *
 */
public class GML2Schema {
    /**
     * An abstract feature provides a set of common properties. A concrete feature type must derive from
     * this type and specify additional properties in an application schema. A feature may optionally
     * possess an identifying attribute ('fid').
     */
    public static interface AbstractFeatureType {
        /** optional */
        public String description();
        
        /** optional */
        
        public String name();
        
        /** optional */
        public Envelope boundedBy();
    }
    /** A feature collection contains zero or more featureMember elements. */
    public static interface AbstractFeatureCollectionType {
        List<Feature> featureMemeber();
    }
}