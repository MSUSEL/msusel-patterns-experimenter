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

import com.google.common.flogger.FluentLogger;
import edu.isu.isuese.datamodel.Project;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.flogger.Flogger;
import lombok.extern.java.Log;

import java.util.Properties;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ArcContext {

    @Getter
    String language;

    @Getter
    Project project;

    @Setter
    private Properties arcProperties;

    private FluentLogger logger;

    public ArcContext(FluentLogger logger) {
        this.logger = logger;
    }

    public String getArcProperty(String prop) {
        if (arcProperties.containsKey(prop)) {
            return arcProperties.getProperty(prop);
        }

        return null; // consider throwing an exception here
    }

    public void registerCommand(Command command) {

    }

    public void registerCollector(Collector collector) {
    }

    public void addArcProperty(String key, String value) {
        arcProperties.setProperty(key, value);
    }

    public void updateProperty(String key, String newValue) {
        arcProperties.setProperty(key, newValue);
    }

    public String getProjectDirectory() {
        return null;
    }

    public FluentLogger logger() {
        return logger;
    }
}
