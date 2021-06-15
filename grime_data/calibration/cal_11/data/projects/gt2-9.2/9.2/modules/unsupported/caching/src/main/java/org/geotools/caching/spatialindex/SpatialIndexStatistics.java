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
package org.geotools.caching.spatialindex;
/** 
 * 
 * Data structure to store statistics about a spatial index.
 * 
 * Tracks:
 * <li>Number of reads.</li>
 * <li>Number of writes.</li>
 * <li>Number of nodes.</li>
 * <li>Size of data.</li>
*
* @author Christophe Rousson, SoC 2007, CRG-ULAVAL
*
 *
 *
 *
 *
 * @source $URL$
*/
public class SpatialIndexStatistics implements Statistics {

	int stats_reads = 0;
	int stats_writes = 0;
	int stats_nodes = 0;
	int stats_data = 0;

	public long getNumberOfData() {
		return stats_data;
	}

	public long getNumberOfNodes() {
		return stats_nodes;
	}

	public long getReads() {
		return stats_reads;
	}

	public long getWrites() {
		return stats_writes;
	}

	public void addToReadsCounter(int count) {
		stats_reads += count;
	}

	public void addToWritesCounter(int count) {
		stats_writes += count;
	}

	public void addToNodesCounter(int count) {
		stats_nodes += count;
	}

	public void addToDataCounter(int count) {
		stats_data += count;
	}

	public void reset() {
		stats_reads = 0;
		stats_writes = 0;
		stats_nodes = 0;
		stats_data = 0;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Reads = " + stats_reads);
		sb.append(" ; Writes = " + stats_writes);
		sb.append(" ; Nodes = " + stats_nodes);
		sb.append(" ; Data = " + stats_data);

		return sb.toString();
	}

}
