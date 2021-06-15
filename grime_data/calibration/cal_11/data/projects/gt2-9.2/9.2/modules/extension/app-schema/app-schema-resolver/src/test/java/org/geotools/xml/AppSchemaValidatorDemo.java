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
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
import java.io.InputStream;

/**
 * <p>
 * A demonstration of schema-validation in which XML schemas are downloaded from the network.
 * Schemas on the classpath are not used.
 * </p>
 * 
 * <p>
 * This demo validates the content of
 * <code>src/test/resources/test-data/validator-demo/file-to-be-validated.xml</code> against the
 * schemas declared in its <code>schemaLocation</code>. The schema-validation should report two
 * validation failures to stderr when run as an application in Eclipse.
 * </p>
 * 
 * <p>
 * To validate any other XML document with an XML Schema grammer, replace the content of
 * <code>src/test/resources/test-data/validator-demo/file-to-be-validated.xml</code> with the
 * document to be validated and run this class as an application in Eclipse. Validation requires the
 * presence of a <code>schemaLocation</code> in the instance document.
 * </p>
 * 
 * <p>
 * Schemas required for validation will be downloaded from the network and placed in
 * <code>target/test-classes/test-data/validator-demo/app-schema-cache</code>. The cache is
 * configured by the existence of
 * <code>src/test/resources/test-data/validator-demo/app-schema-cache</code>, which is copied to
 * <code>target/test-classes</code> by Eclipse and discovered by searching the ancestor directories
 * of the file under validation (also copied and found earlier on the classpath).
 * </p>
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class AppSchemaValidatorDemo {

    /**
     * The classpath resource to be schema-validated.
     */
    public static final String RESOURCE = "/test-data/validator-demo/file-to-be-validated.xml";

    /**
     * Perform the schema-validation.
     * 
     * @param args
     *            ignored
     */
    public static void main(String[] args) {
        // download and cache schemas using app-schema-cache discovered from resource path
        AppSchemaCache cache = AppSchemaCache
                .buildAutomaticallyConfiguredUsingFileUrl(AppSchemaValidatorDemo.class
                        .getResource(RESOURCE));
        // no classpath resolution of schemas; cached downloads only
        AppSchemaResolver resolver = new AppSchemaResolver(null, false, cache);
        AppSchemaValidator validator = AppSchemaValidator.buildValidator(resolver);
        InputStream input = null;
        try {
            input = AppSchemaValidator.class.getResourceAsStream(RESOURCE);
            validator.parse(input);
            // validation failures result in an RuntimeException that lists them
            validator.checkForFailures();
            System.err.println("Successful schema-validation of " + RESOURCE);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // we tried
                }
            }
        }

    }

}
