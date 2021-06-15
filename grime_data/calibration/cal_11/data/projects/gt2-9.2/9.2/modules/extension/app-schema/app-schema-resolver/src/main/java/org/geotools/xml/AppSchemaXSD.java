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
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.xml.SchemaLocationResolver;
import org.geotools.xml.XSD;

/**
 * {@link XSD} that uses {@link AppSchemaResolver} to locate schema resources in a catalog, on the
 * classpath, or in a cache.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @author Niels Charlier (Curtin University of Technology)
 *
 *
 *
 * @source $URL$
 */
public class AppSchemaXSD extends XSD {

    private final String namespaceUri;

    private final String schemaLocation;

    /**
     * The resolver used to locate resources.
     */
    private final AppSchemaResolver resolver;

    /**
     * The {@link Configuration} used to encode documents with this schema.
     */
    private AppSchemaConfiguration configuration;

    /**
     * @param namespaceUri
     * @param schemaLocation
     * @param resolver
     */
    public AppSchemaXSD(String namespaceUri, String schemaLocation, AppSchemaResolver resolver) {
        this.namespaceUri = namespaceUri;
        this.schemaLocation = resolver.resolve(schemaLocation);
        this.resolver = resolver;
    }

    /**
     * @see org.geotools.xml.XSD#getNamespaceURI()
     */
    @Override
    public String getNamespaceURI() {
        return namespaceUri;
    }

    /**
     * @see org.geotools.xml.XSD#getSchemaLocation()
     */
    @Override
    public String getSchemaLocation() {
        return schemaLocation;
    }

    /**
     * @param configuration
     */
    public void setConfiguration(AppSchemaConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @see org.geotools.xml.XSD#createSchemaLocationResolver()
     */
    @Override
    public SchemaLocationResolver createSchemaLocationResolver() {
        return new AppSchemaLocationResolver(resolver);
    }

    /**
     * @see org.geotools.xml.XSD#addDependencies(java.util.Set)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void addDependencies(Set dependencies) {
        if (configuration != null) {
            for (Configuration dependency : (List<Configuration>) configuration.getDependencies()) {
                dependencies.add(dependency.getXSD());
            }
        }
    }
    
    @Override
    public SchemaLocator createSchemaLocator() {
        return new SchemaLocator(this) {
            public boolean canHandle( XSDSchema schema, String namespaceURI,
                    String rawSchemaLocationURI, String resolvedSchemaLocationURI) {
                return xsd.getNamespaceURI().equals(namespaceURI) &&
                        xsd.getSchemaLocation().equals(resolvedSchemaLocationURI); 
            }
        };
    }
    
    @Override
    public XSDSchemaLocator getSupplementarySchemaLocator() {
       return AppSchemaXSDRegistry.getInstance();
    }
    
    @Override
    protected XSDSchema buildSchema() throws IOException {
        //check if schema already exists in registry, if so do not build
        XSDSchema schema = AppSchemaXSDRegistry.getInstance().lookUp(schemaLocation);
        if (schema == null) {
            schema = super.buildSchema();
            //register schema
            AppSchemaXSDRegistry.getInstance().register(schema);
        } else {
            //reset because included schema's are not always complete 
            schema.reset();
        }
        return schema;
    }

}
