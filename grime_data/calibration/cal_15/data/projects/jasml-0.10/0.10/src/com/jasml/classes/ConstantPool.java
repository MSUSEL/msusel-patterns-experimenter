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
 * Author jyang Created on 2006-4-3 14:35:41
 */
package com.jasml.classes;

public class ConstantPool {
    ConstantPoolItem[] poolItems;

    public ConstantPool(ConstantPoolItem[] items) {
        poolItems = items;
    }

    public ConstantPoolItem getConstant(int index) {
        return poolItems[index];
    }

    public int getConstantPoolCount() {
        return poolItems.length;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        ConstantPoolItem item;
        for (int i = 0; i < poolItems.length; i++) {
            item = poolItems[i];
            if (item != null) {
                buf.append(i + " : " + item.toString() + "\r\n");
                if (item instanceof Constant_Double || item instanceof Constant_Long) {
                    i++;
                }
            } else {
                buf.append(i + " : N/A\r\n");
            }
        }
        return buf.toString();
    }
    

}