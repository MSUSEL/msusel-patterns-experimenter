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
package org.geotools.data.wfs.internal;

import static org.geotools.data.wfs.impl.WFSDataStoreFactory.BUFFER_SIZE;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.ENCODING;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.FILTER_COMPLIANCE;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.LENIENT;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.MAXFEATURES;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.NAMESPACE;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.PASSWORD;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.PROTOCOL;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.TIMEOUT;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.TRY_GZIP;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.USERNAME;
import static org.geotools.data.wfs.impl.WFSDataStoreFactory.WFS_STRATEGY;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @see WFSStrategy#setConfig(WFSConfig)
 */
public class WFSConfig {

    private String user;

    private String pass;

    private int timeoutMillis;

    private PreferredHttpMethod preferredMethod;

    private int buffer;

    private boolean tryGZIP;

    private boolean lenient;

    private Integer maxFeatures;

    private Charset defaultEncoding;

    private String wfsStrategy;

    private Integer filterCompliance;

    private String namespaceOverride;

    public static enum PreferredHttpMethod {
        AUTO, HTTP_GET, HTTP_POST
    }

    public WFSConfig() {
        preferredMethod = PreferredHttpMethod.AUTO;
        timeoutMillis = (Integer) TIMEOUT.getDefaultValue();
        buffer = (Integer) BUFFER_SIZE.getDefaultValue();
        tryGZIP = (Boolean) TRY_GZIP.getDefaultValue();
        lenient = (Boolean) LENIENT.getDefaultValue();
        String encoding = (String) ENCODING.getDefaultValue();
        defaultEncoding = Charset.forName(encoding);
        maxFeatures = (Integer) MAXFEATURES.getDefaultValue();
        wfsStrategy = (String) WFS_STRATEGY.getDefaultValue();
        filterCompliance = (Integer) FILTER_COMPLIANCE.getDefaultValue();
        namespaceOverride = (String) NAMESPACE.getDefaultValue();
    }

    public static WFSConfig fromParams(Map<?, ?> params) throws IOException {

        WFSConfig config = new WFSConfig();

        Boolean preferPost = (Boolean) PROTOCOL.lookUp(params);

        if (preferPost == null) {
            config.preferredMethod = PreferredHttpMethod.AUTO;
        } else {
            config.preferredMethod = preferPost.booleanValue() ? PreferredHttpMethod.HTTP_POST
                    : PreferredHttpMethod.HTTP_GET;
        }

        config.preferredMethod = PreferredHttpMethod.HTTP_POST;// remove this line

        config.user = (String) USERNAME.lookUp(params);
        config.pass = (String) PASSWORD.lookUp(params);
        config.timeoutMillis = (Integer) TIMEOUT.lookUp(params);
        config.buffer = (Integer) BUFFER_SIZE.lookUp(params);
        config.tryGZIP = (Boolean) TRY_GZIP.lookUp(params);
        config.lenient = (Boolean) LENIENT.lookUp(params);

        String encoding = (String) ENCODING.lookUp(params);
        config.defaultEncoding = Charset.forName(encoding);

        config.maxFeatures = (Integer) MAXFEATURES.lookUp(params);
        config.wfsStrategy = (String) WFS_STRATEGY.lookUp(params);
        config.filterCompliance = (Integer) FILTER_COMPLIANCE.lookUp(params);
        config.namespaceOverride = (String) NAMESPACE.lookUp(params);

        return config;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the pass
     */
    public String getPassword() {
        return pass;
    }

    /**
     * @return the timeoutMillis
     */
    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    /**
     * @return the preferredMethod
     */
    public PreferredHttpMethod getPreferredMethod() {
        return preferredMethod;
    }

    /**
     * @return the buffer
     */
    public int getBuffer() {
        return buffer;
    }

    /**
     * @return the tryGZIP
     */
    public boolean isTryGZIP() {
        return tryGZIP;
    }

    /**
     * @return the lenient
     */
    public boolean isLenient() {
        return lenient;
    }

    /**
     * @return the maxFeatures
     */
    public Integer getMaxFeatures() {
        return maxFeatures;
    }

    /**
     * @return the defaultEncoding
     */
    public Charset getDefaultEncoding() {
        return defaultEncoding;
    }

    /**
     * @return the wfsStrategy
     */
    public String getWfsStrategy() {
        return wfsStrategy;
    }

    /**
     * @return the filterCompliance
     */
    public Integer getFilterCompliance() {
        return filterCompliance;
    }

    /**
     * @return the namespaceOverride
     */
    public String getNamespaceOverride() {
        return namespaceOverride;
    }
}
