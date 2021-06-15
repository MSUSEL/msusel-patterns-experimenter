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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.framework.exceptions.FatalConfigurationException;
import org.archive.crawler.settings.MapType;


/** A list of all the ProcessorChains.
 *
 * @author John Erik Halse
 */
public class ProcessorChainList {
    private List<ProcessorChain> chainList = new ArrayList<ProcessorChain>();
    private Map<String,ProcessorChain> chainMap
     = new HashMap<String,ProcessorChain>();

    /** Constructs a new ProcessorChainList.
     *
     * @param order the Crawl Order to get configuration from.
     *
     * @throws FatalConfigurationException is thrown if chains could not be
     *         set up properly.
     */
    public ProcessorChainList(CrawlOrder order)
            throws FatalConfigurationException {
        try {
            addProcessorMap(CrawlOrder.ATTR_PRE_FETCH_PROCESSORS,
                (MapType) order.
                    getAttribute(CrawlOrder.ATTR_PRE_FETCH_PROCESSORS));
            addProcessorMap(CrawlOrder.ATTR_FETCH_PROCESSORS, (MapType) order
                    .getAttribute(CrawlOrder.ATTR_FETCH_PROCESSORS));
            addProcessorMap(CrawlOrder.ATTR_EXTRACT_PROCESSORS, (MapType) order
                    .getAttribute(CrawlOrder.ATTR_EXTRACT_PROCESSORS));
            addProcessorMap(CrawlOrder.ATTR_WRITE_PROCESSORS, (MapType) order
                    .getAttribute(CrawlOrder.ATTR_WRITE_PROCESSORS));
            addProcessorMap(CrawlOrder.ATTR_POST_PROCESSORS, (MapType) order
                    .getAttribute(CrawlOrder.ATTR_POST_PROCESSORS));
        } catch (AttributeNotFoundException e) {
            throw new FatalConfigurationException("Could not get processors" +
                " from crawl order: " + e.getMessage());
        } catch (MBeanException e) {
            throw new FatalConfigurationException("Could not get processors" +
                " from crawl order: " + e.getMessage());
        } catch (ReflectionException e) {
            throw new FatalConfigurationException("Could not get processors" +
                " from crawl order: " + e.getMessage());
        }

        if (processorCount() == 0) { throw new FatalConfigurationException(
                "No processors defined"); }
    }

    /** Add a new chain of processors to the chain list.
     *
     * This method takes a map of processors and wraps it in a ProcessorChain
     * object and adds it to the list of chains.
     *
     * @param processorMap the processor map to be added.
     */
    public void addProcessorMap(String name, MapType processorMap) {
        ProcessorChain processorChain = new ProcessorChain(processorMap);
        ProcessorChain previousChain = getLastChain();
        if (previousChain != null) {
            previousChain.setNextChain(processorChain);
        }
        chainList.add(processorChain);
        chainMap.put(name, processorChain);
    }

    /** Get the first processor chain.
     *
     * @return the first processor chain.
     */
    public ProcessorChain getFirstChain() {
        return (ProcessorChain) chainList.get(0);
    }

    /** Get the last processor chain.
     *
     * The last processor chain should contain processors that should always
     * be run for a URI that has started its way through the processors.
     *
     * @return the last processor chain.
     */
    public ProcessorChain getLastChain() {
        if (size() == 0) {
            return null;
        } else {
            return (ProcessorChain) chainList.get(size() - 1);
        }
    }

    /** Get the total number of all processors in all the chains.
     *
     * @return the total number of all processors in all the chains.
     */
    public int processorCount() {
        int processorCount = 0;
        for (Iterator it = iterator(); it.hasNext();) {
            processorCount += ((ProcessorChain) it.next()).size();
        }
        return processorCount;
    }

    /** Get an iterator over the processor chains.
     *
     * @return an iterator over the processor chains.
     */
    public Iterator iterator() {
        return chainList.iterator();
    }

    /** Get the number of processor chains.
     *
     * @return the number of processor chains.
     */
    public int size() {
        return chainList.size();
    }

    /** Get a processor chain by its index in the list of chains.
     *
     * @param index the chains index in the list of chains.
     * @return the requested processor chain.
     */
    public ProcessorChain getProcessorChain(int index) {
        return (ProcessorChain) chainList.get(index);
    }

    /** Get a processor chain by its name.
     *
     * @param name name of the processor chain to get.
     * @return the requested processor chain.
     */
    public ProcessorChain getProcessorChain(String name) {
        return (ProcessorChain) chainMap.get(name);
    }

    public void kickUpdate() {
        for (ProcessorChain chain : chainList) {
            chain.kickUpdate();
        }
    }

}
