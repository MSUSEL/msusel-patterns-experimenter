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
package com.finalist.jaggenerator;

import java.io.Serializable;

/**
 * This class is a bean that represents information concerning a foreign key column in a database table.
 * For further javadoc concerning the fields of this bean,
 * please see {@link java.sql.DatabaseMetaData#getImportedKeys(String, String, String)}.
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class ForeignKey implements Serializable {

   private String pkTableCat;
   private String pkTableSchem;
   private String pkTableName;
   private String pkColumnName;
   private String fkTableCat;
   private String fkTableSchem;
   private String fkTableName;
   private String fkColumnName;
   private short keySeq;
   private short updateRule;
   private short deleteRule;
   private String fkName;
   private String pkName;
   private short deferrability;

   public String getPkTableCat() {
      return pkTableCat;
   }

   public void setPkTableCat(String pkTableCat) {
      this.pkTableCat = pkTableCat;
   }

   public String getPkTableSchem() {
      return pkTableSchem;
   }

   public void setPkTableSchem(String pkTableSchem) {
      this.pkTableSchem = pkTableSchem;
   }

   public String getPkTableName() {
      return pkTableName;
   }

   public void setPkTableName(String pkTableName) {
      this.pkTableName = pkTableName;
   }

   public String getPkColumnName() {
      return pkColumnName;
   }

   public void setPkColumnName(String pkColumnName) {
      this.pkColumnName = pkColumnName;
   }

   public String getFkTableCat() {
      return fkTableCat;
   }

   public void setFkTableCat(String fkTableCat) {
      this.fkTableCat = fkTableCat;
   }

   public String getFkTableSchem() {
      return fkTableSchem;
   }

   public void setFkTableSchem(String fkTableSchem) {
      this.fkTableSchem = fkTableSchem;
   }

   public String getFkTableName() {
      return fkTableName;
   }

   public void setFkTableName(String fkTableName) {
      this.fkTableName = fkTableName;
   }

   public String getFkColumnName() {
      return fkColumnName;
   }

   public void setFkColumnName(String fkColumnName) {
      this.fkColumnName = fkColumnName;
   }

   public short getKeySeq() {
      return keySeq;
   }

   public void setKeySeq(short keySeq) {
      this.keySeq = keySeq;
   }

   public short getUpdateRule() {
      return updateRule;
   }

   public void setUpdateRule(short updateRule) {
      this.updateRule = updateRule;
   }

   public short getDeleteRule() {
      return deleteRule;
   }

   public void setDeleteRule(short deleteRule) {
      this.deleteRule = deleteRule;
   }

   public String getFkName() {
      return fkName;
   }

   public void setFkName(String fkName) {
      this.fkName = fkName;
   }

   public String getPkName() {
      return pkName;
   }

   public void setPkName(String pkName) {
      this.pkName = pkName;
   }

   public short getDeferrability() {
      return deferrability;
   }

   public void setDeferrability(short deferrability) {
      this.deferrability = deferrability;
   }

   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof ForeignKey)) return false;

      final ForeignKey foreignKey = (ForeignKey) o;

      if (deferrability != foreignKey.deferrability) return false;
      if (deleteRule != foreignKey.deleteRule) return false;
      if (keySeq != foreignKey.keySeq) return false;
      if (updateRule != foreignKey.updateRule) return false;
      if (fkName != null ? !fkName.equals(foreignKey.fkName) : foreignKey.fkName != null) return false;
      if (!fkColumnName.equals(foreignKey.fkColumnName)) return false;
      if (fkTableCat != null ? !fkTableCat.equals(foreignKey.fkTableCat) : foreignKey.fkTableCat != null) return false;
      if (!fkTableName.equals(foreignKey.fkTableName)) return false;
      if (fkTableSchem != null ? !fkTableSchem.equals(foreignKey.fkTableSchem) : foreignKey.fkTableSchem != null) return false;
      if (pkName != null ? !pkName.equals(foreignKey.pkName) : foreignKey.pkName != null) return false;
      if (!pkColumnName.equals(foreignKey.pkColumnName)) return false;
      if (pkTableCat != null ? !pkTableCat.equals(foreignKey.pkTableCat) : foreignKey.pkTableCat != null) return false;
      if (!pkTableName.equals(foreignKey.pkTableName)) return false;
      if (pkTableSchem != null ? !pkTableSchem.equals(foreignKey.pkTableSchem) : foreignKey.pkTableSchem != null) return false;

      return true;
   }

   public int hashCode() {
      int result;
      result = (pkTableCat != null ? pkTableCat.hashCode() : 0);
      result = 29 * result + (pkTableSchem != null ? pkTableSchem.hashCode() : 0);
      result = 29 * result + pkTableName.hashCode();
      result = 29 * result + pkColumnName.hashCode();
      result = 29 * result + (fkTableCat != null ? fkTableCat.hashCode() : 0);
      result = 29 * result + (fkTableSchem != null ? fkTableSchem.hashCode() : 0);
      result = 29 * result + fkTableName.hashCode();
      result = 29 * result + fkColumnName.hashCode();
      result = 29 * result + (int) keySeq;
      result = 29 * result + (int) updateRule;
      result = 29 * result + (int) deleteRule;
      result = 29 * result + (fkName != null ? fkName.hashCode() : 0);
      result = 29 * result + (pkName != null ? pkName.hashCode() : 0);
      result = 29 * result + (int) deferrability;
      return result;
   }

   public String toString() {
         return "ForeignKey[" +
               "pkTableCat=" + pkTableCat +
               ", pkTableSchem=" + pkTableSchem +
               ", pkTableName=" + pkTableName +
               ", pkColumnName=" + pkColumnName +
               ", fkTableCat=" + fkTableCat +
               ", fkTableSchem=" + fkTableSchem +
               ", fkTableName=" + fkTableName +
               ", fkTableName=" + fkTableName +
               ", keySeq=" + keySeq +
               ", updateRule=" + updateRule +
               ", deleteRule=" + deleteRule +
               ", fkName=" + fkName +
               ", pkName=" + pkName +
               ", deferrability=" + deferrability + ']';

   }
}
