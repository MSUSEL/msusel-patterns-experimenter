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
package org.geotools.data.wps.request;


import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wps10.DataInputsType1;
import net.opengis.wps10.DataType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.InputType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.Wps10Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wps.WPS;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;


/**
 * Describes an abstract ExecuteProcess request. Provides everything except
 * the versioning info, which subclasses must implement.
 *
 * @author gdavis
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class AbstractExecuteProcessRequest extends AbstractWPSRequest implements ExecuteProcessRequest
{

    /** only support POST for execute requests right now (in the future this
     * could be dynamically set based on what properties are set
     * for this request).
     */
    private boolean usePost = true;

    /**
     * store the inputs for this request
     */
    private Properties inputs;

    protected ResponseFormType responseForm;

    /**
     * Constructs a basic ExecuteProcessRequest, without versioning info.
     *
     * @param onlineResource the location of the request
     * @param properties a set of properties to use. Can be null.
     */
    public AbstractExecuteProcessRequest(URL onlineResource, Properties properties)
    {
        super(onlineResource, properties);
        this.inputs = new Properties();
    }

    protected void initRequest()
    {
        setProperty(REQUEST, "Execute");
    }

    /**
    * @see org.geotools.data.wps.request.ExecuteProcessRequest#setIdentifier(java.lang.String)
    */
    public void setIdentifier(String identifier)
    {
        setProperty(IDENTIFIER, identifier);
    }

    protected abstract void initVersion();

    @Override
    public boolean requiresPost()
    {
        return usePost;
    }

    @Override
    public void performPostOutput(OutputStream outputStream) throws IOException
    {
        // Encode the request into GML2 with the schema provided in the
        // describeprocess
        Configuration config = new WPSConfiguration();
        Encoder encoder = new Encoder(config);
        encoder.setIndenting(true);

        // http://schemas.opengis.net/wps/1.0.0/wpsExecute_request.xsd
        ExecuteType request = createExecuteType();
        encoder.encode(request, WPS.Execute, outputStream);
        // System.out.println(outputStream.toString());
    }

    @SuppressWarnings("unchecked")
    private ExecuteType createExecuteType()
    {
        ExecuteType request = Wps10Factory.eINSTANCE.createExecuteType();

        // identifier
        CodeType codetype = Ows11Factory.eINSTANCE.createCodeType();
        String iden = (String) this.properties.get(this.IDENTIFIER);
        codetype.setValue(iden);
        request.setIdentifier(codetype);

        // service and version
        request.setService("WPS"); // TODO: un-hardcode
        request.setVersion("1.0.0"); // TODO: un-hardcode

        // inputs - loop through inputs and add them
        if ((this.inputs != null) && !this.inputs.isEmpty())
        {
            DataInputsType1 inputtypes = Wps10Factory.eINSTANCE.createDataInputsType1();

            Set<Object> keyset = this.inputs.keySet();
            Iterator<Object> iterator = keyset.iterator();
            while (iterator.hasNext())
            {
                Object key = iterator.next();
                List<DataType> objects = (List<DataType>) this.inputs.get(key);

                // go through the list and create on input for each datatype in the list
                Iterator<DataType> iterator2 = objects.iterator();
                while (iterator2.hasNext())
                {
                    // identifier
                    EObject oInput = iterator2.next();
                    if (oInput instanceof DataType) {
                    	DataType dt = (DataType) oInput;
                    	InputType input = Wps10Factory.eINSTANCE.createInputType();
                    	CodeType ct = Ows11Factory.eINSTANCE.createCodeType();
                    	ct.setValue((String) key);
                    	input.setIdentifier(ct);
                    	input.setData((DataType) dt);
                    	inputtypes.getInput().add(input);
                    } else if (oInput instanceof InputReferenceType) {
                    	InputReferenceType rt = (InputReferenceType) oInput;
                    	InputType input = Wps10Factory.eINSTANCE.createInputType();
                    	CodeType ct = Ows11Factory.eINSTANCE.createCodeType();
                    	ct.setValue((String) key);
                    	input.setIdentifier(ct);
                    	input.setReference(rt);
                    	inputtypes.getInput().add(input);
                    } else {
                        throw new IllegalStateException("The input for key " + key + " contain an unsupported object of type " + oInput.getClass());
                    }
                }
            }

            request.setDataInputs(inputtypes);
        }

        if (this.responseForm != null)
        {
            // responsetype
            request.setResponseForm(this.responseForm);
        }

        return request;
    }

    /**
     * Add an input to the input properties.
     * If null is passed as the value, remove any current input with the given name.
     * @param name input name
     * @param value the list of datatype input objects
     */
    public void addInput(String name, List<EObject> value)
    {
        if (value == null)
        {
            inputs.remove(name);
        }
        else
        {
            // check input for validity
            for (EObject o : value) {
                if(!(o instanceof DataType) && !(o instanceof InputReferenceType)) {
                    throw new IllegalArgumentException("The values can be either of type net.opengis.wps10.DataType " +
                    		"or of type net.opengis.wps10.InputReferenceType");
                }
            }
            // all right, let's roll
            inputs.put(name, value);
        }
    }

    public void setResponseForm(ResponseFormType responseForm)
    {
        if (responseForm != null)
        {
            this.responseForm = responseForm;
        }

    }

}
