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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;	
import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.se.v1_1.bindings.*;
import org.geotools.styling.DefaultResourceLocator;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/se schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class SEConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public SEConfiguration() {
       super(SE.getInstance());
       
       addDependency(new OGCConfiguration());
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
        //Types
        container.registerComponentImplementation(SE.directionType,DirectionTypeBinding.class);
        //container.registerComponentImplementation(SE.FunctionType,FunctionTypeBinding.class);
        //container.registerComponentImplementation(SE.MethodType,MethodTypeBinding.class);
        //container.registerComponentImplementation(SE.ModeType,ModeTypeBinding.class);
        container.registerComponentImplementation(SE.ParameterValueType,ParameterValueTypeBinding.class);
        container.registerComponentImplementation(SE.searchDirectionType,SearchDirectionTypeBinding.class);
        container.registerComponentImplementation(SE.SelectedChannelType,SelectedChannelTypeBinding.class);
        container.registerComponentImplementation(SE.stripOffPositionType,StripOffPositionTypeBinding.class);
        container.registerComponentImplementation(SE.SymbolizerType,SymbolizerTypeBinding.class);
        container.registerComponentImplementation(SE.ThreshholdsBelongToType,ThreshholdsBelongToTypeBinding.class);
        //container.registerComponentImplementation(SE.VersionType,VersionTypeBinding.class);
        
        //Elements
        container.registerComponentImplementation(SE.AnchorPoint,AnchorPointBinding.class);
        container.registerComponentImplementation(SE.BaseSymbolizer,BaseSymbolizerBinding.class);
        container.registerComponentImplementation(SE.BrightnessOnly,BrightnessOnlyBinding.class);
        container.registerComponentImplementation(SE.Categorize,CategorizeBinding.class);
        container.registerComponentImplementation(SE.ChangeCase,ChangeCaseBinding.class);
        container.registerComponentImplementation(SE.ChannelSelection,ChannelSelectionBinding.class);
        container.registerComponentImplementation(SE.ColorMap,ColorMapBinding.class);
        //container.registerComponentImplementation(SE.ColorReplacement,ColorReplacementBinding.class);
        container.registerComponentImplementation(SE.Concatenate,ConcatenateBinding.class);
        container.registerComponentImplementation(SE.ContrastEnhancement,ContrastEnhancementBinding.class);
        container.registerComponentImplementation(SE.CoverageName,CoverageNameBinding.class);
        container.registerComponentImplementation(SE.CoverageStyle,CoverageStyleBinding.class);
        
        container.registerComponentImplementation(SE.Description,DescriptionBinding.class);
        container.registerComponentImplementation(SE.Displacement,DisplacementBinding.class);
        container.registerComponentImplementation(SE.ExternalGraphic,ExternalGraphicBinding.class);
        container.registerComponentImplementation(SE.FeatureTypeStyle,FeatureTypeStyleBinding.class);
        container.registerComponentImplementation(SE.Fill,FillBinding.class);
        container.registerComponentImplementation(SE.Font,FontBinding.class);
        container.registerComponentImplementation(SE.FormatDate,FormatDateBinding.class);
        container.registerComponentImplementation(SE.FormatNumber,FormatNumberBinding.class);
        container.registerComponentImplementation(SE.Geometry,GeometryBinding.class);
        container.registerComponentImplementation(SE.Graphic,GraphicBinding.class);
        container.registerComponentImplementation(SE.GraphicFill,GraphicFillBinding.class);
        container.registerComponentImplementation(SE.GraphicStroke,GraphicStrokeBinding.class);
        container.registerComponentImplementation(SE.Halo,HaloBinding.class);
        //container.registerComponentImplementation(SE.Histogram,HistogramBinding.class);
        container.registerComponentImplementation(SE.ImageOutline,ImageOutlineBinding.class);
        container.registerComponentImplementation(SE.InlineContent,InlineContentBinding.class);
        container.registerComponentImplementation(SE.Interpolate,InterpolateBinding.class);
        container.registerComponentImplementation(SE.InterpolationPoint,InterpolationPointBinding.class);
        container.registerComponentImplementation(SE.LabelPlacement,LabelPlacementBinding.class);
        container.registerComponentImplementation(SE.LegendGraphic,LegendGraphicBinding.class);
        container.registerComponentImplementation(SE.LinePlacement,LinePlacementBinding.class);
        container.registerComponentImplementation(SE.LineSymbolizer,LineSymbolizerBinding.class);
        //container.registerComponentImplementation(SE.MapItem,MapItemBinding.class);
        container.registerComponentImplementation(SE.Mark,MarkBinding.class);
        //container.registerComponentImplementation(SE.Normalize,NormalizeBinding.class);
        container.registerComponentImplementation(SE.OnlineResource,OnlineResourceBinding.class);
        container.registerComponentImplementation(SE.OverlapBehavior,OverlapBehaviorBinding.class);
        container.registerComponentImplementation(SE.PointPlacement,PointPlacementBinding.class);
        container.registerComponentImplementation(SE.PointSymbolizer,PointSymbolizerBinding.class);
        container.registerComponentImplementation(SE.PolygonSymbolizer,PolygonSymbolizerBinding.class);
        container.registerComponentImplementation(SE.RasterSymbolizer,RasterSymbolizerBinding.class);
        //container.registerComponentImplementation(SE.Recode,RecodeBinding.class);
        container.registerComponentImplementation(SE.Rule,RuleBinding.class);
        container.registerComponentImplementation(SE.ShadedRelief,ShadedReliefBinding.class);
        container.registerComponentImplementation(SE.StringLength,StringLengthBinding.class);
        container.registerComponentImplementation(SE.StringPosition,StringPositionBinding.class);
        container.registerComponentImplementation(SE.Stroke,StrokeBinding.class);
        container.registerComponentImplementation(SE.Substring,SubstringBinding.class);
        container.registerComponentImplementation(SE.SvgParameter,SvgParameterBinding.class);
        container.registerComponentImplementation(SE.TextSymbolizer,TextSymbolizerBinding.class);
        container.registerComponentImplementation(SE.Trim,TrimBinding.class);
        container.registerComponentImplementation(SE.VendorOption,VendorOptionBinding.class);
    }
    
    @Override
    protected void configureContext(MutablePicoContainer container) {
        super.configureContext(container);
        
        container.registerComponentImplementation(StyleFactory.class, StyleFactoryImpl.class);
        container.registerComponentInstance(ResourceLocator.class, new DefaultResourceLocator());
    }
    
    @Override
    protected void configureParser(Parser parser) {
        parser.setHandleMixedContent(true);
    }
} 
