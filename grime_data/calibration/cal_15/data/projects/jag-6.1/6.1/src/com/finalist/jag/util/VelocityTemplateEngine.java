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
package com.finalist.jag.util;

import com.finalist.util.Diff;
import com.finalist.jaggenerator.HtmlContentPopUp;
import com.finalist.jaggenerator.JagGenerator;
import com.finalist.jaggenerator.modules.*;
import com.finalist.jag.JApplicationGen;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Collection;
import java.util.Iterator;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.log4j.lf5.LogLevel;

/**
 * A TemplateEngine implementation for Apache's "Velocity" open-source templating engine.
 * <p>
 *
 *
 * @author Michael O'Connor
 */
public class VelocityTemplateEngine implements TemplateEngine {
   /**
    * The templates should output a line beginning with FILE_HEADER to mark
    * the boundary between output files.  In this way, the output of one template
    * can result in the creation of multiple files.
    */
   private static final String FILE_HEADER = "////File:";
   private Boolean overwrite;
   VelocityEngine ve = new VelocityEngine();


   public int process(Collection documents, String outputDir) throws InterruptedException {
      overwrite = null;
      int totalNumberOfNewFiles = 0;
      String templateDir = JagGenerator.jagGenerator.root.config.selectedTemplate.getTemplateDir().getAbsolutePath();
      try {
         //Initialise Velocity:
         Properties props = new Properties();
          //file.resource.loader.path
         props.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateDir + "/templates");
         props.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "false");
         ve.init(props);
      } catch (Exception e) {
         JApplicationGen.log("FATAL: Can't initialise the Velocity template engine. Aborting!  :" + e);
         //this app runs in its own thread - so just quit!
         System.exit(1);
      }

      VelocityContext context = initialiseContext();

      JApplicationGen.createOutputStructure(templateDir);
      JApplicationGen.log("Number of template files found : " + documents.size() + "\n");
      Iterator iterator = documents.iterator();
      while (iterator.hasNext()) {
         File templateFile = (File) iterator.next();
         String relativePath = templateFile.getPath().substring((templateDir + "/templates/").length());
         Template template = null;
         try {

            template = ve.getTemplate(relativePath);
         } catch (ResourceNotFoundException rnfe) {
            JApplicationGen.log("Velocity template error: cannot find template " + relativePath + ":" + rnfe, LogLevel.ERROR);
         } catch (ParseErrorException pee) {
            JApplicationGen.log("Velocity template error: Syntax error in template " + relativePath + ":" + pee, LogLevel.ERROR);
         } catch (Exception e) {
            JApplicationGen.log("Velocity template error: template " + relativePath + ":" + e, LogLevel.ERROR);
         }
         if (template == null) continue;

         StringWriter writer = new StringWriter();
         try {
            JApplicationGen.log("[Processing " + relativePath + "]");
            template.merge(context, writer);
            writer.flush();
            if (writer.toString().indexOf(FILE_HEADER) != -1) {
               FileCreationResult result = createOutputFiles(outputDir, writer.toString());

               if (result.created == 0 && result.skipped == 0) {
                  JApplicationGen.log("No files generated by : "
                                      + relativePath);
               }
               if (result.skipped > 0) {
                  JApplicationGen.log("Skipped generation of " + result.skipped +
                                      (result.skipped == 1 ? " file." : " files."));
               }
               if (result.created > 0) {
                  JApplicationGen.log("Created " + result.created + " new " +
                                      (result.created == 1 ? "file" : "files") +
                                      " from template " + relativePath);
               }
               totalNumberOfNewFiles += result.created;
            }

         } catch (Exception e) {
            JApplicationGen.log("Velocity parser error: template " + templateFile.getName() + ":" + e, LogLevel.ERROR);
            e.printStackTrace();
         } finally {
            try {
               writer.close();
            } catch (IOException e) {
            }
         }
      }

      return totalNumberOfNewFiles;
   }

   public void setOverwrite(Boolean overwrite) {
      this.overwrite = overwrite;
   }

   /**
    * Sets up the Velocity context: where objects are placed that will be needed by the templates.
    *
    * @return the context.
    */
   private VelocityContext initialiseContext() {
      VelocityContext context = new VelocityContext();

      context.put("config", JagGenerator.getObjectsFromTree(Config.class).get(0));
      context.put("app", JagGenerator.getObjectsFromTree(App.class).get(0));
      context.put("paths", JagGenerator.getObjectsFromTree(Paths.class).get(0));
      context.put("datasource", JagGenerator.getObjectsFromTree(Datasource.class).get(0));
      context.put("entities", JagGenerator.getObjectsFromTree(Entity.class));
      context.put("sessions", JagGenerator.getObjectsFromTree(Session.class));

      return context;
   }

   private FileCreationResult createOutputFiles(String outputDir, String parseResult) throws InterruptedException {
      int newFileCounter = 0;
      int skippedFilesCounter = 0;
      // In case of windows \r\n will be found. On linux \n will be found.
      char newline = parseResult.indexOf('\r') != -1 ? '\r' : '\n';
      int pos = parseResult.indexOf(FILE_HEADER);
      while (pos != -1) {
         boolean skipFile = false;
         int nextPos = parseResult.indexOf(FILE_HEADER, pos + 1);
         nextPos = nextPos == -1 ? parseResult.length() : nextPos;
         String fileContent = parseResult.substring(pos, nextPos);
         int endHeaderPos = fileContent.indexOf(newline);
         if (endHeaderPos == -1) {
            JApplicationGen.log("Bad template: expected newline after header.");
            return new FileCreationResult(0, 0);
         }
         String header = fileContent.substring(0, endHeaderPos).trim();
         fileContent = fileContent.substring(endHeaderPos).trim();
         if ("".equals(fileContent)) {
            JApplicationGen.log("Warning! - Empty file - probably a bad template.");
         }

         int fileNamePos = header.indexOf(':');
         if (fileNamePos == -1 || fileNamePos == (header.length() - 1)) {
            JApplicationGen.log("Bad template: header doesn't specify an output file.");
            return new FileCreationResult(0, 0);
         }
         String fileName = header.substring(header.indexOf(':') + 1).trim();
         File origFile = new File(outputDir + '/' + fileName);
         File tempFile = new File(outputDir + '/' + fileName + "_temp");

         try {
            // Add a newline at the end of the file. 
            if (newline == '\r') {
                fileContent += "\r\n";
            } else {
                fileContent += "\r";
            }
            FileUtils.createFile(tempFile, fileContent);

            if (origFile.exists()) {
               if (overwrite != Boolean.FALSE) {
                  String diff = null;
                  try {
                     diff = new Diff(origFile, tempFile).performDiff();
                     int choice = 999;
                     if (diff != null) {
                        if (overwrite == null) {
                           while (choice == 999 || JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_VIEW_DIFF) {
                              choice = JOptionPane.showOptionDialog(null,
                                                                    "The file " + origFile + " differs from the existing copy.\n" +
                                                                    "Do you want to overwrite this file?",
                                                                    "File already exists!",
                                                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                                                    JOptionPane.QUESTION_MESSAGE,
                                                                    null,
                                                                    JApplicationGen.DIALOGUE_OPTIONS,
                                                                    JApplicationGen.OPTION_VIEW_DIFF);
                              if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_VIEW_DIFF) {
                                 new HtmlContentPopUp(null, "Diff report:", true, diff, false).show();
                              }
                           }
                           if (choice == JOptionPane.CLOSED_OPTION ||
                               JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_NO) {
                              skipFile = true;
                           }
                           else if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_NO_ALL) {
                              overwrite = Boolean.FALSE;
                              skipFile = true;
                           }
                           else if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_YES_ALL) {
                              overwrite = Boolean.TRUE;
                           }
                        }
                     } else {
                        skipFile = true;
                     }
                  } catch (IOException e) {
                     JApplicationGen.log("Error! Can't perform diff - file '" + origFile.getPath() +
                                         "' will not be overwritten: " + e);
                     skipFile = true;
                  }
               } else {
                  skipFile = true;
               }
            }

         } catch (IOException e) {
            JApplicationGen.log("Error! Can't create temp file: " + tempFile.getPath() + " - " + e);
            tempFile = null;
         }

         if (skipFile) {
            JApplicationGen.log("Skipping file " + origFile);
            FileUtils.deleteFile(tempFile);
            skippedFilesCounter++;
         } else if (tempFile != null) {
            newFileCounter++;
            if (origFile.exists()) {
               JApplicationGen.log("Backing up and overwriting " + origFile);
               File backup = new File(origFile.getPath() + ".backup");
               FileUtils.deleteFile(backup);
               origFile.renameTo(backup);
               FileUtils.deleteFile(origFile);
            } else {
               JApplicationGen.log("Creating new file " + origFile);
            }
            tempFile.renameTo(origFile);
         }

         parseResult = parseResult.substring(nextPos);
         pos = parseResult.indexOf(FILE_HEADER);
      }

      return new FileCreationResult(newFileCounter, skippedFilesCounter);
   }

}
