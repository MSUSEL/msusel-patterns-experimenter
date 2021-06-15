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
package org.hibernate.envers.configuration;

import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import org.hibernate.MappingException;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.entities.EntitiesConfigurations;
import org.hibernate.envers.entities.PropertyData;
import org.hibernate.envers.revisioninfo.ModifiedEntityNamesReader;
import org.hibernate.envers.revisioninfo.RevisionInfoNumberReader;
import org.hibernate.envers.revisioninfo.RevisionInfoQueryCreator;
import org.hibernate.envers.strategy.AuditStrategy;
import org.hibernate.envers.strategy.ValidityAuditStrategy;
import org.hibernate.envers.synchronization.AuditProcessManager;
import org.hibernate.envers.tools.reflection.ReflectionTools;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.Getter;
import org.hibernate.service.classloading.spi.ClassLoaderService;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Stephanie Pau at Markit Group Plc
 */
public class AuditConfiguration {
	private final GlobalConfiguration globalCfg;
	private final AuditEntitiesConfiguration auditEntCfg;
	private final AuditProcessManager auditProcessManager;
	private final AuditStrategy auditStrategy;
	private final EntitiesConfigurations entCfg;
	private final RevisionInfoQueryCreator revisionInfoQueryCreator;
	private final RevisionInfoNumberReader revisionInfoNumberReader;
	private final ModifiedEntityNamesReader modifiedEntityNamesReader;
	private final ClassLoaderService classLoaderService;

	public AuditEntitiesConfiguration getAuditEntCfg() {
		return auditEntCfg;
	}

	public AuditProcessManager getSyncManager() {
		return auditProcessManager;
	}

	public GlobalConfiguration getGlobalCfg() {
		return globalCfg;
	}

	public EntitiesConfigurations getEntCfg() {
		return entCfg;
	}

	public RevisionInfoQueryCreator getRevisionInfoQueryCreator() {
		return revisionInfoQueryCreator;
	}

	public RevisionInfoNumberReader getRevisionInfoNumberReader() {
		return revisionInfoNumberReader;
	}

	public ModifiedEntityNamesReader getModifiedEntityNamesReader() {
		return modifiedEntityNamesReader;
	}

	public AuditStrategy getAuditStrategy() {
		return auditStrategy;
	}

	public AuditConfiguration(Configuration cfg) {
		this( cfg, null );
	}

	public AuditConfiguration(Configuration cfg, ClassLoaderService classLoaderService) {
		Properties properties = cfg.getProperties();

		ReflectionManager reflectionManager = cfg.getReflectionManager();
		globalCfg = new GlobalConfiguration( properties );
		RevisionInfoConfiguration revInfoCfg = new RevisionInfoConfiguration( globalCfg );
		RevisionInfoConfigurationResult revInfoCfgResult = revInfoCfg.configure( cfg, reflectionManager );
		auditEntCfg = new AuditEntitiesConfiguration( properties, revInfoCfgResult.getRevisionInfoEntityName() );
		auditProcessManager = new AuditProcessManager( revInfoCfgResult.getRevisionInfoGenerator() );
		revisionInfoQueryCreator = revInfoCfgResult.getRevisionInfoQueryCreator();
		revisionInfoNumberReader = revInfoCfgResult.getRevisionInfoNumberReader();
		modifiedEntityNamesReader = revInfoCfgResult.getModifiedEntityNamesReader();
		this.classLoaderService = classLoaderService;
		auditStrategy = initializeAuditStrategy(
				revInfoCfgResult.getRevisionInfoClass(),
				revInfoCfgResult.getRevisionInfoTimestampData()
		);
		entCfg = new EntitiesConfigurator().configure(
				cfg, reflectionManager, globalCfg, auditEntCfg, auditStrategy,
				revInfoCfgResult.getRevisionInfoXmlMapping(), revInfoCfgResult.getRevisionInfoRelationMapping()
		);
	}

	private AuditStrategy initializeAuditStrategy(Class<?> revisionInfoClass, PropertyData revisionInfoTimestampData) {
		AuditStrategy strategy;

		try {

			Class<?> auditStrategyClass = null;
			if ( classLoaderService != null ) {
				auditStrategyClass = classLoaderService.classForName( auditEntCfg.getAuditStrategyName() );
			}
			else {
				auditStrategyClass = ReflectHelper.classForName( auditEntCfg.getAuditStrategyName() );
			}

			strategy = (AuditStrategy) ReflectHelper.getDefaultConstructor(auditStrategyClass).newInstance();
		}
		catch ( Exception e ) {
			throw new MappingException(
					String.format( "Unable to create AuditStrategy[%s] instance.", auditEntCfg.getAuditStrategyName() ),
					e
			);
		}

		if ( strategy instanceof ValidityAuditStrategy ) {
			// further initialization required
			Getter revisionTimestampGetter = ReflectionTools.getGetter( revisionInfoClass, revisionInfoTimestampData );
			( (ValidityAuditStrategy) strategy ).setRevisionTimestampGetter( revisionTimestampGetter );
		}

		return strategy;
	}

	//

	private static Map<Configuration, AuditConfiguration> cfgs
			= new WeakHashMap<Configuration, AuditConfiguration>();

	public synchronized static AuditConfiguration getFor(Configuration cfg) {
		return getFor( cfg, null );
	}

	public synchronized static AuditConfiguration getFor(Configuration cfg, ClassLoaderService classLoaderService) {
		AuditConfiguration verCfg = cfgs.get( cfg );

		if ( verCfg == null ) {
			verCfg = new AuditConfiguration( cfg, classLoaderService );
			cfgs.put( cfg, verCfg );

			cfg.buildMappings();
		}

		return verCfg;
	}
}
