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
package org.hibernate.engine.profile;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.type.BagType;
import org.hibernate.type.Type;

/**
 * A 'fetch profile' allows a user to dynamically modify the fetching strategy used for particular associations at
 * runtime, whereas that information was historically only statically defined in the metadata.
 * <p/>
 * This class defines the runtime representation of this data.
 *
 * @author Steve Ebersole
 */
public class FetchProfile {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, FetchProfile.class.getName());

	private final String name;
	private Map<String,Fetch> fetches = new HashMap<String,Fetch>();

	private boolean containsJoinFetchedCollection = false;
	private boolean containsJoinFetchedBag = false;
	private Fetch bagJoinFetch;

	/**
	 * A 'fetch profile' is uniquely named within a
	 * {@link SessionFactoryImplementor SessionFactory}, thus it is also
	 * uniquely and easily identifiable within that
	 * {@link SessionFactoryImplementor SessionFactory}.
	 *
	 * @param name The name under which we are bound in the sessionFactory
	 */
	public FetchProfile(String name) {
		this.name = name;
	}

	/**
	 * Add a fetch to the profile.
	 *
	 * @param association The association to be fetched
	 * @param fetchStyleName The name of the fetch style to apply
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public void addFetch(Association association, String fetchStyleName) {
		addFetch( association, Fetch.Style.parse( fetchStyleName ) );
	}

	/**
	 * Add a fetch to the profile.
	 *
	 * @param association The association to be fetched
	 * @param style The style to apply
	 */
	public void addFetch(Association association, Fetch.Style style) {
		addFetch( new Fetch( association, style ) );
	}

	/**
	 * Add a fetch to the profile.
	 *
	 * @param fetch The fetch to add.
	 */
	public void addFetch(final Fetch fetch) {
		final String fetchAssociactionRole = fetch.getAssociation().getRole();
		Type associationType = fetch.getAssociation().getOwner().getPropertyType( fetch.getAssociation().getAssociationPath() );
		if ( associationType.isCollectionType() ) {
			LOG.tracev( "Handling request to add collection fetch [{0}]", fetchAssociactionRole );

			// couple of things for which to account in the case of collection
			// join fetches
			if ( Fetch.Style.JOIN == fetch.getStyle() ) {
				// first, if this is a bag we need to ignore it if we previously
				// processed collection join fetches
				if ( BagType.class.isInstance( associationType ) ) {
					if ( containsJoinFetchedCollection ) {
						LOG.containsJoinFetchedCollection( fetchAssociactionRole );
						return; // EARLY EXIT!!!
					}
				}

				// also, in cases where we are asked to add a collection join
				// fetch where we had already added a bag join fetch previously,
				// we need to go back and ignore that previous bag join fetch.
				if ( containsJoinFetchedBag ) {
					// just for safety...
					if ( fetches.remove( bagJoinFetch.getAssociation().getRole() ) != bagJoinFetch ) {
						LOG.unableToRemoveBagJoinFetch();
					}
					bagJoinFetch = null;
					containsJoinFetchedBag = false;
				}

				containsJoinFetchedCollection = true;
			}
		}
		fetches.put( fetchAssociactionRole, fetch );
	}

	/**
	 * Getter for property 'name'.
	 *
	 * @return Value for property 'name'.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for property 'fetches'.  Map of {@link Fetch} instances, keyed by association <tt>role</tt>
	 *
	 * @return Value for property 'fetches'.
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public Map<String,Fetch> getFetches() {
		return fetches;
	}

	public Fetch getFetchByRole(String role) {
		return fetches.get( role );
	}

	/**
	 * Getter for property 'containsJoinFetchedCollection', which flags whether
	 * this fetch profile contained any collection join fetches.
	 *
	 * @return Value for property 'containsJoinFetchedCollection'.
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public boolean isContainsJoinFetchedCollection() {
		return containsJoinFetchedCollection;
	}

	/**
	 * Getter for property 'containsJoinFetchedBag', which flags whether this
	 * fetch profile contained any bag join fetches
	 *
	 * @return Value for property 'containsJoinFetchedBag'.
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	public boolean isContainsJoinFetchedBag() {
		return containsJoinFetchedBag;
	}
}
