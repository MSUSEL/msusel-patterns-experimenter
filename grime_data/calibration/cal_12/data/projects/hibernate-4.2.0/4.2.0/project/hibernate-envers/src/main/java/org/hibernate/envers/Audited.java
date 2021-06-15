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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When applied to a class, indicates that all of its properties should be audited.
 * When applied to a field, indicates that this field should be audited.
 * @author Adam Warski (adam at warski dot org)
 * @author Tomasz Bech
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Audited {
    ModificationStore modStore()    default ModificationStore.FULL;

	/**
	 * @return Specifies if the entity that is the target of the relation should be audited or not. If not, then when
	 * reading a historic version an audited entity, the relation will always point to the "current" entity.
	 * This is useful for dictionary-like entities, which don't change and don't need to be audited.
	 */
    RelationTargetAuditMode targetAuditMode() default RelationTargetAuditMode.AUDITED;

    /**
     * @return Specifies the superclasses for which properties should be audited, even if the superclasses are not
     * annotated with {@link Audited}. Causes all properties of the listed classes to be audited, just as if the
     * classes had {@link Audited} annotation applied on the class level.
     *
     * The scope of this functionality is limited to the class hierarchy of the annotated entity.
     *
     * If a parent type lists any of its parent types using this attribute, all properties in the specified classes
     * will also be audited.
     *
     * @deprecated Use {@code @AuditOverride(forClass=SomeEntity.class)} instead.
     */
    Class[] auditParents() default {};

    /**
     * @return Should a modification flag be stored for each property in the annotated class or for the annotated
     * property. The flag stores information if a property has been changed at a given revision.
     * This can be used for example in queries.
     */
	boolean withModifiedFlag() default false;
}
