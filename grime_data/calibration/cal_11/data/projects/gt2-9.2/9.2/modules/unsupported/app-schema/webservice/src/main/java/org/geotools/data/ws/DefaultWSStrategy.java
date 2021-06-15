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
package org.geotools.data.ws;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import java.util.Map;

import java.util.logging.Logger;

import net.opengis.wfs.GetFeatureType;

import org.geotools.data.DataUtilities;
import org.geotools.data.Query;

import org.geotools.filter.Capabilities;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.visitor.CapabilitiesFilterSplitter;
import org.geotools.util.logging.Logging;

import org.geotools.wfs.v1_1.WFSConfiguration;
import org.opengis.filter.Filter;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * @author rpetty
 * @version $Id$
 * @since 2.6
 *
 *
 *
 * @source $URL$
 *         http://gtsvn.refractions.net/trunk/modules/unsupported/app-schema/webservice/src/main
 *         /java/org/geotools/data /ws/v1_1_0/DefaultWSStrategy.java $
 */
@SuppressWarnings("nls")
public class DefaultWSStrategy implements WSStrategy {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.ws");

    private Configuration cfg;

    private Template requestTemplate;

    private final org.geotools.xml.Configuration ws_Configuration = new WFSConfiguration();

    public DefaultWSStrategy(URL templateDirectory, String templateName) {
        LOGGER.info("template directory is: " + templateDirectory);
        initialiseFreeMarkerConfiguration(templateDirectory);
        try {
            requestTemplate = cfg.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialiseFreeMarkerConfiguration(URL templateDirectory) {
        cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(DataUtilities.urlToFile(templateDirectory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    public Template getTemplate() {
        return requestTemplate;
    }

    /**
     * Creates the mapping {@link GetFeatureType GetFeature} request for the given {@link Query} and
     * {@code outputFormat}, and post-processing filter based on the server's stated filter
     * capabilities.
     * 
     * @see WSStrategy#createGetFeatureRequest(Query)
     */   
    public Map getRequestData(Query query) throws IOException {

        Map root = new HashMap();
        Filter filter = query.getFilter();
        Integer maxfeatures = query.getMaxFeatures();
        if (maxfeatures == null) {
            maxfeatures = new Integer(0);
        }
        //provide a variety of ways to express the data sent to a webservice
        //more can be added, and referenced in the template via by the name added to root.
        String filterString = filter.toString();
        String cqlFilter = CQL.toCQL(filter);
       
        LOGGER.info("Filter string: " + filterString);
        LOGGER.info("Filter CQL: " + cqlFilter);
        LOGGER.info("MaxFeatures: " + maxfeatures);
        
        root.put("filterString", filterString);
        root.put("filterCql", cqlFilter);
        // maxFeatures.toString removes commas that would otherwise appear in the result, and cause a crash.
        root.put("maxFeatures", maxfeatures.toString());
        root.put("query", query);        
        
        return root;
    }

    /**
     * @see WFSStrategy#getWfsConfiguration()
     */
    public org.geotools.xml.Configuration getWsConfiguration() {
        return ws_Configuration;
    }

    /**
     * Splits the filter provided by the geotools query into the server supported and unsupported
     * ones.
     * 
     * @param caps
     *            the server filter capabilities description
     * @param queryFilter
     * @return a two-element array where the first element is the supported filter and the second
     *         the one to post-process
     * @see WSStrategy#splitFilters(WS_Protocol, Filter)
     */
    public Filter[] splitFilters(Capabilities caps, Filter queryFilter) {
        CapabilitiesFilterSplitter splitter = new CapabilitiesFilterSplitter(caps, null, null);

        queryFilter.accept(splitter, null);

        Filter server = splitter.getFilterPre();
        Filter post = splitter.getFilterPost();

        return new Filter[] { server, post };
    }

}
