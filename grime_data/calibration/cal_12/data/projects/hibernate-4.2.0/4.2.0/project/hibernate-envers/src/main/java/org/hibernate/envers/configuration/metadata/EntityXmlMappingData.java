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
package org.hibernate.envers.configuration.metadata;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class EntityXmlMappingData {
    private Document mainXmlMapping;
    private List<Document> additionalXmlMappings;
    /**
     * The xml element that maps the class. The root can be one of the folowing elements:
     * class, subclass, union-subclass, joined-subclass
     */
    private Element classMapping;

    public EntityXmlMappingData() {
        mainXmlMapping = DocumentHelper.createDocument();
        additionalXmlMappings = new ArrayList<Document>();
    }

    public Document getMainXmlMapping() {
        return mainXmlMapping;
    }

    public List<Document> getAdditionalXmlMappings() {
        return additionalXmlMappings;
    }

    public Document newAdditionalMapping() {
        Document additionalMapping = DocumentHelper.createDocument();
        additionalXmlMappings.add(additionalMapping);

        return additionalMapping;
    }

    public Element getClassMapping() {
        return classMapping;
    }

    public void setClassMapping(Element classMapping) {
        this.classMapping = classMapping;
    }
}
