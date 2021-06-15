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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * The runner for test methods that run without any browser ({@link BrowserRunner.Browser.NONE})
 *
 * @version $Revision: 5633 $
 * @author Ahmed Ashour
 */
class BrowserNoneClassRunner extends BlockJUnit4ClassRunner {

    public BrowserNoneClassRunner(final Class<WebTestCase> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodBlock(final FrameworkMethod method) {
        final Object test;
        final WebTestCase testCase;
        try {
            testCase = (WebTestCase) createTest();
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return testCase;
                }
            } .run();
        }
        catch (final Throwable e) {
            return new Fail(e);
        }

        Statement statement = methodInvoker(method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withRules(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = withAfters(method, test, statement);

        final NotYetImplemented notYetImplementedBrowsers = method.getAnnotation(NotYetImplemented.class);
        final boolean notYetImplemented = notYetImplementedBrowsers != null;
        statement = new BrowserStatement(statement, method.getMethod(), false,
                notYetImplemented, 1, "");
        return statement;
    }

    @Override
    protected String getName() {
        return "[No Browser]";
    }

    @Override
    protected String testName(final FrameworkMethod method) {
        if (!BrowserVersionClassRunner.maven_) {
            return super.testName(method);
        }
        String className = method.getMethod().getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return String.format("%s[No Browser]", className + '.' + method.getName());
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        final List<FrameworkMethod> methods = super.computeTestMethods();
        for (int i = 0; i < methods.size(); i++) {
            final Method method = methods.get(i).getMethod();
            final Browsers browsers = method.getAnnotation(Browsers.class);
            if (browsers == null || browsers.value()[0] != Browser.NONE) {
                methods.remove(i--);
            }
        }
        return methods;
    }

    static boolean containsTestMethods(final Class<WebTestCase> klass) {
        for (final Method method : klass.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers != null && browsers.value()[0] == Browser.NONE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void validateTestMethods(final List<Throwable> errors) {
        super.validateTestMethods(errors);
        final List<Throwable> collectederrors = new ArrayList<Throwable>();
        for (final FrameworkMethod method : computeTestMethods()) {
            final Browsers browsers = method.getAnnotation(Browsers.class);
            if (browsers != null) {
                for (final Browser browser : browsers.value()) {
                    if (browser == Browser.NONE && browsers.value().length > 1) {
                        collectederrors.add(new Exception("Method " + method.getName()
                                + "() cannot have Browser.NONE along with other Browsers."));
                    }
                }
            }
        }
        for (final Throwable error : collectederrors) {
            errors.add(error);
        }
    }

    private Statement withRules(final FrameworkMethod method, final Object target, final Statement statement) {
        Statement result = statement;
        for (final MethodRule each : rules(target)) {
            result = each.apply(result, method, target);
        }
        return result;
    }
}
