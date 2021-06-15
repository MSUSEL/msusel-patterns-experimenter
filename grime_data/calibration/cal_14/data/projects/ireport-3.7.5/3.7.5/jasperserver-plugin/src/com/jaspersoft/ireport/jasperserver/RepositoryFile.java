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

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.io.File;


/**
 *
 * @author gtoffoli
 */
public class RepositoryFile extends RepositoryFolder {

    private String localFileName = null;
    
    
    /** Creates a new instance of RepositoryFolder */
    public RepositoryFile(JServer server, ResourceDescriptor descriptor) {
        super( server, descriptor);
    }

    public String toString()
    {
        if (getDescriptor() != null)
        {
            return ""+getDescriptor().getLabel();
        }   
        return "???";
    }
    
    /**
     * This method return the file rapresented by this resource file.
     * The file is cached in a temporary directory for subsequent calls to this method.
     * Please note: the file is never removed... a delete of this file should be done
     * on plugin startup....
     * The method returns the cached file name.
     *
     */
    public String getFile() throws Exception
    {
        if (localFileName == null || localFileName.length() == 0 || !(new File(localFileName).exists()))
        {
            try {
                String localFile = JasperServerManager.getMainInstance().createTmpFileName("file",getExtension());
                File file = new File(localFile);
                getServer().getWSClient().get(getDescriptor(), file);
                this.localFileName = file.getCanonicalPath();
           } catch (Exception ex)
           {
                throw ex;
           }
        }
        return localFileName;
    }
    
    public String getExtension()
    {
        String name = getDescriptor().getName();
        String ext = null;
        if (name.lastIndexOf(".") >= 0)
        {
            ext = name.substring(name.lastIndexOf(".")+1);
        }
        if (ext != null && ext.length() > 0) return ext;
        return null;
    }
    /**
     * If localFileName exists, remove it and set localFileName to NULL.
     */
    public void resetFileCache()
    {
        if (localFileName != null)
        {
            File f = new File(localFileName);
            if (f.exists())
            {
                f.delete();
            }
        }
        
        localFileName = null;
    }
    
}
