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

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.hibernate.internal.util.collections.CollectionHelper;

/**
 * Identifier generator container, Useful to keep named generator in annotations
 *
 * @author Emmanuel Bernard
 */
public class IdGenerator implements Serializable {
    private final String name;
    private final String strategy;
    private final Map<String, String> parameters;

    public IdGenerator( String name,
                        String strategy,
                        Map<String, String> parameters ) {
        this.name = name;
        this.strategy = strategy;
        if ( CollectionHelper.isEmpty( parameters ) ) {
            this.parameters = Collections.emptyMap();
        }
        else {
            this.parameters = Collections.unmodifiableMap( parameters );
        }
    }

    /**
     * @return identifier generator strategy
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * @return generator name
     */
    public String getName() {
        return name;
    }

    /**
     * @return generator configuration parameters
     */
    public Map<String, String> getParameters() {
		return parameters;
    }
}
