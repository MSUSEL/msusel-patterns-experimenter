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
package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.AnchorPoint;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Halo;
import org.geotools.styling.ImageOutline;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.OverlapBehavior;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.ShadedRelief;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleVisitor;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;

/**
 * 
 *
 * @source $URL$
 */
public class StyleCollector implements StyleVisitor {

    List<FeatureTypeStyle> featureTypeStyles = new ArrayList<FeatureTypeStyle>();

    List<Rule> rules = new ArrayList<Rule>();

    List<Symbolizer> symbolizers = new ArrayList<Symbolizer>();
    
    List<Style> styles = new ArrayList<Style>();
    
    List<StyledLayer> layers = new ArrayList<StyledLayer>();

    @Override
    public void visit(StyledLayerDescriptor sld) {
        for (StyledLayer sl : sld.getStyledLayers()) {
            if (sl instanceof UserLayer) {
                ((UserLayer) sl).accept(this);
            } else if(sl instanceof NamedLayer) {
                ((NamedLayer) sl).accept(this);
            }
            
        }
    }

    @Override
    public void visit(NamedLayer layer) {
        layers.add(layer);
        for (Style style : layer.getStyles()) {
            style.accept(this);
        }
    }

    @Override
    public void visit(UserLayer layer) {
        layers.add(layer);
        for (Style style : layer.getUserStyles()) {
            style.accept(this);
        }
    }

    @Override
    public void visit(FeatureTypeConstraint ftc) {

    }

    @Override
    public void visit(Style style) {
        styles.add(style);
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            featureTypeStyles.add(fts);
            fts.accept(this);
        }

    }

    @Override
    public void visit(Rule rule) {
        for (Symbolizer symbolizer : rule.symbolizers()) {
            symbolizers.add(symbolizer);
        }
    }

    @Override
    public void visit(FeatureTypeStyle fts) {
        for (Rule rule : fts.rules()) {
            rules.add(rule);
            rule.accept(this);
        }

    }

    @Override
    public void visit(Fill fill) {

    }

    @Override
    public void visit(Stroke stroke) {

    }

    @Override
    public void visit(Symbolizer sym) {

    }

    @Override
    public void visit(PointSymbolizer ps) {

    }

    @Override
    public void visit(LineSymbolizer line) {

    }

    @Override
    public void visit(PolygonSymbolizer poly) {

    }

    @Override
    public void visit(TextSymbolizer text) {

    }

    @Override
    public void visit(RasterSymbolizer raster) {

    }

    @Override
    public void visit(Graphic gr) {

    }

    @Override
    public void visit(Mark mark) {

    }

    @Override
    public void visit(ExternalGraphic exgr) {

    }

    @Override
    public void visit(PointPlacement pp) {

    }

    @Override
    public void visit(AnchorPoint ap) {

    }

    @Override
    public void visit(Displacement dis) {

    }

    @Override
    public void visit(LinePlacement lp) {

    }

    @Override
    public void visit(Halo halo) {

    }

    @Override
    public void visit(ColorMap colorMap) {

    }

    @Override
    public void visit(ColorMapEntry colorMapEntry) {

    }

    @Override
    public void visit(ContrastEnhancement contrastEnhancement) {

    }

    @Override
    public void visit(ImageOutline outline) {

    }

    @Override
    public void visit(ChannelSelection cs) {

    }

    @Override
    public void visit(OverlapBehavior ob) {

    }

    @Override
    public void visit(SelectedChannelType sct) {

    }

    @Override
    public void visit(ShadedRelief sr) {

    }

}
