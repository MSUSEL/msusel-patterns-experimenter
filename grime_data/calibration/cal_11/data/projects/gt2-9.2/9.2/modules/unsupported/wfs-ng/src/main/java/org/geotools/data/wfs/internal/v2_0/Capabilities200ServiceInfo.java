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
package org.geotools.data.wfs.internal.v2_0;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import net.opengis.ows11.OnlineResourceType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.ServiceProviderType;
import net.opengis.wfs20.WFSCapabilitiesType;

import org.geotools.data.ServiceInfo;
import org.geotools.data.wfs.impl.WFSServiceInfo;

/**
 * Adapts a WFS capabilities document to {@link ServiceInfo}
 */
public final class Capabilities200ServiceInfo implements WFSServiceInfo {

    private final WFSCapabilitiesType capabilities;

    private final URI schemaUri;

    private final URI getCapsUrl;

    public Capabilities200ServiceInfo(String schemaUri, URL getCapsUrl,
            WFSCapabilitiesType capabilities) {
        try {
            this.getCapsUrl = getCapsUrl.toURI();
            this.schemaUri = new URI(schemaUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.capabilities = capabilities;
    }

    /**
     * Maps to the capabilities' service identification abstract
     * 
     * @see ServiceInfo#getDescription()
     */
    public String getDescription() {
        ServiceIdentificationType serviceIdentification = capabilities.getServiceIdentification();
        if (serviceIdentification == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<String> abs = serviceIdentification.getAbstract();
        return abs == null || abs.isEmpty() ? null : abs.get(0);
    }

    /**
     * @return {@code null}
     * @see ServiceInfo#getDescription()
     */
    public Icon getIcon() {
        return null; // talk to Eclesia the icons are in renderer?
    }

    /**
     * Maps to the capabilities' service identification keywords list
     * 
     * @see ServiceInfo#getDescription()
     */
    public Set<String> getKeywords() {
        Set<String> kws = new HashSet<String>();
        ServiceIdentificationType serviceIdentification = capabilities.getServiceIdentification();
        if (serviceIdentification != null) {
            @SuppressWarnings("unchecked")
            List<String> keywords = serviceIdentification.getKeywords();
            if (keywords != null) {
                kws.addAll(keywords);
                kws.remove(null);
            }
        }
        return kws;
    }

    /**
     * @see ServiceInfo#getPublisher()
     */
    public URI getPublisher() {
        ServiceProviderType serviceProvider = capabilities.getServiceProvider();
        if (null == serviceProvider) {
            return null;
        }
        OnlineResourceType providerSite = serviceProvider.getProviderSite();
        if (null == providerSite) {
            return null;
        }
        String href = providerSite.getHref();
        try {
            return href == null ? null : new URI(href);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Maps to the WFS xsd schema in schemas.opengis.net
     * 
     * @see ServiceInfo#getSchema()
     */
    public URI getSchema() {
        return schemaUri;
    }

    /**
     * Maps to the URL of the capabilities document
     * 
     * @see ServiceInfo#getSource()
     */
    public URI getSource() {
        return getCapsUrl;
    }

    /**
     * @see ServiceInfo#getTitle()
     */
    public String getTitle() {
        ServiceIdentificationType serviceIdentification = capabilities.getServiceIdentification();
        if (serviceIdentification == null || serviceIdentification.getTitle() == null
                || serviceIdentification.getTitle().isEmpty()) {
            return null;
        }
        return String.valueOf(serviceIdentification.getTitle().get(0));
    }

    /**
     * @see WFSServiceInfo#getVersion()
     */
    public String getVersion() {
        return capabilities.getVersion();
    }
}
