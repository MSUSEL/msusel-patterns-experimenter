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
package org.hibernate.engine.jdbc.internal;


/**
 * Models type info extracted from {@link java.sql.DatabaseMetaData#getTypeInfo()}
 *
 * @author Steve Ebersole
 */
public class TypeInfo {
	private final String typeName;
	private final int jdbcTypeCode;
	private final String[] createParams;
	private final boolean unsigned;
	private final int precision;
	private final short minimumScale;
	private final short maximumScale;
	private final boolean fixedPrecisionScale;
	private final String literalPrefix;
	private final String literalSuffix;
	private final boolean caseSensitive;
	private final TypeSearchability searchability;
	private final TypeNullability nullability;

	public TypeInfo(
			String typeName,
			int jdbcTypeCode,
			String[] createParams,
			boolean unsigned,
			int precision,
			short minimumScale,
			short maximumScale,
			boolean fixedPrecisionScale,
			String literalPrefix,
			String literalSuffix,
			boolean caseSensitive,
			TypeSearchability searchability,
			TypeNullability nullability) {
		this.typeName = typeName;
		this.jdbcTypeCode = jdbcTypeCode;
		this.createParams = createParams;
		this.unsigned = unsigned;
		this.precision = precision;
		this.minimumScale = minimumScale;
		this.maximumScale = maximumScale;
		this.fixedPrecisionScale = fixedPrecisionScale;
		this.literalPrefix = literalPrefix;
		this.literalSuffix = literalSuffix;
		this.caseSensitive = caseSensitive;
		this.searchability = searchability;
		this.nullability = nullability;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getJdbcTypeCode() {
		return jdbcTypeCode;
	}

	public String[] getCreateParams() {
		return createParams;
	}

	public boolean isUnsigned() {
		return unsigned;
	}

	public int getPrecision() {
		return precision;
	}

	public short getMinimumScale() {
		return minimumScale;
	}

	public short getMaximumScale() {
		return maximumScale;
	}

	public boolean isFixedPrecisionScale() {
		return fixedPrecisionScale;
	}

	public String getLiteralPrefix() {
		return literalPrefix;
	}

	public String getLiteralSuffix() {
		return literalSuffix;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public TypeSearchability getSearchability() {
		return searchability;
	}

	public TypeNullability getNullability() {
		return nullability;
	}
}
