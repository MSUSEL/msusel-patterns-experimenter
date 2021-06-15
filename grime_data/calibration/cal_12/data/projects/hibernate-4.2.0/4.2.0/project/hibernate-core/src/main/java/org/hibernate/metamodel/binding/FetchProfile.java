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

import java.util.Collections;
import java.util.Set;

/**
 * A fetch profile allows a user to dynamically modify the fetching strategy used for particular associations at runtime, whereas
 * that information was historically only statically defined in the metadata.
 * <p/>
 * This class represent the data as it is defined in their metadata.
 *
 * @author Steve Ebersole
 * @see org.hibernate.engine.profile.FetchProfile
 */
public class FetchProfile {

    private final String name;
    private final Set<Fetch> fetches;

    /**
     * Create a fetch profile representation.
     *
     * @param name The name of the fetch profile.
     * @param fetches
     */
    public FetchProfile( String name,
                         Set<Fetch> fetches ) {
        this.name = name;
        this.fetches = fetches;
    }

    /**
     * Retrieve the name of the fetch profile.
     *
     * @return The profile name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve the fetches associated with this profile
     *
     * @return The fetches associated with this profile.
     */
    public Set<Fetch> getFetches() {
        return Collections.unmodifiableSet(fetches);
    }

    /**
     * Adds a fetch to this profile.
     *
     * @param entity The entity which contains the association to be fetched
     * @param association The association to fetch
     * @param style The style of fetch t apply
     */
    public void addFetch( String entity,
                          String association,
                          String style ) {
        fetches.add(new Fetch(entity, association, style));
    }

    /**
     * Defines an individual association fetch within the given profile.
     */
    public static class Fetch {
        private final String entity;
        private final String association;
        private final String style;

        public Fetch( String entity,
                      String association,
                      String style ) {
            this.entity = entity;
            this.association = association;
            this.style = style;
        }

        public String getEntity() {
            return entity;
        }

        public String getAssociation() {
            return association;
        }

        public String getStyle() {
            return style;
        }
    }
}
