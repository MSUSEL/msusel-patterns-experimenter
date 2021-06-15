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
package com.jaspersoft.ireport;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.Repository;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {

        
        if (System.getProperty("javax.xml.parsers.SAXParserFactory") == null)
        {
            System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
        }

        if (System.getProperty("javax.xml.parsers.DocumentBuilderFactory") == null)
        {
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        }

        if (System.getProperty("javax.xml.datatype.DatatypeFactory") == null)
        {
            System.setProperty("javax.xml.datatype.DatatypeFactory","com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl");
        }
        
        

        /*
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            public void dump(String f)
            {
               
                FileObject nodeFP = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(f);
                System.out.println(" Dumping " + f + " -----------------------------");
                if (nodeFP != null)
                {
                    try {
                        System.out.println("URL: " + nodeFP.getURL());
                    } catch (FileStateInvalidException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    try {
                        FileUtil.copy(nodeFP.getInputStream(), System.out);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    System.out.println(f + " END -----------------------------");
                    System.out.flush();
                }
                else
                {
                    System.out.println(f + " NOT FOND -----------------------------");
                }
            }
            public void run() {

                dump("Windows2Local/WindowManager.wswmgr");
                dump("Windows2/WindowManager.wswmgr");
                dump("Windows2Local/Modes/explorer/runtime.wstcref");
                dump("Windows2/Modes/explorer/runtime.wstcref");
            }
        });

         */


    }
}
