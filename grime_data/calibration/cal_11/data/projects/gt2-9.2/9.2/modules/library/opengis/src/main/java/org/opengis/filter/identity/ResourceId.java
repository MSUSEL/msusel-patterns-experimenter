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
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.identity;

import java.util.Date;

import org.opengis.annotation.XmlElement;

/**
 * Resource identifier as per FES 2.0.
 * <p>
 * Please note this is a query object for use with the Filter <b>Id</b> filter as shown:
 * <pre>Filter filter = filterFactory.id(
 *    ff.featureId("CITY.123"),
 *    ff.resourceId("CITY.123",Version.Action.PREVIOUS) );</pre>
 * In cases where a plain FetureId is used for lookup it is understood to refer to
 * Version.Action.LAST.
 * <p>
 * If an implementation that references this International Standard does not support versioning, any
 * value specified for the attributes {@link #getPreviousRid() previousRid}, {@link #getVersion()
 * version}, {@link #getStartTime() startTime}, and {@link #getEndTime() endTime} shall be ignored
 * and the predicate shall always select the single version that is available.
 * </p>
 */
@XmlElement("ResourceId")
public interface ResourceId extends FeatureId {

    /**
     * Used to navigate versions of a resource.
     * <p>
     * 
     * @return Version based resource query; non {@code null} but possibly {@link Version#isEmpty()
     *         empty} if used a date range query or asked for a specific feature id + version id
     */
    @XmlElement("version")
    Version getVersion();

    /**
     * Used to select versions of a resource between start and end time. </p>
     * 
     * @return start time for a time based query; or {@code null} if using version or an end time
     *         was provided but the start time is unconstrained
     * TODO: consider using an org.geotools.util.Range<Date> instead of both start and end time?
     */
    @XmlElement("startTime")
    Date getStartTime();

    /**
     * Used to select versions of a resource between start and end time.
     * 
     * @return end time for a time based query; or {@code null} if using version or an start time
     *         was provided but the end time is unconstrained
     * TODO: consider using an org.geotools.util.Range<Date> instead of both start and end time?
     */
    @XmlElement("endTime")
    Date getEndTime();
    
}