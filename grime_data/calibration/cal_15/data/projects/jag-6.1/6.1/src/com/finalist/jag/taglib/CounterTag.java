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
package com.finalist.jag.taglib;



import com.finalist.jag.*;
import com.finalist.jag.taglib.util.RequestUtil;

import java.util.*;


/**
 * Class CounterTag
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class CounterTag extends TagBodySupport {

    /** Field name */
    private String name = null;

    /** Field sensitive */
    private String odd = "false";

    /** Field output */
    private String output = null;

    /** Field equal */
    protected boolean body = false;

    /** Field counter */
    protected int counter = 0;

    /////////////////////////////////////

    /**
     * Method getName
     *
     *
     * @return
     *
     */
    public String getName() {
        return (this.name);
    }

    /**
     * Method setName
     *
     *
     * @param name
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method getOdd
     *
     *
     * @return
     *
     */
    public String getOdd() {
        return (this.odd);
    }

    /**
     * Method setOdd
     *
     *
     * @param odd
     *
     */
    public void setOdd(String odd) {
        this.odd = odd;
    }

    /**
     * Method getOutput
     *
     *
     * @return
     *
     */
    public String getOutput() {
        return (this.output);
    }

    /**
     * Method setOutput
     *
     *
     * @param name
     *
     */
    public void setOutput(String output) {
        this.output = output;
    }
    /**
     * Method doStartTag
     *
     *
     * @return
     *
     * @throws JagException
     *
     */
    public int doStartTag() throws JagException {

        SessionContext session = getPageContext().getSessionContext();

        try {
            String s = (String) getPageContext().getAttribute(name);
            int    n = new Integer(s).intValue();

            body = n % 2 == 1;

            if (odd != null && odd.equalsIgnoreCase("false")) {
                body = !body;
            }

            if (output != null && output.equalsIgnoreCase("true")) {
            	getWriter().print(s);
            }
        } catch (Exception e) {
            new JagException(e.getMessage());
        }

        return (EVAL_PAGE);
    }

    /**
     * Method doAfterBodyTag
     *
     *
     * @return
     *
     * @throws JagException
     *
     */
    public int doAfterBodyTag() throws JagException {

        return (body && (counter++ < 1))
               ? (EVAL_BODY_TAG)
               : (SKIP_BODY);
    }
}
