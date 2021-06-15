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
package org.geotools.coverage.processing;

import java.awt.Color;
import java.io.Writer;
import java.util.Locale;
import javax.media.jai.EnumeratedParameter;
import javax.media.jai.Interpolation;
import javax.media.jai.KernelJAI;

import org.opengis.util.InternationalString;
import org.geotools.coverage.AbstractCoverage;
import org.geotools.parameter.ParameterWriter;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ImageUtilities;


/**
 * Format grid coverage operation parameters in a tabular format.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class CoverageParameterWriter extends ParameterWriter {
    /**
     * Creates a new formatter writting parameters to the specified output stream.
     */
    public CoverageParameterWriter(final Writer out) {
        super(out);
    }

    /**
     * Formats the specified value as a string.
     */
    @Override
    protected String formatValue(final Object value) {
        if (KernelJAI.GRADIENT_MASK_SOBEL_HORIZONTAL.equals(value)) {
            return "GRADIENT_MASK_SOBEL_HORIZONTAL";
        }
        if (KernelJAI.GRADIENT_MASK_SOBEL_VERTICAL.equals(value)) {
            return "GRADIENT_MASK_SOBEL_VERTICAL";
        }
        if (value instanceof AbstractCoverage) {
            final InternationalString name = ((AbstractCoverage) value).getName();
            final Locale locale = getLocale();
            return (name != null) ? name.toString(locale) :
                Vocabulary.getResources(locale).getString(VocabularyKeys.UNTITLED);
        }
        if (value instanceof Interpolation) {
            return ImageUtilities.getInterpolationName((Interpolation) value);
        }
        if (value instanceof EnumeratedParameter) {
            return ((EnumeratedParameter) value).getName();
        }
        if (value instanceof Color) {
            final Color c = (Color) value;
            return "RGB["+c.getRed()+','+c.getGreen()+','+c.getBlue()+']';
        }
        return super.formatValue(value);
    }
}
