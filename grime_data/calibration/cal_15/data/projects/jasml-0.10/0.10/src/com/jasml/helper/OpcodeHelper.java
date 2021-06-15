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
 * Author jyang
 * Created on 2006-4-5 9:29:37
 */
package com.jasml.helper;

import java.util.HashMap;

public class OpcodeHelper {

	public static OpcodeInfo[] OPCODES;

	public static HashMap OPCODES_MAP = new HashMap(256);
	//    static{        
	//        for(int i=0;i<Constants.OPCODE_NAMES.length;i++){
	//           OPCODES[i] = new OpcodeInfo((byte)i,
	//                   Constants.OPCODE_NAMES[i],
	//                   (short)(Constants.LENGTH_OF_OPERANDS[i]==null?0:Constants.LENGTH_OF_OPERANDS[i].length),
	//                   Constants.LENGTH_OF_OPERANDS[i]);
	//           OPCODES_MAP.put(Constants.OPCODE_NAMES[i], new Byte((byte)i));
	//        }
	//    } 

	static {
		OpcodeLoader loader = new OpcodeLoader();
		try {
			OPCODES = loader.loadOpcodes();
			for(int i=0;i<OPCODES.length;i++){
				if(OPCODES[i]!=null){
					OPCODES_MAP.put(OPCODES[i].opname, new Byte(OPCODES[i].opcode));
				}
			}
		} catch (Exception e) {
			System.out.println("Can not initializing opcode data.");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static OpcodeInfo getOpcodeInfo(byte opcode) {
		return OPCODES[opcode&0xFF];
	}

	public static OpcodeInfo getOpcodeInfo(String opname) {
		Object o = OPCODES_MAP.get(opname);
		if (o == null) {
			return null;
		}
		return OPCODES[((Byte) o).byteValue() & 0xFF];
	}
}
