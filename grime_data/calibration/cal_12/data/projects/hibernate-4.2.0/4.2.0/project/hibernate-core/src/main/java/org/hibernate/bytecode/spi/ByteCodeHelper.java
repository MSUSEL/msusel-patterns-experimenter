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
package org.hibernate.bytecode.spi;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * A helper for reading byte code from various input sources.
 *
 * @author Steve Ebersole
 */
public class ByteCodeHelper {
	private ByteCodeHelper() {
	}

	/**
	 * Reads class byte array info from the given input stream.
	 * <p/>
	 * The stream is closed within this method!
	 *
	 * @param inputStream The stream containing the class binary; null will lead to an {@link IOException}
	 *
	 * @return The read bytes
	 *
	 * @throws IOException Indicates a problem accessing the given stream.
	 */
	public static byte[] readByteCode(InputStream inputStream) throws IOException {
		if ( inputStream == null ) {
			throw new IOException( "null input stream" );
		}

		byte[] buffer = new byte[409600];
		byte[] classBytes = new byte[0];

		try {
			int r = inputStream.read( buffer );
			while ( r >= buffer.length ) {
				byte[] temp = new byte[ classBytes.length + buffer.length ];
				// copy any previously read bytes into the temp array
				System.arraycopy( classBytes, 0, temp, 0, classBytes.length );
				// copy the just read bytes into the temp array (after the previously read)
				System.arraycopy( buffer, 0, temp, classBytes.length, buffer.length );
				classBytes = temp;
				// read the next set of bytes into buffer
				r = inputStream.read( buffer );
			}
			if ( r != -1 ) {
				byte[] temp = new byte[ classBytes.length + r ];
				// copy any previously read bytes into the temp array
				System.arraycopy( classBytes, 0, temp, 0, classBytes.length );
				// copy the just read bytes into the temp array (after the previously read)
				System.arraycopy( buffer, 0, temp, classBytes.length, r );
				classBytes = temp;
			}
		}
		finally {
			try {
				inputStream.close();
			}
			catch (IOException ignore) {
				// intentionally empty
			}
		}

		return classBytes;
	}

	/**
	 * Read class definition from a file.
	 *
	 * @param file The file to read.
	 *
	 * @return The class bytes
	 *
	 * @throws IOException Indicates a problem accessing the given stream.
	 */
	public static byte[] readByteCode(File file) throws IOException {
		return ByteCodeHelper.readByteCode( new FileInputStream( file ) );
	}

	/**
	 * Read class definition a zip (jar) file entry.
	 *
	 * @param zip The zip entry stream.
	 * 
	 * @return The class bytes
	 *
	 * @throws IOException Indicates a problem accessing the given stream.
	 */
	public static byte[] readByteCode(ZipInputStream zip) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        InputStream in = new BufferedInputStream( zip );
        int b;
        while ( ( b = in.read() ) != -1 ) {
            bout.write( b );
        }
        return bout.toByteArray();
    }
}
