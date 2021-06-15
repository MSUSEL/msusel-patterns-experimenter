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
package org.hibernate.type.descriptor.java;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.hibernate.HibernateException;
import org.hibernate.type.descriptor.WrapperOptions;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class JdbcTimeTypeDescriptor extends AbstractTypeDescriptor<Date> {
	public static final JdbcTimeTypeDescriptor INSTANCE = new JdbcTimeTypeDescriptor();
	public static final String TIME_FORMAT = "HH:mm:ss";

	public static class TimeMutabilityPlan extends MutableMutabilityPlan<Date> {
		public static final TimeMutabilityPlan INSTANCE = new TimeMutabilityPlan();

		public Date deepCopyNotNull(Date value) {
			return Time.class.isInstance( value )
					? new Time( value.getTime() )
					: new Date( value.getTime() );
		}
	}

	public JdbcTimeTypeDescriptor() {
		super( Date.class, TimeMutabilityPlan.INSTANCE );
	}

	public String toString(Date value) {
		return new SimpleDateFormat( TIME_FORMAT ).format( value );
	}

	public java.util.Date fromString(String string) {
		try {
			return new Time( new SimpleDateFormat( TIME_FORMAT ).parse( string ).getTime() );
		}
		catch ( ParseException pe ) {
			throw new HibernateException( "could not parse time string" + string, pe );
		}
	}

	@Override
	public int extractHashCode(Date value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( value );
		int hashCode = 1;
		hashCode = 31 * hashCode + calendar.get( Calendar.HOUR_OF_DAY );
		hashCode = 31 * hashCode + calendar.get( Calendar.MINUTE );
		hashCode = 31 * hashCode + calendar.get( Calendar.SECOND );
		hashCode = 31 * hashCode + calendar.get( Calendar.MILLISECOND );
		return hashCode;
	}

	@Override
	public boolean areEqual(Date one, Date another) {
		if ( one == another ) {
			return true;
		}
		if ( one == null || another == null ) {
			return false;
		}

		if ( one.getTime() == another.getTime() ) {
			return true;
		}

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime( one );
		calendar2.setTime( another );

		return calendar1.get( Calendar.HOUR_OF_DAY ) == calendar2.get( Calendar.HOUR_OF_DAY )
				&& calendar1.get( Calendar.MINUTE ) == calendar2.get( Calendar.MINUTE )
				&& calendar1.get( Calendar.SECOND ) == calendar2.get( Calendar.SECOND )
				&& calendar1.get( Calendar.MILLISECOND ) == calendar2.get( Calendar.MILLISECOND );
	}

	@SuppressWarnings({ "unchecked" })
	public <X> X unwrap(Date value, Class<X> type, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( Time.class.isAssignableFrom( type ) ) {
			final Time rtn = Time.class.isInstance( value )
					? ( Time ) value
					: new Time( value.getTime() );
			return (X) rtn;
		}
		if ( java.sql.Date.class.isAssignableFrom( type ) ) {
			final java.sql.Date rtn = java.sql.Date.class.isInstance( value )
					? ( java.sql.Date ) value
					: new java.sql.Date( value.getTime() );
			return (X) rtn;
		}
		if ( java.sql.Timestamp.class.isAssignableFrom( type ) ) {
			final java.sql.Timestamp rtn = java.sql.Timestamp.class.isInstance( value )
					? ( java.sql.Timestamp ) value
					: new java.sql.Timestamp( value.getTime() );
			return (X) rtn;
		}
		if ( java.util.Date.class.isAssignableFrom( type ) ) {
			return (X) value;
		}
		if ( Calendar.class.isAssignableFrom( type ) ) {
			final GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeInMillis( value.getTime() );
			return (X) cal;
		}
		if ( Long.class.isAssignableFrom( type ) ) {
			return (X) Long.valueOf( value.getTime() );
		}
		throw unknownUnwrap( type );
	}

	@SuppressWarnings({ "UnnecessaryUnboxing" })
	public <X> Date wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}
		if ( Time.class.isInstance( value ) ) {
			return (Time) value;
		}

		if ( Long.class.isInstance( value ) ) {
			return new Time( ( (Long) value ).longValue() );
		}

		if ( Calendar.class.isInstance( value ) ) {
			return new Time( ( (Calendar) value ).getTimeInMillis() );
		}

		if ( Date.class.isInstance( value ) ) {
			return (Date) value;
		}

		throw unknownWrap( value.getClass() );
	}
}
