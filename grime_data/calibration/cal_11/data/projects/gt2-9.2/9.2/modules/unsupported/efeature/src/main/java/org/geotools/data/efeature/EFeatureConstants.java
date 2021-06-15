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
package org.geotools.data.efeature;

import org.geotools.data.efeature.impl.EFeatureImpl;
import org.opengis.feature.Feature;

/**
 * 
 *
 * @source $URL$
 */
public class EFeatureConstants {
    
    public static final String DEFAULT_SRID = EFeatureImpl.DEFAULT_SRID;

    public static final String DEFAULT_GEOMETRY_NAME = EFeatureImpl.DEFAULT_GEOMETRY_NAME;

    public static final boolean DEFAULT_IS_SIMPLE = EFeatureImpl.DEFAULT_IS_SIMPLE;

    public static final Feature DEFAULT_FEATURE = EFeatureImpl.DEFAULT_DATA;

    public static final EFeatureInfo DEFAULT_FEATURE_STRUCTURE = EFeatureImpl.DEFAULT_FEATURE_STRUCTURE;

    public static final String ELEM_REGISTRY = "registry";

    public static final String ELEM_STORE = "store";

    public static final String ELEM_FOLDER = "folder";

    public static final String ELEM_FEATURE = "feature";

    public static final String ELEM_GEOMETRY = "geometry";

    public static final String ELEM_ATTRIBUTE = "attribute";

    public static final String ATTR_NS_URI = "nsURI";

    public static final String ATTR_NAME = "name";

    public static final String ATTR_CLASS = "class";

    public static final String ATTR_IS_OBJECT = "isObject";

    public static final String ATTR_ID = "id";

    public static final String ATTR_REFERENCE = "reference";

    public static final String ATTR_IS_DEFAULT = "isDefault";

    public static final String ATTR_TYPE = "type";

    public static final String ATTR_SRID = "srid";

    public static final String ATTR_REGISTRY_ID = "registryID";

    public static final String ATTR_DOMAIN_ID = "domainID";

}
