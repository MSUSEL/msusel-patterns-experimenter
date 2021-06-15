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
package org.apache.hadoop.eclipse.server;

import java.io.File;
import java.util.logging.Logger;

import org.apache.hadoop.eclipse.Activator;
import org.apache.hadoop.eclipse.ErrorMessageDialog;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.jarpackager.IJarExportRunnable;
import org.eclipse.jdt.ui.jarpackager.JarPackageData;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Methods for interacting with the jar file containing the
 * Mapper/Reducer/Driver classes for a MapReduce job.
 */

public class JarModule implements IRunnableWithProgress {

  static Logger log = Logger.getLogger(JarModule.class.getName());

  private IResource resource;

  private File jarFile;

  public JarModule(IResource resource) {
    this.resource = resource;
  }

  public String getName() {
    return resource.getProject().getName() + "/" + resource.getName();
  }

  /**
   * Creates a JAR file containing the given resource (Java class with
   * main()) and all associated resources
   * 
   * @param resource the resource
   * @return a file designing the created package
   */
  public void run(IProgressMonitor monitor) {

    log.fine("Build jar");
    JarPackageData jarrer = new JarPackageData();

    jarrer.setExportJavaFiles(true);
    jarrer.setExportClassFiles(true);
    jarrer.setExportOutputFolders(true);
    jarrer.setOverwrite(true);

    try {
      // IJavaProject project =
      // (IJavaProject) resource.getProject().getNature(JavaCore.NATURE_ID);

      // check this is the case before letting this method get called
      Object element = resource.getAdapter(IJavaElement.class);
      IType type = ((ICompilationUnit) element).findPrimaryType();
      jarrer.setManifestMainClass(type);

      // Create a temporary JAR file name
      File baseDir = Activator.getDefault().getStateLocation().toFile();

      String prefix =
          String.format("%s_%s-", resource.getProject().getName(), resource
              .getName());
      File jarFile = File.createTempFile(prefix, ".jar", baseDir);
      jarrer.setJarLocation(new Path(jarFile.getAbsolutePath()));

      jarrer.setElements(resource.getProject().members(IResource.FILE));
      IJarExportRunnable runnable =
          jarrer.createJarExportRunnable(Display.getDefault()
              .getActiveShell());
      runnable.run(monitor);

      this.jarFile = jarFile;

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * Allow the retrieval of the resulting JAR file
   * 
   * @return the generated JAR file
   */
  public File getJarFile() {
    return this.jarFile;
  }

  /**
   * Static way to create a JAR package for the given resource and showing a
   * progress bar
   * 
   * @param resource
   * @return
   */
  public static File createJarPackage(IResource resource) {

    JarModule jarModule = new JarModule(resource);
    try {
      PlatformUI.getWorkbench().getProgressService().run(false, true,
          jarModule);

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    File jarFile = jarModule.getJarFile();
    if (jarFile == null) {
      ErrorMessageDialog.display("Run on Hadoop",
          "Unable to create or locate the JAR file for the Job");
      return null;
    }

    return jarFile;
  }

}
