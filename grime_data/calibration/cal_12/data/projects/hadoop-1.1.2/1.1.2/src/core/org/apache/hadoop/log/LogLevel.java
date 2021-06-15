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
package org.apache.hadoop.log;

import java.io.*;
import java.net.*;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.apache.commons.logging.impl.*;
import org.apache.hadoop.http.HttpServer;
import org.apache.hadoop.util.ServletUtil;

/**
 * Change log level in runtime.
 */
public class LogLevel {
  public static final String USAGES = "\nUSAGES:\n"
    + "java " + LogLevel.class.getName()
    + " -getlevel <host:port> <name>\n"
    + "java " + LogLevel.class.getName()
    + " -setlevel <host:port> <name> <level>\n";

  /**
   * A command line implementation
   */
  public static void main(String[] args) {
    if (args.length == 3 && "-getlevel".equals(args[0])) {
      process("http://" + args[1] + "/logLevel?log=" + args[2]);
      return;
    }
    else if (args.length == 4 && "-setlevel".equals(args[0])) {
      process("http://" + args[1] + "/logLevel?log=" + args[2]
              + "&level=" + args[3]);
      return;
    }

    System.err.println(USAGES);
    System.exit(-1);
  }

  private static void process(String urlstring) {
    try {
      URL url = new URL(urlstring);
      System.out.println("Connecting to " + url);
      URLConnection connection = url.openConnection();
      connection.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(
          connection.getInputStream()));
      for(String line; (line = in.readLine()) != null; )
        if (line.startsWith(MARKER)) {
          System.out.println(TAG.matcher(line).replaceAll(""));
        }
      in.close();
    } catch (IOException ioe) {
      System.err.println("" + ioe);
    }
  }

  static final String MARKER = "<!-- OUTPUT -->";
  static final Pattern TAG = Pattern.compile("<[^>]*>");

  /**
   * A servlet implementation
   */
  public static class Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response
        ) throws ServletException, IOException {

      // Do the authorization
      if (!HttpServer.hasAdministratorAccess(getServletContext(), request,
          response)) {
        return;
      }

      PrintWriter out = ServletUtil.initHTML(response, "Log Level");
      String logName = ServletUtil.getParameter(request, "log");
      String level = ServletUtil.getParameter(request, "level");

      if (logName != null) {
        out.println("<br /><hr /><h3>Results</h3>");
        out.println(MARKER
            + "Submitted Log Name: <b>" + logName + "</b><br />");

        Log log = LogFactory.getLog(logName);
        out.println(MARKER
            + "Log Class: <b>" + log.getClass().getName() +"</b><br />");
        if (level != null) {
          out.println(MARKER + "Submitted Level: <b>" + level + "</b><br />");
        }

        if (log instanceof Log4JLogger) {
          process(((Log4JLogger)log).getLogger(), level, out);
        }
        else if (log instanceof Jdk14Logger) {
          process(((Jdk14Logger)log).getLogger(), level, out);
        }
        else {
          out.println("Sorry, " + log.getClass() + " not supported.<br />");
        }
      }

      out.println(FORMS);
      out.println(ServletUtil.HTML_TAIL);
    }

    static final String FORMS = "\n<br /><hr /><h3>Get / Set</h3>"
        + "\n<form>Log: <input type='text' size='50' name='log' /> "
        + "<input type='submit' value='Get Log Level' />"
        + "</form>"
        + "\n<form>Log: <input type='text' size='50' name='log' /> "
        + "Level: <input type='text' name='level' /> "
        + "<input type='submit' value='Set Log Level' />"
        + "</form>";

    private static void process(org.apache.log4j.Logger log, String level,
        PrintWriter out) throws IOException {
      if (level != null) {
        log.setLevel(org.apache.log4j.Level.toLevel(level));
        out.println(MARKER + "Setting Level to " + level + " ...<br />");
      }
      out.println(MARKER
          + "Effective level: <b>" + log.getEffectiveLevel() + "</b><br />");
    }

    private static void process(java.util.logging.Logger log, String level,
        PrintWriter out) throws IOException {
      if (level != null) {
        log.setLevel(java.util.logging.Level.parse(level));
        out.println(MARKER + "Setting Level to " + level + " ...<br />");
      }

      java.util.logging.Level lev;
      for(; (lev = log.getLevel()) == null; log = log.getParent());
      out.println(MARKER + "Effective level: <b>" + lev + "</b><br />");
    }
  }
}
