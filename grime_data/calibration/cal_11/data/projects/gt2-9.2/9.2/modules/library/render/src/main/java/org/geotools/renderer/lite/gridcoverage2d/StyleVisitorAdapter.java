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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
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
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;

/**
 * Simple empty implementation for the {@link StyleVisitor} interface.
 * 
 * <p>
 * This class works as an adapter when we want to build simple implementations of
 * the {@link StyleVisitor} interface that can visit only a subclass of the
 * {@link StyleVisitor} nodes.
 * 
 * @author Simone Giannecchini. GeoSolutions
 * 
 *
 *
 *
 * @source $URL$
 */
public class StyleVisitorAdapter implements StyleVisitor {

	/**
	 * Default constructor for a {@link StyleVisitorAdapter}.
	 */
	public StyleVisitorAdapter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.StyledLayerDescriptor)
	 */
	public void visit(StyledLayerDescriptor sld) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(StyledLayerDescriptor)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.NamedLayer)
	 */
	public void visit(NamedLayer layer) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(NamedLayer)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.UserLayer)
	 */
	public void visit(UserLayer layer) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(UserLayer)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.FeatureTypeConstraint)
	 */
	public void visit(FeatureTypeConstraint ftc) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(FeatureTypeConstraint)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Style)
	 */
	public void visit(Style style) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Style)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Rule)
	 */
	public void visit(Rule rule) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Rule)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.FeatureTypeStyle)
	 */
	public void visit(FeatureTypeStyle fts) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(FeatureTypeStyle)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Fill)
	 */
	public void visit(Fill fill) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Fill)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Stroke)
	 */
	public void visit(Stroke stroke) {

		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Stroke)"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Symbolizer)
	 */
	public void visit(Symbolizer sym) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Symbolizer)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointSymbolizer)
	 */
	public void visit(PointSymbolizer ps) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(PointSymbolizer)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LineSymbolizer)
	 */
	public void visit(LineSymbolizer line) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(LineSymbolizer)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
	 */
	public void visit(PolygonSymbolizer poly) {

		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(PolygonSymbolizer)"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.TextSymbolizer)
	 */
	public void visit(TextSymbolizer text) {

		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(TextSymbolizer)"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.RasterSymbolizer)
	 */
	public void visit(RasterSymbolizer raster) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(RasterSymbolizer)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Graphic)
	 */
	public void visit(Graphic gr) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Graphic)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Mark)
	 */
	public void visit(Mark mark) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Mark)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ExternalGraphic)
	 */
	public void visit(ExternalGraphic exgr) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(ExternalGraphic)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.PointPlacement)
	 */
	public void visit(PointPlacement pp) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(PointPlacement)"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.AnchorPoint)
	 */
	public void visit(AnchorPoint ap) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(AnchorPoint)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Displacement)
	 */
	public void visit(Displacement dis) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Displacement)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.LinePlacement)
	 */
	public void visit(LinePlacement lp) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(LinePlacement)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.Halo)
	 */
	public void visit(Halo halo) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(Halo)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ColorMap)
	 */
	public void visit(ColorMap colorMap) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(ColorMapTransform)"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ColorMapEntry)
	 */
	public void visit(ColorMapEntry colorMapEntry) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(ColorMapEntry)"));

	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ContrastEnhancement)
	 */
	public void visit(ContrastEnhancement ce) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(ContrastEnhancement)"));

	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ChannelSelection)
	 */
	public void visit(ChannelSelection cs) {

		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(ChannelSelection)"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.SelectedChannelType)
	 */
	public void visit(SelectedChannelType sct) {

		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(SelectedChannelType)"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.OverlapBehavior)
	 */
	public void visit(OverlapBehavior ob) {

		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(OverlapBehavior)"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ShadedRelief)
	 */
	public void visit(ShadedRelief sr) {
		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(ShadedRelief)"));

	}

	/*
	 * (non-Javadoc)
	 * @see org.geotools.styling.StyleVisitor#visit(org.geotools.styling.ImageOutline)
	 */
	public void visit(ImageOutline io) {

		throw new UnsupportedOperationException(
				Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"visit(ImageOutline)"));
	}

}
