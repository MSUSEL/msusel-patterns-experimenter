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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sfs.mock;

import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;

import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * This class handle requests like
 * http://localhost:8082/simplefeatureservice/data/layerAsia?mode=features
 * http://localhost:8082/simplefeatureservice/data/layerAsia
 * It generates a list of features satifsying the provided filters 
 * @author narad
 *
 *
 *
 * @source $URL$
 */
public class DataLayerResource extends Resource {

    public DataLayerResource(Context context, Request request, Response response) {
        super(context, request, response);

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
    }

    /**
     * Allow a POST http request
     *
     * @return
     */
    public boolean allowPost() {
        return true;
    }

    /**
     * Handle a POST HTTP request.
     *
     * @param entity
     * @throws ResourceException
     */
    @Override
    public void acceptRepresentation(Representation entity) throws ResourceException {
        getResponse().setEntity(represent(new Variant(MediaType.APPLICATION_JSON)));
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    /**
     * This method is used to server a list of available layers
     * http://localhost:8084/simplefeatureservice-mockup-service-1.0-SNAPSHOT/data/layerAsia?mode=features
     * You can use the URL given below too
     * http://localhost:8084/simplefeatureservice-mockup-service-1.0-SNAPSHOT/data/layerAsia
     * @param variant
     * @return
     * @throws ResourceException
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        String _strJson = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"tiger_roads.1\",\"geometry\":{\"type\":\"MultiLineString\",\"coordinates\":[[[-73.999559,40.73158],[-73.999079,40.732188]]]},\"geometry_name\":\"the_geom\",\"properties\":{\"CFCC\":\"A41\",\"NAME\":\"Washington Sq W\"}},{\"type\":\"Feature\",\"id\":\"tiger_roads.2\",\"geometry\":{\"type\":\"MultiLineString\",\"coordinates\":[[[-73.950718,40.810874],[-73.952101,40.811472]]]},\"geometry_name\":\"the_geom\",\"properties\":{\"CFCC\":\"A42\",\"NAME\":\"W 126th St\"}}";
        Representation representation = new StringRepresentation(_strJson, MediaType.APPLICATION_JSON);
        return representation;
    }
}
