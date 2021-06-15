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
package org.hibernate.testing;

import org.hibernate.dialect.Dialect;

/**
 * Container class for different implementation of the {@link DialectCheck} interface.
 *
 * @author Hardy Ferentschik
 * @author Steve Ebersole
 */
abstract public class DialectChecks {
	public static class SupportsSequences implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsSequences();
		}
	}

	public static class SupportsExpectedLobUsagePattern implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsExpectedLobUsagePattern();
		}
	}

	public static class SupportsIdentityColumns implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsIdentityColumns();
		}
	}

	public static class SupportsColumnCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsColumnCheck();
		}
	}

	public static class SupportsEmptyInListCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsEmptyInList();
		}
	}

	public static class CaseSensitiveCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.areStringComparisonsCaseInsensitive();
		}
	}

	public static class SupportsResultSetPositioningOnForwardOnlyCursorCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsResultSetPositionQueryMethodsOnForwardOnlyCursor();
		}
	}

	public static class SupportsCircularCascadeDeleteCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsCircularCascadeDeleteConstraints();
		}
	}

	public static class SupportsUnboundedLobLocatorMaterializationCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsExpectedLobUsagePattern() && dialect.supportsUnboundedLobLocatorMaterialization();
		}
	}

	public static class SupportSubqueryAsLeftHandSideInPredicate implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsSubselectAsInPredicateLHS();
		}
	}

	public static class SupportLimitCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsLimit();
		}
	}

	public static class SupportLimitAndOffsetCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsLimit() && dialect.supportsLimitOffset();
		}
	}

	public static class SupportsParametersInInsertSelectCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsParametersInInsertSelect();
		}
	}

	public static class HasSelfReferentialForeignKeyBugCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.hasSelfReferentialForeignKeyBug();
		}
	}

	public static class SupportsRowValueConstructorSyntaxInInListCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsRowValueConstructorSyntaxInInList();
		}
	}

	public static class DoesReadCommittedCauseWritersToBlockReadersCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.doesReadCommittedCauseWritersToBlockReaders();
		}
	}

	public static class DoesReadCommittedNotCauseWritersToBlockReadersCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return ! dialect.doesReadCommittedCauseWritersToBlockReaders();
		}
	}

	public static class DoesRepeatableReadCauseReadersToBlockWritersCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.doesRepeatableReadCauseReadersToBlockWriters();
		}
	}

	public static class DoesRepeatableReadNotCauseReadersToBlockWritersCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return ! dialect.doesRepeatableReadCauseReadersToBlockWriters();
		}
	}

	public static class SupportsExistsInSelectCheck implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsExistsInSelect();
		}
	}
	
	public static class SupportsLobValueChangePropogation implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsLobValueChangePropogation();
		}
	}
	
	public static class SupportsLockTimeouts implements DialectCheck {
		public boolean isMatch(Dialect dialect) {
			return dialect.supportsLockTimeouts();
		}
	}
}
