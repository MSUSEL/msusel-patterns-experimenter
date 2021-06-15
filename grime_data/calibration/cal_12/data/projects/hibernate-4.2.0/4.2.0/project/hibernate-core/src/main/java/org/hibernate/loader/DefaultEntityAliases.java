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
package org.hibernate.loader;

import java.util.Collections;
import java.util.Map;

import org.hibernate.internal.util.StringHelper;
import org.hibernate.persister.entity.Loadable;

/**
 * EntityAliases which handles the logic of selecting user provided aliases (via return-property),
 * before using the default aliases.
 *
 * @author max
 *
 */
public class DefaultEntityAliases implements EntityAliases {

	private final String[] suffixedKeyColumns;
	private final String[] suffixedVersionColumn;
	private final String[][] suffixedPropertyColumns;
	private final String suffixedDiscriminatorColumn;
	private final String suffix;
	private final String rowIdAlias;
	private final Map userProvidedAliases;

	/**
	 * Calculate and cache select-clause aliases
	 *
	 * @param userProvidedAliases The explicit aliases provided in a result-set mapping.
	 * @param persister The persister for which we are generating select aliases
	 * @param suffix The calculated suffix.
	 */
	public DefaultEntityAliases(
			Map userProvidedAliases,
			Loadable persister,
			String suffix) {
		this.suffix = suffix;
		this.userProvidedAliases = userProvidedAliases;

		suffixedKeyColumns = determineKeyAlias( persister, suffix );
		suffixedPropertyColumns = determinePropertyAliases( persister );
		suffixedDiscriminatorColumn = determineDiscriminatorAlias( persister, suffix );
		suffixedVersionColumn = determineVersionAlias( persister );
		rowIdAlias = Loadable.ROWID_ALIAS + suffix; // TODO: not visible to the user!
	}

	public DefaultEntityAliases(Loadable persister, String suffix) {
		this( Collections.EMPTY_MAP, persister, suffix );
	}

	private String[] determineKeyAlias(Loadable persister, String suffix) {
		final String[] aliases;
		final String[] keyColumnsCandidates = getUserProvidedAliases( persister.getIdentifierPropertyName(), null );
		if ( keyColumnsCandidates == null ) {
			aliases = getUserProvidedAliases(
					"id",
					getIdentifierAliases(persister, suffix)
			);
		}
		else {
			aliases = keyColumnsCandidates;
		}
		final String[] rtn = StringHelper.unquote( aliases, persister.getFactory().getDialect() );
		intern( rtn );
		return rtn;
	}

	private String[][] determinePropertyAliases(Loadable persister) {
		return getSuffixedPropertyAliases( persister );
	}

	private String determineDiscriminatorAlias(Loadable persister, String suffix) {
		String alias = getUserProvidedAlias( "class", getDiscriminatorAlias( persister, suffix ) );
		return StringHelper.unquote( alias, persister.getFactory().getDialect() );
	}

	private String[] determineVersionAlias(Loadable persister) {
		return persister.isVersioned()
				? suffixedPropertyColumns[ persister.getVersionProperty() ]
				: null;
	}

	protected String getDiscriminatorAlias(Loadable persister, String suffix) {
		return persister.getDiscriminatorAlias(suffix);
	}

	protected String[] getIdentifierAliases(Loadable persister, String suffix) {
		return persister.getIdentifierAliases(suffix);
	}

	protected String[] getPropertyAliases(Loadable persister, int j) {
		return persister.getPropertyAliases(suffix, j);
	}

	private String[] getUserProvidedAliases(String propertyPath, String[] defaultAliases) {
		String[] result = (String[]) userProvidedAliases.get(propertyPath);
		if (result==null) {
			return defaultAliases;
		}
		else {
			return result;
		}
	}

	private String getUserProvidedAlias(String propertyPath, String defaultAlias) {
		String[] columns = (String[]) userProvidedAliases.get(propertyPath);
		if (columns==null) {
			return defaultAlias;
		}
		else {
			return columns[0];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String[][] getSuffixedPropertyAliases(Loadable persister) {
		final int size = persister.getPropertyNames().length;
		final String[][] suffixedPropertyAliases = new String[size][];
		for ( int j = 0; j < size; j++ ) {
			suffixedPropertyAliases[j] = getUserProvidedAliases(
					persister.getPropertyNames()[j],
					getPropertyAliases( persister, j )
			);
			suffixedPropertyAliases[j] = StringHelper.unquote( suffixedPropertyAliases[j], persister.getFactory().getDialect() );
			intern( suffixedPropertyAliases[j] );
		}
		return suffixedPropertyAliases;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getSuffixedVersionAliases() {
		return suffixedVersionColumn;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[][] getSuffixedPropertyAliases() {
		return suffixedPropertyColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSuffixedDiscriminatorAlias() {
		return suffixedDiscriminatorColumn;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getSuffixedKeyAliases() {
		return suffixedKeyColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getRowIdAlias() {
		return rowIdAlias;
	}

	private static void intern(String[] strings) {
		for (int i=0; i<strings.length; i++ ) {
			strings[i] = strings[i].intern();
		}
	}
}
