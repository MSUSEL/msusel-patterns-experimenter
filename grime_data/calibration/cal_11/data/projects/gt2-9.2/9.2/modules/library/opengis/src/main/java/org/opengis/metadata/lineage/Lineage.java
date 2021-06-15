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
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.lineage;

import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;


/**
 * Information about the events or source data used in constructing the data specified by
 * the scope or lack of knowledge about lineage.
 *
 * Only one of {@linkplain #getStatement statement}, {@linkplain #getProcessSteps process steps}
 * and {@linkplain #getSources sources} should be provided.
 *
 *
 *
 * @source $URL$
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@Profile (level=CORE)
@UML(identifier="LI_Lineage", specification=ISO_19115)
public interface Lineage {
    /**
     * General explanation of the data producer's knowledge about the lineage of a dataset.
     * Should be provided only if
     * {@linkplain org.opengis.metadata.quality.Scope#getLevel scope level} is
     * {@linkplain org.opengis.metadata.maintenance.ScopeCode#DATASET dataset} or
     * {@linkplain org.opengis.metadata.maintenance.ScopeCode#SERIES series}.
     *
     * @return Explanation of the data producer's knowledge about the lineage, or {@code null}.
     */
    @Profile (level=CORE)
    @UML(identifier="statement", obligation=CONDITIONAL, specification=ISO_19115)
    InternationalString getStatement();

    /**
     * Information about an event in the creation process for the data specified by the scope.
     *
     * @return Information about an event in the creation process.
     */
    @UML(identifier="processStep", obligation=CONDITIONAL, specification=ISO_19115)
    Collection<? extends ProcessStep> getProcessSteps();

    /**
     * Information about the source data used in creating the data specified by the scope.
     *
     * @return Information about the source data.
     */
    @UML(identifier="source", obligation=CONDITIONAL, specification=ISO_19115)
    Collection<? extends Source> getSources();
}
