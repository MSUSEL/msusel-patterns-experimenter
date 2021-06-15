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
package org.hibernate.metamodel.source.hbm;

import org.hibernate.EntityMode;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.engine.OptimisticLockStyle;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbCacheElement;
import org.hibernate.internal.jaxb.mapping.hbm.JaxbHibernateMapping;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.binding.Caching;
import org.hibernate.metamodel.binding.IdGenerator;
import org.hibernate.metamodel.source.MappingException;
import org.hibernate.metamodel.source.binder.DiscriminatorSource;
import org.hibernate.metamodel.source.binder.IdentifierSource;
import org.hibernate.metamodel.source.binder.RelationalValueSource;
import org.hibernate.metamodel.source.binder.RootEntitySource;
import org.hibernate.metamodel.source.binder.SimpleIdentifierSource;
import org.hibernate.metamodel.source.binder.SingularAttributeSource;
import org.hibernate.metamodel.source.binder.TableSource;

/**
 * @author Steve Ebersole
 */
public class RootEntitySourceImpl extends AbstractEntitySourceImpl implements RootEntitySource {
	protected RootEntitySourceImpl(MappingDocument sourceMappingDocument, JaxbHibernateMapping.JaxbClass entityElement) {
		super( sourceMappingDocument, entityElement );
	}

	@Override
	protected JaxbHibernateMapping.JaxbClass entityElement() {
		return (JaxbHibernateMapping.JaxbClass) super.entityElement();
	}

	@Override
	public IdentifierSource getIdentifierSource() {
		if ( entityElement().getId() != null ) {
			return new SimpleIdentifierSource() {
				@Override
				public SingularAttributeSource getIdentifierAttributeSource() {
					return new SingularIdentifierAttributeSourceImpl(
							entityElement().getId(),
							sourceMappingDocument().getMappingLocalBindingContext()
					);
				}

				@Override
				public IdGenerator getIdentifierGeneratorDescriptor() {
					if ( entityElement().getId().getGenerator() != null ) {
						final String generatorName = entityElement().getId().getGenerator().getClazz();
						IdGenerator idGenerator = sourceMappingDocument().getMappingLocalBindingContext()
								.getMetadataImplementor()
								.getIdGenerator( generatorName );
						if ( idGenerator == null ) {
							idGenerator = new IdGenerator(
									getEntityName() + generatorName,
									generatorName,
									Helper.extractParameters( entityElement().getId().getGenerator().getParam() )
							);
						}
						return idGenerator;
					}
					return null;
				}

				@Override
				public Nature getNature() {
					return Nature.SIMPLE;
				}
			};
		}
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public SingularAttributeSource getVersioningAttributeSource() {
		if ( entityElement().getVersion() != null ) {
			return new VersionAttributeSourceImpl(
					entityElement().getVersion(),
					sourceMappingDocument().getMappingLocalBindingContext()
			);
		}
		else if ( entityElement().getTimestamp() != null ) {
			return new TimestampAttributeSourceImpl(
					entityElement().getTimestamp(),
					sourceMappingDocument().getMappingLocalBindingContext()
			);
		}
		return null;
	}

	@Override
	public EntityMode getEntityMode() {
		return determineEntityMode();
	}

	@Override
	public boolean isMutable() {
		return entityElement().isMutable();
	}


	@Override
	public boolean isExplicitPolymorphism() {
		return "explicit".equals( entityElement().getPolymorphism() );
	}

	@Override
	public String getWhere() {
		return entityElement().getWhere();
	}

	@Override
	public String getRowId() {
		return entityElement().getRowid();
	}

	@Override
	public OptimisticLockStyle getOptimisticLockStyle() {
		final String optimisticLockModeString = Helper.getStringValue( entityElement().getOptimisticLock(), "version" );
		try {
			return OptimisticLockStyle.valueOf( optimisticLockModeString.toUpperCase() );
		}
		catch ( Exception e ) {
			throw new MappingException(
					"Unknown optimistic-lock value : " + optimisticLockModeString,
					sourceMappingDocument().getOrigin()
			);
		}
	}

	@Override
	public Caching getCaching() {
		final JaxbCacheElement cache = entityElement().getCache();
		if ( cache == null ) {
			return null;
		}
		final String region = cache.getRegion() != null ? cache.getRegion() : getEntityName();
		final AccessType accessType = Enum.valueOf( AccessType.class, cache.getUsage() );
		final boolean cacheLazyProps = !"non-lazy".equals( cache.getInclude() );
		return new Caching( region, accessType, cacheLazyProps );
	}

	@Override
	public TableSource getPrimaryTable() {
		return new TableSource() {
			@Override
			public String getExplicitSchemaName() {
				return entityElement().getSchema();
			}

			@Override
			public String getExplicitCatalogName() {
				return entityElement().getCatalog();
			}

			@Override
			public String getExplicitTableName() {
				return entityElement().getTable();
			}

			@Override
			public String getLogicalName() {
				// logical name for the primary table is null
				return null;
			}
		};
	}

	@Override
	public String getDiscriminatorMatchValue() {
		return entityElement().getDiscriminatorValue();
	}

	@Override
	public DiscriminatorSource getDiscriminatorSource() {
		final JaxbHibernateMapping.JaxbClass.JaxbDiscriminator discriminatorElement = entityElement().getDiscriminator();
		if ( discriminatorElement == null ) {
			return null;
		}

		return new DiscriminatorSource() {
			@Override
			public RelationalValueSource getDiscriminatorRelationalValueSource() {
				if ( StringHelper.isNotEmpty( discriminatorElement.getColumnAttribute() ) ) {
					return new ColumnAttributeSourceImpl(
							null, // root table
							discriminatorElement.getColumnAttribute(),
							discriminatorElement.isInsert(),
							discriminatorElement.isInsert()
					);
				}
				else if ( StringHelper.isNotEmpty( discriminatorElement.getFormulaAttribute() ) ) {
					return new FormulaImpl( null, discriminatorElement.getFormulaAttribute() );
				}
				else if ( discriminatorElement.getColumn() != null ) {
					return new ColumnSourceImpl(
							null, // root table
							discriminatorElement.getColumn(),
							discriminatorElement.isInsert(),
							discriminatorElement.isInsert()
					);
				}
				else if ( StringHelper.isNotEmpty( discriminatorElement.getFormula() ) ) {
					return new FormulaImpl( null, discriminatorElement.getFormula() );
				}
				else {
					throw new MappingException( "could not determine source of discriminator mapping", getOrigin() );
				}
			}

			@Override
			public String getExplicitHibernateTypeName() {
				return discriminatorElement.getType();
			}

			@Override
			public boolean isForced() {
				return discriminatorElement.isForce();
			}

			@Override
			public boolean isInserted() {
				return discriminatorElement.isInsert();
			}
		};
	}
}
