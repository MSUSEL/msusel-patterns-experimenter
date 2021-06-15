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
package com.jaspersoft.ireport.examples;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 *
 * @author gtoffoli
 */
public class TestScriptlet extends JRAbstractScriptlet {

    @Override
    public void beforeReportInit() throws JRScriptletException {
       this.setVariableValue("testVariable", "beforeReportInit");
    }

    @Override
    public void afterReportInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "afterReportInit");
    }

    @Override
    public void beforePageInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "beforePageInit");
    }

    @Override
    public void afterPageInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "afterPageInit");
    }

    @Override
    public void beforeColumnInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "beforeColumnInit");
    }

    @Override
    public void afterColumnInit() throws JRScriptletException {
        this.setVariableValue("testVariable", "afterColumnInit");
    }

    @Override
    public void beforeGroupInit(String arg0) throws JRScriptletException {
        this.setVariableValue("testVariable", "beforeGroupInit");
    }

    @Override
    public void afterGroupInit(String arg0) throws JRScriptletException {
        this.setVariableValue("testVariable", "afterGroupInit");
    }

    @Override
    public void beforeDetailEval() throws JRScriptletException {
        this.setVariableValue("testVariable", "beforeDetailEval");
    }

    @Override
    public void afterDetailEval() throws JRScriptletException {
        this.setVariableValue("testVariable", "afterDetailEval");
    }

}
