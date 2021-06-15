====
    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
====

Changes for SensorML version 1.0:

2006-09-06:
(1) added gml4sml.xsd compiled using GML subsetting stylesheets
(2) renamed sweImports.xsd to swe4sml.xsd
(3) removed PropertyList in base.xsd -> using swe:DataGroup instead
(4) renamed coordinateSystem.xsd to referenceFrame.xsd
(5) added spatialReferenceFrame and temporalReferenceFrame in referenceFrame.xsd

2006-09-06
(1) now deriving everything from gml:AbstractFeature by restriction in base.xsd
(2) global reorganization of all .xsd files
(3) renamed processes/ProcessList/process to components/ComponentList/component in process.xsd

2006-09-07
(1) derived sml:AbstractComponentType from sml:AbstractProcessType by extension
    adding sml:spatialReferenceFrame, sml:temporalReferenceFrame, gml:boundedBy, gml:location
    sml:interfaces, sml:inputs, sml:outputs, sml:parameters.
(2) Component derives from AbstractComponentType by simply adding an optional method
(3) System derives from AbstractComponentType by adding sml:components, sml:positions

2006-09-12
(1) added additional properties in Event as requested

2006-10-10
(1) Fixed to use SWE common AbstractRecordType instead of AbstractDataGroupType
(2) Removed referenceFrame.xsd schema since it consisted only of properties using gml:CRS -> moved to system.xsd

2006-10-11
(1) Harmonize use of onlineResource in both ResponsibleParty and Document objects
(2) Reordered capabilities and characteristics

2006-12-18 (svn version 1306)
(1) changed imports from gml 3.2.0 to gml 3.1.1 and fixed discepancies in element names

2007-01-24
(1) Harmonized with latest changes to SWE Common, mostly:
	(a) import paths and namespace fixes to 0.0.0 and 0.0 respectively
	(b) swe:propertyName attribute to xs:token
	(c) _DataRecord to AbstractDataRecord

2007-05-09
(1) BUG FIX: Made choice in IoComponentPropertyType optional in order to allow xlink to be used