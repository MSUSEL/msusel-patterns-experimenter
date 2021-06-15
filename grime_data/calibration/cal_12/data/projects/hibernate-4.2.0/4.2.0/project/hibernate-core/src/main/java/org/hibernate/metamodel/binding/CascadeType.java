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
package org.hibernate.metamodel.binding;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.CascadeStyle;


/**
 * @author Hardy Ferentschik
 * @todo integrate this w/ org.hibernate.engine.spi.CascadeStyle
 */
public enum CascadeType {
	/**
	 * Cascades save, delete, update, evict, lock, replicate, merge, persist
	 */
	ALL,

	/**
	 * Cascades save, delete, update, evict, lock, replicate, merge, persist + delete orphans
	 */
	ALL_DELETE_ORPHAN,

	/**
	 * Cascades save and update
	 */
	UPDATE,

	/**
	 * Cascades persist
	 */
	PERSIST,

	/**
	 * Cascades merge
	 */
	MERGE,

	/**
	 * Cascades lock
	 */
	LOCK,

	/**
	 * Cascades refresh
	 */
	REFRESH,

	/**
	 * Cascades replicate
	 */
	REPLICATE,

	/**
	 * Cascades evict
	 */
	EVICT,

	/**
	 * Cascade delete
	 */
	DELETE,

	/**
	 * Cascade delete + delete orphans
	 */
	DELETE_ORPHAN,

	/**
	 * No cascading
	 */
	NONE;

	private static final Map<String, CascadeType> hbmOptionToCascadeType = new HashMap<String, CascadeType>();

	static {
		hbmOptionToCascadeType.put( "all", ALL );
		hbmOptionToCascadeType.put( "all-delete-orphan", ALL_DELETE_ORPHAN );
		hbmOptionToCascadeType.put( "save-update", UPDATE );
		hbmOptionToCascadeType.put( "persist", PERSIST );
		hbmOptionToCascadeType.put( "merge", MERGE );
		hbmOptionToCascadeType.put( "lock", LOCK );
		hbmOptionToCascadeType.put( "refresh", REFRESH );
		hbmOptionToCascadeType.put( "replicate", REPLICATE );
		hbmOptionToCascadeType.put( "evict", EVICT );
		hbmOptionToCascadeType.put( "delete", DELETE );
		hbmOptionToCascadeType.put( "remove", DELETE ); // adds remove as a sort-of alias for delete...
		hbmOptionToCascadeType.put( "delete-orphan", DELETE_ORPHAN );
		hbmOptionToCascadeType.put( "none", NONE );
	}

	private static final Map<javax.persistence.CascadeType, CascadeType> jpaCascadeTypeToHibernateCascadeType = new HashMap<javax.persistence.CascadeType, CascadeType>();

	static {
		jpaCascadeTypeToHibernateCascadeType.put( javax.persistence.CascadeType.ALL, ALL );
		jpaCascadeTypeToHibernateCascadeType.put( javax.persistence.CascadeType.PERSIST, PERSIST );
		jpaCascadeTypeToHibernateCascadeType.put( javax.persistence.CascadeType.MERGE, MERGE );
		jpaCascadeTypeToHibernateCascadeType.put( javax.persistence.CascadeType.REFRESH, REFRESH );
		jpaCascadeTypeToHibernateCascadeType.put( javax.persistence.CascadeType.DETACH, EVICT );
	}

	private static final Map<CascadeType, CascadeStyle> cascadeTypeToCascadeStyle = new HashMap<CascadeType, CascadeStyle>();
	static {
		cascadeTypeToCascadeStyle.put( ALL, CascadeStyle.ALL );
		cascadeTypeToCascadeStyle.put( ALL_DELETE_ORPHAN, CascadeStyle.ALL_DELETE_ORPHAN );
		cascadeTypeToCascadeStyle.put( UPDATE, CascadeStyle.UPDATE );
		cascadeTypeToCascadeStyle.put( PERSIST, CascadeStyle.PERSIST );
		cascadeTypeToCascadeStyle.put( MERGE, CascadeStyle.MERGE );
		cascadeTypeToCascadeStyle.put( LOCK, CascadeStyle.LOCK );
		cascadeTypeToCascadeStyle.put( REFRESH, CascadeStyle.REFRESH );
		cascadeTypeToCascadeStyle.put( REPLICATE, CascadeStyle.REPLICATE );
		cascadeTypeToCascadeStyle.put( EVICT, CascadeStyle.EVICT );
		cascadeTypeToCascadeStyle.put( DELETE, CascadeStyle.DELETE );
		cascadeTypeToCascadeStyle.put( DELETE_ORPHAN, CascadeStyle.DELETE_ORPHAN );
		cascadeTypeToCascadeStyle.put( NONE, CascadeStyle.NONE );
	}

	/**
	 * @param hbmOptionName the cascading option as specified in the hbm mapping file
	 *
	 * @return Returns the {@code CascadeType} for a given hbm cascading option
	 */
	public static CascadeType getCascadeType(String hbmOptionName) {
		return hbmOptionToCascadeType.get( hbmOptionName );
	}

	/**
	 * @param jpaCascade the jpa cascade type
	 *
	 * @return Returns the Hibernate {@code CascadeType} for a given jpa cascade type
	 */
	public static CascadeType getCascadeType(javax.persistence.CascadeType jpaCascade) {
		return jpaCascadeTypeToHibernateCascadeType.get( jpaCascade );
	}

	/**
	 * @return Returns the {@code CascadeStyle} that corresponds to this {@code CascadeType}
	 *
	 * @throws MappingException if there is not corresponding {@code CascadeStyle}
	 */
	public CascadeStyle toCascadeStyle() {
		CascadeStyle cascadeStyle = cascadeTypeToCascadeStyle.get( this );
		if ( cascadeStyle == null ) {
			throw new MappingException( "No CascadeStyle that corresponds with CascadeType=" + this.name() );
		}
		return cascadeStyle;
	}
}
