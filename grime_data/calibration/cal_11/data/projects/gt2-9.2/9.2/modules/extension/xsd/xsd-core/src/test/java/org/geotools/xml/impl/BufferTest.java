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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xml.impl;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 *
 * @source $URL$
 */
public class BufferTest extends TestCase {
    Buffer buffer;

    protected void setUp() throws Exception {
        buffer = new Buffer(10);
    }

    public void test() throws Exception {
        Consumer consumer = new Consumer(buffer);
        Thread thread = new Thread(consumer);
        thread.start();

        for (int i = 0; i < 1000; i++) {
            buffer.put(new Integer(i));
        }

        thread.join();

        for (int i = 0; i < consumer.taken.size(); i++) {
            Integer integer = (Integer) consumer.taken.get(i);
            assertEquals(i, integer.intValue());
        }
    }

    static class Consumer implements Runnable {
        Buffer buffer;
        List taken;

        public Consumer(Buffer buffer) {
            this.buffer = buffer;
        }

        public void run() {
            taken = new ArrayList();

            for (int i = 0; i < 1000; i++) {
                taken.add(buffer.get());
            }
        }
    }
}
