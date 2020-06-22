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
package edu.montana.gsoc.msusel.arc;

import com.google.common.collect.Maps;
import com.google.common.flogger.FluentLogger;
import edu.isu.isuese.datamodel.Project;
import edu.isu.isuese.datamodel.util.DBManager;
import edu.montana.gsoc.msusel.arc.db.DbProperties;
import groovy.util.logging.Log4j2;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.flogger.Flogger;
import lombok.extern.java.Log;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Properties;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ArcContext {

    @Getter
    String language;

    @Getter @Setter
    Project project;

    Map<String, Command> commandMap;
    Map<String, Collector> collectorMap;

    @Setter
    private Properties arcProperties;

    Logger logger;

    public ArcContext(Logger logger) {
        this.logger = logger;
        commandMap = Maps.newHashMap();
        collectorMap = Maps.newHashMap();
    }

    public String getArcProperty(String prop) {
        if (arcProperties.containsKey(prop)) {
            return arcProperties.getProperty(prop);
        }

        return null; // consider throwing an exception here
    }

    public void registerCommand(Command command) {
        if (command != null)
            commandMap.put(command.getToolName(), command);
    }

    public void registerCollector(Collector collector) {
        if (collector != null)
            collectorMap.put(collector.getName(), collector);
    }

    public Command getRegisteredCommand(String name) {
        return commandMap.get(name);
    }

    public Collector getRegisteredCollector(String name) {
        return collectorMap.get(name);
    }

    public void addArcProperty(String key, String value) {
        arcProperties.setProperty(key, value);
    }

    public void updateProperty(String key, String newValue) {
        arcProperties.setProperty(key, newValue);
    }

    public String getProjectDirectory() {
        if (project != null) {
            return project.getFullPath();
        } else {
            return null;
        }
    }

    public Logger logger() {
        return logger;
    }

    public void open() {
        DBManager.getInstance().open(getArcProperty(DbProperties.DB_DRIVER),
                getArcProperty(DbProperties.DB_URL),
                getArcProperty(DbProperties.DB_USER),
                getArcProperty(DbProperties.DB_PASS));
    }

    public void close() {
        DBManager.getInstance().close();
    }
}
