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
package org.hibernate.test.hql;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Mimic a JDK 5 enum.
 *
 * @author Steve Ebersole
 */
public class Classification implements Serializable, Comparable {

	public static final Classification COOL = new Classification( "COOL", 0 );
	public static final Classification LAME = new Classification( "LAME", 1 );

	private static final HashMap INSTANCES = new HashMap();
	static {
		INSTANCES.put( COOL.name, COOL );
		INSTANCES.put( LAME.name, LAME );
	}

	private final String name;
	private final int ordinal;
	private final int hashCode;

	private Classification(String name, int ordinal) {
		this.name = name;
		this.ordinal = ordinal;

		int hashCode = name.hashCode();
		hashCode = 29 * hashCode + ordinal;
		this.hashCode = hashCode;
	}

	public String name() {
		return name;
	}

	public int ordinal() {
		return ordinal;
	}

	public boolean equals(Object obj) {
		return compareTo( obj ) == 0;
	}

	public int compareTo(Object o) {
		int otherOrdinal = ( ( Classification ) o ).ordinal;
		if ( ordinal == otherOrdinal ) {
			return 0;
		}
		else if ( ordinal > otherOrdinal ) {
			return 1;
		}
		else {
			return -1;
		}
	}

	public int hashCode() {
		return hashCode;
	}

	public static Classification valueOf(String name) {
		return ( Classification ) INSTANCES.get( name );
	}

	public static Classification valueOf(Integer ordinal) {
		if ( ordinal == null ) {
			return null;
		}
		switch ( ordinal.intValue() ) {
			case 0: return COOL;
			case 1: return LAME;
			default: throw new IllegalArgumentException( "unknown classification ordinal [" + ordinal + "]" );
		}
	}
}
