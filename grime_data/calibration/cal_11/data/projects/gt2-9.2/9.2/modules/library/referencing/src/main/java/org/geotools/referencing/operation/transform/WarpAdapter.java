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
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import java.awt.geom.Point2D;
import java.awt.image.RasterFormatException;
import javax.media.jai.Warp;

import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Wraps an arbitrary {@link MathTransform2D} into an image warp operation.
 * This warp operation is used by {@link org.geotools.coverage.processing.operation.Resample}
 * when no standard warp operation has been found applicable.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class WarpAdapter extends Warp {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -8679060848877065181L;

    /**
     * The coverage name. Used for formatting error message.
     */
    private final CharSequence name;

    /**
     * The <strong>inverse</strong> of the transform to apply for projecting an image.
     * This transform maps destination pixels to source pixels.
     */
    private final MathTransform2D inverse;

    /**
     * Constructs a new {@code WarpAdapter} using the given transform.
     *
     * @param name    The coverage name. Used for formatting error message.
     * @param inverse The <strong>inverse</strong> of the transformation to apply for projecting
     *                an image. This inverse transform maps destination pixels to source pixels.
     */
    public WarpAdapter(final CharSequence name, final MathTransform2D inverse) {
        this.name    = name;
        this.inverse = inverse;
    }

    /**
     * Returns the transform from image's destination pixels to source pixels.
     */
    public MathTransform2D getTransform() {
        return inverse;
    }

    /**
     * Computes the source pixel positions for a given rectangular
     * destination region, subsampled with an integral period.
     */
    public float[] warpSparseRect(final int xmin,    final int ymin,
                                  final int width,   final int height,
                                  final int periodX, final int periodY, float[] destRect)
    {
        if (periodX < 1) throw new IllegalArgumentException(String.valueOf(periodX));
        if (periodY < 1) throw new IllegalArgumentException(String.valueOf(periodY));

        final int xmax  = xmin + width;
        final int ymax  = ymin + height;
        final int count = ((width+(periodX-1))/periodX) * ((height+(periodY-1))/periodY);
        if (destRect == null) {
            destRect = new float[2*count];
        }
        int index = 0;
        for (int y=ymin; y<ymax; y+=periodY) {
            for (int x=xmin; x<xmax; x+=periodX) {
                destRect[index++] = x + 0.5f;
                destRect[index++] = y + 0.5f;
            }
        }
        try {
            inverse.transform(destRect, 0, destRect, 0, count);
        } catch (TransformException exception) {
            // At least one transformation failed. In Geotools MapProjection
            // implementation, unprojected coordinates are set to (NaN,NaN).
            RasterFormatException e = new RasterFormatException(Errors.format(
                            ErrorKeys.CANT_REPROJECT_$1, name));
            e.initCause(exception);
            throw e;
        }
        while (--index >= 0) {
            destRect[index] -= 0.5f;
        }
        return destRect;
    }

    /**
     * Computes the source point corresponding to the supplied point.
     *
     * @param destPt The position in destination image coordinates
     *               to map to source image coordinates.
     */
    @Override
    public Point2D mapDestPoint(final Point2D destPt) {
        Point2D result = new Point2D.Double(destPt.getX()+0.5, destPt.getY()+0.5);
        try {
            result = inverse.transform(result, result);
        } catch (TransformException exception) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.BAD_PARAMETER_$2, "destPt", destPt), exception);
        }
        result.setLocation(result.getX()-0.5, result.getY()-0.5);
        return result;
    }

    /**
     * Computes the destination point corresponding to the supplied point.
     *
     * @param sourcePt The position in source image coordinates
     *                 to map to destination image coordinates.
     */
    @Override
    public Point2D mapSourcePoint(final Point2D sourcePt) {
        Point2D result = new Point2D.Double(sourcePt.getX()+0.5, sourcePt.getY()+0.5);
        try {
            result = inverse.inverse().transform(result, result);
        } catch (TransformException exception) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.BAD_PARAMETER_$2, "sourcePt", sourcePt), exception);
        }
        result.setLocation(result.getX()-0.5, result.getY()-0.5);
        return result;
    }
}
