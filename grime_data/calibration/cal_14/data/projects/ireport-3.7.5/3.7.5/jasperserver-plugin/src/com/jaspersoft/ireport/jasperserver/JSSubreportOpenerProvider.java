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
package com.jaspersoft.ireport.jasperserver;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlEditorSupport;
import com.jaspersoft.ireport.designer.SubreportOpenerProvider;
import com.jaspersoft.ireport.designer.utils.ExpressionInterpreter;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @version $Id: JSSubreportOpenerProvider.java 0 2009-10-19 10:36:07 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class JSSubreportOpenerProvider implements SubreportOpenerProvider {

    public File openingSubreport(JrxmlEditorSupport ed, JRDesignSubreport subreportElement, File subreportFile) {

        if (subreportFile != null) return subreportFile;

        if (subreportElement.getExpression() != null &&
            subreportElement.getExpression().getValueClassName() != null &&
            subreportElement.getExpression().getValueClassName().equals("java.lang.String"))
        {
            JasperDesign jasperDesign = ed.getCurrentModel();
            JRDesignDataset dataset =  jasperDesign.getMainDesignDataset();

            ExpressionInterpreter interpreter = new ExpressionInterpreter(dataset, IReportManager.getReportClassLoader());

            Object obj = interpreter.interpretExpression( subreportElement.getExpression().getText() );

            if (obj instanceof String &&
                obj != null)
            {
                String exp_value = (String)obj;
                if (exp_value.startsWith("repo:"))
                {
                    String repo_url = exp_value.substring(5);
                    if (repo_url.length() > 0)
                    {
                        File currentfile = FileUtil.toFile( ed.getDataObject().getPrimaryFile() );
                        RepositoryReportUnit reportUnit = JasperServerManager.getMainInstance().getJrxmlReportUnitMap().get( currentfile.getPath() );

                        JServer server = reportUnit.getServer();

                        if (repo_url.startsWith("/"))
                        {
                            // independent resource...
                        }
                        else
                        {
                            // resource inside the report unit...
                            List<RepositoryFolder> children = reportUnit.getChildren(true);
                            for (RepositoryFolder child : children)
                            {
                                if (child.getDescriptor().getName().equals(repo_url))
                                {
                                    if (child instanceof RepositoryJrxmlFile)
                                    {
                                        try {
                                            return new File(((RepositoryJrxmlFile) child).getFile());
                                        } catch (Exception ex) {
                                            return subreportFile;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
            // check if the expression starts with repo:....

        }

        return subreportFile;
    }

}
