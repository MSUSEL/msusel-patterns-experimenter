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
package org.hibernate.envers.test.entities.ids;
import java.io.Serializable;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * @author Slawek Garwol (slawekgarwol at gmail dot com)
 */
@Embeddable
@TypeDef(name = "customEnum", typeClass = CustomEnumUserType.class)
public class EmbIdWithCustomType implements Serializable {
    private Integer x;

    @Type(type = "customEnum")
    private CustomEnum customEnum;

    public EmbIdWithCustomType() {
    }

    public EmbIdWithCustomType(Integer x, CustomEnum customEnum) {
        this.x = x;
        this.customEnum = customEnum;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public CustomEnum getCustomEnum() {
        return customEnum;
    }

    public void setCustomEnum(CustomEnum customEnum) {
        this.customEnum = customEnum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EmbIdWithCustomType)) return false;

        EmbIdWithCustomType that = (EmbIdWithCustomType) obj;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        if (customEnum != that.customEnum) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (x != null ? x.hashCode() : 0);
        result = 31 * result + (customEnum != null ? customEnum.hashCode() : 0);
        return result;
    }
}
