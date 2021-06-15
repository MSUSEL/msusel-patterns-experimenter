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
package org.archive.crawler.framework;

import java.util.Iterator;
import java.util.logging.Logger;

import org.archive.crawler.settings.MapType;


/** This class groups together a number of processors that logically fit
 * together.
 *
 * @author John Erik Halse
 */
public class ProcessorChain {
    private static Logger logger =
        Logger.getLogger("org.archive.crawler.framework.ProcessorChain");

    private final MapType processorMap;
    private ProcessorChain nextChain;
    private Processor firstProcessor;

    /** Construct a new processor chain.
     *
     * @param processorMap a map of the processors belonging to this chain.
     */
    public ProcessorChain(MapType processorMap) {
        this.processorMap = processorMap;

        Processor previous = null;

        for (Iterator it = processorMap.iterator(null); it.hasNext();) {
            Processor p = (Processor) it.next();

            if (previous == null) {
                firstProcessor = p;
            } else {
                previous.setDefaultNextProcessor(p);
            }

            logger.info(
                "Processor: " + p.getName() + " --> " + p.getClass().getName());

            previous = p;
        }
    }

    /** Set the processor chain that the URI should be working through after
     * finishing this one.
     *
     * @param nextProcessorChain the chain that should be processed after this
     *        one.
     */
    public void setNextChain(ProcessorChain nextProcessorChain) {
        this.nextChain = nextProcessorChain;
    }

    /** Get the processor chain that the URI should be working through after
     * finishing this one.
     *
     * @return the next processor chain.
     */
    public ProcessorChain getNextProcessorChain() {
        return nextChain;
    }

    /** Get the first processor in the chain.
     *
     * @return the first processor in the chain.
     */
    public Processor getFirstProcessor() {
        return firstProcessor;
    }

    /** Get the first processor that is of class <code>classType</code> or a
     * subclass of it.
     *
     * @param classType the class of the requested processor.
     * @return the first processor matching the classType.
     */
    public Processor getProcessor(Class classType) {
        for (Iterator it = processorMap.iterator(null); it.hasNext();) {
            Processor p = (Processor) it.next();
            if (classType.isInstance(p)) {
                return p;
            }
        }
        return null;
    }

    /** Get the number of processors in this chain.
     *
     * @return the number of processors in this chain.
     */
    public int size() {
        return processorMap.size(null);
    }

    /** Get an iterator over the processors in this chain.
     *
     * @return an iterator over the processors in this chain.
     */
    public Iterator iterator() {
        return processorMap.iterator(null);
    }

    public void kickUpdate() {
        Iterator iter = iterator();
        while(iter.hasNext()) {
            Processor p = (Processor) iter.next(); 
            p.kickUpdate(); 
        }
    }
}
