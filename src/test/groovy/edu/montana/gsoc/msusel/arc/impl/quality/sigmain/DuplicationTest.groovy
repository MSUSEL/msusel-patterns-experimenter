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
package edu.montana.gsoc.msusel.arc.impl.quality.sigmain

import spock.lang.Specification

class DuplicationTest extends Specification {

    Duplication fixture

    void setup() {
        fixture = new Duplication()
    }

    void cleanup() {
    }

    def "Measure"() {
    }

    def "ScanSelf"() {
    }

    def "ScanOther"() {
    }

    def "Sanitize"() {
        given:
        String actual = fixture.sanitize("""\
/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant;

/**
 * Used to report attempts to set an unsupported element
 * When the attempt to set the element is made,
 * the code does not not know the name of the task/type
 * based on a mapping from the classname to the task/type.
 * However one class may be used by a lot of task/types.
 * This exception may be caught by code that does know
 * the task/type and it will reset the message to the
 * correct message.
 * This will be done once (in the case of a recursive
 * call to handlechildren).
 *
 * @since Ant 1.6.3
 */
public class UnsupportedElementException extends BuildException {

    private final String element;

    /**
     * Constructs an unsupported element exception.
     * @param msg The string containing the message.
     * @param element The name of the incorrect element.
     */
    public UnsupportedElementException(String msg, String element) {
        super(msg); // what's up bob
        this.element = element;
        // whole line
    }

    /**
     * Get the element that is wrong.
     *
     * @return the element name.
     */
    public String getElement(/* String param1 */) {
        return element;
        // System.out.println("Test");
    }
}
""")
        String expected = """\
package org.apache.tools.ant;
public class UnsupportedElementException extends BuildException {
private final String element;
public UnsupportedElementException(String msg, String element) {
super(msg);
this.element = element;
}
public String getElement() {
return element;
}
}"""
        expect:
        expected == actual
    }

    def "Process Text"() {
        given:
        String text = """\
package org.apache.tools.ant;
public class UnsupportedElementException extends BuildException {
private final String element;
public UnsupportedElementException(String msg, String element) {
super(msg);
this.element = element;
}
public String getElement() {
return element;
}
}"""
        List<String> lines = text.split("\n")

        when:
        String actual = fixture.processText(lines, text)
        
        then:
        actual == ""

    }
}
