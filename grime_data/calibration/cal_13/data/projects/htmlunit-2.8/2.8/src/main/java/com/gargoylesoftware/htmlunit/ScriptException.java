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
package com.gargoylesoftware.htmlunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.StringTokenizer;

import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.RhinoException;
import net.sourceforge.htmlunit.corejs.javascript.WrappedException;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * An exception that will be thrown if an error occurs during the processing of
 * a script.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 */
public class ScriptException extends RuntimeException {

    private static final long serialVersionUID = 4788896649084231283L;
    private final String scriptSourceCode_;
    private final HtmlPage page_;

    /**
     * Creates an instance.
     * @param page the page in which the script causing this exception was executed
     * @param throwable the exception that was thrown from the script engine
     * @param scriptSourceCode the code that was being executed when this exception
     * was thrown. This may be null if the exception was not caused by execution
     * of JavaScript.
     */
    public ScriptException(final HtmlPage page, final Throwable throwable,
            final String scriptSourceCode) {
        super(getMessageFrom(throwable), throwable);
        scriptSourceCode_ = scriptSourceCode;
        page_ = page;
    }

    private static String getMessageFrom(final Throwable throwable) {
        if (throwable == null) {
            return "null";
        }
        return throwable.getMessage();
    }

    /**
     * Creates an instance.
     * @param page the page in which the script causing this exception was executed
     * @param throwable the exception that was thrown from the script engine
     */
    public ScriptException(final HtmlPage page, final Throwable throwable) {
        this(page, throwable, null);
    }

    /**
     * Prints the stack trace to System.out. If this exception contains another
     * exception then the stack traces for both will be printed.
     */
    @Override
    public void printStackTrace() {
        printStackTrace(System.out);
    }

    /**
     * Prints the stack trace. If this exception contains another exception then
     * the stack traces for both will be printed.
     *
     * @param writer Where the stack trace will be written
     */
    @Override
    public void printStackTrace(final PrintWriter writer) {
        writer.write(createPrintableStackTrace());
    }

    /**
     * Prints the stack trace. If this exception contains another exception then
     * the stack traces for both will be printed.
     *
     * @param stream Where the stack trace will be written
     */
    @Override
    public void printStackTrace(final PrintStream stream) {
        stream.print(createPrintableStackTrace());
    }

    private String createPrintableStackTrace() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        printWriter.println("======= EXCEPTION START ========");

        if (getCause() != null) {
            if (getCause() instanceof EcmaError) {
                final EcmaError ecmaError = (EcmaError) getCause();
                printWriter.print("EcmaError: ");
                printWriter.print("lineNumber=[");
                printWriter.print(ecmaError.lineNumber());
                printWriter.print("] column=[");
                printWriter.print(ecmaError.columnNumber());
                printWriter.print("] lineSource=[");
                printWriter.print(getFailingLine());
                printWriter.print("] name=[");
                printWriter.print(ecmaError.getName());
                printWriter.print("] sourceName=[");
                printWriter.print(ecmaError.sourceName());
                printWriter.print("] message=[");
                printWriter.print(ecmaError.getMessage());
                printWriter.print("]");
                printWriter.println();
            }
            else {
                printWriter.println("Exception class=[" + getCause().getClass().getName() + "]");
            }
        }

        super.printStackTrace(printWriter);
        if (getCause() != null && getCause() instanceof JavaScriptException) {
            final Object value = ((JavaScriptException) getCause()).getValue();

            printWriter.print("JavaScriptException value = ");
            if (value instanceof Throwable) {
                ((Throwable) value).printStackTrace(printWriter);
            }
            else {
                printWriter.println(value);
            }
        }
        else if (getCause() != null && getCause() instanceof WrappedException) {
            final WrappedException wrappedException = (WrappedException) getCause();
            printWriter.print("WrappedException: ");
            wrappedException.printStackTrace(printWriter);

            final Throwable innerException = wrappedException.getWrappedException();
            if (innerException == null) {
                printWriter.println("Inside wrapped exception: null");
            }
            else {
                printWriter.println("Inside wrapped exception:");
                innerException.printStackTrace(printWriter);
            }
        }
        else if (getCause() != null) {
            printWriter.println("Enclosed exception: ");
            getCause().printStackTrace(printWriter);
        }

        if (scriptSourceCode_ != null && scriptSourceCode_.length() > 0) {
            printWriter.println("== CALLING JAVASCRIPT ==");
            printWriter.println(scriptSourceCode_);
        }
        printWriter.println("======= EXCEPTION END ========");

        return stringWriter.toString();
    }

    /**
     * Returns the source code line that failed.
     * @return the source code line that failed
     */
    public String getScriptSourceCode() {
        return scriptSourceCode_;
    }

    /**
     * Returns the line of source that was being executed when this exception was
     * thrown.
     *
     * @return the line of source or an empty string if the exception was not thrown
     * due to the execution of a script.
     */
    public String getFailingLine() {
        final int lineNumber = getFailingLineNumber();
        if (lineNumber == -1 || scriptSourceCode_ == null) {
            return "<no source>";
        }

        try {
            final BufferedReader reader = new BufferedReader(new StringReader(scriptSourceCode_));
            for (int i = 0; i < lineNumber - 1; i++) {
                reader.readLine();
            }
            final String result = reader.readLine();
            reader.close();
            return result;
        }
        catch (final IOException e) {
            // Theoretically impossible
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns the line number of the source that was executing at the time of the exception.
     *
     * @return the line number or -1 if the exception was not thrown due to the
     * execution of a script.
     */
    public int getFailingLineNumber() {
        if (getCause() instanceof RhinoException) {
            final RhinoException cause = (RhinoException) getCause();
            return cause.lineNumber();
        }

        return -1;
    }

    /**
     * Gets the HTML page in which the script error occurred.<br/>
     * Caution: this page may be only partially parsed if the exception occurred in a script
     * executed at parsing time.
     * @return the page
     */
    public HtmlPage getPage() {
        return page_;
    }

    /**
     * Prints the script stack trace.
     * This represents only the script calls.
     * @param writer where the stack trace will be written
     */
    public void printScriptStackTrace(final PrintWriter writer) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        getCause().printStackTrace(printWriter);

        writer.print(getCause().getMessage());
        final StringTokenizer st = new StringTokenizer(stringWriter.toString(), "\r\n");
        while (st.hasMoreTokens()) {
            final String line = st.nextToken();
            if (line.contains("at script")) {
                writer.println();
                writer.print(line.replaceFirst("at script\\.?", "at "));
            }
        }
    }

}
