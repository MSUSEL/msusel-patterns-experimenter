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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

/**
 *
 * @version $Id: JSFileResolver.java 0 2010-05-27 21:50:17 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class JSFileResolver extends SimpleFileResolver {

    JServer server = null;
    JasperDesign jasperDesign = null;
    String reportUnitUri = null;

    List<ResourceDescriptor> reportUnitResources = null;

    //.getProperty("ireport.jasperserver.reportUnit")

    public JSFileResolver(File parentFolder, JServer server, JasperDesign jasperDesign)
    {
        this(Arrays.asList(parentFolder), server, jasperDesign);
    }

    public JSFileResolver(List<File> parentFolders, JServer server, JasperDesign jasperDesign)
    {
        super(parentFolders);
        this.server = server;
        this.jasperDesign = jasperDesign;
        setResolveAbsolutePath(true);

        if (jasperDesign.getProperty("ireport.jasperserver.reportUnit") != null)
        {
            reportUnitUri = jasperDesign.getProperty("ireport.jasperserver.reportUnit");
        }
    }

    @Override
    public File resolveFile(String fileName) {

        if (fileName.startsWith("repo:"))
        {
            // resolve locally....
            String objectUri = fileName.substring(5);
            try {
            
                if (objectUri.contains("/"))
                {
                    // Locate the resource inside the repository...
                }
                else if (reportUnitUri != null)
                {
                    // Locate the resource inside the report unit, if any...
                    if (reportUnitResources == null)
                    {
                        ResourceDescriptor rd = new ResourceDescriptor();
                        rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
                        rd.setUriString(reportUnitUri);
                        rd = server.getWSClient().get(rd, null);
                        reportUnitResources = server.getWSClient().list(rd);
                        if (reportUnitResources == null)
                        {
                            reportUnitResources = new ArrayList<ResourceDescriptor>();
                        }
                    }

                    // find the resource...
                    for (ResourceDescriptor resource : reportUnitResources)
                    {

                        if (resource.getName().equals(objectUri))
                        {
                            if (resource.getWsType().equals(ResourceDescriptor.TYPE_IMAGE) ||
                                //resource.getWsType().equals(ResourceDescriptor.TYPE_JRXML) ||
                                resource.getWsType().equals(ResourceDescriptor.TYPE_RESOURCE_BUNDLE) ||
                                resource.getWsType().equals(ResourceDescriptor.TYPE_STYLE_TEMPLATE))
                            {
                                // Export the file in a temporary file...
                                String resolvedFileName = JasperServerManager.createTmpFileName(null, resource.getName());
                                // Export the file here..
                                File resolvedFile = new File(resolvedFileName);
                                server.getWSClient().get(resource, resolvedFile);
                                return resolvedFile;
                            }
                        }
                    }
                    System.out.println("Resource " + objectUri + " not found in the report unit at " + reportUnitUri);
                }
            } catch (Exception ex)
            {
                System.out.println("Unable to resolve " + objectUri + " on " + server.getName() + "server ( " +ex.getMessage()+")");
            }
        }
        return super.resolveFile(fileName);
    }

}
