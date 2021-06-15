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
package org.geotools.styling.builder;

import org.geotools.styling.RemoteOWS;

/**
 * 
 *
 * @source $URL$
 */
public class RemoteOWSBuilder extends AbstractSLDBuilder<RemoteOWS> {
    private String service;

    private String onlineResource;

    public RemoteOWSBuilder() {
        this(null);
    }

    public RemoteOWSBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
        reset();
    }

    public RemoteOWSBuilder resource(String onlineResource) {
        this.onlineResource = onlineResource;
        this.unset = false;
        return this;
    }

    public RemoteOWSBuilder service(String service) {
        this.service = service;
        this.unset = false;
        return this;
    }

    public RemoteOWS build() {
        if (unset) {
            return null;
        }
        RemoteOWS remote = sf.createRemoteOWS(service, onlineResource);
        return remote;
    }

    public RemoteOWSBuilder reset() {
        unset = true;
        this.onlineResource = null;
        this.service = null;
        return this;
    }

    public RemoteOWSBuilder reset(RemoteOWS remote) {
        if (remote == null) {
            return unset();
        }
        this.onlineResource = remote.getOnlineResource();
        this.service = remote.getService();
        unset = false;
        return this;
    }

    public RemoteOWSBuilder unset() {
        return (RemoteOWSBuilder) super.unset();
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        throw new UnsupportedOperationException(
                "Cannot build a SLD out of a simple remote ows spec");
    }

}
