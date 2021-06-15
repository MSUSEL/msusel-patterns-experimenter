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


/**
 * Type of the revision.
 * @author Adam Warski (adam at warski dot org)
 */
public enum RevisionType {
    /**
     * Indicates that the entity was added (persisted) at that revision.
     */
    ADD((byte) 0),
    /**
     * Indicates that the entity was modified (one or more of its fields) at that revision.
     */
    MOD((byte) 1),
    /**
     * Indicates that the entity was deleted (removed) at that revision.
     */
    DEL((byte) 2);

    private Byte representation;

    RevisionType(byte representation) {
        this.representation = representation;
    }

    public Byte getRepresentation() {
        return representation;
    }

    public static RevisionType fromRepresentation(Object representation) {
        if (representation == null || !(representation instanceof Byte)) {
            return null;
        }

        switch ((Byte) representation) {
            case 0: return ADD;
            case 1: return MOD;
            case 2: return DEL;
        }

        throw new IllegalArgumentException("Unknown representation: " + representation);
    }
}
