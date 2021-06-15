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
 * Created on 2006-4-3 17:33:59
 */
package com.jasml.classes;

public class Attribute {
    // two byte attribute name index into constant pool, for attribute already known, 
    // this is not used, but translated into attribute tag    
    public int attribute_name_index;
    
    public byte attribute_tag;
    
    public int attribute_length;	
	
    public byte[] attrInfo;
    
    public String attribute_name;
	
    /*
     * This is for subclass
     */
	public Attribute(byte attrTag, int attrLength){
	    this.attribute_tag = attrTag;
	    this.attribute_length = attrLength;
	    attribute_name = Constants.ATTRIBUTE_NAMES[attribute_tag];
	}
	
	/*
	 * this is for the unknow attributes, will just store the attrInfo.
	 */
	public Attribute(int attribute_name_index, int attribute_length, byte[] attrInfo){
	    this.attribute_name_index = attribute_name_index;
	    this.attribute_length = attribute_length;
	    this.attrInfo = attrInfo;
	}

}
