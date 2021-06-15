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

import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @version $Id: JrxmlNotifier.java 0 2009-12-09 11:48:09 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class JrxmlNotifier implements FileResourceUpdatingListener {

    public void resourceWillBeUpdated(RepositoryFile repositoryFile, RepositoryReportUnit reportUnit, File file) throws Exception {

        try {
            JasperDesign jd = (JasperDesign) JRXmlLoader.load(file);
            if (jd.getQuery() != null &&
                jd.getQuery().getLanguage() != null &&
                jd.getQuery().getLanguage().equalsIgnoreCase("xmla-mdx"))
            {
                JOptionPane.showMessageDialog(Misc.getMainFrame(), "You are trying to upload on JasperServer a report which uses the xmla-mdx query language.\nThis language is not supported by JasperServer and it is outdated.\nYou should use the mdx language instead.", "Unsupported language", JOptionPane.WARNING_MESSAGE);

            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    public void resourceUpdated(RepositoryFile rf, RepositoryReportUnit reportUnit, File file) throws Exception {
    }



}
