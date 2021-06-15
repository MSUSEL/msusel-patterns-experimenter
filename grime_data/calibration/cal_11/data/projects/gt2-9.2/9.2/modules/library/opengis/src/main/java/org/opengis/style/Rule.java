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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import java.util.List;
import org.opengis.annotation.Extension;
import org.opengis.filter.Filter;
import org.opengis.annotation.UML;
import org.opengis.annotation.XmlElement;

import org.opengis.metadata.citation.OnLineResource;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

/**
 * A rule consists of two important parts: a {@linkplain Filter filter} and a list of
 * {@linkplain Symbol symbols}.  When it is time to draw a given feature, the rendering
 * engine examines each rule in the FeatureStyle, first checking its Filter (or ElseFilter).  If the
 * Filter passes, then every Symbolizer for that rule is applied to the given
 * feature.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@XmlElement("Rule")
@UML(identifier="PF_PortrayalRule", specification=ISO_19117)
public interface Rule {

    /**
     * Returns a name for this rule.
     * This can be any string that uniquely identifies this rule within a given
     * canvas.  It is not meant to be human-friendly.  (The "title" property is
     * meant to be human friendly.)
     * @return a name for this rule.
     */
    @XmlElement("Name")
    @UML(identifier="ruleName", obligation=MANDATORY, specification=ISO_19117)
    String getName();

    /**
     * Returns the description of this rule.
     *
     * @return Description with usual informations used
     * for user interfaces.
     */
    @XmlElement("Description")
    @UML(identifier="description", obligation=OPTIONAL, specification=ISO_19117)
    Description getDescription();

    /**
     * Returns a small Graphic that could be used by the rendering engine to
     * draw a legend window.
     * <p>
     * A nice user interface may want to present the user with a legend that
     * indicates how features of a given type are being portrayed.  Through its
     * {@code LegendGraphic} property, a {@code Rule} may provide a custom picture
     * to be used in such a legend window.
     * @return
     */
    @XmlElement("LegendGraphic")
    GraphicLegend getLegend();

    /**
     * Returns the filter that will limit the features for which this {@code Rule} will
     * fire.  This can only be non-null if {@link #isElseFilter} returns false.  If this
     * value is null and {@code isElseFilter} is false, this means that this {@code Rule}
     * should fire for all features.
     * @return Filter, use Filter.INCLUDES to indicate everything; or Filter.EXCLUDES for an "else" rule
     */
    @XmlElement("Filter")
    @UML(identifier="queryStatement", obligation=MANDATORY, specification=ISO_19117)
    Filter getFilter();

    /**
     * Returns true if this {@code Rule} is to fire only if no other rules in the containing
     * style have fired yet.  If this is true, then the {@linkplain #getFilter filter} must be Filter.EXCLUDES.
     * @return true if the filter is an else filter
     */
    @XmlElement("ElseFilter")
    boolean isElseFilter();

    /**
     * Returns the minimum value (inclusive) in the denominator of the current map scale
     * at which this {@code Rule} will fire.
     * If, for example, the {@code MinScaleDenominator} were 10000, then this rule
     * would only fire at scales of 1:X where X is greater than 10000.
     * A value of zero indicates that there is no minimum.
     * @return Min scale double value
     */
    @XmlElement("MinScaleDenominator")
    double getMinScaleDenominator();

    /**
     * Returns the maximum value (exclusive) in the denominator of the current map scale
     * at which this {@code Rule} will fire.
     * If, for example, the {@code MaxScaleDenominator} were 98765, then this rule
     * would only fire at scales of 1:X where X is less than 98765.
     * A value of {@link Double#POSITIVE_INFINITY} indicates that there is no maximum.
     * @return Max scale double value
     */
    @XmlElement("MaxScaleDenominator")
    double getMaxScaleDenominator();

    /**
     * This method returns the list of Symbolizer objects
     * contained by this {@code Rule}.
     *
     * We use a list of <? extends Symbolizer> to enable the possibility
     * for an implementation to return a special type of Symbolizer.
     * This doesnt mean a Rule must return a list of PointSymbolizer or
     * TextSymbolizer only, no. The purpose of this if to offer the solution
     * to return different implementations like MutableSymbolizer or RichSymbolizer
     * and then avoid redundant cast in the code.
     * If you dont intend to use a special interface you can override this method
     * by : List<Symbolizer> symbolizers();
     * 
     * @return the list of Symbolizer
     */
    @XmlElement("Symbolizer")
    @UML(identifier="portrayAction", obligation=MANDATORY, specification=ISO_19117)
    List<? extends Symbolizer> symbolizers();
    
    /**
     * It is common to have a style coming from a external xml file, this method
     * provide a way to get the original source if there is one.
     * OGC SLD specification can use this method to know if a style must be
     * written completely or if writing the online resource path is enough.
     * 
     * @return OnlineResource or null
     */
    OnLineResource getOnlineResource();
    
    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
