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
package org.hibernate.dialect;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;

import org.hibernate.engine.spi.SessionImplementor;

/**
 * Strategy for how dialects need {@code LOB} values to be merged.
 *
 * @author Steve Ebersole
 */
public interface LobMergeStrategy {
	/**
	 * Perform merge on {@link Blob} values.
	 *
	 * @param original The detached {@code BLOB} state
	 * @param target The managed {@code BLOB} state
	 * @param session The session
	 *
	 * @return The merged {@code BLOB} state
	 */
	public Blob mergeBlob(Blob original, Blob target, SessionImplementor session);

	/**
	 * Perform merge on {@link Clob} values.
	 *
	 * @param original The detached {@code CLOB} state
	 * @param target The managed {@code CLOB} state
	 * @param session The session
	 *
	 * @return The merged {@code CLOB} state
	 */
	public Clob mergeClob(Clob original, Clob target, SessionImplementor session);

	/**
	 * Perform merge on {@link NClob} values.
	 *
	 * @param original The detached {@code NCLOB} state
	 * @param target The managed {@code NCLOB} state
	 * @param session The session
	 *
	 * @return The merged {@code NCLOB} state
	 */
	public NClob mergeNClob(NClob original, NClob target, SessionImplementor session);
}
