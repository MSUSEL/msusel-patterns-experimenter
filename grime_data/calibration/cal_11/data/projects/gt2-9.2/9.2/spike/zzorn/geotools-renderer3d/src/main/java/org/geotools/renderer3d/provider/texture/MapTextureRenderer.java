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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer3d.provider.texture;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.renderer3d.utils.BoundingRectangle;
import org.geotools.renderer3d.utils.ParameterChecker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * A renderer that can render parts of a GeoTools MapContext to requesed textures.
 *
 * @author Hans H�ggstr�m
 */
public final class MapTextureRenderer
        implements TextureRenderer
{

    //======================================================================
    // Private Fields

    private final MapContext myMap;
    private final StreamingRenderer myStreamingRenderer = new StreamingRenderer();
    private final Color myBackgroundColor;
    private final double myScaleX;
    private final double myScaleY;
    private final double myTranslateX;
    private final double myTranslateY;
    private static final double DEFAULT_SCALE = 0.001;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * @param map             the map to render from
     * @param backgroundColor a color to fill the underlying map with.
     * @param scaleX          amount to scale the map along x
     * @param scaleY          amount to scale the map along y
     * @param deltaX          amount to translate the map along x
     * @param deltaY          amount to translate the map along y
     */
    public MapTextureRenderer( final MapContext map,
                               final Color backgroundColor,
                               final double scaleX,
                               final double scaleY,
                               final double deltaX,
                               final double deltaY )
    {
        ParameterChecker.checkNotNull( map, "map" );
        ParameterChecker.checkNotNull( backgroundColor, "backgroundColor" );

        myMap = map;
        myBackgroundColor = backgroundColor;

        myScaleX = scaleX;
        myScaleY = scaleY;
        myTranslateX = deltaX;
        myTranslateY = deltaY;

        myStreamingRenderer.setContext( map );
        myStreamingRenderer.setInteractive( true );
        myStreamingRenderer.setJava2DHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING,
                                                                RenderingHints.VALUE_ANTIALIAS_ON ) );
    }

    public MapTextureRenderer( final MapContext mapContextToRender, final Color color )
    {
        this( mapContextToRender, color, DEFAULT_SCALE, DEFAULT_SCALE, 0, 0 );
    }

    //----------------------------------------------------------------------
    // TextureRenderer Implementation

    public void renderArea( final BoundingRectangle area, final BufferedImage target )
    {
        final int width = target.getWidth();
        final int height = target.getHeight();

        final Graphics2D graphics = (Graphics2D) target.getGraphics();

        // Clear to color
        graphics.setColor( myBackgroundColor );
        graphics.fillRect( 0, 0, width, height );

        final BoundingRectangle transformedArea = area.transform( myTranslateX, myTranslateY, myScaleX, myScaleY );

        // Create the source and destination areas
        final Rectangle targetArea = new Rectangle( width, height );
        final ReferencedEnvelope sourceArea = new ReferencedEnvelope( transformedArea.getX1(),
                                                                      transformedArea.getX2(),
                                                                      transformedArea.getY1(),
                                                                      transformedArea.getY2(),
                                                                      myMap.getCoordinateReferenceSystem() );

        // Render
        myStreamingRenderer.paint( graphics, targetArea, sourceArea );

/*
        // DEBUG:
        graphics.setColor( Color.BLACK );
        graphics.drawRect( 0,0,width-1, height-1 );
*/
    }

}
