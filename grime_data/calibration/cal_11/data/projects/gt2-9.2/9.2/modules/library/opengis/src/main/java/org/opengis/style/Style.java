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
import org.opengis.annotation.UML;

import org.opengis.annotation.XmlElement;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

/**
 * <p>A UserStyle is at the same semantic level as a NamedStyle used in the context of a
 * WMS. In a sense, a named style can be thought of as a reference to a hidden UserStyle
 * that is stored inside of a map server.</p>
 *
 * 
 * <p>A portrayal catalog consits of a set of feature portrayal objects. Many may
 * exist for each feature type that may occur in the dataset. each feature object 
 * has assigned a set of portrayal rules.</p>
 * 
 * This class is a merged between ISO 19117 Portrayal and OGC SLD 1.1.0
 *  
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/sld">Implementation specification 1.1.0</A>
 * 
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@UML(identifier="PF_PortrayalCatalog", specification=ISO_19117)
@XmlElement("UserStyle")
public interface Style {

    /** 
     * Style name (machine readable, don't show to users) 
     * 
     * @return String, identification name of this style
     */
    @XmlElement("UserStyle")
    String getName();
    
    /**
     * Returns the description of this style.
     *
     * @return Description with usual informations used
     * for user interfaces.
     */
    @XmlElement("Description")
    Description getDescription();
    
    /**
     * The IsDefault element identifies whether a style is the default style of a layer, for use in
     * SLD ‘library mode’ when rendering or for storing inside of a map server. IsDefault uses
     * “1” or “true” for true and “0” or “false” for false. The default value is “0”.
     */
    @XmlElement("IsDefault")
    boolean isDefault();
    
    /**
     * Returns a collection of feature type style.
     * 
     */
    @UML(identifier="featurePortrayal", obligation=MANDATORY, specification=ISO_19117)
    @XmlElement("FeatureTypeStyle")
    List<? extends FeatureTypeStyle> featureTypeStyles();
        
    /**
     * Returns the default specification used if no rule return true.
     * This specification should not use any external functions.
     * This specification should use at least one spatial attribut.
     * 
     * @return PortrayalSpecification
     */
    @UML(identifier="defaultPortrayalSpec", obligation=MANDATORY, specification=ISO_19117)
    Symbolizer getDefaultSpecification();
    
    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
