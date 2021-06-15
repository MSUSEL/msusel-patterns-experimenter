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
package org.hibernate.metamodel.relational;

import java.io.Serializable;

/**
 * Models size restrictions/requirements on a column's datatype.
 * <p/>
 * IMPL NOTE: since we do not necessarily know the datatype up front, and therefore do not necessarily know
 * whether length or precision/scale sizing is needed, we simply account for both here.  Additionally LOB
 * definitions, by standard, are allowed a "multiplier" consisting of 'K' (Kb), 'M' (Mb) or 'G' (Gb).
 *
 * @author Steve Ebersole
 */
public class Size implements Serializable {
	public static enum LobMultiplier {
		NONE( 1 ),
		K( NONE.factor * 1024 ),
		M( K.factor * 1024 ),
		G( M.factor * 1024 );

		private long factor;

		private LobMultiplier(long factor) {
			this.factor = factor;
		}

		public long getFactor() {
			return factor;
		}
	}

	public static final int DEFAULT_LENGTH = 255;
	public static final int DEFAULT_PRECISION = 19;
	public static final int DEFAULT_SCALE = 2;

	private long length = DEFAULT_LENGTH;
	private int precision = DEFAULT_PRECISION;
	private int scale = DEFAULT_SCALE;
	private LobMultiplier lobMultiplier = LobMultiplier.NONE;

	public Size() {
	}

	/**
	 * Complete constructor.
	 *
	 * @param precision numeric precision
	 * @param scale numeric scale
	 * @param length type length
	 * @param lobMultiplier LOB length multiplier
	 */
	public Size(int precision, int scale, long length, LobMultiplier lobMultiplier) {
		this.precision = precision;
		this.scale = scale;
		this.length = length;
		this.lobMultiplier = lobMultiplier;
	}

	public static Size precision(int precision) {
		return new Size( precision, -1, -1, null );
	}

	public static Size precision(int precision, int scale) {
		return new Size( precision, scale, -1, null );
	}

	public static Size length(long length) {
		return new Size( -1, -1, length, null );
	}

	public static Size length(long length, LobMultiplier lobMultiplier) {
		return new Size( -1, -1, length, lobMultiplier );
	}

	public int getPrecision() {
		return precision;
	}

	public int getScale() {
		return scale;
	}

	public long getLength() {
		return length;
	}

	public LobMultiplier getLobMultiplier() {
		return lobMultiplier;
	}

	public void initialize(Size size) {
		this.precision = size.precision;
		this.scale =  size.scale;
		this.length = size.length;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public void setLobMultiplier(LobMultiplier lobMultiplier) {
		this.lobMultiplier = lobMultiplier;
	}
}
