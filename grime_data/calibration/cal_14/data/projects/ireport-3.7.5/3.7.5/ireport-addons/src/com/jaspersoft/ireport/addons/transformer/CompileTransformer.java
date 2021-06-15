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

package com.jaspersoft.ireport.addons.transformer;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.io.File;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @version $Id: CompileTransformer.java 0 2010-05-31 19:47:22 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class CompileTransformer implements Transformer {

    private CompileTransformerOptions optionsPanel = null;

    public JasperDesign transform(String srcFileName) throws TransformException {

        String jasperFileName = Misc.changeFileExtension(srcFileName,"jasper");
        if ( ((CompileTransformerOptions)getComponent()).isDefaultDirectory())
        {
            File f = new File(jasperFileName);
              if (!IReportManager.getPreferences().getBoolean("useReportDirectoryToCompile", true))
              {
                 jasperFileName = IReportManager.getPreferences().get("reportDirectoryToCompile", ".");
                 if (!jasperFileName.endsWith(File.separator))
                 {
                        jasperFileName += File.separator;
                 }
                 jasperFileName += f.getName();
              }
        }
        
        if (((CompileTransformerOptions)getComponent()).isBackupJasper())
        {
            File oldJasper = new File(jasperFileName);
            if (oldJasper.exists())
            {
                oldJasper.renameTo(new File( Misc.changeFileExtension(srcFileName,"jrxml.bak")) );
            }
        }
        try {
            // Compile the report...
            JasperCompileManager.compileReportToFile(srcFileName, jasperFileName);
        } catch (JRException ex) {
            throw new TransformException(ex);
        }

        return null;
    }

    public String getName() {
        return "Compile files";
    }

    public Component getComponent() {
        if (optionsPanel == null)
        {
            optionsPanel = new CompileTransformerOptions();
        }
        return optionsPanel;
    }


}
