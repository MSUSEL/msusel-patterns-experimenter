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
package org.apache.james.transport;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.component.Component;

/**
 *
 * $Id: Loader.java,v 1.8.2.3 2004/03/15 03:54:18 noel Exp $
 */
public class Loader implements Contextualizable, Component {
    protected ClassLoader mailetClassLoader = null;
    protected String baseDirectory = null;
    protected Logger logger;
    protected final String MAILET_PACKAGE = "mailetpackage";
    protected final String MATCHER_PACKAGE = "matcherpackage";
      /**
     * The list of packages that may contain Mailets or matchers
     */
    protected Vector packages;

    /**
     * @see org.apache.avalon.framework.context.Contextualizable#contextualize(Context)
     */
    public void contextualize(final Context context) throws ContextException 
    {
        try 
        {
            baseDirectory = ((File)context.get( "app.home") ).getCanonicalPath();
        } 
        catch (Throwable e) 
        {
            logger.error( "can't get base directory for mailet loader" );
            throw new ContextException("can't contextualise mailet loader " + e.getMessage(), e);
        }
    }

    /**
     * Method setLogger.
     * @param logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    protected void getPackages(Configuration conf, String packageType)
        throws ConfigurationException {
        packages = new Vector();
        packages.addElement("");
        final Configuration[] pkgConfs = conf.getChildren(packageType);
        for (int i = 0; i < pkgConfs.length; i++) {
            Configuration c = pkgConfs[i];
            String packageName = c.getValue();
            if (!packageName.endsWith(".")) {
                packageName += ".";
            }
            packages.addElement(packageName);
        }
    }
    /**
     * Method getMailetClassLoader.
     */
    protected void configureMailetClassLoader() {
        File base = new File(baseDirectory + "/SAR-INF/lib");
        String[] flist = base.list();
        Vector jarlist = new Vector();
        URL[] classPath = null;
        try {
            jarlist.add(new URL("file:///" + baseDirectory + "/SAR-INF/classes/"));
        } catch (MalformedURLException e) {
            logger.error(
                "can't add "
                    + "file:///"
                    + baseDirectory
                    + "/SAR-INF/classes/ to mailet classloader");
        }
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                try {
                    if (flist[i].indexOf("jar") == flist[i].length() - 3) {
                        jarlist.add(new URL("file:///" + baseDirectory +"/SAR-INF/lib/"+ flist[i]));
                        logger.debug("added file:///" + baseDirectory +"/SAR-INF/lib/" + flist[i] + " to mailet Classloader");
                    }
                } catch (MalformedURLException e) {
                    logger.error("can't add file:///" + baseDirectory +"/SAR-INF/lib/"+ flist[i] + " to mailet classloader");
                }
            }
        }
        classPath = (URL[]) jarlist.toArray(new URL[jarlist.size()]);
        mailetClassLoader = new URLClassLoader(classPath, this.getClass().getClassLoader());
    }
}
