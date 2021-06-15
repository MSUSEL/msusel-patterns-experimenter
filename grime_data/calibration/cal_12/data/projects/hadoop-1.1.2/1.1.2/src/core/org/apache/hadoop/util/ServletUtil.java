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
package org.apache.hadoop.util;

import java.io.*;
import java.util.Calendar;

import javax.servlet.*;

public class ServletUtil {
  /**
   * Initial HTML header
   */
  public static PrintWriter initHTML(ServletResponse response, String title
      ) throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<html>\n"
        + "<link rel='stylesheet' type='text/css' href='/static/hadoop.css'>\n"
        + "<title>" + title + "</title>\n"
        + "<body>\n"
        + "<h1>" + title + "</h1>\n");
    return out;
  }

  /**
   * Get a parameter from a ServletRequest.
   * Return null if the parameter contains only white spaces.
   */
  public static String getParameter(ServletRequest request, String name) {
    String s = request.getParameter(name);
    if (s == null) {
      return null;
    }
    s = s.trim();
    return s.length() == 0? null: s;
  }

  public static final String HTML_TAIL = "<hr />\n"
    + "This is <a href='http://hadoop.apache.org/'>Apache Hadoop</a> release "
    + VersionInfo.getVersion() + "\n"
    + "</body></html>";
  
  /**
   * HTML footer to be added in the jsps.
   * @return the HTML footer.
   */
  public static String htmlFooter() {
    return HTML_TAIL;
  }
  
  /**
   * Generate the percentage graph and returns HTML representation string
   * of the same.
   * 
   * @param perc The percentage value for which graph is to be generated
   * @param width The width of the display table
   * @return HTML String representation of the percentage graph
   * @throws IOException
   */
  public static String percentageGraph(int perc, int width) throws IOException {
    assert perc >= 0; assert perc <= 100;

    StringBuilder builder = new StringBuilder();

    builder.append("<table border=\"1px\" width=\""); builder.append(width);
    builder.append("px\"><tr>");
    if(perc > 0) {
      builder.append("<td cellspacing=\"0\" class=\"perc_filled\" width=\"");
      builder.append(perc); builder.append("%\"></td>");
    }if(perc < 100) {
      builder.append("<td cellspacing=\"0\" class=\"perc_nonfilled\" width=\"");
      builder.append(100 - perc); builder.append("%\"></td>");
    }
    builder.append("</tr></table>");
    return builder.toString();
  }
  
  /**
   * Generate the percentage graph and returns HTML representation string
   * of the same.
   * @param perc The percentage value for which graph is to be generated
   * @param width The width of the display table
   * @return HTML String representation of the percentage graph
   * @throws IOException
   */
  public static String percentageGraph(float perc, int width) throws IOException {
    return percentageGraph((int)perc, width);
  }
}
