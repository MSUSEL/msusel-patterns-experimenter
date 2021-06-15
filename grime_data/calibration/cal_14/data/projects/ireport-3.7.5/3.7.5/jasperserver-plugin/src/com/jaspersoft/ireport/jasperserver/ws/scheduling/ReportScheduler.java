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
package com.jaspersoft.ireport.jasperserver.ws.scheduling;

/**
 * ReportScheduler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */
public interface ReportScheduler extends java.rmi.Remote {
    public void deleteJob(long id) throws java.rmi.RemoteException;
    public void deleteJobs(long[] ids) throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.Job getJob(long id) throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.Job scheduleJob(com.jaspersoft.jasperserver.ws.scheduling.Job job) throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.Job updateJob(com.jaspersoft.jasperserver.ws.scheduling.Job job) throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.JobSummary[] getAllJobs() throws java.rmi.RemoteException;
    public com.jaspersoft.jasperserver.ws.scheduling.JobSummary[] getReportJobs(java.lang.String reportURI) throws java.rmi.RemoteException;
}
