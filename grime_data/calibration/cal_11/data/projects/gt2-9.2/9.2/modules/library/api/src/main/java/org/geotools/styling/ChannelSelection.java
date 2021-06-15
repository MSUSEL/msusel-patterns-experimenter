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
package org.geotools.styling;



/**
 * The ChannelSelection element specifies the false-color channel selection for
 * a multi-spectral raster source  (such as a multi-band satellite-imagery
 * source).  It is defined as:
 * <PRE>
 * &lt;xs:element name="ChannelSelection"&gt;
 * &lt;xs:complexType&gt;
 *     &lt;xs:choice&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element ref="sld:RedChannel"/&gt;
 *         &lt;xs:element ref="sld:GreenChannel"/&gt;
 *         &lt;xs:element ref="sld:BlueChannel"/&gt;
 *       &lt;/xs:sequence&gt;
 *       &lt;xs:element ref="sld:GrayChannel"/&gt;
 *     &lt;/xs:choice&gt;
 *   &lt;/xs:complexType&gt;
 * &lt;/xs:element&gt;
 * &lt;xs:element name="RedChannel" type="sld:SelectedChannelType"/&gt;
 * &lt;xs:element name="GreenChannel" type="sld:SelectedChannelType"/&gt;
 * &lt;xs:element name="BlueChannel" type="sld:SelectedChannelType"/&gt;
 * &lt;xs:element name="GrayChannel" type="sld:SelectedChannelType"/&gt;
 * </PRE>
 * Either a channel may be selected to display in each of red, green, and blue,
 * or a single channel may be selected to display in grayscale.  (The spelling
 * ?gray? is used since it seems to be more common on the Web than ?grey? by a
 * ratio of about 3:1.) Contrast enhancement may be applied to each channel in
 * isolation.  Channels are identified by a system and data-dependent
 * character identifier.  Commonly, channels will be labelled as ?1?, ?2?,
 * etc.
 *
 * @author iant
 *
 *
 * @source $URL$
 */
public interface ChannelSelection extends org.opengis.style.ChannelSelection {
    /**
     * Set the RGB channels to be used
     *
     * @param red the red channel
     * @param green the green channel
     * @param blue the blue channel
     */
    void setRGBChannels(SelectedChannelType red, SelectedChannelType green, SelectedChannelType blue);

    /**
     * Set the RGB channels to be used
     *
     * @param channels array of channels in RGB order
     */
    void setRGBChannels(SelectedChannelType[] channels);

    
    /**
     * get the RGB channels to be used
     *
     * @return array of channels in RGB order
     */
    SelectedChannelType[] getRGBChannels();

    /**
     * Set the gray channel to be used
     *
     * @param gray the gray channel
     */
    void setGrayChannel(SelectedChannelType gray);

    /**
     * Get the gray channel to be used
     *
     * @return the gray channel
     */
    SelectedChannelType getGrayChannel();

    /**
     * set the channels to be used
     *
     * @param channels array of channels
     * @deprecated Please use setRGBChannels
     */
    void setSelectedChannels(SelectedChannelType[] channels);

    /**
     * get the channels to be used
     *
     * @return array of channels
     * @deprecated Please use getRGBChannels
     */
    SelectedChannelType[] getSelectedChannels();

    public void accept(org.geotools.styling.StyleVisitor visitor);
}
