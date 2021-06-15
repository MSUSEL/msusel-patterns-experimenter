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

import org.opengis.filter.expression.VolatileFunction;

/**
 * A simple visitor whose purpose is to extract the set of attributes used by a
 * Style, that is, those that the Style expects to find in order to work
 * properly
 * 
 * This is very similiar to StyleAttributeExtractor, but with these differences:
 * a) it doesnt the count the <PropertyName> tag in the <Geometry> b) it doesnt
 * count anything in the <TextSymbolizer>'s <Priority> tag c) it doesnt count
 * anything in the <TextSymbolizer>'s <Label> tag
 * 
 * The reasons for this are because these fields are ALWAYS taken directly from
 * the feature, not from the style.
 * 
 * So, for making queries (knowing any property that might be possibily be used
 * in the SLD), use StyleAttributeExtractor. If you want to know what a
 * symbolizer actually needs to cache, then use this
 * (StyleAttributeExtractorTruncated).
 * 
 * @author dblasby
 *
 *
 * @source $URL$
 */
public class StyleAttributeExtractorTruncated extends StyleAttributeExtractor
		implements StyleVisitor {

	boolean usingVolatileFunctions = false;
	
	@Override
	public void clear() {
		super.clear();
		usingVolatileFunctions = false;
	}
	
	public boolean isUsingVolatileFunctions() {
		return usingVolatileFunctions;
	}

	public Object visit(org.opengis.filter.expression.Function expression, Object data) {
		usingVolatileFunctions |= (expression instanceof VolatileFunction);
		return super.visit(expression, data);
	};

	public void visit(RasterSymbolizer rs) {
		if (rs != null) {
			if (rs.getImageOutline() != null) {
				rs.getImageOutline().accept(this);
			}

			if (rs.getOpacity() != null) {
				rs.getOpacity().accept(this, null);
			}
		}
	}

	/**
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointSymbolizer)
	 */
	public void visit(PointSymbolizer ps) {

		if (ps.getGraphic() != null) {
			ps.getGraphic().accept(this);
		}

	}

	/**
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LineSymbolizer)
	 */
	public void visit(LineSymbolizer line) {

		if (line.getStroke() != null) {
			line.getStroke().accept(this);
		}
	}

	/**
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
	 */
	public void visit(PolygonSymbolizer poly) {

		if (poly.getStroke() != null) {
			poly.getStroke().accept(this);
		}

		if (poly.getFill() != null) {
			poly.getFill().accept(this);
		}
	}

	/**
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.TextSymbolizer)
	 */
	public void visit(TextSymbolizer text) {

		if (text instanceof TextSymbolizer2) {
			if (((TextSymbolizer2) text).getGraphic() != null)
				((TextSymbolizer2) text).getGraphic().accept(this);
		}

		if (text.getFill() != null) {
			text.getFill().accept(this);
		}

		if (text.getHalo() != null) {
			text.getHalo().accept(this);
		}

		if (text.getFonts() != null) {
			Font[] fonts = text.getFonts();

			for (int i = 0; i < fonts.length; i++) {
				Font font = fonts[i];

				if (font.getFontFamily() != null) {
					font.getFontFamily().accept(this, null);
				}

				if (font.getFontSize() != null) {
					font.getFontSize().accept(this, null);
				}

				if (font.getFontStyle() != null) {
					font.getFontStyle().accept(this, null);
				}

				if (font.getFontWeight() != null) {
					font.getFontWeight().accept(this, null);
				}
			}
		}

		if (text.getHalo() != null) {
			text.getHalo().accept(this);
		}

		if (text.getPlacement() != null) {
			text.getPlacement().accept(this);
		}

	}

}
