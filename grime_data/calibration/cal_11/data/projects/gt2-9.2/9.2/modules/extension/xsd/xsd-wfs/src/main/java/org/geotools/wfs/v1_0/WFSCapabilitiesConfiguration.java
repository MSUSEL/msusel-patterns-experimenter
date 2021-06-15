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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v1_0;

/**
 * Parser configuration for the wfs 1.0 {@link WFSCapabilities schema}, different from the
 * {@link WFSConfiguration} because WFS 1.0 bases capabilities document and request/response
 * documents on different schemas.
 * 
 * @see WFSCapabilities
 */
public class WFSCapabilitiesConfiguration extends org.geotools.wfs.WFSConfiguration {
    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public WFSCapabilitiesConfiguration() {
        super(WFSCapabilities.getInstance());

        addDependency(new org.geotools.filter.v1_0.capabilities.OGCConfiguration());
    }

    protected void configureBindings(org.picocontainer.MutablePicoContainer container) {

        container
                .registerComponentImplementation(WFSCapabilities.Service, ServiceTypeBinding.class);
        container.registerComponentImplementation(WFSCapabilities.LatLongBoundingBox,
                LatLongBoundingBoxBinding.class);

        container.registerComponentImplementation(WFSCapabilities.DCPType, DCPTypeBinding.class);
        container.registerComponentImplementation(WFSCapabilities.Capability,
                CapabilityBinding.class);
    }
}
