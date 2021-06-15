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
 * Author jyang Created on 2006-4-3 19:19:48
 */
package com.jasml.classes;

public class Attribute_Code extends Attribute {

	public int max_stack;

	public int max_locals;

	public int code_length;

	public Opcode[] codes;

	public int exception_table_length;

	public ExceptionTableItem[] exception_table;

	public int attributes_count;

	public Attribute[] attributes;

	public Attribute_Code() {
		super(Constants.ATTRIBUTE_Code, 0);
	}

	public Attribute_Code(int attrLength, int max_stack, int max_locals, int code_length, Opcode[] codes, int exception_table_length,
			ExceptionTableItem[] exception_Table, int attributes_count, Attribute[] attributes) {
		super(Constants.ATTRIBUTE_Code, attrLength);
		this.max_locals = max_locals;
		this.max_stack = max_stack;
		this.code_length = code_length;
		this.codes = codes;
		this.exception_table_length = exception_table_length;
		this.exception_table = exception_Table;
		this.attributes_count = attributes_count;
		this.attributes = attributes;
	}

	public static class ExceptionTableItem {
		public int start_pc;

		public int end_pc;

		public int handler_pc;

		public int catch_type;

		public ExceptionTableItem(int start_pc, int end_pc, int handler_pc, int catch_type) {
			this.start_pc = start_pc;
			this.end_pc = end_pc;
			this.handler_pc = handler_pc;
			this.catch_type = catch_type;
		}
	}

	public static class Opcode {
		public byte opcode;

		public byte[][] operands;

		public int offset;

		/*
		 * parameter operands store the operands used by the opcode.
		 * the value of first dimension of the operands array decides the number operands,
		 * the second dimesion the respective operands.       
		 */
		public Opcode(int offset, byte opcode, byte[][] operands) {
			this.opcode = opcode;
			this.operands = operands;
			this.offset = offset;
		}

		public Opcode() {

		}
	}

}