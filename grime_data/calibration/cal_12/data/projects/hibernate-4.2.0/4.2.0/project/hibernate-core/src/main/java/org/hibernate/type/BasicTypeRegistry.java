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
package org.hibernate.type;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.UserType;

/**
 * A registry of {@link BasicType} instances
 *
 * @author Steve Ebersole
 */
public class BasicTypeRegistry implements Serializable {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, BasicTypeRegistry.class.getName());

	// TODO : analyze these sizing params; unfortunately this seems to be the only way to give a "concurrencyLevel"
	private Map<String,BasicType> registry = new ConcurrentHashMap<String, BasicType>( 100, .75f, 1 );
	private boolean locked = false;

	public BasicTypeRegistry() {
		register( BooleanType.INSTANCE );
		register( NumericBooleanType.INSTANCE );
		register( TrueFalseType.INSTANCE );
		register( YesNoType.INSTANCE );

		register( ByteType.INSTANCE );
		register( CharacterType.INSTANCE );
		register( ShortType.INSTANCE );
		register( IntegerType.INSTANCE );
		register( LongType.INSTANCE );
		register( FloatType.INSTANCE );
		register( DoubleType.INSTANCE );
		register( BigDecimalType.INSTANCE );
		register( BigIntegerType.INSTANCE );

		register( StringType.INSTANCE );
		register( StringNVarcharType.INSTANCE );
		register( CharacterNCharType.INSTANCE );
		register( UrlType.INSTANCE );

		register( DateType.INSTANCE );
		register( TimeType.INSTANCE );
		register( TimestampType.INSTANCE );
		register( DbTimestampType.INSTANCE );
		register( CalendarType.INSTANCE );
		register( CalendarDateType.INSTANCE );

		register( LocaleType.INSTANCE );
		register( CurrencyType.INSTANCE );
		register( TimeZoneType.INSTANCE );
		register( ClassType.INSTANCE );
		register( UUIDBinaryType.INSTANCE );
		register( UUIDCharType.INSTANCE );
		register( PostgresUUIDType.INSTANCE );

		register( BinaryType.INSTANCE );
		register( WrapperBinaryType.INSTANCE );
		register( ImageType.INSTANCE );
		register( CharArrayType.INSTANCE );
		register( CharacterArrayType.INSTANCE );
		register( TextType.INSTANCE );
		register( NTextType.INSTANCE );
		register( BlobType.INSTANCE );
		register( MaterializedBlobType.INSTANCE );
		register( ClobType.INSTANCE );
		register( NClobType.INSTANCE );
		register( MaterializedClobType.INSTANCE );
		register( MaterializedNClobType.INSTANCE );
		register( SerializableType.INSTANCE );

		register( ObjectType.INSTANCE );

		//noinspection unchecked
		register( new AdaptedImmutableType( DateType.INSTANCE ) );
		//noinspection unchecked
		register( new AdaptedImmutableType( TimeType.INSTANCE ) );
		//noinspection unchecked
		register( new AdaptedImmutableType( TimestampType.INSTANCE ) );
		//noinspection unchecked
		register( new AdaptedImmutableType( DbTimestampType.INSTANCE ) );
		//noinspection unchecked
		register( new AdaptedImmutableType( CalendarType.INSTANCE ) );
		//noinspection unchecked
		register( new AdaptedImmutableType( CalendarDateType.INSTANCE ) );
		//noinspection unchecked
		register( new AdaptedImmutableType( BinaryType.INSTANCE ) );
		//noinspection unchecked
		register( new AdaptedImmutableType( SerializableType.INSTANCE ) );
	}

	/**
	 * Constructor version used during shallow copy
	 *
	 * @param registeredTypes The type map to copy over
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	private BasicTypeRegistry(Map<String, BasicType> registeredTypes) {
		registry.putAll( registeredTypes );
		locked = true;
	}

	public void register(BasicType type) {
		if ( locked ) {
			throw new HibernateException( "Can not alter TypeRegistry at this time" );
		}

		if ( type == null ) {
			throw new HibernateException( "Type to register cannot be null" );
		}

		if ( type.getRegistrationKeys() == null || type.getRegistrationKeys().length == 0 ) {
			LOG.typeDefinedNoRegistrationKeys( type );
		}

		for ( String key : type.getRegistrationKeys() ) {
			// be safe...
            if (key == null) continue;
            LOG.debugf("Adding type registration %s -> %s", key, type);
			final Type old = registry.put( key, type );
            if (old != null && old != type) LOG.typeRegistrationOverridesPrevious(key, old);
		}
	}

	public void register(UserType type, String[] keys) {
		register( new CustomType( type, keys ) );
	}

	public void register(CompositeUserType type, String[] keys) {
		register( new CompositeCustomType( type, keys ) );
	}

	public BasicType getRegisteredType(String key) {
		return registry.get( key );
	}

	public BasicTypeRegistry shallowCopy() {
		return new BasicTypeRegistry( this.registry );
	}
}
