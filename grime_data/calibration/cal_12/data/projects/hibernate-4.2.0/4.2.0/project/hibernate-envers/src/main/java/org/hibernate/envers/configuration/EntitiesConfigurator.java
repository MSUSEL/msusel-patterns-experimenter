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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import org.hibernate.MappingException;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.configuration.metadata.AuditEntityNameRegister;
import org.hibernate.envers.configuration.metadata.AuditMetadataGenerator;
import org.hibernate.envers.configuration.metadata.EntityXmlMappingData;
import org.hibernate.envers.configuration.metadata.reader.AnnotationsMetadataReader;
import org.hibernate.envers.configuration.metadata.reader.ClassAuditingData;
import org.hibernate.envers.entities.EntitiesConfigurations;
import org.hibernate.envers.strategy.AuditStrategy;
import org.hibernate.envers.tools.StringTools;
import org.hibernate.envers.tools.graph.GraphTopologicalSort;
import org.hibernate.mapping.PersistentClass;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class EntitiesConfigurator {
    public EntitiesConfigurations configure(Configuration cfg, ReflectionManager reflectionManager,
                                            GlobalConfiguration globalCfg, AuditEntitiesConfiguration verEntCfg,
                                            AuditStrategy auditStrategy,
                                            Document revisionInfoXmlMapping, Element revisionInfoRelationMapping) {
        // Creating a name register to capture all audit entity names created.
        AuditEntityNameRegister auditEntityNameRegister = new AuditEntityNameRegister();
        DOMWriter writer = new DOMWriter();

        // Sorting the persistent class topologically - superclass always before subclass
        Iterator<PersistentClass> classes = GraphTopologicalSort.sort(new PersistentClassGraphDefiner(cfg)).iterator();

        ClassesAuditingData classesAuditingData = new ClassesAuditingData();
        Map<PersistentClass, EntityXmlMappingData> xmlMappings = new HashMap<PersistentClass, EntityXmlMappingData>();

        // Reading metadata from annotations
        while (classes.hasNext()) {
            PersistentClass pc = classes.next();

            // Collecting information from annotations on the persistent class pc
            AnnotationsMetadataReader annotationsMetadataReader =
                    new AnnotationsMetadataReader(globalCfg, reflectionManager, pc);
            ClassAuditingData auditData = annotationsMetadataReader.getAuditData();

            classesAuditingData.addClassAuditingData(pc, auditData);
        }

        // Now that all information is read we can update the calculated fields.
        classesAuditingData.updateCalculatedFields();

        AuditMetadataGenerator auditMetaGen = new AuditMetadataGenerator(cfg, globalCfg, verEntCfg, auditStrategy,
                revisionInfoRelationMapping, auditEntityNameRegister);

        // First pass
        for (Map.Entry<PersistentClass, ClassAuditingData> pcDatasEntry : classesAuditingData.getAllClassAuditedData()) {
            PersistentClass pc = pcDatasEntry.getKey();
            ClassAuditingData auditData = pcDatasEntry.getValue();

            EntityXmlMappingData xmlMappingData = new EntityXmlMappingData();
            if (auditData.isAudited()) {
                if (!StringTools.isEmpty(auditData.getAuditTable().value())) {
                    verEntCfg.addCustomAuditTableName(pc.getEntityName(), auditData.getAuditTable().value());
                }

                auditMetaGen.generateFirstPass(pc, auditData, xmlMappingData, true);
			} else {
				auditMetaGen.generateFirstPass(pc, auditData, xmlMappingData, false);
			}

            xmlMappings.put(pc, xmlMappingData);
        }

        // Second pass
        for (Map.Entry<PersistentClass, ClassAuditingData> pcDatasEntry : classesAuditingData.getAllClassAuditedData()) {
            EntityXmlMappingData xmlMappingData = xmlMappings.get(pcDatasEntry.getKey());

            if (pcDatasEntry.getValue().isAudited()) {
                auditMetaGen.generateSecondPass(pcDatasEntry.getKey(), pcDatasEntry.getValue(), xmlMappingData);
                try {
                    cfg.addDocument(writer.write(xmlMappingData.getMainXmlMapping()));
                    //writeDocument(xmlMappingData.getMainXmlMapping());

                    for (Document additionalMapping : xmlMappingData.getAdditionalXmlMappings()) {
                        cfg.addDocument(writer.write(additionalMapping));
                        //writeDocument(additionalMapping);
                    }
                } catch (DocumentException e) {
                    throw new MappingException(e);
                }
            }
        }

        // Only if there are any versioned classes
        if (auditMetaGen.getEntitiesConfigurations().size() > 0) {
            try {
                if (revisionInfoXmlMapping !=  null) {
                    //writeDocument(revisionInfoXmlMapping);
                    cfg.addDocument(writer.write(revisionInfoXmlMapping));
                }
            } catch (DocumentException e) {
                throw new MappingException(e);
            }
        }

		return new EntitiesConfigurations(auditMetaGen.getEntitiesConfigurations(),
				auditMetaGen.getNotAuditedEntitiesConfigurations());
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private void writeDocument(Document e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer w = new PrintWriter(baos);

        try {
            XMLWriter xw = new XMLWriter(w, new OutputFormat(" ", true));
            xw.write(e);
            w.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println("-----------");
        System.out.println(baos.toString());
        System.out.println("-----------");
    }
}
