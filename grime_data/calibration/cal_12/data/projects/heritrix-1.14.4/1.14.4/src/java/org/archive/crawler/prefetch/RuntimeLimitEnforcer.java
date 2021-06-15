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
package org.archive.crawler.prefetch;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.admin.CrawlJob;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.framework.Processor;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;

/**
 * A processor to enforce runtime limits on crawls.
 * <p>
 * This processor extends and improves on the 'max-time' capability of Heritrix.
 * Essentially, the 'Terminate job' option functions the same way as 'max-time'. 
 * The processor however also enables pausing when the runtime is exceeded and  
 * the blocking of all URIs. 
 * <p>
 * <ol>
 * <li>Pause job - Pauses the crawl. A change (increase) to the 
 *     runtime duration will make it pausible to resume the crawl. 
 *     Attempts to resume the crawl without modifying the run time 
 *     will cause it to be immediately paused again.</li>
 * <li>Terminate job - Terminates the job. Equivalent
 *     to using the max-time setting on the CrawlController.</li>
 * <li>Block URIs - Blocks each URI with an -5002
 *     (blocked by custom processor) fetch status code. This will
 *     cause all the URIs queued to wind up in the crawl.log.</li>
 * <ol>
 * <p>
 * The processor allows variable runtime based on host (or other  
 * override/refinement criteria) however using such overrides only makes sense  
 * when using 'Block URIs' as pause and terminate will have global impact once
 * encountered anywhere. 
 * 
 * @author Kristinn Sigur&eth;sson
 */
public class RuntimeLimitEnforcer 
                extends Processor implements FetchStatusCodes {

    private static final long serialVersionUID = 1L;
    
    protected Logger logger = Logger.getLogger(
            RuntimeLimitEnforcer.class.getName());
    
    public static final String ATTR_RUNTIME_SECONDS = "runtime-sec".intern();
    protected static final long DEFAULT_RUNTIME_SECONDS = 86400; // 1 day

    public static final String ATTR_END_OPERATION = "end-operation".intern();
    protected static final String OP_PAUSE = "Pause job".intern();
    protected static final String OP_TERMINATE = "Terminate job".intern();
    protected static final String OP_BLOCK_URIS = "Block URIs".intern();
    protected static final String DEFAULT_END_OPERATION = OP_PAUSE;
    protected static final String[] AVAILABLE_END_OPERATIONS = {
        OP_PAUSE, OP_TERMINATE, OP_BLOCK_URIS};
    
    public RuntimeLimitEnforcer(String name) {
        super(name, "A processor that halts further progress once a fixed " +
                "amount of time has elapsed since the start of a crawl. " +
                "It is possible to configure this processor per host, but " +
                "it should be noted that Heritrix does not track runtime " +
                "per host seperately. Especially when using facilities " +
                "like the BdbFrontier's hold-queues, the actual amount of " +
                "time spent crawling a host may have little relevance to " +
                "total elapsed time. Note however that using overrides " +
                "and/or refinements only makes sense when using the " +
                "'Block URIs' end operation. The pause and terminate " +
                "operations have global impact once encountered.");
        Type t =  new SimpleType(
                ATTR_RUNTIME_SECONDS,
                "The amount of time, in seconds, that the crawl will be " +
                "allowed to run before this processor performs it's 'end " +
                "operation.'",
                DEFAULT_RUNTIME_SECONDS);
        addElementToDefinition(t);
        t = new SimpleType(
                ATTR_END_OPERATION,
                "The action that the processor takes once the runtime has " +
                "elapsed.\n " +
                "Operation: Pause job - Pauses the crawl. A change " +
                "(increase) to the runtime duration will " +
                "make it pausible to resume the crawl. Attempts to resume " +
                "the crawl without modifying the run time will cause it to " +
                "be immediately paused again.\n " +
                "Operation: Terminate job - Terminates the job. Equivalent " +
                "to using the max-time setting on the CrawlController.\n " +
                "Operation: Block URIs - Blocks each URI with an -5002 " +
                "(blocked by custom processor) fetch status code. This will " +
                "cause all the URIs queued to wind up in the crawl.log.",
                DEFAULT_END_OPERATION, 
                AVAILABLE_END_OPERATIONS);
        addElementToDefinition(t);
    }

    protected void innerProcess(CrawlURI curi) throws InterruptedException {
        long allowedRuntime = getRuntime(curi);
        long currentRuntime = getController().getStatistics().crawlDuration();
        if(currentRuntime > allowedRuntime){
            String op = (String)getUncheckedAttribute(curi,ATTR_END_OPERATION);
            if(op != null){
                if(op.equals(OP_PAUSE)){
                    getController().requestCrawlPause();
                } else if(op.equals(OP_TERMINATE)){
                    getController().requestCrawlStop(
                            CrawlJob.STATUS_FINISHED_TIME_LIMIT);
                } else if(op.equals(OP_BLOCK_URIS)){
                    curi.setFetchStatus(S_BLOCKED_BY_RUNTIME_LIMIT);
                    curi.addAnnotation("Runtime exceeded " + allowedRuntime + 
                            "ms");
                    curi.skipToProcessorChain(
                            getController().getPostprocessorChain());
                }
            } else {
                logger.log(Level.SEVERE,"Null value for " + ATTR_END_OPERATION + 
                        " when processing " + curi.toString());
            }
        }
    }
    
    /**
     * Returns the amount of time to allow the crawl to run before this 
     * processor interrupts.
     * @return the amount of time in milliseconds.
     */
    protected long getRuntime(CrawlURI curi){
        Object o = getUncheckedAttribute(curi,ATTR_RUNTIME_SECONDS);
        if(o == null){
            logger.log(Level.SEVERE,"Null value for " + ATTR_RUNTIME_SECONDS + 
                    " when processing " + curi.toString());
            return Long.MAX_VALUE;
        }
        return ((Long)o).longValue()*1000; //extract value and convert to ms.
    }
    
}
