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
package org.hibernate.metamodel.source.annotations.xml;

import org.jboss.jandex.DotName;

/**
 * Pseudo JPA Annotation name to distinguish Annotations defined in <persistence-unit-metadata>
 *
 * @author Strong Liu
 */
public interface PseudoJpaDotNames {
	DotName DEFAULT_ACCESS = DotName.createSimple( "default.access" );
	DotName DEFAULT_DELIMITED_IDENTIFIERS = DotName.createSimple( "default.delimited.identifiers" );
	DotName DEFAULT_ENTITY_LISTENERS = DotName.createSimple( "default.entity.listeners" );
	DotName DEFAULT_POST_LOAD = DotName.createSimple( "default.entity.listener.post.load" );
	DotName DEFAULT_POST_PERSIST = DotName.createSimple( "default.entity.listener.post.persist" );
	DotName DEFAULT_POST_REMOVE = DotName.createSimple( "default.entity.listener.post.remove" );
	DotName DEFAULT_POST_UPDATE = DotName.createSimple( "default.entity.listener.post.update" );
	DotName DEFAULT_PRE_PERSIST = DotName.createSimple( "default.entity.listener.pre.persist" );
	DotName DEFAULT_PRE_REMOVE = DotName.createSimple( "default.entity.listener.pre.remove" );
	DotName DEFAULT_PRE_UPDATE = DotName.createSimple( "default.entity.listener.pre.update" );
}
