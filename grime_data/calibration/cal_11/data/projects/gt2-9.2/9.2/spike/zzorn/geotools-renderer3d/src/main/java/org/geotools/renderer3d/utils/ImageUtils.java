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
package org.geotools.renderer3d.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Image related utility functions
 *
 * @author Hans H�ggstr�m
 */
public final class ImageUtils
{

    //======================================================================
    // Private Constants

    private static final Color TRANSPARENT = new Color( 0, 0, 0, 0 );

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Static Methods

    /**
     * @param width  width in pixels for the image to be created
     * @param height height in pixels for the image to be created
     *
     * @return a new buffered image with a test pattern, intended to be used as a placeholder during development and testing.
     */
    @SuppressWarnings( { "MagicNumber" } )
    public static BufferedImage createPlaceholderPicture( final int width,
                                                          final int height )
    {
        final BufferedImage placeholderPicture = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        final Graphics2D graphics = (Graphics2D) placeholderPicture.getGraphics();

        // Background
        graphics.setColor( Color.GRAY );
        graphics.fillRect( 0, 0, width, height );

        // Red X
        graphics.setColor( Color.RED );
        graphics.setStroke( new BasicStroke( 20 ) );
        graphics.drawLine( 0, 0, width, height );
        graphics.drawLine( width, 0, 0, height );

        // Frame
        graphics.setColor( Color.BLACK );
        graphics.setStroke( new BasicStroke( 1 ) );
        graphics.drawRect( 0, 0, width - 1, height - 1 );
        graphics.drawLine( 0, 0, width, height );
        graphics.drawLine( width, 0, 0, height );

        // Text
        graphics.setColor( Color.YELLOW );
        graphics.drawString( "Texture Not Found", 0, height / 2 );

        return placeholderPicture;
    }


    /**
     * Creates subimages for each tile in the specified source image.
     * The returned subimages share the same image data with the source image.
     *
     * @param imageWithTiles      the source image, divided into tiles along the x and y direction.
     *                            Should have a size that is a multiple of the number of tiles in each direction, so that all tiles get the same size.
     * @param numberOfTilesAcross number of columns of tiles in the source image
     * @param numberOfTilesDown   number of rows of tiles in the source image
     *
     * @return an array with the tiles, ordered by rows from left to right.
     */
    public static BufferedImage[] splitImageIntoTiles( final BufferedImage imageWithTiles,
                                                       final int numberOfTilesAcross,
                                                       final int numberOfTilesDown )
    {
        final int tileWidth = imageWithTiles.getWidth() / numberOfTilesAcross;
        final int tileHeight = imageWithTiles.getHeight() / numberOfTilesDown;
        final int totalNumberOfTiles = numberOfTilesDown * numberOfTilesAcross;

        final BufferedImage[] tiles = new BufferedImage[totalNumberOfTiles];
        for ( int y = 0; y < numberOfTilesDown; y++ )
        {
            for ( int x = 0; x < numberOfTilesAcross; x++ )
            {
                tiles[ x + y * numberOfTilesAcross ] =
                        imageWithTiles.getSubimage( x * tileWidth, y * tileHeight, tileWidth, tileHeight );
            }
        }

        return tiles;
    }


    /**
     * Fills the given image with the specified color.
     */
    public static void clearToColor( final BufferedImage image, final Color color )
    {
        final Graphics2D g2 = image.createGraphics();

        g2.setColor( color );
        g2.fillRect( 0, 0, image.getWidth(), image.getHeight() );
    }

    /**
     * Fills the given image with a transparent black color (0,0,0,0).
     */
    public static void clear( final BufferedImage image )
    {
        final Graphics2D g2 = image.createGraphics();

        g2.setColor( TRANSPARENT );
        g2.fillRect( 0, 0, image.getWidth(), image.getHeight() );
    }

    //======================================================================
    // Private Methods

    private ImageUtils()
    {
    }


}
