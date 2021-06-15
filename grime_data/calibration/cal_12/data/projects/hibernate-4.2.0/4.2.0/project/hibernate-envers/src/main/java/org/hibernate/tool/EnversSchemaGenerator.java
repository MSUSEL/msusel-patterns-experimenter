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
package org.hibernate.tool;

import java.sql.Connection;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class EnversSchemaGenerator {
    private final SchemaExport export;

    public EnversSchemaGenerator(ServiceRegistry serviceRegistry, Configuration configuration) {
        configuration = configureAuditing(configuration);
        export = new SchemaExport(serviceRegistry, configuration);
    }

    public EnversSchemaGenerator(Configuration configuration) {
        configuration = configureAuditing(configuration);
        export = new SchemaExport(configuration);
    }

    public EnversSchemaGenerator(Configuration configuration, Properties properties) throws HibernateException {
        configuration = configureAuditing(configuration);
        export = new SchemaExport(configuration, properties);
    }

    public EnversSchemaGenerator(Configuration configuration, Connection connection) throws HibernateException {
        configuration = configureAuditing(configuration);
        export = new SchemaExport(configuration, connection);
    }

    public SchemaExport export() {
        return export;
    }

    private Configuration configureAuditing(Configuration configuration) {
        configuration.buildMappings();
		AuditConfiguration.getFor(configuration);
        return configuration;
    }
}
