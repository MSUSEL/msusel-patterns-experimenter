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
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.crs;

import java.util.Set;

import org.geotools.factory.AbstractFactory;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.Citations;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.DerivedCRS;
import org.opengis.referencing.crs.EngineeringCRS;
import org.opengis.referencing.crs.GeocentricCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ImageCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.util.InternationalString;

/**
 * A disabled version of {@link EPSGCRSAuthorityFactory} that exists solely as a workaround for
 * Eclipse capture of transitive dependencies. The real implementation in gt-epsg-wkt; Maven does
 * the right thing and does not include the provider at test time, but Eclipse captures it as a
 * transitive dependency, causing the wrong provider to be used for srsName encoding. The presence
 * of this class allows gt-xsd-gml3 unit tests to pass in Eclipse.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 * @see <a href="http://jira.codehaus.org/browse/GEOT-3112">GEOT-3112</a>
 *
 *
 * @source $URL$
 */
public class EPSGCRSAuthorityFactory extends AbstractFactory implements CRSAuthorityFactory {

    private static final String EXCEPTION_MESSAGE = "Factory disabled for gt-xsd-gml3 testing (see GEOT-3112)";

    public EPSGCRSAuthorityFactory() {
        super(MINIMUM_PRIORITY);
    }

    public Citation getAuthority() {
        CitationImpl c = new CitationImpl(EXCEPTION_MESSAGE);
        c.freeze();
        return c;
    }

    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type)
            throws FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public InternationalString getDescriptionText(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public IdentifiedObject createObject(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public Citation getVendor() {
        return Citations.GEOTOOLS;
    }

    public CoordinateReferenceSystem createCoordinateReferenceSystem(String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public CompoundCRS createCompoundCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public DerivedCRS createDerivedCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public EngineeringCRS createEngineeringCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public GeographicCRS createGeographicCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public GeocentricCRS createGeocentricCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public ImageCRS createImageCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public ProjectedCRS createProjectedCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public TemporalCRS createTemporalCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    public VerticalCRS createVerticalCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

}
