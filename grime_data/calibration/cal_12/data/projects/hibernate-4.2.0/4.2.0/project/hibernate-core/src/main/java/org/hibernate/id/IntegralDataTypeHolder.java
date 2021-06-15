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
package org.hibernate.id;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Defines a common api for dealing with data of integral data type.
 *
 * @author Steve Ebersole
 */
public interface IntegralDataTypeHolder extends Serializable {
	/**
	 * Initialize the internal value from the given primitive long.
	 *
	 * @param value The primitive integral value.
	 *
	 * @return <tt>this</tt>, for method chaining
	 */
	public IntegralDataTypeHolder initialize(long value);

	/**
	 * Initialize the internal value from the given result set, using the specified default value
	 * if we could not get a value from the result set (aka result was null).
	 *
	 * @param resultSet The JDBC result set
	 * @param defaultValue The default value to use if we did not get a result set value.
	 *
	 * @return <tt>this</tt>, for method chaining
	 *
	 * @throws SQLException Any exception from accessing the result set
	 */
	public IntegralDataTypeHolder initialize(ResultSet resultSet, long defaultValue) throws SQLException;

	/**
	 * Bind this holders internal value to the given result set.
	 *
	 * @param preparedStatement The JDBC prepared statement
	 * @param position The position at which to bind
	 *
	 * @throws SQLException Any exception from accessing the statement
	 */
	public void bind(PreparedStatement preparedStatement, int position) throws SQLException;

	/**
	 * Equivalent to a ++ operation
	 *
	 * @return <tt>this</tt>, for method chaining
	 */
	public IntegralDataTypeHolder increment();

	/**
	 * Perform an addition
	 *
	 * @param addend The value to add to this integral.
	 *
	 * @return <tt>this</tt>, for method chaining
	 */
	public IntegralDataTypeHolder add(long addend);

	/**
	 * Equivalent to a -- operation
	 *
	 * @return <tt>this</tt>, for method chaining
	 */
	public IntegralDataTypeHolder decrement();

	/**
	 * Perform a subtraction
	 *
	 * @param subtrahend The value to subtract from this integral.
	 *
	 * @return <tt>this</tt>, for method chaining
	 */
	public IntegralDataTypeHolder subtract(long subtrahend);

	/**
	 * Perform a multiplication.
	 *
	 * @param factor The factor by which to multiple this integral
	 *
	 * @return <tt>this</tt>, for method chaining
	 */
	public IntegralDataTypeHolder multiplyBy(IntegralDataTypeHolder factor);

	/**
	 * Perform a multiplication.
	 *
	 * @param factor The factor by which to multiple this integral
	 *
	 * @return <tt>this</tt>, for method chaining
	 */
	public IntegralDataTypeHolder multiplyBy(long factor);

	/**
	 * Perform an equality comparison check
	 *
	 * @param other The other value to check against our internal state
	 *
	 * @return True if the two are equal
	 */
	public boolean eq(IntegralDataTypeHolder other);

	/**
	 * Perform an equality comparison check
	 *
	 * @param other The other value to check against our internal state
	 *
	 * @return True if the two are equal
	 */
	public boolean eq(long other);

	/**
	 * Perform a "less than" comparison check.  We check to see if our value is less than
	 * the incoming value...
	 *
	 * @param other The other value to check against our internal state
	 *
	 * @return True if our value is less than the 'other' value.
	 */
	public boolean lt(IntegralDataTypeHolder other);

	/**
	 * Perform a "less than" comparison check.  We check to see if our value is less than
	 * the incoming value...
	 *
	 * @param other The other value to check against our internal state
	 *
	 * @return True if our value is less than the 'other' value.
	 */
	public boolean lt(long other);

	/**
	 * Perform a "greater than" comparison check.  We check to see if our value is greater
	 * than the incoming value...
	 *
	 * @param other The other value to check against our internal state
	 *
	 * @return True if our value is greater than the 'other' value.
	 */
	public boolean gt(IntegralDataTypeHolder other);

	/**
	 * Perform a "greater than" comparison check.  We check to see if our value is greater
	 * than the incoming value...
	 *
	 * @param other The other value to check against our internal state
	 *
	 * @return True if our value is greater than the 'other' value.
	 */
	public boolean gt(long other);

	/**
	 * Make a copy of this holder.
	 *
	 * @return The copy.
	 */
	public IntegralDataTypeHolder copy();

	/**
	 * Return the internal value.
	 *
	 * @return The current internal value
	 */
	public Number makeValue();

	/**
	 * Increment the internal state, but return the pre-incremented value.
	 *
	 * @return The pre-incremented internal value
	 */
	public Number makeValueThenIncrement();

	/**
	 * Increment the internal state by the given addend, but return the pre-incremented value.
	 *
	 * @param addend The value to be added to our internal state
	 *
	 * @return The pre-incremented internal value
	 */
	public Number makeValueThenAdd(long addend);
}
