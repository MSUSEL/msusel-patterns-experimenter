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
package com.jaspersoft.ireport.jasperserver.ws;

public class ManagementServiceServiceLocator extends org.apache.axis.client.Service implements com.jaspersoft.ireport.jasperserver.ws.ManagementServiceService {

    public ManagementServiceServiceLocator() {
    }


    public ManagementServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ManagementServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for repository
    private java.lang.String repository_address = "http://127.0.0.1:8080/jasperserver/services/repository";

    public java.lang.String getrepositoryAddress() {
        return repository_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String repositoryWSDDServiceName = "repository";

    public java.lang.String getrepositoryWSDDServiceName() {
        return repositoryWSDDServiceName;
    }

    public void setrepositoryWSDDServiceName(java.lang.String name) {
        repositoryWSDDServiceName = name;
    }

    public com.jaspersoft.ireport.jasperserver.ws.ManagementService getrepository() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(repository_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getrepository(endpoint);
    }

    public com.jaspersoft.ireport.jasperserver.ws.ManagementService getrepository(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.jaspersoft.ireport.jasperserver.ws.RepositorySoapBindingStub _stub = new com.jaspersoft.ireport.jasperserver.ws.RepositorySoapBindingStub(portAddress, this);
            _stub.setPortName(getrepositoryWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setrepositoryEndpointAddress(java.lang.String address) {
        repository_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.jaspersoft.ireport.jasperserver.ws.ManagementService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.jaspersoft.ireport.jasperserver.ws.RepositorySoapBindingStub _stub = new com.jaspersoft.ireport.jasperserver.ws.RepositorySoapBindingStub(new java.net.URL(repository_address), this);
                _stub.setPortName(getrepositoryWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("repository".equals(inputPortName)) {
            return getrepository();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://127.0.0.1:8080/jasperserver/services/repository", "ManagementServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://127.0.0.1:8080/jasperserver/services/repository", "repository"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("repository".equals(portName)) {
            setrepositoryEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
