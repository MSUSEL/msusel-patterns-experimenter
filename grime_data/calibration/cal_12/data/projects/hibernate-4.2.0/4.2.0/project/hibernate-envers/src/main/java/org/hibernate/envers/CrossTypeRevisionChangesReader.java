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
package org.hibernate.envers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.envers.tools.Pair;

/**
 * Queries that allow retrieving snapshots of all entities (regardless of their particular type) changed in the given
 * revision. Note that this API can be legally used only when default mechanism of tracking modified entity names
 * is enabled.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public interface CrossTypeRevisionChangesReader {
    /**
     * Find all entities changed (added, updated and removed) in a given revision. Executes <i>n+1</i> SQL queries,
     * where <i>n</i> is a number of different entity classes modified within specified revision.
     * @param revision Revision number.
     * @return Snapshots of all audited entities changed in a given revision.
     * @throws IllegalStateException If the associated entity manager is closed.
     * @throws IllegalArgumentException If a revision number is <code>null</code>, less or equal to 0.
     */
    public List<Object> findEntities(Number revision) throws IllegalStateException, IllegalArgumentException;

    /**
     * Find all entities changed (added, updated or removed) in a given revision. Executes <i>n+1</i> SQL queries,
     * where <i>n</i> is a number of different entity classes modified within specified revision.
     * @param revision Revision number.
     * @param revisionType Type of modification.
     * @return Snapshots of all audited entities changed in a given revision and filtered by modification type.
     * @throws IllegalStateException If the associated entity manager is closed.
     * @throws IllegalArgumentException If a revision number is {@code null}, less or equal to 0.
     */
    public List<Object> findEntities(Number revision, RevisionType revisionType) throws IllegalStateException,
                                                                                        IllegalArgumentException;

    /**
     * Find all entities changed (added, updated and removed) in a given revision grouped by modification type.
     * Executes <i>mn+1</i> SQL queries, where:
     * <ul>
     * <li><i>n</i> - number of different entity classes modified within specified revision.
     * <li><i>m</i> - number of different revision types. See {@link RevisionType} enum.
     * </ul>
     * @param revision Revision number.
     * @return Map containing lists of entity snapshots grouped by modification operation (e.g. addition, update, removal).
     * @throws IllegalStateException If the associated entity manager is closed.
     * @throws IllegalArgumentException If a revision number is {@code null}, less or equal to 0.
     */
    public Map<RevisionType, List<Object>> findEntitiesGroupByRevisionType(Number revision) throws IllegalStateException,
                                                                                                   IllegalArgumentException;

    /**
     * Returns set of entity names and corresponding Java classes modified in a given revision.
     * @param revision Revision number.
     * @return Set of entity names and corresponding Java classes modified in a given revision.
     * @throws IllegalStateException If the associated entity manager is closed.
     * @throws IllegalArgumentException If a revision number is {@code null}, less or equal to 0.
     */
    public Set<Pair<String, Class>> findEntityTypes(Number revision) throws IllegalStateException, IllegalArgumentException;
}
