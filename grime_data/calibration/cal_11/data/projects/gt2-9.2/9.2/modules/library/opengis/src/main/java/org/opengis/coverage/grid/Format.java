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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage.grid;

import org.opengis.parameter.ParameterValueGroup;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A discovery mechanism to determine the formats supported by a {@link GridCoverageExchange}
 * implementation. A {@code GridCoverageExchange} implementation can support a number of
 * file format or resources.
 *
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 *
 */
@UML(identifier="CV_Format", specification=OGC_01004)
public interface Format {
    /**
     * Name of the file format.
     */
    @UML(identifier="name", obligation=MANDATORY, specification=OGC_01004)
    String getName();

    /**
     * Description of the file format.
     * If no description, the value will be {@code null}.
     */
    @UML(identifier="description", obligation=OPTIONAL, specification=OGC_01004)
    String getDescription();

    /**
     * Vendor or agency for the format.
     */
    @UML(identifier="vendor", obligation=OPTIONAL, specification=OGC_01004)
    String getVendor();

    /**
     * Documentation URL for the format.
     */
    @UML(identifier="docURL", obligation=OPTIONAL, specification=OGC_01004)
    String getDocURL();

    /**
     * Version number of the format.
     */
    @UML(identifier="version", obligation=OPTIONAL, specification=OGC_01004)
    String getVersion();

    /**
     * Retrieve the parameter information for a {@link GridCoverageReader#read read} operation.
     */
    @UML(identifier="getParameterInfo, numParameters", obligation=MANDATORY, specification=OGC_01004)
    ParameterValueGroup getReadParameters();

    /**
     * Retrieve the parameter information for a {@link GridCoverageWriter#write write} operation.
     */
    @UML(identifier="getParameterInfo, numParameters", obligation=MANDATORY, specification=OGC_01004)
    ParameterValueGroup getWriteParameters();
}
