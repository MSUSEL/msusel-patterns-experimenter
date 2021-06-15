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

import com.finalist.jag.TagEngine;
import com.finalist.jag.SessionContext;
import com.finalist.jag.PageContext;
import com.finalist.jag.JApplicationGen;
import com.finalist.jag.skelet.*;
import com.finalist.jag.template.*;
import com.finalist.jag.template.parser.JagParser;
import com.finalist.jag.template.parser.CharBuffer;
import com.finalist.jag.template.parser.JagBlockCollection;
import com.finalist.util.Diff;
import com.finalist.jaggenerator.HtmlContentPopUp;
import com.finalist.jaggenerator.JagGenerator;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/*
Bugs
		<jag:equal name="blabla" property="blabla" parameter="blabla">
		<jag:equal>
		missing/invalid closing tag results in a infinity loop.
*/

/**
 * This is the original "JSP-tag like" template engine designed by Wendel D. de Witte.
 * <p>
 * This template engine is maintained for backwards compatability, and will probably
 * not be developed further.
 *
 * @see VelocityTemplateEngine
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class JAGTemplateEngine implements TemplateEngine {

   private Boolean overwrite;

   public void setOverwrite(Boolean overwrite) {
      this.overwrite = overwrite;
   }
   
   public int process(Collection documents, String outputDir) throws InterruptedException {
      overwrite = null;
      int totalNumberOfNewFiles = 0;
      String templateDir = JagGenerator.getTemplate().getTemplateDir().getAbsolutePath();

      SkeletDataObj skeletObj = null;
      try {
         SkeletLoader skeletLoader =
               new XMLSkeletLoader(new File(JApplicationGen.getApplicationFile()));
         skeletObj = skeletLoader.getSkeletData();
         if (JApplicationGen.isDisplaySkeletView()) {
            JagSkeletViewer.show(skeletObj);
         }
      } catch (Exception exc) {
         JApplicationGen.log("[Skelet Error] :" + exc.getMessage());
         return 0;
      }

      SessionContext sessionContext = new SessionContext();
      sessionContext.setSkelet(skeletObj);

      TagEngine tagEngine = new TagEngine();

      JApplicationGen.createOutputStructure(templateDir);

      JApplicationGen.log("Number of template files found : " + documents.size() + "\n");
      JagParser parser = new JagParser();
      Iterator iterator = documents.iterator();
      while (iterator.hasNext()) {
         File document = (File) iterator.next();

         try {
            BufferedReader in = new BufferedReader(new FileReader(document));
            CharBuffer input = new CharBuffer(in);

            JApplicationGen.log("[Processing "
                  + document.getPath() + "]");
            parser.process(input);
            in.close();
         } catch (Exception exc) {
         }

         JagBlockCollection lnkJagBlockCollection = parser.getJagBlockCollection();
         //new JagBlockViewer(lnkJagBlockCollection);

         TemplateStructureFactory textBlockTagFactory = new TemplateStructureFactory();
         textBlockTagFactory.create(lnkJagBlockCollection);
         TemplateStructure templateData = textBlockTagFactory.getTemplateStructure();
         //TemplateTreeItemViewer.show(templateData.getRoot());

         TemplateHeaderFactory headerFactory = new TemplateHeaderFactory();
         headerFactory.create(lnkJagBlockCollection);
         TemplateHeaderCollection headers = headerFactory.getHeaderCollection();

         PageContext pageContext = new PageContext(sessionContext);
         pageContext.setHeaderCollection(headers);
         pageContext.setTemplateData(templateData);
         tagEngine.processTags(pageContext);

         FileCreationResult result = createOutputFiles(outputDir, pageContext.getTextCollection());
         if (result.created == 0 && result.skipped == 0) {
            JApplicationGen.log("No files generated by : "
                  + document);
         }
         if (result.skipped > 0) {
            JApplicationGen.log("Skipped generation of " + result.skipped + (result.skipped == 1 ? " file." : " files."));
         }
         if (result.created > 0) {
            JApplicationGen.log("Created " + result.created + " new " + (result.created == 1 ? "file" : "files") +
                  " from template " + document.getName());
         }

         totalNumberOfNewFiles += result.created;
      }

      return totalNumberOfNewFiles;
   }

   private FileCreationResult createOutputFiles(String outputDir, TemplateTextBlockList textBlocks)
   throws InterruptedException {
      String userdir = System.getProperty("user.dir");
      System.setProperty("user.dir", outputDir);
      boolean skipFile = false;
      int newFileCounter = 0;
      int skippedFilesCounter = 0;
      Iterator iterator = textBlocks.iterator();
      StringBuffer fileBuffer = new StringBuffer();
      while (iterator.hasNext()) {
         skipFile = false;
         TemplateTextBlock textBlock = (TemplateTextBlock) iterator.next();
         fileBuffer.append(textBlock.getText());
         if (textBlock.newFile()) {
            try {
               File tempFile = null;
               File file = new File(new File(textBlock.getFile()).getCanonicalPath());
               String path = file.getCanonicalPath();
               String name = file.getName();
               path = path.substring(0, (path.length() - name.length()));


               if (file.exists() && overwrite != Boolean.TRUE) {
                  if (overwrite == Boolean.FALSE) {
                     skippedFilesCounter++;
                     fileBuffer = new StringBuffer();
                     continue;
                  }
                  tempFile = new File(path + "_generated_" + name);
                  FileUtils.createFile(tempFile, fileBuffer.toString());
                  String diff = new Diff(file, tempFile).performDiff();
                  int choice = 999;
                  if (diff != null) {
                     while (choice == 999 ||
                           JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_VIEW_DIFF) {
                        choice = JOptionPane.showOptionDialog(null,
                              "The file " + file + " differs from the existing copy.\n" +
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
                     } else if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_NO_ALL) {
                        overwrite = Boolean.FALSE;
                        skipFile = true;
                     } else if (JApplicationGen.DIALOGUE_OPTIONS[choice] == JApplicationGen.OPTION_YES_ALL) {
                        overwrite = Boolean.TRUE;
                     }
                  } else {
                     skipFile = true;
                  }
               }

               if (skipFile) {
                  FileUtils.deleteFile(tempFile);
                  skippedFilesCounter++;
                  fileBuffer = new StringBuffer();
                  continue;
               }

               if (tempFile != null) {
                  FileUtils.deleteFile(file);
                  tempFile.renameTo(file);
               } else {
                  FileUtils.createFile(file, fileBuffer.toString());
               }
               fileBuffer = new StringBuffer();
               newFileCounter++;
            } catch (IOException exc) {
               JApplicationGen.log("[Error] Create output file failed : " + textBlock.getFile());
               JApplicationGen.log(exc.getMessage());
            }
         }
      }
      System.setProperty("user.dir", userdir);
      return new FileCreationResult(newFileCounter, skippedFilesCounter);
   }

}
