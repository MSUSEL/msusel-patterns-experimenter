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
package org.geotools.data.wps;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import net.opengis.wps10.DescriptionType;
import net.opengis.wps10.ProcessDescriptionType;

import org.geotools.data.Parameter;
import org.geotools.process.Process;
import org.geotools.process.impl.SingleProcessFactory;
import org.geotools.text.Text;
import org.opengis.util.InternationalString;


/**
 * This class acts as a ProcessFactory for any process. It handles converting related bean
 * structures into a ProcessFactory object, and can be created from a describeprocess response and
 * can be passed around as a process encompassing object for ease of use. This factory can make a
 * representation of a process and its "execute" method will actually build a request to the server
 * to execute the process and return the results.
 *
 * @author GDavis
 *
 *
 *
 *
 *
 * @source $URL$
 */
public class WPSFactory extends SingleProcessFactory
{

    private ProcessDescriptionType pdt;

    private URL serverUrl;

    private String version;

    private String title;

    private String identifier;

    private String description;

    private Map<String, Parameter<?>> parameterInfo = new TreeMap<String, Parameter<?>>();

    private Map<String, Parameter<?>> resultInfo = new TreeMap<String, Parameter<?>>();

    public WPSFactory(ProcessDescriptionType pdt, URL serverUrl)
    {
        this.pdt = pdt;
        this.serverUrl = serverUrl;
        buildValuesFromProcessDescriptionType();
    }

    /**
     * Go through the ProcessDescriptionType object tree and set this flactory's values based on it.
     *
     * @param pdt
     */
    private void buildValuesFromProcessDescriptionType()
    {
        this.version = this.pdt.getProcessVersion();
        this.title = this.pdt.getTitle().getValue();
        this.identifier = this.pdt.getIdentifier().getValue();
        this.description = WPSUtils.isAbstractNull(this.pdt) ? "" : this.pdt.getAbstract().getValue();
        this.parameterInfo = WPSUtils.createInputParamMap(this.pdt, this.parameterInfo);
        this.resultInfo = WPSUtils.createOutputParamMap(this.pdt, this.resultInfo);
    }

    /**
     * Create a representation of a process
     */
    public Process create()
    {
        return new WPSProcess(this);
    }

    public InternationalString getDescription()
    {
        return Text.text(description);
    }

    public Map<String, Parameter<?>> getParameterInfo()
    {
        return Collections.unmodifiableMap(parameterInfo);
    }

    public Map<String, Parameter<?>> getResultInfo(Map<String, Object> parameters) throws IllegalArgumentException
    {
        return Collections.unmodifiableMap(resultInfo);
    }

    public InternationalString getTitle()
    {
        return Text.text(title);
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public String getVersion()
    {
        return version;
    }

    public boolean supportsProgress()
    {
        // unknown, so return false
        return false;
    }

    public ProcessDescriptionType getProcessDescriptionType()
    {
        return this.pdt;
    }

    public URL getServerURL()
    {
        return this.serverUrl;
    }

}
