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
package org.hibernate.tuple;
import org.hibernate.FetchMode;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.type.Type;

/**
 * Represents a basic property within the Hibernate runtime-metamodel.
 *
 * @author Steve Ebersole
 */
public class StandardProperty extends Property {

    private final boolean lazy;
    private final boolean insertable;
    private final boolean updateable;
	private final boolean insertGenerated;
	private final boolean updateGenerated;
    private final boolean nullable;
    private final boolean dirtyCheckable;
    private final boolean versionable;
    private final CascadeStyle cascadeStyle;
	private final FetchMode fetchMode;

    /**
     * Constructs StandardProperty instances.
     *
     * @param name The name by which the property can be referenced within
     * its owner.
     * @param node The node name to use for XML-based representation of this
     * property.
     * @param type The Hibernate Type of this property.
     * @param lazy Should this property be handled lazily?
     * @param insertable Is this property an insertable value?
     * @param updateable Is this property an updateable value?
     * @param insertGenerated Is this property generated in the database on insert?
     * @param updateGenerated Is this property generated in the database on update?
     * @param nullable Is this property a nullable value?
     * @param checkable Is this property a checkable value?
     * @param versionable Is this property a versionable value?
     * @param cascadeStyle The cascade style for this property's value.
     * @param fetchMode Any fetch mode defined for this property
     */
    public StandardProperty(
            String name,
            String node,
            Type type,
            boolean lazy,
            boolean insertable,
            boolean updateable,
            boolean insertGenerated,
            boolean updateGenerated,
            boolean nullable,
            boolean checkable,
            boolean versionable,
            CascadeStyle cascadeStyle,
            FetchMode fetchMode) {
        super(name, node, type);
        this.lazy = lazy;
        this.insertable = insertable;
        this.updateable = updateable;
        this.insertGenerated = insertGenerated;
	    this.updateGenerated = updateGenerated;
        this.nullable = nullable;
        this.dirtyCheckable = checkable;
        this.versionable = versionable;
        this.cascadeStyle = cascadeStyle;
	    this.fetchMode = fetchMode;
    }

    public boolean isLazy() {
        return lazy;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public boolean isUpdateable() {
        return updateable;
    }

	public boolean isInsertGenerated() {
		return insertGenerated;
	}

	public boolean isUpdateGenerated() {
		return updateGenerated;
	}

    public boolean isNullable() {
        return nullable;
    }

    public boolean isDirtyCheckable(boolean hasUninitializedProperties) {
        return isDirtyCheckable() && ( !hasUninitializedProperties || !isLazy() );
    }

    public boolean isDirtyCheckable() {
        return dirtyCheckable;
    }

    public boolean isVersionable() {
        return versionable;
    }

    public CascadeStyle getCascadeStyle() {
        return cascadeStyle;
    }

	public FetchMode getFetchMode() {
		return fetchMode;
	}
}
