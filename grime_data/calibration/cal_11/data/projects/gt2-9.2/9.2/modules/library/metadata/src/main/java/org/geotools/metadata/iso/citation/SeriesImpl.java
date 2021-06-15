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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.citation;

import org.opengis.metadata.citation.Series;
import org.opengis.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;
import org.geotools.util.SimpleInternationalString;


/**
 * Information about the series, or aggregate dataset, to which a dataset belongs.
 *
 * @author Jody Garnett
 * @author Martin Desruisseaux
 *
 * @since 2.1
 *
 *
 * @source $URL$
 */
public class SeriesImpl extends MetadataEntity implements Series {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 2784101441023323052L;

    /**
     * Name of the series, or aggregate dataset, of which the dataset is a part.
     */
    private InternationalString name;

    /**
     * Information identifying the issue of the series.
     */
    private String issueIdentification;

    /**
     * Details on which pages of the publication the article was published.
     */
    private String page;

    /**
     * Constructs a default series.
     */
    public SeriesImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public SeriesImpl(final Series source) {
        super(source);
    }

    /**
     * Constructs a series with the specified name.
     */
    public SeriesImpl(final CharSequence name) {
        final InternationalString n;
        if (name instanceof InternationalString) {
            n = (InternationalString) name;
        } else {
            n = new SimpleInternationalString(name.toString());
        }
        setName(n);
    }

    /**
     * Returne the name of the series, or aggregate dataset, of which the dataset is a part.
     */
    public InternationalString getName() {
        return name;
    }

    /**
     * Set the name of the series, or aggregate dataset, of which the dataset is a part.
     */
    public synchronized void setName(final InternationalString newValue) {
        checkWritePermission();
        name = newValue;
    }

    /**
     * Returns information identifying the issue of the series.
     */
    public String getIssueIdentification() {
        return issueIdentification;
    }

    /**
     * Set information identifying the issue of the series.
     */
    public synchronized void setIssueIdentification(final String newValue) {
        checkWritePermission();
        issueIdentification = newValue;
    }

    /**
     * Returns details on which pages of the publication the article was published.
     */
    public String getPage() {
        return page;
    }

    /**
     * Set details on which pages of the publication the article was published.
     */
    public synchronized void setPage(final String newValue) {
        checkWritePermission();
        page = newValue;
    }
}
