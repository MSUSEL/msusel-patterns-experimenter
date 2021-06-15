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
package com.jaspersoft.jrx;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.queryexecuters.QueryExecuterDef;
import com.jaspersoft.ireport.locale.I18n;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.ResourceBundle;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        I18n.addBundleLocation(ResourceBundle.getBundle("/com/jaspersoft/jrx/Bundle"));

        // Add to the iReport classpath the jar with the
        // JRXPathQueryExecuter....

        List<String> classpath = IReportManager.getInstance().getClasspath();
        File libDir = InstalledFileLocator.getDefault().locate("modules/ext", null, false);

        // find a jar called jasperreports-extensions-*.jar
        if (libDir != null && libDir.isDirectory())
        {
            File[] jars = libDir.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.toLowerCase().startsWith("jasperreports-extensions-") &&
                        name.toLowerCase().endsWith(".jar"))
                    {
                        return true;
                    }
                    return false;
                }
            });

            for (int i=0; i<jars.length; ++i)
            {
                if (classpath.contains(jars[i].getPath())) continue;
                classpath.add(jars[i].getPath());
            }
            IReportManager.getInstance().setClasspath(classpath);
        }

        // Plugging the new datasource implementation.
        IReportManager.getInstance().addConnectionImplementationFactory(new JRXMLDataSourceConnectionFactory());

        // adding the query executer for xpath2...
        QueryExecuterDef qed = new QueryExecuterDef("xpath2",
                "com.jaspersoft.jrx.query.JRXPathQueryExecuterFactory",
                "com.jaspersoft.ireport.designer.data.fieldsproviders.XMLFieldsProvider");
        
        IReportManager.getInstance().addQueryExecuterDef(qed, false);

        QueryExecuterDef qedPlSQL = new QueryExecuterDef("plsql",
                "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory",
                "com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider");

        IReportManager.getInstance().addQueryExecuterDef(qedPlSQL, false);


        // Pluggin the new exporter...
        IReportManager.getInstance().getExporterFactories().add(new JRTxtExporterFactory());

    }
}
