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

/**
 * {@linkplain org.opengis.metadata.identification.Identification} information
 * (includes data and service identification).
 * The following is adapted from
 * <A HREF="http://www.opengis.org/docs/01-111.pdf">OpenGIS&reg; Metadata (Topic 11)</A> specification.
 *
 * <P ALIGN="justify">Identification information contains information to uniquely identify the data.
 * Identification information includes information about the citation for the resource, an abstract,
 * the purpose, credit, the status and points of contact.
 * The {@linkplain org.opengis.metadata.identification.Identification identification}
 * entity is mandatory. It may be specified (subclassed) as
 * {@linkplain org.opengis.metadata.identification.DataIdentification data identification}
 * when used to identify data and as
 * {@linkplain org.opengis.metadata.identification.ServiceIdentification service identification}
 * when used to identify a service.
 *
 * {@linkplain org.opengis.metadata.identification.Identification} is an aggregate of the following entities:</P>
 * <UL>
 *   <LI>{@link org.opengis.metadata.distribution.Format}, format of the data</LI>
 *   <LI>{@link org.opengis.metadata.identification.BrowseGraphic}, graphic overview of the data</LI>
 *   <LI>{@link org.opengis.metadata.identification.Usage}, specific uses of the data</LI>
 *   <LI>{@link org.opengis.metadata.constraint.Constraints}, constraints placed on the resource</LI>
 *   <LI>{@link org.opengis.metadata.identification.Keywords}, keywords describing the resource</LI>
 *   <LI>{@link org.opengis.metadata.maintenance.MaintenanceInformation}, how often the data is scheduled
 *       to be updated and the scope of the update</LI>
 * </UL>
 *
 * <P ALIGN="justify">The
 * {@linkplain org.opengis.metadata.identification.DataIdentification#getGeographicBox geographic box} and
 * {@linkplain org.opengis.metadata.identification.DataIdentification#getGeographicDescription geographic description}
 * elements of {@linkplain org.opengis.metadata.identification.DataIdentification data identification} are conditional;
 * one of them shall be included if the dataset is spatially referenced. If necessary both may be used.</P>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since   GeoAPI 2.0
 */
package org.opengis.metadata.identification;
