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
package com.finalist.util;

import com.finalist.jaggenerator.HtmlContentPopUp;

import java.io.*;
import java.util.*;

/**
 * This class performs a 'diff' on two files: i.e. compares the two files and returns a result
 * containing information about the lines that differ.
 * <p>
 * <b>NOTE:</b> This diff tool does not use the same algorithm as the familiar command-line diff,
 * so the results will not always be identical.
 *
 * @todo The output of the diff report if hardcoded to HTML, for the time being.
 * @todo It would be nice if performDiff() generated XML and createReport() used XSLT..
 *
 * @author Michael O'Connor - Finalist IT Group.
 */
public class Diff {
   private File file1;
   private File file2;
   private BufferedReader in1;
   private BufferedReader in2;
   private HashMap lineCounter = new HashMap(2);

   private static final String REPORT_HEADER =
         "<html><head><style type='text/css'>" +
         "body, table { font-size: 9px; font-family: Verdana, Arial, Helvetica, sans-serif }" +
         "font.file1	{color:#ff0000;}" +
         "font.file1-code	{color:#ff0000; font-family: 'courier new',monospace;}" +
         "font.file2	{color:#0000ff;}" +
         "font.file2-code	{color:#0000ff; font-family: 'courier new',monospace;}" +
         "</style></head><body>" +
         "<table><tr><td width='30'><font class='file1'>&lt;</td><td>";

   /**
    * Creates a new Diff, given two files that will be compared.
    *
    * @param file1 the first file.
    * @param file2 the second file.
    * @throws IOException if either of the files doesn't exist.
    */
   public Diff(File file1, File file2) throws IOException {
      this.file1 = file1;
      this.file2 = file2;
      if (!file1.exists()) {
         throw new IOException("File " + file1 + " doesn't exist!");
      }
      if (!file2.exists()) {
         throw new IOException("File " + file2 + " doesn't exist!");
      }
      in1 = new BufferedReader(new FileReader(file1));
      in2 = new BufferedReader(new FileReader(file2));
      lineCounter.put(in1, new Integer(1));
      lineCounter.put(in2, new Integer(1));
   }

   /**
    * Performs the diff on the two files specified in the constructor, returning a
    * formatted human-readable report (HTML, in this case).
    *
    * @return a String representation of the diff report, or <code>null</code> if the
    * two files were identical (excluding whitespace differences).
    * @throws IOException if the files couldn't be read.
    */
   public String performDiff() throws IOException {
      List diffLines = getDiffLines();
      return createReport(diffLines);
   }

   /**
    * Performs the diff and returns the result as a List of DiffConflictLine objects.
    *
    * @return
    * @throws IOException if the files couldn't be read.
    */
   public List getDiffLines() throws IOException {
      ArrayList diffLines = new ArrayList();
      try {
         DiffConflictLine line1 = nextLine(in1);
         DiffConflictLine line2 = nextLine(in2);
         while (!(line1.isEof() && line2.isEof())) {
            if (line1.isEof()) {
               diffLines.add(line2);
               line2 = nextLine(in2);
               continue;
            } else if (line2.isEof()) {
               diffLines.add(line1);
               line1 = nextLine(in1);
               continue;
            }
            if (line1.lineEquals(line2)) {
               //System.out.println("equals: current=" + line1.getLine() + "-x-" + line2.getLine());
               line1 = nextLine(in1);
               line2 = nextLine(in2);
            } else {
               DiffConflictLine conflictLine = null;
               if (containsLine(file2, line1.getLine(), line2.getLineNumber())) {
                  //System.out.println("a is in #2: current=" + line1.getLine() + "-x-" + line2.getLine());
                  MatchResult betterMatch = bestMatch(line1.getLineNumber(), line2.getLineNumber());
                  //System.out.println("current=" + line1.getLine() + "-x-" + line2.getLine() + "\tbetterMatch? " + betterMatch);
                  if (betterMatch != null) {
                     diffLines.add(line1);
                     while(line1.getLineNumber() < betterMatch.endFile1Sequence) {
                        line1 = nextLine(in1);
                        //System.out.println(">>adding '" + line1 + "' from file#1 to conflicts");
                        diffLines.add(line1);
                     }
                     line1 = nextLine(in1);
                     continue;
                  } else {
                     conflictLine = line2;
                     line2 = nextLine(in2);
                  }
               } else {
                  //System.out.println("a isn't in #2: current=" + line1.getLine() + "-x-" + line2.getLine());
                  conflictLine = line1;
                  line1 = nextLine(in1);
               }
               diffLines.add(conflictLine);
            }
         }
         //new HtmlContentPopUp(null, "Diff report:", true, createReport(diffLines)).show();

      } finally {
         if (in1 != null) try { in1.close(); } catch (IOException _) {}
         if (in2 != null) try { in2.close(); } catch (IOException _) {}
      }
      return diffLines;
   }

