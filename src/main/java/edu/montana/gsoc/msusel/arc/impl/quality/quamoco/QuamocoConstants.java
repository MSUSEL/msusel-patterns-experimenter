/*
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
package edu.montana.gsoc.msusel.arc.impl.quality.quamoco;

import java.io.File;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public interface QuamocoConstants {

    String QUAMOCO_CMD_NAME         = "Quamoco";
    String QM_HOME_PROP_KEY         = "quamoco.models.dir";
    String QUAMOCO_LANG_MODELS_FILE = "config" + File.separator + "quamoco" + File.separator + "languages.properties";
    String QUAMOCO_METRICS_FILE     = "config" + File.separator + "quamoco" + File.separator + "metrics.properties";
    String QUAMOCO_CONFIG_DIR       = "config" + File.separator + "quamoco" + File.separator;
    String QUAMOCO_REPO_NAME        = "arc-quamoco";
    String QUAMOCO_REPO_KEY         = "arc-quamoco";
    String QUAMOCO_TOOL_NAME        = "Arc Quamoco";
}
