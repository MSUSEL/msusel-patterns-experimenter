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
package com.jaspersoft.ireport.designer.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.util.FileResolver;

/**
 *
 * @author gtoffoli
 */
public class ProxyFileResolver implements FileResolver {

    private List<FileResolver> resolvers = null;
    
    /**
     * Add a resolver on top of the list....
     * 
     * @param resolver
     */
    public void addResolver(FileResolver resolver)
    {
        if (!resolvers.contains(resolver))
        {
            resolvers.add(0, resolver);
        }
    }
    
    public void removeResolver(FileResolver resolver)
    {
        resolvers.remove(resolver);
    }
    
    public ProxyFileResolver()
    {
        resolvers = new ArrayList<FileResolver>();
    }
    
    public ProxyFileResolver(List<FileResolver> resolverList)
    {
        this();
        resolvers.addAll(resolverList);
    }
    
    public File resolveFile(String arg0) {
        
        for (FileResolver res : resolvers)
        {
            try {
                File f = res.resolveFile(arg0);
                if (f!= null) return f;
            } catch (Exception ex)
            {
                
            }
        }
        
        return null;
    }

}
