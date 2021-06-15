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
package org.geotools.arcsde;

import java.io.IOException;

import com.esri.sde.sdk.client.SeError;
import com.esri.sde.sdk.client.SeException;

/**
 * An IOException that wraps an {@link SeException} in order to report the {@link SeError} messages
 * that otherwise get hidden in a normal stack trace.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5
 *
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/ArcSdeException.java $
 */
public class ArcSdeException extends IOException {

    private static final long serialVersionUID = -1392514883217797825L;

    public ArcSdeException(SeException cause) {
        this("", cause);
    }

    public ArcSdeException(String msg, SeException cause) {
        super(msg);
        if (cause != null) {
            this.initCause(cause);
        }
    }

    @Override
    public SeException getCause() {
        return (SeException) super.getCause();
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        SeError error = getSeError();
        StringBuffer sb = new StringBuffer();
        if (message != null) {
            sb.append(message);
        }
        if (error != null) {
            int sdeError = error.getSdeError();
            String sdeErrMsg = error.getSdeErrMsg();
            String extErrMsg = error.getExtErrMsg();
            String errDesc = error.getErrDesc();

            sb.append("[SDE error ").append(sdeError);
            if (sdeErrMsg != null && !"".equals(sdeErrMsg)) {
                sb.append(" ").append(sdeErrMsg);
            }
            sb.append("]");
            if (errDesc != null && !"".equals(errDesc)) {
                sb.append("[Error desc=").append(errDesc).append("]");
            }
            if (extErrMsg != null && !"".equals(extErrMsg)) {
                sb.append("[Extended desc=").append(extErrMsg).append("]");
            }
        }
        return sb.toString();
    }

    public SeError getSeError() {
        SeException ex = getCause();
        if (ex == null) {
            return null;
        }
        return ex.getSeError();
    }

    /**
     * SeException is pretty sad (Caused by: com.esri.sde.sdk.client.SeException: ) leaving you to
     * hunt and peck at the SeError for a good description of what went bad.
     * <p>
     * This class tries to grab as much information as possible form SeError.
     * 
     * @return String describing the message from SeException.
     */
    public static String toMessage(SeException e) {
        StringBuffer buf = new StringBuffer();
        if (e.getSeError() != null) {
            SeError error = e.getSeError();
            buf.append("SDE Error ");
            buf.append(error.getSdeError());
            buf.append(" ");
            buf.append(error.getSdeErrMsg());
            if (error.getExtErrMsg() != null) {
                buf.append("\n");
                buf.append(error.getExtErrMsg());
            }
            if (error.getErrDesc() != null) {
                buf.append("\n");
                buf.append(error.getErrDesc());
            }
        }
        if (e.getMessage() != null) {
            buf.append("\n");
            buf.append(e.getMessage());
        }
        return buf.toString();
    }
}
