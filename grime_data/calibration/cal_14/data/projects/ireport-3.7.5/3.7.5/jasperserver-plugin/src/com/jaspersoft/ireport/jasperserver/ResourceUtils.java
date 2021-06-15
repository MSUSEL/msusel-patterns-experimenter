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
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;

/**
 *
 * @author gtoffoli
 */
public class ResourceUtils {

    /**
     * Create a clone of the resource descriptor. The replication is recursive.
     * @param rd The ResourceDescriptor to clone
     * @return the new clone
     */
    public static ResourceDescriptor cloneResourceDescriptor(ResourceDescriptor rd)
    {
        ResourceDescriptor newRd = new ResourceDescriptor();
        newRd.setName( rd.getName());
        newRd.setWsType( rd.getWsType() );
        newRd.setLabel( rd.getLabel());
        newRd.setDescription( rd.getDescription());
        newRd.setUriString( rd.getUriString());
        newRd.setIsNew( rd.getIsNew());
        
        for (int i=0; i< rd.getChildren().size(); ++i)
        {
            ResourceDescriptor tmpRd = (ResourceDescriptor)rd.getChildren().get(i);
            newRd.getChildren().add( cloneResourceDescriptor( tmpRd ) );
        }
        
        for (int i=0; i< rd.getProperties().size(); ++i)
        {
            ResourceProperty tmpRp = (ResourceProperty)rd.getProperties().get(i);
            newRd.getProperties().add( cloneResourceProperty( tmpRp ) );
        }
        
        return newRd;
    }
    
    /**
     * Create a clone of the resource property. The replication is recursive.
     * @param rp The ResourceProperty to clone
     * @return the new clone
     */
    public static ResourceProperty cloneResourceProperty(ResourceProperty rp)
    {
        ResourceProperty newRp = new ResourceProperty(rp.getName(), rp.getValue());
        
        for (int i=0; i< rp.getProperties().size(); ++i)
        {
            ResourceProperty tmpRp = (ResourceProperty)rp.getProperties().get(i);
            newRp.getProperties().add( cloneResourceProperty( tmpRp ) );
        }
        
        return newRp;
    }

}
