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
package org.hibernate.test.annotations.id.sequences;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Unlike Hibernate's UUID generator.  This avoids 
 * meaningless synchronization and has less
 * than a chance of an asteroid hitting you on the head
 * even after trillions of rows are inserted.  I know
 * this to be true because it says so in Wikipedia(haha).
 * http://en.wikipedia.org/wiki/UUID#Random_UUID_probability_of_duplicates
 *
 */
public class UUIDGenerator implements IdentifierGenerator {

    public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
        UUID uuid = UUID.randomUUID();
        String sud = uuid.toString();
        System.out.println("uuid="+uuid);
        sud = sud.replaceAll("-", "");
        
        BigInteger integer = new BigInteger(sud,16);

        System.out.println("bi ="+integer.toString() );
        return integer;
    }

}
