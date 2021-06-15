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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.EventQueue;
import java.io.InputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.netbeans.api.queries.FileEncodingQuery;
import org.openide.filesystems.FileObject;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author gtoffoli
 */
public class JrxmlLoader implements ErrorHandler {

   FileObject file = null;

   public JrxmlLoader(FileObject f)
   {
       this.file = f;
   }

   public JrxmlLoader()
   {
       this(null);
   }


   public JasperDesign reloadJasperDesign() throws JRException {

       if (file == null)
       {
           // Unable to load the jrxml file
           throw new JRException(I18n.getString("JrxmlLoader.Error.FileNF"));
       }

       try {
           InputStream  in = file.getInputStream();
           return reloadJasperDesign(in);
       }
       catch (JRException  ex)
       {
           throw ex;
       } catch (Throwable ex) {
           //ex.printStackTrace();
           throw new JRException(ex);
       }
   }

   public JasperDesign reloadJasperDesign(InputStream in) throws JRException {

       if (EventQueue.isDispatchThread())
       {
           throw new IllegalStateException(I18n.getString("JrxmlLoader.Error.Warning"));
       }

       try {

           JasperDesign jd = JRXmlLoader.load(in);

           return jd;

       }
       catch (JRException  ex)
       {
           ex.printStackTrace();

           throw ex;
       }
       catch (Throwable ex)
       {
           ex.printStackTrace();

           throw new JRException(ex);
       }
       finally
       {
            if (in != null)
            {
                try {
                    in.close();
                } catch (Exception ex2)
                {
                }
            }
       }

   }

   public void warning(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

}

