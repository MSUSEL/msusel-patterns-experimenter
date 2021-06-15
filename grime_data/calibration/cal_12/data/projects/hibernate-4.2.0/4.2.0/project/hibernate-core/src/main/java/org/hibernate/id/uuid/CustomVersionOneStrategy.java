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
import java.util.UUID;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.UUIDGenerationStrategy;
import org.hibernate.internal.util.BytesHelper;

/**
 * Applies a version 1 (time-based) generation strategy (using ip address rather than mac address) but applies them in a
 * different layout.  The strategy is very similar to the legacy {@link org.hibernate.id.UUIDHexGenerator} id generator
 * but uses a RFC 4122 compliant layout (variant 2).
 * <p/>
 * NOTE : Can be a bottle neck due to the need to synchronize in order to increment an
 * internal count as part of the algorithm.
 *
 * @author Steve Ebersole
 */
public class CustomVersionOneStrategy implements UUIDGenerationStrategy {
	public int getGeneratedVersion() {
		return 1;
	}

	private final long mostSignificantBits;

	public CustomVersionOneStrategy() {
		// generate the "most significant bits" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		byte[] hiBits = new byte[8];
		// use address as first 32 bits (8 * 4 bytes)
		System.arraycopy( Helper.getAddressBytes(), 0, hiBits, 0, 4 );
		// use the "jvm identifier" as the next 32 bits
		System.arraycopy( Helper.getJvmIdentifierBytes(), 0, hiBits, 4, 4 );
		// set the version (rfc term) appropriately
		hiBits[6] &= 0x0f;
		hiBits[6] |= 0x10;

		mostSignificantBits = BytesHelper.asLong( hiBits );
	}

	public UUID generateUUID(SessionImplementor session) {
		long leastSignificantBits = generateLeastSignificantBits( System.currentTimeMillis() );
		return new UUID( mostSignificantBits, leastSignificantBits );
	}

	public long getMostSignificantBits() {
		return mostSignificantBits;
	}

	public static long generateLeastSignificantBits(long seed) {
		byte[] loBits = new byte[8];

		short hiTime = (short) ( seed >>> 32 );
		int loTime = (int) seed;
		System.arraycopy( BytesHelper.fromShort( hiTime ), 0, loBits, 0, 2 );
		System.arraycopy( BytesHelper.fromInt( loTime ), 0, loBits, 2, 4 );
		System.arraycopy( Helper.getCountBytes(), 0, loBits, 6, 2 );
		loBits[0] &= 0x3f;
		loBits[0] |= ((byte)2 << (byte)6);

		return BytesHelper.asLong( loBits );
	}

	public static void main(String[] args) {
		CustomVersionOneStrategy strategy = new CustomVersionOneStrategy();

		for ( int i = 0; i < 1000; i++ ) {
			System.out.println( "Generation # " + i + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
			byte[] loBits = new byte[8];

			long sysTime = System.currentTimeMillis();
			short hiTime = (short) ( System.currentTimeMillis() >>> 32 );
			int loTime = (int) sysTime;
			System.arraycopy( BytesHelper.fromShort( hiTime ), 0, loBits, 0, 2 );
			System.arraycopy( BytesHelper.fromInt( loTime ), 0, loBits, 2, 4 );
			System.arraycopy( Helper.getCountBytes(), 0, loBits, 6, 2 );

			System.out.println( "    before bit setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
			System.out.println( "       loBits[0] : " + BytesHelper.toBinaryString( loBits[0] ) );
			System.out.println( "             lsb : " + BytesHelper.toBinaryString( BytesHelper.asLong( loBits ) ) );
			System.out.println( "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );

			loBits[0] &= 0x3f;
			loBits[0] |= ((byte)2 << (byte)6);

			System.out.println( "    after bit setting ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
			System.out.println( "       loBits[0] : " + BytesHelper.toBinaryString( loBits[0] ) );
			long leastSignificantBits = BytesHelper.asLong( loBits );
			System.out.println( "             lsb : " + BytesHelper.toBinaryString( leastSignificantBits ) );
			System.out.println( "    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );


			UUID uuid = new UUID( strategy.mostSignificantBits, leastSignificantBits );
			System.out.println( "  uuid : " + uuid.toString() );
			System.out.println( "  variant : " + uuid.variant() );
			System.out.println( "  version : " + uuid.version() );
			if ( uuid.variant() != 2 ) {
				throw new RuntimeException( "bad variant" );
			}
			System.out.println( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
		}
	}
}
