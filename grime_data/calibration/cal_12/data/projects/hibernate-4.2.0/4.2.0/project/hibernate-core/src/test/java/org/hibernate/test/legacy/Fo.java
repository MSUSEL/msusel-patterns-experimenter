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
//$Id: Fo.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.io.Serializable;

public final class Fo {
	
	public static Fo newFo(FumCompositeID id) {
		Fo fo = newFo();
		fo.id = id;
		return fo;
	}

	public static Fo newFo() {
		return new Fo();
	}
	
	private Fo() {}

	private FumCompositeID id;
	private byte[] buf;
	private Serializable serial;
	private long version;
	private int x;

	public FumCompositeID getId() {
		return id;
	}

	public void setId(FumCompositeID id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public byte[] getBuf() {
		return buf;
	}
	
	
	public Serializable getSerial() {
		return serial;
	}
	
	
	public void setBuf(byte[] buf) {
		this.buf = buf;
	}
	
	
	public void setSerial(Serializable serial) {
		this.serial = serial;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
	
}







