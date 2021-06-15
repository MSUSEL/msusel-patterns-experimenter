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
package com.itextpdf.text.xml.xmp;

/**
 * An implementation of an XmpSchema.
 */
public class XmpMMSchema extends XmpSchema {

	private static final long serialVersionUID = 1408509596611634862L;
	/** default namespace identifier*/
	public static final String DEFAULT_XPATH_ID = "xmpMM";
	/** default namespace uri*/
	public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/xap/1.0/mm/";
	

	/** A reference to the original document from which this one is derived. It is a minimal reference; missing components can be assumed to be unchanged. For example, a new version might only need to specify the instance ID and version number of the previous version, or a rendition might only need to specify the instance ID and rendition class of the original. */
	public static final String DERIVEDFROM = "xmpMM:DerivedFrom"; 
	/** The common identifier for all versions and renditions of a document. */
	public static final String DOCUMENTID = "xmpMM:DocumentID";
	/** An ordered array of high-level user actions that resulted in this resource. It is intended to give human readers a general indication of the steps taken to make the changes from the previous version to this one. The list should be at an abstract level; it is not intended to be an exhaustive keystroke or other detailed history. */
	public static final String HISTORY = "xmpMM:History";
	/** A reference to the document as it was prior to becoming managed. It is set when a managed document is introduced to an asset management system that does not currently own it. It may or may not include references to different management systems. */
	public static final String MANAGEDFROM = "xmpMM:ManagedFrom";
	/** The name of the asset management system that manages this resource. */
	public static final String MANAGER = "xmpMM:Manager";
	/** A URI identifying the managed resource to the asset management system; the presence of this property is the formal indication that this resource is managed. The form and content of this URI is private to the asset management system. */
	public static final String MANAGETO = "xmpMM:ManageTo";
	/** A URI that can be used to access information about the managed resource through a web browser. It might require a custom browser plugin. */
	public static final String MANAGEUI = "xmpMM:ManageUI";
	/** Specifies a particular variant of the asset management system. The format of this property is private to the specific asset management system. */
	public static final String MANAGERVARIANT = "xmpMM:ManagerVariant";
	/** The rendition class name for this resource.*/
	public static final String RENDITIONCLASS = "xmpMM:RenditionClass";
	/**  Can be used to provide additional rendition parameters that are too complex or verbose to encode in xmpMM: RenditionClass. */
	public static final String RENDITIONPARAMS = "xmpMM:RenditionParams";
	/** The document version identifier for this resource. */
	public static final String VERSIONID = "xmpMM:VersionID";
	/** The version history associated with this resource.*/
	public static final String VERSIONS = "xmpMM:Versions";
	
	public XmpMMSchema() {
		super("xmlns:" + DEFAULT_XPATH_ID + "=\"" + DEFAULT_XPATH_URI + "\"");
	}
}
