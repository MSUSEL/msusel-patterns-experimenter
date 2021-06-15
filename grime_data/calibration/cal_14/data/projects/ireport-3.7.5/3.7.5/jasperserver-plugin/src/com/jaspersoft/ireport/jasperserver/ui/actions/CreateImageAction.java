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
package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.designer.palette.actions.CreateReportElementAction;
import com.jaspersoft.ireport.jasperserver.RepoImageCache;
import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import java.io.File;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRExpressionUtil;

/**
 *
 * @author gtoffoli
 */
public class CreateImageAction  extends CreateReportElementAction 
{

    public JRDesignElement createReportElement(JasperDesign jd)
    {
            RepositoryFile file = (RepositoryFile) getPaletteItem().getData();
            
            
            JRDesignElement element = new JRDesignImage(jd);
            element.setWidth(100);
            element.setHeight(50);
            
            JRDesignExpression exp = new JRDesignExpression();
            exp.setValueClassName("java.lang.String");
            exp.setText("\"repo:" + file.getDescriptor().getUriString()+"\"");
            
            // Try to load the image...
            try {
                String fname = file.getFile();
                
                ImageIcon img = new ImageIcon(fname);
                element.setWidth(img.getIconWidth());
                element.setHeight(img.getIconHeight());
                
                RepoImageCache.getInstance().put( JRExpressionUtil.getSimpleExpressionText(exp) , new File(fname));
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            ((JRDesignImage)element).setExpression(exp);
            
            return element;
    }
}
