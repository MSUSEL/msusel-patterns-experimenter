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
package org.hibernate.id;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.engine.spi.SessionImplementor;

/**
 * A strategy for generating a variant 2 {@link UUID} value.
 *
 * @author Steve Ebersole
 */
public interface UUIDGenerationStrategy extends Serializable {
	/**
	 * Which variant, according to IETF RFC 4122, of UUID does this strategy generate?  RFC 4122 defines
	 * 5 variants (though it only describes algorithms to generate 4):<ul>
	 * <li>1 = time based</li>
	 * <li>2 = DCE based using POSIX UIDs</li>
	 * <li>3 = name based (md5 hash)</li>
	 * <li>4 = random numbers based</li>
	 * <li>5 = name based (sha-1 hash)</li>
	 * </ul>
	 * Returning the values above should be reserved to those generators creating variants compliant with the
	 * corresponding RFC definition; others can feel free to return other values as they see fit.
	 * <p/>
	 * Informational only, and not used at this time.
	 *
	 * @return The supported generation version
	 */
	public int getGeneratedVersion();

	/**
	 * Generate the UUID.
	 *
	 * @param session The session asking for the generation
	 *
	 * @return The generated UUID.
	 */
	public UUID generateUUID(SessionImplementor session);

}
