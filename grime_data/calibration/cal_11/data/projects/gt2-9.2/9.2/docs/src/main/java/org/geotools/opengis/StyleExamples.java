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
package org.geotools.opengis;

import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactory2;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.AnchorPoint;
import org.opengis.style.Displacement;
import org.opengis.style.ExternalMark;
import org.opengis.style.Fill;
import org.opengis.style.Graphic;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Mark;
import org.opengis.style.PointSymbolizer;
import org.opengis.style.Stroke;

public class StyleExamples {

/**
 * This example is limited to just the gt-opengis style interfaces which are immutable once created.
 * 
 * @throws Exception
 */
private void styleFactoryExample() throws Exception {
    // styleFactoryExample start
    //
    org.opengis.style.StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    //
    // create the graphical mark used to represent a city
    Stroke stroke = sf.stroke(ff.literal("#000000"), null, null, null, null, null, null);
    Fill fill = sf.fill(null, ff.literal(Color.BLUE), ff.literal(1.0));
    
    // OnLineResource implemented by gt-metadata - so no factory!
    OnLineResourceImpl svg = new OnLineResourceImpl(new URI("file:city.svg"));
    svg.freeze(); // freeze to prevent modification at runtime
    
    OnLineResourceImpl png = new OnLineResourceImpl(new URI("file:city.png"));
    png.freeze(); // freeze to prevent modification at runtime
    
    //
    // List of symbols is considered in order with the rendering engine choosing
    // the first one it can handle. Allowing for svg, png, mark order
    List<GraphicalSymbol> symbols = new ArrayList<GraphicalSymbol>();
    symbols.add(sf.externalGraphic(svg, "svg", null)); // svg preferred
    symbols.add(sf.externalGraphic(png, "png", null)); // png preferred
    symbols.add(sf.mark(ff.literal("circle"), fill, stroke)); // simple circle backup plan
    
    Expression opacity = null; // use default
    Expression size = ff.literal(10);
    Expression rotation = null; // use default
    AnchorPoint anchor = null; // use default
    Displacement displacement = null; // use default
    
    // define a point symbolizer of a small circle
    Graphic circle = sf.graphic(symbols, opacity, size, rotation, anchor, displacement);
    PointSymbolizer pointSymbolizer = sf.pointSymbolizer("point", ff.property("the_geom"), null,
            null, circle);
    // styleFactoryExample end
}

}
