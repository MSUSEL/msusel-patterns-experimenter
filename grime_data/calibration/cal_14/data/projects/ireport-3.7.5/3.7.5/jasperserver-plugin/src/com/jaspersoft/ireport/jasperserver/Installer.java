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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.jasperserver;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.jasperserver.ws.IReportSSLSocketFactory;
import com.jaspersoft.ireport.jasperserver.ws.IReportTrustManager;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import net.sf.jasperreports.engine.util.JRProperties;
import org.apache.commons.httpclient.protocol.Protocol;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        IReportManager.getInstance().getFileResolvers().add(RepoImageCache.getInstance());
        IReportManager.getInstance().addCustomLinkType("ReportExecution", "ReportExecution");

        // Set a fake query executer for sl languages...
        if (JRProperties.getProperty("net.sf.jasperreports.query.executer.factory.sl") == null)
        {
            IReportManager.getInstance().setJRProperty("net.sf.jasperreports.query.executer.factory.sl", "net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory");
        }


        if (JRProperties.getProperty("net.sf.jasperreports.query.executer.factory.domain") == null)
        {
            IReportManager.getInstance().setJRProperty("net.sf.jasperreports.query.executer.factory.domain", "net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory");
        }


        try {
            IReportTrustManager tm = new IReportTrustManager();

            Protocol protocol = new Protocol("https", new IReportSSLSocketFactory(), 443);
            Protocol.registerProtocol("https", protocol);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
