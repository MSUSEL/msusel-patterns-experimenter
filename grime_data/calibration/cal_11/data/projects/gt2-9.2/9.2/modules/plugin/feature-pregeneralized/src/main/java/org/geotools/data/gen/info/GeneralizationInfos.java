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

package org.geotools.data.gen.info;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Christian Mueller
 * 
 * Container for for GeneralizationInfo objects
 * 
 *
 *
 *
 * @source $URL$
 */
public class GeneralizationInfos {
    private Map<String, GeneralizationInfo> infoMap;

    private String dataSourceName, dataSourceNameSpace;

    public GeneralizationInfos() {
        infoMap = new HashMap<String, GeneralizationInfo>();
    }

    /**
     * add a GeneralizationInfo object
     * 
     * @param info
     * 
     */
    public void addGeneralizationInfo(GeneralizationInfo info) {
        infoMap.put(info.getBaseFeatureName(), info);
    }

    /**
     * remove a GeneralizationInfo object
     * 
     * @param info
     */
    public void removeGeneralizationInfo(GeneralizationInfo info) {
        infoMap.remove(info.getBaseFeatureName());
    }

    /**
     * get GeneralizationInfo for baseFeatureName
     * 
     * @see GeneralizationInfo for info about baseFeatureName
     * 
     * @param baseFeatureName
     * @return GeneralizationInfo or null
     */
    public GeneralizationInfo getGeneralizationInfoForBaseFeatureName(String baseFeatureName) {
        return infoMap.get(baseFeatureName);
    }

    /**
     * get GeneralizationInfo for featureName
     * 
     * @see GeneralizationInfo for info about featureName
     * 
     * @param featureName
     * @return GeneralizationInfo or null
     */
    public GeneralizationInfo getGeneralizationInfoForFeatureName(String featureName) {
        for (GeneralizationInfo info : infoMap.values())
            if (info.getFeatureName().equals(featureName))
                return info;
        return null;
    }

    /**
     * @see GeneralizationInfo for info about basefeatureName
     * 
     * @return list of base feature names
     */
    public Collection<String> getBaseFeatureNames() {
        TreeSet<String> names = new TreeSet<String>();
        names.addAll(infoMap.keySet());
        return names;
    }

    /**
     * @see GeneralizationInfo for info about featureName
     * 
     * @return list of feature names
     */

    public Collection<String> getFeatureNames() {
        TreeSet<String> names = new TreeSet<String>();
        for (GeneralizationInfo info : infoMap.values())
            names.add(info.getFeatureName());
        return names;
    }

    /**
     * This data source is the default data source for all GeneraliziationInfo objects in this
     * container
     * 
     * @return the data source name or null
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * This workspace is the default workspace for all GeneraliziationInfo objects in this container
     * 
     * @return the namespace name or null
     */

    public String getDataSourceNameSpace() {
        return dataSourceNameSpace;
    }

    public void setDataSourceNameSpace(String namespace) {
        this.dataSourceNameSpace = namespace;
    }

    public Collection<GeneralizationInfo> getGeneralizationInfoCollection() {
        return infoMap.values();
    }

    /**
     * 
     * @throws IOException
     *             if the validation of the generalization info objects fails
     * 
     */
    public void validate() throws IOException {
        for (GeneralizationInfo gi : getGeneralizationInfoCollection()) {
            gi.validate();
        }
    }

}
