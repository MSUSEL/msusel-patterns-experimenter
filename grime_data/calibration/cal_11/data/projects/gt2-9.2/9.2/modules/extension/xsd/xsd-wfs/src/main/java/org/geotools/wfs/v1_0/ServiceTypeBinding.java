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
package org.geotools.wfs.v1_0;

import javax.xml.namespace.QName;

import net.opengis.ows10.CodeType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.ServiceIdentificationType;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.AttributeInstance;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class ServiceTypeBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.Service;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return ServiceIdentificationType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Ows10Factory ows10Factory = Ows10Factory.eINSTANCE;
        ServiceIdentificationType service = ows10Factory.createServiceIdentificationType();

        String name = (String) node.getChildValue("Name");
        String title = (String) node.getChildValue("Title");
        String keywords = (String) node.getChildValue("Keywords");
        if (keywords != null) {
            KeywordsType kwd = ows10Factory.createKeywordsType();
            String[] split = (keywords).split(",");
            for (int i = 0; i < split.length; i++) {
                String kw = split[i].trim();
                kwd.getKeyword().add(kw);
            }
            service.getKeywords().add(kwd);
        }

        String abstract_ = (String) node.getChildValue("Abstract");
        String accessConstraints = (String) node.getChildValue("AccessConstraints");
        String fees = (String) node.getChildValue("Fees");
        // OnlineResource

        CodeType serviceType = ows10Factory.createCodeType();
        serviceType.setValue(name);
        service.setServiceType(serviceType);
        
        service.setServiceTypeVersion("1.0.0");
        
        service.setTitle(title);
        service.setAbstract(abstract_);
        service.setAccessConstraints(accessConstraints);
        service.setFees(fees);

        // service.setServiceType(value)
        // service.setServiceTypeVersion(serviceTypeVersion);

        return service;
    }
}
