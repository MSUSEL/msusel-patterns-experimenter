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
package org.geotools.data.ows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a base object for a WMS getCapabilities response.
 *
 * @author Richard Gould, Refractions Research
 *
 *
 * @source $URL$
 */
public class WMSCapabilities extends Capabilities {
    private WMSRequest request;
    private Layer layer;
    
    private List<Layer> layers; //cache
	private String[] exceptions;

	/**
	 * Get the root layer, the contents of the Web Map Server are the children
	 * of this layer.
	 * 
	 * @return The "root" Layer for the Web Map Server
	 */
    public Layer getLayer() {
        return layer;
    }

    @Override
    public void setVersion(String version) {
        super.setVersion(version);
        boolean forceXY = !getVersion().startsWith("1.3");
        if( layer != null ){
            fixLayerBoundingBox( layer, forceXY );
            layer.clearCache();
        }
    }
    
    public void setLayer(Layer layer) {
        this.layer = layer;
        if( getVersion() != null ){
            boolean forceXY = !getVersion().startsWith("1.3");
            fixLayerBoundingBox( layer, forceXY );
            layer.clearCache();
        }
    }
    
    /**
     * Fix the provided layer's bounding box so that it can be correctly handled.
     * <p>
     * Call layer.clearCache() after this method.
     * 
     * @param layer
     * @param forceXY true prior to WMS 1.3.0, false after WMS 1.3.0
     */
    static void fixLayerBoundingBox( Layer layer, boolean forceXY ){
        if( layer == null ){
            return;
        }
        if( layer.getLayerBoundingBoxes() != null ){
            for( CRSEnvelope boundingBox : layer.getLayerBoundingBoxes() ){
                String srsName = boundingBox.getSRSName();
                boundingBox.setSRSName(srsName, forceXY);                
            }
        }
        for( Layer child : layer.getChildren() ){
            fixLayerBoundingBox(child, forceXY);
        }
    }
        
    /**
     * Access a flat view of the layers available in the WMS.
     * <p>
     * The information available here is the same as doing a top down
     * walk of all the layers available via getLayer().
     * 
     * @return List of all available layers
     */
    public List<Layer> getLayerList() {
        if (layers == null) {
            layers = new ArrayList<Layer>();
            layers.add(layer);
            addChildrenRecursive(layers, layer);
        }        
        return Collections.unmodifiableList(layers);
    }
    
    private void addChildrenRecursive(List<Layer> layers, Layer layer) {
        if (layer.getChildren() != null){
            for( Layer child : layer.getChildren() ){
                layers.add( child );
                addChildrenRecursive( layers, child );
            }
        }
    }

    /**
     * The request contains information about possible Requests that can be 
     * made against this server, including URLs and formats.
     *
     * @return Returns the request.
     */
    public WMSRequest getRequest() {
        return request;
    }

    /**
     * @param request The request to set.
     */
    public void setRequest(WMSRequest request) {
        this.request = request;
    }

	/**
	 * Exceptions declare what kind of formats this server can return exceptions
	 * in. They are used during subsequent requests.
	 */
	public String[] getExceptions() {
	    return exceptions;
	}

	public void setExceptions(String[] exceptions) {
	    this.exceptions = exceptions;
	}
}
