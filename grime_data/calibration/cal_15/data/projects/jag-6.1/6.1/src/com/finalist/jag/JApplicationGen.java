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
package com.finalist.jag;

import com.finalist.jag.util.DirectoryIterator;
import com.finalist.jag.util.Log;
import com.finalist.jag.util.TemplateEngine;
import com.finalist.jaggenerator.ConsoleLogger;
import com.finalist.jaggenerator.JagGenerator;
import com.finalist.jaggenerator.LibCopier;
import com.finalist.jaggenerator.modules.Paths;
import com.finalist.jaggenerator.modules.Config;
import com.finalist.jaggenerator.template.Template;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.lf5.LogLevel;


/**
 * This class is responsible for generating the J2EE project from the application file specified in the GUI.
 * Originally this was a console-based Java app, seperate from the GUI.
 *
 */
public class JApplicationGen {

   static org.apache.commons.logging.Log log = LogFactory.getLog(JApplicationGen.class);

   private Boolean overwrite = null;
   private static ConsoleLogger logger;
   private static final String CVS_DIR = "CVS";
   public static final String OPTION_YES = "Yes";
   public static final String OPTION_NO = "No";
   public static final String OPTION_YES_ALL = "Yes to all";
   public static final String OPTION_NO_ALL = "No to all";
   public static final String OPTION_VIEW_DIFF = "View Diff";
   public static final Object[] DIALOGUE_OPTIONS =
         {OPTION_YES, OPTION_NO, OPTION_YES_ALL, OPTION_NO_ALL, OPTION_VIEW_DIFF};

   /**	Jag command line option */
   private static final String SVIEW = "-sview";

   private static String outputDir = "";
   private static String applicationFile = "Skelet.xml";
   private static boolean displaySkeletView = false;
   private static boolean exitOnFinish = false;


   /**
    * Constructor JApplicationGen
    *
    *
    * @param property
    *
    */
   public JApplicationGen(String[] property) {
      if (property.length > 0) {
         JApplicationGen.outputDir = property[0];
         log.debug("Output dir is: " + this.outputDir);

      }

      if (property.length > 1) {
         this.applicationFile = property[1];
         log.debug("project file: : " + this.applicationFile);
      }

   }