   /**
    * Finds the longest sequence of lines in file#1 starting with line# 'count1',
    * which matches a sequence in file#2 starting with line# 'count2'.
    * A match is deemed valid if the distance from count1 to the start of the match is less than
    * the length of the sequence.
    * @param count1
    * @param count2
    * @return
    */
   private MatchResult bestMatch(int count1, int count2) {
      int matchDistance = count1;
      int matchLength = 0;
      BufferedReader in1 = null;
      BufferedReader in2 = null;
      try {
         in1 = new BufferedReader(new FileReader(file1));
         in2 = new BufferedReader(new FileReader(file2));

         //1: read through upto the count1 and count2 starting points:
         for (int i = 1; i < count1; i++) {
            in1.readLine();
         }
         for (int i = 1; i < count2; i++) {
            in2.readLine();
         }

         //2: read through file#1 until a match is found with the line from file#2:
         String s1 = in1.readLine();
         String s2 = in2.readLine();
         while (s1 != null) {
            if (s1.trim().equals(s2.trim())) {
               break;
            }
            s1 = in1.readLine();
            count1++;
         }
         if (s1 == null) {
            //no match was found!
            return null;
         }
         matchDistance = count1 - matchDistance;

         //3: follow through the match until it stops:
         while (!(s1 == null || s2 == null) &&
               s1.trim().equals(s2.trim())) {
            matchLength++;
            s1 = in1.readLine();
            s2 = in2.readLine();
         }
      } catch (FileNotFoundException e) {
         //this won't happen!
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (in1 != null) try { in1.close(); } catch (IOException _) {}
         if (in2 != null) try { in2.close(); } catch (IOException _) {}
      }
      if (matchLength < matchDistance) {
         return null;
      }

      return new MatchResult(matchLength, count1 - 1);
   }

   private boolean containsLine(File file, String line, int startPos) {
      int lineCount = 0;
      BufferedReader in = null;
      try {
         in = new BufferedReader(new FileReader(file));
         String s = in.readLine();
         lineCount++;
         while (s != null) {
            if (lineCount > startPos && s != null && line.trim().equals(s.trim())) {
               return true;
            }
            s = in.readLine();
            lineCount++;
         }
      } catch (FileNotFoundException e) {
         //this won't happen!
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (in != null) try { in.close(); } catch (IOException _) {}
      }
      return false;
   }

   private DiffConflictLine nextLine(BufferedReader reader) throws IOException {
      String line = "";
      int lineNumber = currentLineNumber(reader);
      while (line != null && "".equals(line.trim())) {
         line = reader.readLine();
         lineNumber++;
      }
      lineCounter.put(reader, new Integer(lineNumber));
      return line == null ? DiffConflictLine.EOF : new DiffConflictLine(reader == in1, lineNumber - 1, line);
   }

   private int currentLineNumber(BufferedReader reader) {
      return ((Integer) lineCounter.get(reader)).intValue();
   }

   private String createReport(List diffLines) throws IOException {
      if (diffLines.isEmpty()) {
         return null;
      }
      StringBuffer report = new StringBuffer(REPORT_HEADER);
      report.append(file1.getCanonicalPath());
      report.append("</font></td></tr><tr><td width='30'><font class='file2'>&gt;</td><td>");
      report.append(file2.getCanonicalPath()).append("</font></td></tr></table><br>");
      ArrayList temp = new ArrayList();
      for (int i = 0; i < diffLines.size(); i++) {
         DiffConflictLine line = (DiffConflictLine) diffLines.get(i);
         DiffConflictLine next = (i == diffLines.size() - 1) ? null : (DiffConflictLine) diffLines.get(i + 1);
         if (line.precedes(next)) {
            DiffConflictLine last = lastLine(temp);
            if (last != null && !last.precedes(line)) {
               reportLineGroup(report, temp);
            }
            temp.add(line);
            continue;
         }
         if (temp.isEmpty()) {
            reportSingleLine(report, line);
         } else {
            DiffConflictLine last = lastLine(temp);
            if (last.precedes(line)) {
               temp.add(line);
            } else {
               reportLineGroup(report, temp);
               temp.add(line);
            }
         }
      }
      if (temp.size() > 1) {
         reportLineGroup(report, temp);
      } else if (temp.size() == 1) {
         reportSingleLine(report, (DiffConflictLine) temp.get(0));
      }

      report.append("</body></html>");
      return report.toString();
   }

   private void reportSingleLine(StringBuffer report, DiffConflictLine line) {
      report.append("<font class=");
      report.append(line.isFirstFile() ? "'file1'" : "'file2'");
      report.append(">").append(line.getLineNumber()).append("</font><br>");
      report.append(line.toString());
   }

   private void reportLineGroup(StringBuffer report, ArrayList temp) {
      DiffConflictLine last = lastLine(temp);
      DiffConflictLine first = (DiffConflictLine) temp.get(0);
      report.append("<font class=");
      report.append(first.isFirstFile() ? "'file1'" : "'file2'");
      report.append(">").append(first.getLineNumber()).append(',').append(last.getLineNumber());
      report.append("</font><br>");
      Iterator j = temp.iterator();
      while (j.hasNext()) {
         DiffConflictLine groupedLine = (DiffConflictLine) j.next();
         report.append(groupedLine.toString());
      }
      temp.clear();
   }

   private DiffConflictLine lastLine(List group) {
      return (group == null || group.isEmpty()) ? null : (DiffConflictLine) group.get(group.size() - 1);
   }

   /**
    * For testing purposes - I distance off with JUnit, honestly - but it's too slow!
    *
    * @param args [1] file#1, [2] file#2.
    */
   public static void main(String[] args) {
      try {
         Diff diff = new Diff(new File(args[0]), new File(args[1]));
         System.out.println(diff.performDiff());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}

class MatchResult {
   public MatchResult(int length, int endFile1Sequence) {
      this.length = length;
      this.endFile1Sequence = endFile1Sequence;
   }

   int endFile1Sequence;
   int length;

   public String toString() {
      return "Match: end#1=" + endFile1Sequence + ", length=" + length;
   }

}
