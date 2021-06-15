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
package org.geotools.xml.handlers.xsi;

import java.util.LinkedList;
import java.util.List;

import org.geotools.xml.XSIElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


/**
 * <p>
 * Represents an Extension element
 * </p>
 *
 * @author dzwiers www.refractions.net
 *
 *
 * @source $URL$
 */
public class ExtensionHandler extends XSIElementHandler {
    /** 'extension' */
    public static final String LOCALNAME = "extension";
    private String base;

    //    private AnyAttributeHandler anyAttribute;
    // TODO USE the anyAttribute !
    private Object child;
    private List attributeDec;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return LOCALNAME.hashCode() * ((base == null) ? 1 : base.hashCode()) * ((child == null)
        ? 1 : child.hashCode());
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName)
        throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            logger.fine("Getting Handler for " + localName
                + " inside Extension");

            // all
            if (AllHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                AllHandler ah = new AllHandler();

                if (child == null) {
                    child = ah;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + AllHandler.LOCALNAME
                        + "' declaration.");
                }

                return ah;
            }
            if (AttributeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attributeDec == null) {
                    attributeDec = new LinkedList();
                }

                AttributeHandler ah = new AttributeHandler();
                attributeDec.add(ah);

                return ah;
            }

            // attributeGroup
            if (AttributeGroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attributeDec == null) {
                    attributeDec = new LinkedList();
                }

                AttributeGroupHandler ah = new AttributeGroupHandler();
                attributeDec.add(ah);

                return ah;
            }

            // choice
            if (ChoiceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                ChoiceHandler ah = new ChoiceHandler();

                if (child == null) {
                    child = ah;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + ChoiceHandler.LOCALNAME
                        + "' declaration.");
                }

                return ah;
            }

            // group
            if (GroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                GroupHandler ah = new GroupHandler();

                if (child == null) {
                    child = ah;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + GroupHandler.LOCALNAME
                        + "' declaration.");
                }
                return ah;
            }

            // sequence
            if (SequenceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SequenceHandler ah = new SequenceHandler();

                if (child == null) {
                    child = ah;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + SequenceHandler.LOCALNAME
                        + "' declaration.");
                }

                return ah;
            }
        }

        logger.info("Handler not found for " + localName + " in Extension");

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes atts){
        base = atts.getValue("", "base");

        if (base == null) {
            base = atts.getValue(namespaceURI, "base");
        }
    }

    /**
     * <p>
     * gets a list of AttributeHandlers
     * </p>
     *
     */
    public List getAttributeDeclarations() {
        return attributeDec;
    }

    /**
     * <p>
     * Returns the 'base' attribute
     * </p>
     *
     */
    public String getBase() {
        return base;
    }

    /**
     * <p>
     * Returns the child handler
     * </p>
     *
     */
    public Object getChild() {
        return child;
    }

    /* (non-Javadoc)
     * @see schema.XSIElementHandler#getHandlerType()
     */
    public int getHandlerType() {
        return DEFAULT;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
     *      java.lang.String)
     */
    public void endElement(String namespaceURI, String localName){
        // do nothing
    }
}
