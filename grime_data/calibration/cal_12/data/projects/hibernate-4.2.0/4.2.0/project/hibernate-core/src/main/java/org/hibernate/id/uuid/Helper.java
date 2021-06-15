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
package org.hibernate.id.uuid;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.hibernate.internal.util.BytesHelper;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class Helper {

	// IP ADDRESS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static final byte[] ADDRESS_BYTES;
	private static final int ADDRESS_INT;
	private static final String ADDRESS_HEX_STRING;

	static {
		byte[] address;
		try {
			address = InetAddress.getLocalHost().getAddress();
		}
		catch ( Exception e ) {
			address = new byte[4];
		}
		ADDRESS_BYTES = address;
		ADDRESS_INT = BytesHelper.toInt( ADDRESS_BYTES );
		ADDRESS_HEX_STRING = format( ADDRESS_INT );
	}

	public static byte[] getAddressBytes() {
		return ADDRESS_BYTES;
	}

	public static int getAddressInt() {
		return ADDRESS_INT;
	}

	public static String getAddressHexString() {
		return ADDRESS_HEX_STRING;
	}


	// JVM identifier ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static final byte[] JVM_IDENTIFIER_BYTES;
	private static final int JVM_IDENTIFIER_INT;
	private static final String JVM_IDENTIFIER_HEX_STRING;

	static {
		JVM_IDENTIFIER_INT = (int) ( System.currentTimeMillis() >>> 8 );
		JVM_IDENTIFIER_BYTES = BytesHelper.fromInt( JVM_IDENTIFIER_INT );
		JVM_IDENTIFIER_HEX_STRING = format( JVM_IDENTIFIER_INT );
	}

	public static byte[] getJvmIdentifierBytes() {
		return JVM_IDENTIFIER_BYTES;
	}

	public static int getJvmIdentifierInt() {
		return JVM_IDENTIFIER_INT;
	}

	public static String getJvmIdentifierHexString() {
		return JVM_IDENTIFIER_HEX_STRING;
	}


	// counter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static short counter = (short) 0;

	/**
	 * Unique in a millisecond for this JVM instance (unless there are > Short.MAX_VALUE instances created in a
	 * millisecond)
	 */
	public static short getCountShort() {
		synchronized ( Helper.class ) {
			if ( counter < 0 ) {
				counter = 0;
			}
			return counter++;
		}
	}

	public static byte[] getCountBytes() {
		return BytesHelper.fromShort( getCountShort() );
	}


	// Helper methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static String format(int value) {
		final String formatted = Integer.toHexString( value );
		StringBuilder buf = new StringBuilder( "00000000" );
		buf.replace( 8 - formatted.length(), 8, formatted );
		return buf.toString();
	}

	public static String format(short value) {
		String formatted = Integer.toHexString( value );
		StringBuilder buf = new StringBuilder( "0000" );
		buf.replace( 4 - formatted.length(), 4, formatted );
		return buf.toString();
	}


	public static void main(String[] args) throws UnknownHostException {
		byte[] addressBytes = InetAddress.getLocalHost().getAddress();
		System.out.println( "Raw ip address bytes : " + addressBytes.toString() );

		int addressInt = BytesHelper.toInt( addressBytes );
		System.out.println( "ip address int : " + addressInt );

		String formatted = Integer.toHexString( addressInt );
		StringBuilder buf = new StringBuilder( "00000000" );
		buf.replace( 8 - formatted.length(), 8, formatted );
		String addressHex = buf.toString();
		System.out.println( "ip address hex : " + addressHex );
	}
}
