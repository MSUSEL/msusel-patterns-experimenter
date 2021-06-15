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

package com.jaspersoft.ireport.samples.db;

import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import java.sql.Connection;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

/**
 *
 * @version $Id: SampleDatabaseConnection.java 0 2010-01-11 16:20:39 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 * This class does not override the get
 *
 */
public class SampleDatabaseConnection extends JDBCConnection {

    public SampleDatabaseConnection()
    {
        super();
        setName(NbBundle.getMessage(SampleDatabaseConnection.class , "connectionName"));
        setJDBCDriver("org.hsqldb.jdbcDriver");
        setUsername("sa");
        setSavePassword(true);
        setPassword("");
        setUrl("jdbc:hsqldb:hsql://127.0.0.1/");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(SampleDatabaseConnection.class , "connectionType");
    }

    @Override
    public IReportConnectionEditor getIReportConnectionEditor() {
        return new SampleDabataseConnectionEditor();
    }


    @Override
    public Connection getConnection() {

        System.out.println("Getting connection: server state " + RunSampleDatabaseAction.getServer().getState());
        System.out.flush();
        //if (RunSampleDatabaseAction.getServer().getState() != 1)
        //{
            RunSampleDatabaseAction.getServer().start();
        //}
        
        return super.getConnection();
    }
}
