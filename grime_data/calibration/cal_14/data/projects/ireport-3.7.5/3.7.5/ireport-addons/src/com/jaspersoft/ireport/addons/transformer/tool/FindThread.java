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
package com.jaspersoft.ireport.addons.transformer.tool;

import javax.swing.table.*;
import java.io.*;
/**
 *
 * @author  Administrator
 */
public class FindThread implements Runnable {
    
    private TransformationFrame massiveCompilerFrame = null;
    private boolean stop = false;
    private Thread thread = null;
    
    public FindThread(TransformationFrame mcf)
    {
        this.massiveCompilerFrame = mcf;
        thread = new Thread(this);
    }
    
    public void stop()
    {
        stop = true;
    }
    
    public void start()
    {
        thread.start();
    }
    
    public void run() {
        if (massiveCompilerFrame == null)
        {
            return;
        }
        
        // Prepare the file search....
        
        DefaultTableModel dtm = (DefaultTableModel)massiveCompilerFrame.getFileTable().getModel();
        
        dtm.setRowCount(0);
        massiveCompilerFrame.getFileTable().updateUI();
        
        // Path
        File path = new File(massiveCompilerFrame.getFindDirectory());
        
        if (path == null || !path.exists() || path.isFile())
        {
            // Invalid conditions to search... 
            return;
        }
        
        if (!stop)  findFiles(path, massiveCompilerFrame.isSearchSubDirectory(),dtm);
        
        massiveCompilerFrame.finishedFind();
        return;
    }
    
    private int findFiles(File path, boolean recursive, DefaultTableModel tmodel)
    {
        if (stop) return 0;
        int count = 0;
        File[] files = path.listFiles();
        for (int i=0; i<files.length; ++i)
        {
            if (stop) return 0;
            if (files[i].isDirectory() && recursive)
            {
                count += findFiles( files[i], recursive,tmodel);
            }
            else
            {
                // Is the file a JasperReports source?
                if (files[i].getName().toLowerCase().endsWith(".xml") ||
                    files[i].getName().toLowerCase().endsWith(".jrxml"))
                {
                    // Ok, for me is a good file, get it !
                    FileEntry fe = new FileEntry();
                    fe.setFile( files[i] );
                    
                    // Looking for compiled and compilation version...
                    
                    
                    // ....
                    fe.setStatus( fe.STATUS_NOT_TRANSFORMED );
                    tmodel.addRow( new Object[]{fe,fe,fe.decodeStatus(fe.getStatus())});
                    
                }
            }
        }
        
        return count;
    }
    
}
