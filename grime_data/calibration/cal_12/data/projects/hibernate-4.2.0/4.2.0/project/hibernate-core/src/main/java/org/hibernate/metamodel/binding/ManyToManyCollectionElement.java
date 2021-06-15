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
package org.hibernate.metamodel.binding;

import java.util.HashMap;

import org.dom4j.Element;

/**
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class ManyToManyCollectionElement extends AbstractCollectionElement {

	private final java.util.Map manyToManyFilters = new HashMap();
	private String manyToManyWhere;
	private String manyToManyOrderBy;


	ManyToManyCollectionElement(AbstractPluralAttributeBinding binding) {
		super( binding );
	}

	@Override
	public CollectionElementNature getCollectionElementNature() {
		return CollectionElementNature.MANY_TO_MANY;
	}

	public void fromHbmXml(Element node){
	/*
    <!ELEMENT many-to-many (meta*,(column|formula)*,filter*)>
   	<!ATTLIST many-to-many class CDATA #IMPLIED>
	<!ATTLIST many-to-many node CDATA #IMPLIED>
	<!ATTLIST many-to-many embed-xml (true|false) "true">
	<!ATTLIST many-to-many entity-name CDATA #IMPLIED>
	<!ATTLIST many-to-many column CDATA #IMPLIED>
	<!ATTLIST many-to-many formula CDATA #IMPLIED>
	<!ATTLIST many-to-many not-found (exception|ignore) "exception">
	<!ATTLIST many-to-many outer-join (true|false|auto) #IMPLIED>
	<!ATTLIST many-to-many fetch (join|select) #IMPLIED>
	<!ATTLIST many-to-many lazy (false|proxy) #IMPLIED>
	<!ATTLIST many-to-many foreign-key CDATA #IMPLIED>
	<!ATTLIST many-to-many unique (true|false) "false">
	<!ATTLIST many-to-many where CDATA #IMPLIED>
	<!ATTLIST many-to-many order-by CDATA #IMPLIED>
	<!ATTLIST many-to-many property-ref CDATA #IMPLIED>
    */
	}

	public String getManyToManyWhere() {
		return manyToManyWhere;
	}

	public void setManyToManyWhere(String manyToManyWhere) {
		this.manyToManyWhere = manyToManyWhere;
	}

	public String getManyToManyOrderBy() {
		return manyToManyOrderBy;
	}

	public void setManyToManyOrderBy(String manyToManyOrderBy) {
		this.manyToManyOrderBy = manyToManyOrderBy;
	}
}
