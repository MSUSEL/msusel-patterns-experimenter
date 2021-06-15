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
package org.hibernate.envers.test.integration.data;
import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.envers.Audited;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
public class LobTestEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Lob
    @Audited
    private String stringLob;

    @Lob
    @Audited
    private byte[] byteLob;

    @Lob
    @Audited
    private char[] charLob;

    public LobTestEntity() {
    }

    public LobTestEntity(String stringLob, byte[] byteLob, char[] charLob) {
        this.stringLob = stringLob;
        this.byteLob = byteLob;
        this.charLob = charLob;
    }

    public LobTestEntity(Integer id, String stringLob, byte[] byteLob, char[] charLob) {
        this.id = id;
        this.stringLob = stringLob;
        this.byteLob = byteLob;
        this.charLob = charLob;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStringLob() {
        return stringLob;
    }

    public void setStringLob(String stringLob) {
        this.stringLob = stringLob;
    }

    public byte[] getByteLob() {
        return byteLob;
    }

    public void setByteLob(byte[] byteLob) {
        this.byteLob = byteLob;
    }

    public char[] getCharLob() {
        return charLob;
    }

    public void setCharLob(char[] charLob) {
        this.charLob = charLob;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LobTestEntity)) return false;

        LobTestEntity that = (LobTestEntity) o;

        if (!Arrays.equals(byteLob, that.byteLob)) return false;
        if (!Arrays.equals(charLob, that.charLob)) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (stringLob != null ? !stringLob.equals(that.stringLob) : that.stringLob != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (stringLob != null ? stringLob.hashCode() : 0);
        result = 31 * result + (byteLob != null ? Arrays.hashCode(byteLob) : 0);
        result = 31 * result + (charLob != null ? Arrays.hashCode(charLob) : 0);
        return result;
    }
}