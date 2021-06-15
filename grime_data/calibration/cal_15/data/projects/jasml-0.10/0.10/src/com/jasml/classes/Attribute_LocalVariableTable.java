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
 * Author jyang Created on 2006-4-3 19:21:58
 */
package com.jasml.classes;

public class Attribute_LocalVariableTable extends Attribute {
    public int local_variable_table_length;

    public LocalVariable[] local_variable_table;

    public Attribute_LocalVariableTable(int attrLength, int local_variable_table_length, LocalVariable[] local_variable_table) {
        super(Constants.ATTRIBUTE_LocalVariableTable, attrLength );
        this.local_variable_table_length = local_variable_table_length;
        this.local_variable_table = local_variable_table;
    }

    public static class LocalVariable {
        public int start_pc;

        public int length;

        public int name_index;

        public int descriptor_index;

        public int index;

        public LocalVariable(int start_pc, int length, int name_index, int descriptor_index, int index) {
            this.start_pc = start_pc;
            this.length = length;
            this.name_index = name_index;
            this.descriptor_index = descriptor_index;
            this.index = index;
        }
    }

}