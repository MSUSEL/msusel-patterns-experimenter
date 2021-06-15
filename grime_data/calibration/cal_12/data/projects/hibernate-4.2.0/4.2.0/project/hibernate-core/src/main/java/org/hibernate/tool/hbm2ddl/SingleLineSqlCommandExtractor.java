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
package org.hibernate.tool.hbm2ddl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.internal.util.StringHelper;

/**
 * Class responsible for extracting SQL statements from import script. Treads each line as a complete SQL statement.
 * Comment lines shall start with {@code --}, {@code //} or {@code /*} character sequence.
 *
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class SingleLineSqlCommandExtractor implements ImportSqlCommandExtractor {
	@Override
	public String[] extractCommands(Reader reader) {
		BufferedReader bufferedReader = new BufferedReader( reader );
		List<String> statementList = new LinkedList<String>();
		try {
			for ( String sql = bufferedReader.readLine(); sql != null; sql = bufferedReader.readLine() ) {
				String trimmedSql = sql.trim();
				if ( StringHelper.isEmpty( trimmedSql ) || isComment( trimmedSql ) ) {
					continue;
				}
				if ( trimmedSql.endsWith( ";" ) ) {
					trimmedSql = trimmedSql.substring( 0, trimmedSql.length() - 1 );
				}
				statementList.add( trimmedSql );
			}
			return statementList.toArray( new String[statementList.size()] );
		}
		catch ( IOException e ) {
			throw new ImportScriptException( "Error during import script parsing.", e );
		}
	}

	private boolean isComment(final String line) {
		return line.startsWith( "--" ) || line.startsWith( "//" ) || line.startsWith( "/*" );
	}
}
