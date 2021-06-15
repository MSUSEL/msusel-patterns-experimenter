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
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import org.opengis.annotation.XmlElement;

/**
 * The OverlapBehavior element tells a system how to behave when multiple
 * raster images in a layer  overlap each other, for example with
 * satellite-image scenes. LATEST_ON_TOP and EARLIEST_ON_TOP refer to the
 * time the scene was captured.   AVERAGE means to average multiple scenes
 * together.   This can produce blurry results if the source images are
 * not perfectly aligned in their geo-referencing. RANDOM means to select
 * an image (or piece thereof) randomly and place it on top.  This can
 * produce crisper  results than AVERAGE potentially more efficiently than
 * LATEST_ON_TOP or EARLIEST_ON_TOP.   The default behaviour is
 * system-dependent.
 *
 * @return LATEST_ON_TOP, EARLIEST_ON_TOP, AVERAGE or RANDOM
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2  
 */
@XmlElement("OverlapBehavior")
public enum OverlapBehavior {
    LATEST_ON_TOP, 
    EARLIEST_ON_TOP, 
    AVERAGE,
    RANDOM
}
