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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.ows10.DCPType;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Factory;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.Node;

public class CapabilityBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.Capability;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return OperationsMetadataType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Ows10Factory ows10Factory = Ows10Factory.eINSTANCE;

        OperationsMetadataType om = ows10Factory.createOperationsMetadataType();

        Node request = node.getChild("Request");

        OperationType operation;

        operation = getCapabilities(request.getChild("GetCapabilities"), ows10Factory);
        addOperation(om, operation);

        operation = dft(request.getChild("DescribeFeatureType"), ows10Factory);
        addOperation(om, operation);

        operation = getFeature(request.getChild("GetFeature"), ows10Factory);
        addOperation(om, operation);

        operation = createOperation("Transaction", node, ows10Factory);
        addOperation(om, operation);

        operation = createOperation("LockFeature", node, ows10Factory);
        addOperation(om, operation);

        operation = createOperation("GetFeatureWithLock", node, ows10Factory);
        addOperation(om, operation);

        operation = createOperation("Transaction", node, ows10Factory);
        addOperation(om, operation);

        return om;
    }

    private OperationType getFeature(Node node, Ows10Factory ows10Factory) {
        OperationType operationType = createOperation("GetFeature", node, ows10Factory);
        addParameter(node, ows10Factory, operationType, "ResultFormat");
        return operationType;
    }

    private OperationType dft(Node node, Ows10Factory ows10Factory) {
        OperationType operationType = createOperation("DescribeFeatureType", node, ows10Factory);

        addParameter(node, ows10Factory, operationType, "SchemaDescriptionLanguage");
        return operationType;
    }

    private OperationType createOperation(String opetationName, Node node, Ows10Factory ows10Factory) {
        if (node == null) {
            return null;
        }
        OperationType operationType = ows10Factory.createOperationType();
        operationType.setName(opetationName);
        addDCPTypes(node, operationType);
        return operationType;
    }

    @SuppressWarnings("unchecked")
    private void addParameter(Node node, Ows10Factory ows10Factory, OperationType operationType,
            String parameterName) {
        Node paramParentNode = node.getChild(parameterName);
        List<String> paramValues = childNames(paramParentNode);

        DomainType domain = ows10Factory.createDomainType();
        domain.setName(parameterName);

        for (String paramValue : paramValues) {
            domain.getValue().add(paramValue);
        }
        operationType.getParameter().add(domain);
    }

    @SuppressWarnings("unchecked")
    private List<String> childNames(Node node) {
        if (null == node) {
            return Collections.emptyList();
        }
        List<Node> children = node.getChildren();
        List<String> names = new ArrayList<String>(children.size());
        for (Node child : children) {
            InstanceComponent component = child.getComponent();
            String paramValue = component.getName();
            names.add(paramValue);
        }
        return names;
    }

    private OperationType getCapabilities(Node node, Ows10Factory ows10Factory) {
        if (node == null) {
            return null;
        }
        OperationType operationType = ows10Factory.createOperationType();
        operationType.setName("GetCapabilities");
        addDCPTypes(node, operationType);
        return operationType;
    }

    @SuppressWarnings("unchecked")
    private void addDCPTypes(Node node, OperationType operationType) {
        List<Node> dcpNodes = node.getChildren(DCPType.class);
        for (Node dcpNode : dcpNodes) {
            DCPType dcp = (DCPType) dcpNode.getValue();
            operationType.getDCP().add(dcp);
        }
    }

    @SuppressWarnings("unchecked")
    private void addOperation(OperationsMetadataType om, OperationType operation) {
        if (operation != null) {
            om.getOperation().add(operation);
        }
    }

}
