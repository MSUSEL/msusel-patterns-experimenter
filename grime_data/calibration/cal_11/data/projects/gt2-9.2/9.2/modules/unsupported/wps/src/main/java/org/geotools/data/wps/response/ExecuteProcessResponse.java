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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wps.response;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import net.opengis.ows11.ExceptionReportType;
import net.opengis.wps10.ExecuteResponseType;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.xml.sax.SAXException;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


/**
 * Represents the response from a server after an ExecuteProcess request
 * has been issued.
 *
 * @author gdavis
 *
 *
 * @source $URL$
 */
public class ExecuteProcessResponse extends Response
{

    private ExecuteResponseType exeResponse;
    private ExceptionReportType excepResponse;
    private InputStream rawResponseStream;
    private String rawContentType;

    /**
     * @param contentType
     * @param inputStream
     * @throws ServiceException
     * @throws SAXException
     */
    public ExecuteProcessResponse(HTTPResponse httpResponse, boolean raw) throws IOException, ServiceException
    {
        super(httpResponse);

        InputStream inputStream = null;
        try
        {
            if(!raw) {
                inputStream = httpResponse.getResponseStream();
                parseDocumentResponse(inputStream);
            } else {
                // we need to know if the response was an exception, unfortunately we cannot
                // make that determination just by looking at the mime type ...

                // could be gml or other stuff, not necessarily a service exception, we need to check if it's an exception or not
                rawContentType = httpResponse.getContentType();
                if(rawContentType.matches(".*/xml.*")) {
                    // make sure we don't throw away info
                    inputStream = new BufferedInputStream(httpResponse.getResponseStream());
                    inputStream.mark(8192);
                    
                    try {
                        XmlPullParser parser = new MXParser();
                        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                        parser.setInput(inputStream, "UTF-8");
                        parser.nextTag();
    
                        // get the first tag name
                        String name = parser.getName();
                        inputStream.reset();
                        if("ServiceException".equals(name) || "ExceptionReport".equals(name) || "ExecuteResponse".equals(name)) {
                            parseDocumentResponse(inputStream);
                            return;
                        }
                    } catch(XmlPullParserException e) {
                        throw new IOException("Failed to parse the response", e);
                    }
                } else {
                    inputStream = httpResponse.getResponseStream();
                }

                // ok, it's really the raw response, store it and avoid closing it
                rawResponseStream = inputStream;
                inputStream = null;
            }
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }

    private void parseDocumentResponse(InputStream inputStream) throws IOException {
        // Map hints = new HashMap();
        // hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WPSSchema.getInstance());
        Configuration config = new WPSConfiguration();
        Parser parser = new Parser(config);

        Object object;
        excepResponse = null;
        exeResponse = null;
        try
        {
            // object = DocumentFactory.getInstance(inputStream, hints, Level.WARNING);
            object = parser.parse(inputStream);
        }
        catch (SAXException e)
        {
            throw (IOException) new IOException().initCause(e);
        }
        catch (ParserConfigurationException e)
        {
            throw (IOException) new IOException().initCause(e);
        }

        // try casting the response
        if (object instanceof ExecuteResponseType)
        {
            exeResponse = (ExecuteResponseType) object;
            // in case of exceptions let's be explicit about them
            if(exeResponse.getStatus() != null && exeResponse.getStatus().getProcessFailed() != null) {
                excepResponse = exeResponse.getStatus().getProcessFailed().getExceptionReport();
            }
        }
        // exception caught on server and returned
        else if (object instanceof ExceptionReportType)
        {
            excepResponse = (ExceptionReportType) object;
        }
    }

    public ExecuteResponseType getExecuteResponse()
    {
        return exeResponse;
    }

    public ExceptionReportType getExceptionResponse()
    {
        return excepResponse;
    }

    /**
     * If a raw response was requested, and no service exception has been sent, we should get
     * the raw response stream here
     */
    public InputStream getRawResponseStream() {
        return rawResponseStream;
    }
    
    /**
     * The raw response stream content type
     * @return
     */
    public String getRawContentType() {
        return rawContentType;
    }

}
