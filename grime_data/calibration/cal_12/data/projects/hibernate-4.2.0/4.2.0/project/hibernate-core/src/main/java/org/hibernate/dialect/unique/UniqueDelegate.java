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
package org.hibernate.dialect.unique;

import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.Table;
import org.hibernate.metamodel.relational.UniqueKey;

/**
 * Dialect-level delegate in charge of applying "uniqueness" to a column.
 * Uniqueness can be defined in 1 of 3 ways:
 * 
 * 1.) Add a unique constraint via separate alter table statements.
 * 2.) Add a unique constraint via dialect-specific syntax in table create statement.
 * 3.) Add "unique" syntax to the column itself.
 * 
 * #1 & #2 are preferred, if possible -- #3 should be solely a fall-back.
 * 
 * TODO: This could eventually be simplified.  With AST, 1 "applyUniqueness"
 * method might be possible. But due to .cfg and .mapping still resolving
 * around StringBuilders, separate methods were needed.
 * 
 * See HHH-7797.
 * 
 * @author Brett Meyer
 */
public interface UniqueDelegate {
	
	/**
	 * If the dialect does not supports unique constraints, this method should
	 * return the syntax necessary to mutate the column definition
	 * (usually "unique").
	 * 
	 * @param column
	 * @return String
	 */
	public String applyUniqueToColumn( org.hibernate.mapping.Column column );
	
	/**
	 * If the dialect does not supports unique constraints, this method should
	 * return the syntax necessary to mutate the column definition
	 * (usually "unique").
	 * 
	 * @param column
	 * @return String
	 */
	public String applyUniqueToColumn( Column column );
	
	/**
	 * If constraints are supported, but not in seperate alter statements,
	 * return uniqueConstraintSql in order to add the constraint to the
	 * original table definition.
	 * 
	 * @param table
	 * @return String
	 */
	public String applyUniquesToTable( org.hibernate.mapping.Table table );
	
	/**
	 * If constraints are supported, but not in seperate alter statements,
	 * return uniqueConstraintSql in order to add the constraint to the
	 * original table definition.
	 * 
	 * @param table
	 * @return String
	 */
	public String applyUniquesToTable( Table table );
	
	/**
	 * If creating unique constraints in separate alter statements is
	 * supported, generate the necessary "alter" syntax for the given key.
	 * 
	 * @param uniqueKey
	 * @param defaultCatalog
	 * @param defaultSchema
	 * @return String
	 */
	public String applyUniquesOnAlter( org.hibernate.mapping.UniqueKey uniqueKey,
			String defaultCatalog, String defaultSchema );
	
	/**
	 * If creating unique constraints in separate alter statements is
	 * supported, generate the necessary "alter" syntax for the given key.
	 * 
	 * @param uniqueKey
	 * @return String
	 */
	public String applyUniquesOnAlter( UniqueKey uniqueKey );
	
	/**
	 * If dropping unique constraints in separate alter statements is
	 * supported, generate the necessary "alter" syntax for the given key.
	 * 
	 * @param uniqueKey
	 * @param defaultCatalog
	 * @param defaultSchema
	 * @return String
	 */
	public String dropUniquesOnAlter( org.hibernate.mapping.UniqueKey uniqueKey,
			String defaultCatalog, String defaultSchema );
	
	/**
	 * If dropping unique constraints in separate alter statements is
	 * supported, generate the necessary "alter" syntax for the given key.
	 * 
	 * @param uniqueKey
	 * @return String
	 */
	public String dropUniquesOnAlter( UniqueKey uniqueKey );
	
	/**
	 * Generates the syntax necessary to create the unique constraint (reused
	 * by all methods).  Ex: "unique (column1, column2, ...)"
	 * 
	 * @param uniqueKey
	 * @return String
	 */
	public String uniqueConstraintSql( org.hibernate.mapping.UniqueKey uniqueKey );
	
	/**
	 * Generates the syntax necessary to create the unique constraint (reused
	 * by all methods).  Ex: "unique (column1, column2, ...)"
	 * 
	 * @param uniqueKey
	 * @return String
	 */
	public String uniqueConstraintSql( UniqueKey uniqueKey );
}
