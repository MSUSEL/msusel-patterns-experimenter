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

import java.awt.Dimension;
import java.net.URL;

import org.opengis.metadata.citation.ResponsibleParty;


/**
 * This is a data model for the Open Web Service (OWS) metadata. This should be 
 * extended while implementing other OWSs. Name, Title and OnlineResource are 
 * required. Everything else is optional.
 * 
 *
 *
 * @source $URL$
 */
public class Service {
    /**
     * The name of the Service (machine readible, typically one word) -
     * Required
     */
    private String name;

    /** The title for the service (human readible) - Required */
    private String title;

    /** The URL pointing to where this Service can be accessed - Required */
    private URL onlineResource;

    /** Keywords that apply to the Service. Can be used for searching, etc */
    private String[] keywordList;

    /**
     * Abstract allows a description providing more information about the
     * Service
     */
    private String _abstract;
    
    /**
     * Information about a contact person for the service.
     */
    private ResponsibleParty contactInformation;
    
    private int layerLimit;
    private int maxWidth;
    private int maxHeight;

    public String get_abstract() {
        return _abstract;
    }

    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    public String[] getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(String[] keywordList) {
        this.keywordList = keywordList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getOnlineResource() {
        return onlineResource;
    }

    public void setOnlineResource(URL onlineResource) {
        this.onlineResource = onlineResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLayerLimit() {
        return layerLimit;
    }

    public void setLayerLimit(int layerLimit) {
        this.layerLimit = layerLimit;
    }

    public Dimension getMaxDimension(){
        return new Dimension( maxWidth, maxHeight );
    }
    
    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * Information about a contact person for the service. Uses the GeoAPI
     * citation metadata model, which does not map directly to the WMS 
     * specification, but it is close.
     * 
     * The Role field is not used.
     * 
     */
	public ResponsibleParty getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(ResponsibleParty contactInformation) {
		this.contactInformation = contactInformation;
	}
}
