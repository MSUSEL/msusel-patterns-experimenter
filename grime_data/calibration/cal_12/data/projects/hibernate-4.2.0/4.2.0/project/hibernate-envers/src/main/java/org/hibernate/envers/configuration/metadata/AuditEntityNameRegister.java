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
package org.hibernate.envers.configuration.metadata;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.MappingException;

/**
 * A register of all audit entity names used so far.
 * @author Adam Warski (adam at warski dot org)
 */
public class AuditEntityNameRegister {
    private final Set<String> auditEntityNames = new HashSet<String>();

    /**
     * @param auditEntityName Name of the audit entity.
     * @return True if the given audit entity name is already used.
     */
    private boolean check(String auditEntityName) {
        return auditEntityNames.contains(auditEntityName);
    }

    /**
     * Register an audit entity name. If the name is already registered, an exception is thrown.
     * @param auditEntityName Name of the audit entity.
     */
    public void register(String auditEntityName) {
        if (auditEntityNames.contains(auditEntityName)) {
            throw new MappingException("The audit entity name '" + auditEntityName + "' is already registered.");
        }
        
        auditEntityNames.add(auditEntityName);
    }

    /**
     * Creates a unique (not yet registered) audit entity name by appending consecutive numbers to the base
     * name. If the base name is not yet used, it is returned unmodified.
	 *
     * @param baseAuditEntityName The base entity name.
	 *
     * @return A unique audit entity name
     */
    public String createUnique(final String baseAuditEntityName) {
        String auditEntityName = baseAuditEntityName;
        int count = 1;
        while (check(auditEntityName)) {
            auditEntityName = baseAuditEntityName + count++;
        }

        return auditEntityName;
    }
}
