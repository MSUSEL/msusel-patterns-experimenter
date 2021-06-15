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
package com.itextpdf.text.pdf.parser;

import java.awt.geom.Rectangle2D;


/**
 * A {@link RenderFilter} that only allows text within a specified rectangular region
 * @since 5.0.1
 */
public class RegionTextRenderFilter extends RenderFilter {

    /** the region to allow text from */
    private final Rectangle2D filterRect;
    
    /**
     * Constructs a filter
     * @param filterRect the rectangle to filter text against.  Note that this is a java.awt.Rectangle !
     */
    public RegionTextRenderFilter(Rectangle2D filterRect) {
        this.filterRect = filterRect;
    }

    /** 
     * @see com.itextpdf.text.pdf.parser.RenderFilter#allowText(com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    public boolean allowText(TextRenderInfo renderInfo){
    	LineSegment segment = renderInfo.getBaseline();
        Vector startPoint = segment.getStartPoint();
        Vector endPoint = segment.getEndPoint();
        
        float x1 = startPoint.get(Vector.I1);
        float y1 = startPoint.get(Vector.I2);
        float x2 = endPoint.get(Vector.I1);
        float y2 = endPoint.get(Vector.I2);
        
        return filterRect.intersectsLine(x1, y1, x2, y2);
    }


}