   /**
    * Method main
    *
    * @param args
    */
   public static void main(String args[]) {
      try {
         long seconds = System.currentTimeMillis();
         Log.setLogger(logger);
         if (args.length < 1) {
            log("[Error] Invalid command line : JApplicationGen <outputpath> <skeletfile> <overwrite>");
            return;
         }
         Boolean overwrite = null;
         if (args.length >2 ) {
            overwrite = new Boolean(args[2]);
            log("Overwrite mode is: " + args[2]);
         }
         if (args.length >3 ) {
            exitOnFinish = new Boolean(args[3]).booleanValue();
         }
         JApplicationGen jag = new JApplicationGen(args);
         if (JagGenerator.jagGenerator == null) {
            JagGenerator.jagGenerator = new JagGenerator();
            if (exitOnFinish) {
               JagGenerator.jagGenerator.setVisible(false);

               // TextConsole is required for command line generation!
               JagGenerator.jagGenerator.setLogger(new ConsoleLogger(JagGenerator.jagGenerator.textConsole));
            }
            JagGenerator.jagGenerator.loadApplicationFile(new File(args[1]));
            JagGenerator.jagGenerator.save();
         }

         Template template = JagGenerator.jagGenerator.root.config.selectedTemplate;
         log.info("Generate using template: " + template.getTemplateDir());
         TemplateEngine engine = null;
         try {
            engine = (TemplateEngine) Class.forName(
                  template.getEngineClass()).newInstance();
         } catch (Exception e) {
            log("Error! The specified template engine '" +
                template.getEngineClass() +
                "' can't be loaded (" + e + ").  Using the default engine..");
            try {
               engine = (TemplateEngine) Class.forName(
                     Template.DEFAULT_ENGINE_CLASS).newInstance();
               if (overwrite != null)
               engine.setOverwrite(overwrite);

            } catch (Exception e1) {
               log("FATAL! default template engine can't be loaded (" + e1 + ").  Aborting.");
               System.exit(1);
            }
         }

         int totalNumberOfNewFiles = engine.process(
                     jag.getTemplateFiles(template.getTemplateDir().getAbsolutePath()),
                     outputDir);


         seconds = System.currentTimeMillis() - seconds;
         seconds = seconds / 1000;
         log("\nTotal number of new files : " + totalNumberOfNewFiles);

         log("\nStart copying required jar files..");
         try {
            File outputLibDirectory = new File(args[0]+File.separator+"lib");
            if (!outputLibDirectory.exists()) {
               // Create the output lib directory
                if (!outputLibDirectory.mkdir()) {
                   log("Failed to create the directory: "+outputLibDirectory);
                }
            }

            HashMap failedCopies = LibCopier.copyJars(args[0]+File.separator+JagGenerator.jagGenerator.root.paths.getConfigOutput()+File.separator+"lib.xml", args[0]+File.separator+"lib");
            if (failedCopies == null) {
               log("\nError occured while copying the required jar files.");
            } else if (failedCopies.size() > 0) {
               // Not all jars could be copied
               log("\nSome required jar files couldn't be copied from JAG.\nPlease add the following jars to the JAG lib directory:\n");
               boolean j2eeJarMissing = false;
               for (Iterator iterator = failedCopies.keySet().iterator(); iterator.hasNext();) {
                  String fileName = (String) iterator.next();
                  String url = (String) failedCopies.get(fileName);
                  log(" - "+fileName+" : "+url);
                  if ("j2ee.jar".equalsIgnoreCase(fileName)) {
                     j2eeJarMissing = true;
                  }
                  log("");
               }
               if (j2eeJarMissing) {
                  log("\nThe j2ee.jar is missing because it isn't distributed with JAG because the j2ee.jar is no open source jar file.");
                  log("Please download the j2ee.jar from the http://java.sun.com site or from for example a JBoss installation.");
               }
            } else {
               log("\nSuccessfully finished copying the required jar files.");
            }

         } catch (Exception e) {
            log("\n\nAn error occured while copying the required jar files.\n");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
         log("Process finished in " + seconds + " seconds");
      } catch (InterruptedException e) {
         try {
            log("INTERRUPTED BY USER - ABORTED!");
            if (exitOnFinish) {
               System.exit(-1);
            }
         } catch (InterruptedException e1) {
         }
      } finally {
         JagGenerator.finishedGeneration();
         if (exitOnFinish) {
            System.exit(0);
         }
      }
   }

   /**
    * Copies all the source files from the specified template directory to the output directory,
    * not including any CVS directories.
    *
    * @param templatePath
    * @throws InterruptedException
    */
   public static void createOutputStructure(String templatePath) throws InterruptedException {
      try {
         templatePath = new File(templatePath).getCanonicalPath();
         templatePath = templatePath.replace('\\', '/');
         File file = null;
         Paths paths = (Paths) JagGenerator.getObjectsFromTree(Paths.class).get(0);
         Config config = (Config) JagGenerator.getObjectsFromTree(Config.class).get(0);
         DirectoryIterator iterator = new DirectoryIterator(templatePath, true, true);
         while ((file = iterator.getNext()) != null) {
            boolean copyFile = false;
            //don't copy CVS files!
            String fullFilename = file.getCanonicalPath();
            int lastDirPos = fullFilename.lastIndexOf(System.getProperty("file.separator"));
            if (CVS_DIR.equals(file.getCanonicalPath().
                  substring(fullFilename.length() - CVS_DIR.length(), fullFilename.length())) ||
                                                                                              CVS_DIR.equals(fullFilename.substring(lastDirPos - CVS_DIR.length(), lastDirPos))) {
               continue;
            }
             if ("readme.txt".equals(file.getName())) {
                 continue;
             }
            String fileOut = outputDir.replace('\\', '/');
            String path = file.getCanonicalPath().replace('\\', '/');


            if (path.indexOf(templatePath) == 0) {
               path = path.substring(templatePath.length());
               // Now check if it is one of the sources. THe should be copied to the configured paths.

               //@TODO make this more generic.
                // Somehow define the different mappings.
                //
                // In the template source directory is checked if relevant sources should be copied.

               /////////////////////
               //// CONFIG SOURCES
               /////////////////////
               if (path.startsWith(Paths.CONF_GENERAL_DIR)) {
                  // Generic configuration files.
                  path = paths.getConfigOutput() + path.substring(Paths.CONF_GENERAL_DIR.length());
                  copyFile = true;
               }
               else if (path.startsWith(Paths.CONF_STRUTS_DIR)) {
                   // Struts specific configuration files.
                   path = paths.getConfigOutput() + path.substring(Paths.CONF_STRUTS_DIR.length());
                   copyFile = true;
               }
               else if (path.startsWith(Paths.CONF_TAPESTRY4_DIR)) {
                   // Tapestry specific configuration files.
                   path = paths.getConfigOutput() + path.substring(Paths.CONF_TAPESTRY4_DIR.length());
                   copyFile = true;
               }
               else if (path.startsWith(Paths.CONF_SWING_DIR)) {
                   // Swing specific configuration files.
                   path = paths.getConfigOutput() + path.substring(Paths.CONF_SWING_DIR.length());
                   copyFile = true;
               }



               /////////////////////////////////////
               // PRESENTATION TIER SOURCES
               /////////////////////////////////////
               else if (path.startsWith(Paths.JAVA_WEB_STRUTS_DIR)) {
                  path = paths.getJspOutput() + path.substring(Paths.JAVA_WEB_STRUTS_DIR.length());
                  if (config.matchWebTier("struts").booleanValue()) {
                     copyFile = true;
                  }
               }
               else if (path.startsWith(Paths.JAVA_WEB_TAPESTRY4_DIR)) {
                  path = paths.getJspOutput() + path.substring(Paths.JAVA_WEB_TAPESTRY4_DIR.length());
                  if (config.matchWebTier("tapestry").booleanValue()) {
                     copyFile = true;
                  }
               }
               else if (path.startsWith(Paths.JAVA_SWING_DIR)) {
                  path = paths.getSwingOutput() + path.substring(Paths.JAVA_SWING_DIR.length());
                  if (config.matchWebTier("swing").booleanValue()) {
                     copyFile = true;
                  }
               }
               else if (path.startsWith(Paths.JAVA_STRUTS_DIR)) {
                  path = paths.getWebOutput() + path.substring(Paths.JAVA_STRUTS_DIR.length());
                  if (config.matchWebTier("struts").booleanValue()) {
                     copyFile = true;
                  }
               }
               else if (path.startsWith(Paths.JAVA_TAPESTRY4_DIR)) {
                  path = paths.getWebOutput() + path.substring(Paths.JAVA_TAPESTRY4_DIR.length());
                  if (config.matchWebTier("tapestry").booleanValue()) {
                     copyFile = true;
                  }
               }


               //////////////////////////////////
               // Business tier sources..
               //////////////////////////////////
               else if (path.startsWith(Paths.JAVA_EJB2_DIR)) {
                  path = paths.getEjbOutput() + path.substring(Paths.JAVA_EJB2_DIR.length());
                  if (config.matchBusinessTier("ejb 2").booleanValue()) {
                     copyFile = true;
                  }
               }
               else if (path.startsWith(Paths.JAVA_EJB3_DIR)) {
                  path = paths.getEjbOutput() + path.substring(Paths.JAVA_EJB3_DIR.length());
                  if (config.matchBusinessTier("ejb 3").booleanValue()) {
                     copyFile = true;
                  }
               }
               else if (path.startsWith(Paths.JAVA_HIBERNATE2_DIR)) {
                  path = paths.getHibernateOutput() + path.substring(Paths.JAVA_HIBERNATE2_DIR.length());
                  if (config.matchBusinessTier("hibernate 2").booleanValue()) {
                     copyFile = true;
                  }
               }
               else if (path.startsWith(Paths.JAVA_HIBERNATE3_DIR)) {
                  path = paths.getHibernateOutput() + path.substring(Paths.JAVA_HIBERNATE3_DIR.length());
                  if (config.matchBusinessTier("hibernate 3").booleanValue()) {
                     copyFile = true;
                  }
               }


               ////////////////////////////
               // MOCK Tier.
               /////////////////////////////
               else if (path.startsWith(Paths.JAVA_MOCK_DIR)) {
                  path = paths.getMockOutput() + path.substring(Paths.JAVA_MOCK_DIR.length());
                  if (config.useMock().booleanValue()) {
                     copyFile = true;
                  }
               }


               ///////////////////////////////
               // SERVICE TIER
               ///////////////////////////////
               else if (path.startsWith(Paths.JAVA_SERVICE_DIR)) {
                  path = paths.getServiceOutput() + path.substring(Paths.JAVA_SERVICE_DIR.length());
                   // Will always be copied!
                  copyFile = true;
               }

               else if (path.startsWith(Paths.JAVA_TEST_DIR)) {
                  path = paths.getTestOutput() + path.substring(Paths.JAVA_TEST_DIR.length());
                  // Will always be copied.
                  copyFile = true;
               }

               else if ((path.indexOf("build.bat") != -1) || ((path.indexOf("deploy.bat") != -1)))  {
                  copyFile = true;
               }
            }

            // On linux this would otherwise fail in case a path starts with a . !
            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            // Only copy the file if the relevant business/web tier was selected
            if (copyFile) {
               fileOut += path;
               path = outputDir + path;
               if (!file.isDirectory()) {
                  String name = file.getName();
                  path = path.substring(0, (path.length() - name.length()));
               }

               new File(path).mkdirs();
               if (!file.isDirectory()) {
                  byte array[] = new byte[1024];
                  int size = 0;
                  try {
                     BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                     BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileOut));

                     while ((size = in.read(array)) != -1)
                        out.write(array, 0, size);
                     in.close();
                     out.flush();
                     out.close();
                  } catch (Exception exc) {
                     log("[Error] Copy output file failed : " + fileOut);
                     log(exc.getMessage());
                  }
               }
            }
         }
      } catch (Exception exc) {
         log.error("Error while copying files: ", exc);
         log(exc.getMessage());
      }
   }

   /**
    * Returns a collection of File for all *.jag files in
    * the given directory.
    */
   private Collection getTemplateFiles(String path) {
      ArrayList documents = new ArrayList();
      DirectoryIterator iterator = new DirectoryIterator(path);
      File skeletFile = null;

      while ((skeletFile = iterator.getNext()) != null) {
         if (!skeletFile.getName().endsWith(".vsl"))
            continue;
         documents.add(skeletFile);
      }

      return documents;
   }

   public static void setLogger(ConsoleLogger a_logger) {
      logger = a_logger;
   }

   /**
    * If the logger is set to the ConsoleLogger and this class has been interrupted by the JAG gui user,
    * the resulting act of updating the JTextField can cause write lock acquisition errors in the Swing classes.
    * This method first checks the interrupted status before logging, and throws an exception if appropriate.
    * @param message
    * @throws InterruptedException
    */
   public static void log(String message) throws InterruptedException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      }
      Log.log(message);
   }

   public static void log(String message, LogLevel level) throws InterruptedException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      }
      logger.log(message, level);
   }

   /**
    * Gets the application file, retrieved from the GUI.
    *
    * @return file name.
    */
   public static String getApplicationFile() {
      return applicationFile;
   }

   /**
    * If this property is set, a graphical tree representation of the applicaton XML file is shown
    * to the user.  Originally used for debugging, I think.
    *
    * @return true if displayed.
    */
   public static boolean isDisplaySkeletView() {
      return displaySkeletView;
   }

}