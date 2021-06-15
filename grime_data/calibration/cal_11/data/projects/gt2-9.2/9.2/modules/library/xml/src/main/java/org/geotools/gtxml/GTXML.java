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
package org.geotools.gtxml;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.geotools.xml.Configuration;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Operates as a front end to GTXML parser/encoder services.
 * <p>
 * This is a simple utility class; if you need more control please look at the implementation of
 * the provided methods.
 *
 *
 * @source $URL$
 */
public class GTXML {
    /**
     * Parse a feature type; using the provided configuration.
     * <p>
     * Usually the configuration is based on org.geotools.wfs.v1_0.WFSConfiguration or
     * org.geotools.wfs.v1_1.WFSConfiguration; you need to indicate which name you want parsed
     * out as a FetureType.
     * 
     * @param configuration wfs configuration to use
     * @param name name to parse out as a feature type
     * @param schema xsd schema to parse
     * @param crs Optional coordinate reference system for generated feature type
     * @return FeatureType
     * @throws IOException
     */
    public static FeatureType parseFeatureType( Configuration configuration, QName name, CoordinateReferenceSystem crs  ) throws IOException {
        return EmfAppSchemaParser.parse(configuration, name, crs);
    }
    
}
