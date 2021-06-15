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
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.openide.util.Mutex;


/**
 *
 * @author gtoffoli
 */
public class RepositoryFolder {
    
    private ResourceDescriptor descriptor;
    private JServer server = null;
    private boolean root = false;
    
    private List children = null;
    
    private boolean loaded = false;

    /** Creates a new instance of RepositoryFolder */
    public RepositoryFolder(JServer server, ResourceDescriptor descriptor, boolean root) {
        this.descriptor = descriptor;
        this.server = server;
        this.root = root;
    }
    
    /** Creates a new instance of RepositoryFolder */
    public RepositoryFolder(JServer server, ResourceDescriptor descriptor) {
        this(server, descriptor, false);
    }

    public String toString()
    {
        if (getDescriptor() != null)
        {
            return ""+getDescriptor().getLabel();
        }
        
        return "???";
    }

    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public ResourceDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(ResourceDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    
    public List getChildren()
    {
        return getChildren(false);
    }
    
    public List getChildren(boolean refresh)
    {
        if (children == null || refresh || !isLoaded()) children = null;
        
        if (children == null)
        {
            try {
                List descriptors = getServer().getWSClient().list(getDescriptor());
                children = parseDescriptors(descriptors);
                loaded = true;
            } catch (Exception ex) {
                
                final String msg = ex.getMessage();
                Mutex.EVENT.readAccess(new Runnable() {

                    public void run() {
                        JOptionPane.showMessageDialog(Misc.getMainFrame(),JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {msg}));
                
                    }
                }); 
                
                ex.printStackTrace();
            }
        } 
        
        return children;
    }
    
    /** Creates a set of RepositoryXXX classes from a set of Descriptors...
     * 
     * @param descriptors
     * @return
     */
    public List parseDescriptors(List descriptors)
    {
       List list = new ArrayList();
       if (descriptors != null)
       {
           for (int i=0; i<descriptors.size(); ++i)
           {
                ResourceDescriptor rd = (ResourceDescriptor)descriptors.get(i);
                
                list.add( createRepositoryObject(getServer(), rd ) );
           }
       }
       return list;
    }
    
    public static RepositoryFolder createRepositoryObject(JServer srv, ResourceDescriptor rd)
    {
        RepositoryFolder rf = null;
        if (rd.getWsType() == null)
        {
            rd.setWsType(ResourceDescriptor.TYPE_UNKNOW);
        }

        if (rd.getWsType().equals( ResourceDescriptor.TYPE_REPORTUNIT)  )
        {
            rf = new RepositoryReportUnit(srv, rd );
        }
        else if (rd.getWsType().equals( ResourceDescriptor.TYPE_FOLDER)  )
        {
            rf = new RepositoryFolder(srv, rd );
        }
        else if (rd.getWsType().equals( ResourceDescriptor.TYPE_JRXML)  )
        {
            rf = new RepositoryJrxmlFile(srv, rd );
        }
        else if (rd.getWsType().equals( ResourceDescriptor.TYPE_STYLE_TEMPLATE)  )
        {
            rf = new RepositoryJrtxFile(srv, rd );
        }
        else if (rd.getWsType().equals( ResourceDescriptor.TYPE_RESOURCE_BUNDLE)  )
        {
            rf = new RepositoryBundleFile(srv, rd );
        }
        else
        {
            rf = new RepositoryFile(srv, rd );
        }
        
        return rf;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
    
    public boolean isDataSource()
    {
       return isDataSource(getDescriptor());
    }
    
    public static boolean isDataSource(ResourceDescriptor rd)
    {
      if (rd == null) return false;
      if (rd.getWsType() == null) return false;
      if (rd.getWsType().equals( rd.TYPE_DATASOURCE) ||
          rd.getWsType().equals( rd.TYPE_DATASOURCE_JDBC) ||
          rd.getWsType().equals( rd.TYPE_DATASOURCE_JNDI) ||
          rd.getWsType().equals( rd.TYPE_DATASOURCE_BEAN) ||
          rd.getWsType().equals( "Domain")) return true;
      return false;
    }
    
    
}
