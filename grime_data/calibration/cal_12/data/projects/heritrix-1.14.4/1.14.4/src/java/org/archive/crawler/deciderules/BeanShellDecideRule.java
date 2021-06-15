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
/* BeanShellDecideRule
*
* $Id: BeanShellDecideRule.java 6149 2009-03-02 22:52:51Z gojomo $
*
* Created on Aug 7, 2006
*
* Copyright (C) 2006 Internet Archive.
*
* This file is part of the Heritrix web crawler (crawler.archive.org).
*
* Heritrix is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser Public License as published by
* the Free Software Foundation; either version 2.1 of the License, or
* any later version.
*
* Heritrix is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser Public License for more details.
*
* You should have received a copy of the GNU Lesser Public License
* along with Heritrix; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.archive.crawler.deciderules;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;

import bsh.EvalError;
import bsh.Interpreter;


/**
 * Rule which runs a groovy script to make its decision. 
 * 
 * Script source may be provided via a file local to the crawler.
 * 
 * Variables available to the script include 'object' (the object to be
 * evaluated, typically a CandidateURI or CrawlURI), 'self' 
 * (this GroovyDecideRule instance), and 'controller' (the crawl's 
 * CrawlController instance). 
 *
 * TODO: reduce copy & paste with GroovyProcessor
 * 
 * @author gojomo
 */
public class BeanShellDecideRule extends DecideRule {

    private static final long serialVersionUID = -8433859929199308527L;

    private static final Logger logger =
        Logger.getLogger(BeanShellDecideRule.class.getName());
    
    /** setting for script file */
    public final static String ATTR_SCRIPT_FILE = "script-file"; 

    /** whether each thread should have its own script runner (true), or
     * they should share a single script runner with synchronized access */
    public final static String ATTR_ISOLATE_THREADS = "isolate-threads";

    protected ThreadLocal<Interpreter> threadInterpreter = 
        new ThreadLocal<Interpreter>();;
    protected Interpreter sharedInterpreter;
    public Map<Object,Object> sharedMap = 
        Collections.synchronizedMap(new HashMap<Object,Object>());
    protected boolean initialized = false; 
    
    public BeanShellDecideRule(String name) {
        super(name);
        setDescription("BeanShellDecideRule. Runs the BeanShell script " +
                "source (supplied via a file path) against " +
                "the current URI. Source should define a script method " +
                "'decisionFor(object)' which will be passed the object" +
                "to be evaluated and returns one of self.ACCEPT, " +
                "self.REJECT, or self.PASS. " +
                "The script may access this BeanShellDecideRule via" +
                "the 'self' variable and the CrawlController via the " +
                "'controller' variable. Runs the groovy script source " +
                "(supplied via a file path) against the " +
                "current URI.");
        Type t = addElementToDefinition(new SimpleType(ATTR_SCRIPT_FILE,
                "BeanShell script file", ""));
        t.setOverrideable(false);
        t = addElementToDefinition(new SimpleType(ATTR_ISOLATE_THREADS,
                "Whether each ToeThread should get its own independent " +
                "script context, or they should share synchronized access " +
                "to one context. Default is true, meaning each threads " +
                "gets its own isolated context.", true));
        t.setOverrideable(false);
    }

    public Object decisionFor(Object object) {
        // depending on previous configuration, interpreter may 
        // be local to this thread or shared
        Interpreter interpreter = getInterpreter(); 
        synchronized(interpreter) {
            // synchronization is harmless for local thread interpreter,
            // necessary for shared interpreter
            try {
                interpreter.set("object",object);
                return interpreter.eval("decisionFor(object)");
            } catch (EvalError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return PASS;
            } 
        }
    }

    /**
     * Get the proper Interpreter instance -- either shared or local 
     * to this thread. 
     * @return Interpreter to use
     */
    protected synchronized Interpreter getInterpreter() {
        if(sharedInterpreter==null 
           && !(Boolean)getUncheckedAttribute(null,ATTR_ISOLATE_THREADS)) {
            // initialize
            sharedInterpreter = newInterpreter();
        }
        if(sharedInterpreter!=null) {
            return sharedInterpreter;
        }
        Interpreter interpreter = threadInterpreter.get(); 
        if(interpreter==null) {
            interpreter = newInterpreter(); 
            threadInterpreter.set(interpreter);
        }
        return interpreter; 
    }

    /**
     * Create a new Interpreter instance, preloaded with any supplied
     * source file and the variables 'self' (this 
     * BeanShellProcessor) and 'controller' (the CrawlController). 
     * 
     * @return  the new Interpreter instance
     */
    protected Interpreter newInterpreter() {
        Interpreter interpreter = new Interpreter(); 
        try {
            interpreter.set("self", this);
            interpreter.set("controller", getController());
            
            String filePath = (String) getUncheckedAttribute(null, ATTR_SCRIPT_FILE);
            if(filePath.length()>0) {
                try {
                    File file = getSettingsHandler().getPathRelativeToWorkingDirectory(filePath);
                    interpreter.source(file.getPath());
                } catch (IOException e) {
                    logger.log(Level.SEVERE,"unable to read script file",e);
                }
            }
        } catch (EvalError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return interpreter; 
    }
    
    
    /**
     * Setup (or reset) Intepreter variables, as appropraite based on 
     * thread-isolation setting. 
     */
    public void kickUpdate() {
        // TODO make it so running state (tallies, etc.) isn't lost on changes
        // unless unavoidable
        if((Boolean)getUncheckedAttribute(null,ATTR_ISOLATE_THREADS)) {
            sharedInterpreter = null; 
            threadInterpreter = new ThreadLocal<Interpreter>(); 
        } else {
            sharedInterpreter = newInterpreter(); 
            threadInterpreter = null;
        }
    }
}
