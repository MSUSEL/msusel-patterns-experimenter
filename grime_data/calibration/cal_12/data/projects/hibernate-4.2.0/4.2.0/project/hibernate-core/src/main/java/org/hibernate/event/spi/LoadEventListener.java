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
package org.hibernate.event.spi;

import java.io.Serializable;

import org.hibernate.HibernateException;

/**
 * Defines the contract for handling of load events generated from a session.
 *
 * @author Steve Ebersole
 */
public interface LoadEventListener extends Serializable {

	/** 
	 * Handle the given load event.
     *
     * @param event The load event to be handled.
     * @throws HibernateException
     */
	public void onLoad(LoadEvent event, LoadType loadType) throws HibernateException;

	public static final LoadType RELOAD = new LoadType("GET")
			.setAllowNulls(false)
			.setAllowProxyCreation(false)
			.setCheckDeleted(true)
			.setNakedEntityReturned(false);

	public static final LoadType GET = new LoadType("GET")
			.setAllowNulls(true)
			.setAllowProxyCreation(false)
			.setCheckDeleted(true)
			.setNakedEntityReturned(false);
	
	public static final LoadType LOAD = new LoadType("LOAD")
			.setAllowNulls(false)
			.setAllowProxyCreation(true)
			.setCheckDeleted(true)
			.setNakedEntityReturned(false);
	
	public static final LoadType IMMEDIATE_LOAD = new LoadType("IMMEDIATE_LOAD")
			.setAllowNulls(true)
			.setAllowProxyCreation(false)
			.setCheckDeleted(false)
			.setNakedEntityReturned(true);
	
	public static final LoadType INTERNAL_LOAD_EAGER = new LoadType("INTERNAL_LOAD_EAGER")
			.setAllowNulls(false)
			.setAllowProxyCreation(false)
			.setCheckDeleted(false)
			.setNakedEntityReturned(false);
	
	public static final LoadType INTERNAL_LOAD_LAZY = new LoadType("INTERNAL_LOAD_LAZY")
			.setAllowNulls(false)
			.setAllowProxyCreation(true)
			.setCheckDeleted(false)
			.setNakedEntityReturned(false);
	
	public static final LoadType INTERNAL_LOAD_NULLABLE = new LoadType("INTERNAL_LOAD_NULLABLE")
			.setAllowNulls(true)
			.setAllowProxyCreation(false)
			.setCheckDeleted(false)
			.setNakedEntityReturned(false);

	public static final class LoadType {
		private String name;

		private boolean nakedEntityReturned;
		private boolean allowNulls;
		private boolean checkDeleted;
		private boolean allowProxyCreation;

        private LoadType(String name) {
	        this.name = name;
        }

		public boolean isAllowNulls() {
			return allowNulls;
		}

		private LoadType setAllowNulls(boolean allowNulls) {
			this.allowNulls = allowNulls;
			return this;
		}

		public boolean isNakedEntityReturned() {
			return nakedEntityReturned;
		}

		private LoadType setNakedEntityReturned(boolean immediateLoad) {
			this.nakedEntityReturned = immediateLoad;
			return this;
		}

		public boolean isCheckDeleted() {
			return checkDeleted;
		}

		private LoadType setCheckDeleted(boolean checkDeleted) {
			this.checkDeleted = checkDeleted;
			return this;
		}

		public boolean isAllowProxyCreation() {
			return allowProxyCreation;
		}

		private LoadType setAllowProxyCreation(boolean allowProxyCreation) {
			this.allowProxyCreation = allowProxyCreation;
			return this;
		}

		public String getName() {
			return name;
		}
		
		public String toString() {
			return name;
		}
	}
}
