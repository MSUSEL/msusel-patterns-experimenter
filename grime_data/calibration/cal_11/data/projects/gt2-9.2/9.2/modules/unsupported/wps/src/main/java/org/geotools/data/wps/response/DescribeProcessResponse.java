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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import net.opengis.ows11.ExceptionReportType;
import net.opengis.wps10.ProcessDescriptionsType;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.ows.ServiceException;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.xml.sax.SAXException;


/**
 * Represents the response from a server after a DescribeProcess request
 * has been issued.
 *
 * @author gdavis
 *
 *
 * @source $URL$
 */
public class DescribeProcessResponse extends Response
{

    private ProcessDescriptionsType processDescs;
    private ExceptionReportType excepResponse;

    /**
     * @param contentType
     * @param inputStream
     * @throws ServiceException
     * @throws SAXException
     */
    public DescribeProcessResponse(HTTPResponse httpResponse) throws IOException, ServiceException
    {
        super(httpResponse);

        InputStream inputStream = null;
        try
        {
            inputStream = httpResponse.getResponseStream();

            // Map hints = new HashMap();
            // hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WPSSchema.getInstance());
            Configuration config = new WPSConfiguration();
            Parser parser = new Parser(config);

            Object object;
            excepResponse = null;
            processDescs = null;
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
            if (object instanceof ProcessDescriptionsType)
            {
                processDescs = (ProcessDescriptionsType) object;
            }
            // exception caught on server and returned
            else if (object instanceof ExceptionReportType)
            {
                excepResponse = (ExceptionReportType) object;
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

    public ProcessDescriptionsType getProcessDesc()
    {
        return processDescs;
    }

    public ExceptionReportType getExceptionResponse()
    {
        return excepResponse;
    }

}
