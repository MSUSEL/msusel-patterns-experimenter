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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.internal.util.collections.ArrayHelper;

/**
 * A contract for defining the aspects of cascading various persistence actions.
 *
 * @author Gavin King
 * @see CascadingAction
 */
public abstract class CascadeStyle implements Serializable {

	/**
	 * For this style, should the given action be cascaded?
	 *
	 * @param action The action to be checked for cascade-ability.
	 *
	 * @return True if the action should be cascaded under this style; false otherwise.
	 */
	public abstract boolean doCascade(CascadingAction action);

	/**
	 * Probably more aptly named something like doCascadeToCollectionElements(); it is
	 * however used from both the collection and to-one logic branches...
	 * <p/>
	 * For this style, should the given action really be cascaded?  The default
	 * implementation is simply to return {@link #doCascade}; for certain
	 * styles (currently only delete-orphan), however, we need to be able to
	 * control this separately.
	 *
	 * @param action The action to be checked for cascade-ability.
	 *
	 * @return True if the action should be really cascaded under this style;
	 *         false otherwise.
	 */
	public boolean reallyDoCascade(CascadingAction action) {
		return doCascade( action );
	}

	/**
	 * Do we need to delete orphaned collection elements?
	 *
	 * @return True if this style need to account for orphan delete
	 *         operations; false otherwise.
	 */
	public boolean hasOrphanDelete() {
		return false;
	}

	public static final class MultipleCascadeStyle extends CascadeStyle {
		private final CascadeStyle[] styles;

		public MultipleCascadeStyle(CascadeStyle[] styles) {
			this.styles = styles;
		}

		public boolean doCascade(CascadingAction action) {
			for ( CascadeStyle style : styles ) {
				if ( style.doCascade( action ) ) {
					return true;
				}
			}
			return false;
		}

		public boolean reallyDoCascade(CascadingAction action) {
			for ( CascadeStyle style : styles ) {
				if ( style.reallyDoCascade( action ) ) {
					return true;
				}
			}
			return false;
		}

		public boolean hasOrphanDelete() {
			for ( CascadeStyle style : styles ) {
				if ( style.hasOrphanDelete() ) {
					return true;
				}
			}
			return false;
		}

		public String toString() {
			return ArrayHelper.toString( styles );
		}
	}

	/**
	 * save / delete / update / evict / lock / replicate / merge / persist + delete orphans
	 */
	public static final CascadeStyle ALL_DELETE_ORPHAN = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return true;
		}

		public boolean hasOrphanDelete() {
			return true;
		}

		public String toString() {
			return "STYLE_ALL_DELETE_ORPHAN";
		}
	};

	/**
	 * save / delete / update / evict / lock / replicate / merge / persist
	 */
	public static final CascadeStyle ALL = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return true;
		}

		public String toString() {
			return "STYLE_ALL";
		}
	};

	/**
	 * save / update
	 */
	public static final CascadeStyle UPDATE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.SAVE_UPDATE;
		}

		public String toString() {
			return "STYLE_SAVE_UPDATE";
		}
	};

	/**
	 * lock
	 */
	public static final CascadeStyle LOCK = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.LOCK;
		}

		public String toString() {
			return "STYLE_LOCK";
		}
	};

	/**
	 * refresh
	 */
	public static final CascadeStyle REFRESH = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.REFRESH;
		}

		public String toString() {
			return "STYLE_REFRESH";
		}
	};

	/**
	 * evict
	 */
	public static final CascadeStyle EVICT = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.EVICT;
		}

		public String toString() {
			return "STYLE_EVICT";
		}
	};

	/**
	 * replicate
	 */
	public static final CascadeStyle REPLICATE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.REPLICATE;
		}

		public String toString() {
			return "STYLE_REPLICATE";
		}
	};
	/**
	 * merge
	 */
	public static final CascadeStyle MERGE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.MERGE;
		}

		public String toString() {
			return "STYLE_MERGE";
		}
	};

	/**
	 * create
	 */
	public static final CascadeStyle PERSIST = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.PERSIST
					|| action == CascadingAction.PERSIST_ON_FLUSH;
		}

		public String toString() {
			return "STYLE_PERSIST";
		}
	};

	/**
	 * delete
	 */
	public static final CascadeStyle DELETE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.DELETE;
		}

		public String toString() {
			return "STYLE_DELETE";
		}
	};

	/**
	 * delete + delete orphans
	 */
	public static final CascadeStyle DELETE_ORPHAN = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action == CascadingAction.DELETE || action == CascadingAction.SAVE_UPDATE;
		}

		public boolean reallyDoCascade(CascadingAction action) {
			return action == CascadingAction.DELETE;
		}

		public boolean hasOrphanDelete() {
			return true;
		}

		public String toString() {
			return "STYLE_DELETE_ORPHAN";
		}
	};

	/**
	 * no cascades
	 */
	public static final CascadeStyle NONE = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return false;
		}

		public String toString() {
			return "STYLE_NONE";
		}
	};

	public CascadeStyle() {
	}

	public static final Map<String, CascadeStyle> STYLES = new HashMap<String, CascadeStyle>();

	static {
		STYLES.put( "all", ALL );
		STYLES.put( "all-delete-orphan", ALL_DELETE_ORPHAN );
		STYLES.put( "save-update", UPDATE );
		STYLES.put( "persist", PERSIST );
		STYLES.put( "merge", MERGE );
		STYLES.put( "lock", LOCK );
		STYLES.put( "refresh", REFRESH );
		STYLES.put( "replicate", REPLICATE );
		STYLES.put( "evict", EVICT );
		STYLES.put( "delete", DELETE );
		STYLES.put( "remove", DELETE ); // adds remove as a sort-of alias for delete...
		STYLES.put( "delete-orphan", DELETE_ORPHAN );
		STYLES.put( "none", NONE );
	}

	/**
	 * Factory method for obtaining named cascade styles
	 *
	 * @param cascade The named cascade style name.
	 *
	 * @return The appropriate CascadeStyle
	 */
	public static CascadeStyle getCascadeStyle(String cascade) {
		CascadeStyle style = STYLES.get( cascade );
		if ( style == null ) {
			throw new MappingException( "Unsupported cascade style: " + cascade );
		}
		else {
			return style;
		}
	}
}
