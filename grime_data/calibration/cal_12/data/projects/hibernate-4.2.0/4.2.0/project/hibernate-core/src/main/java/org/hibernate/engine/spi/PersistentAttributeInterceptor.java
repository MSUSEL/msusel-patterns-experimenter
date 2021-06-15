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
package org.hibernate.engine.spi;

/**
 * @author Steve Ebersole
 */
public interface PersistentAttributeInterceptor {

	public boolean readBoolean(Object obj, String name, boolean oldValue);

	public boolean writeBoolean(Object obj, String name, boolean oldValue, boolean newValue);

	public byte readByte(Object obj, String name, byte oldValue);

	public byte writeByte(Object obj, String name, byte oldValue, byte newValue);

	public char readChar(Object obj, String name, char oldValue);

	public char writeChar(Object obj, String name, char oldValue, char newValue);

	public short readShort(Object obj, String name, short oldValue);

	public short writeShort(Object obj, String name, short oldValue, short newValue);

	public int readInt(Object obj, String name, int oldValue);

	public int writeInt(Object obj, String name, int oldValue, int newValue);

	public float readFloat(Object obj, String name, float oldValue);

	public float writeFloat(Object obj, String name, float oldValue, float newValue);

	public double readDouble(Object obj, String name, double oldValue);

	public double writeDouble(Object obj, String name, double oldValue, double newValue);

	public long readLong(Object obj, String name, long oldValue);

	public long writeLong(Object obj, String name, long oldValue, long newValue);

	public Object readObject(Object obj, String name, Object oldValue);

	public Object writeObject(Object obj, String name, Object oldValue, Object newValue);

}
