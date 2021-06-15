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
package org.archive.crawler.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.UriUniqFilter;
import org.archive.util.fingerprint.MemLongFPSet;


/**
 * BenchmarkUriUniqFilters
 * 
 * @author gojomo
 */
public class BenchmarkUriUniqFilters implements UriUniqFilter.HasUriReceiver {
//    private Logger LOGGER =
//        Logger.getLogger(BenchmarkUriUniqFilters.class.getName());
    
    private BufferedWriter out; // optional to dump uniq items
    String current; // current line/URI being checked
    
    /**
     * Test the UriUniqFilter implementation (MemUriUniqFilter,
     * BloomUriUniqFilter, or BdbUriUniqFilter) named in first
     * argument against the file of one-per-line URIs named
     * in the second argument. 
     * 
     * @param args from cmd-line
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        (new BenchmarkUriUniqFilters()).instanceMain(args);
    }
    
    public void instanceMain(String[] args) throws IOException {
        String testClass = args[0];
        String inputFilename = args[1];
        long start = System.currentTimeMillis();
        UriUniqFilter uniq = createUriUniqFilter(testClass);
        long created = System.currentTimeMillis();
        BufferedReader br = new BufferedReader(new FileReader(inputFilename));
        if(args.length>2) {
            String outputFilename = args[2];
            out = new BufferedWriter(new FileWriter(outputFilename));
        }
        int added = 0;
        while((current=br.readLine())!=null) {
            added++;
            uniq.add(current,null);
        }
        uniq.close();
        long finished = System.currentTimeMillis();
        if(out!=null) {
            out.close();
        }
        System.out.println(added+" adds");
        System.out.println(uniq.count()+" retained");
        System.out.println((created-start)+"ms to setup UUF");
        System.out.println((finished-created)+"ms to perform all adds");
    }
    
    private UriUniqFilter createUriUniqFilter(String testClass) throws IOException {
        UriUniqFilter uniq = null;
        if(BdbUriUniqFilter.class.getName().endsWith(testClass)) {;
            // BDB setup
            File tmpDir = File.createTempFile("uuf","benchmark");
            tmpDir.delete();
            tmpDir.mkdir();
            uniq = new BdbUriUniqFilter(tmpDir, 50);
        } else if(BloomUriUniqFilter.class.getName().endsWith(testClass)) {
            // bloom setup
            uniq = new BloomUriUniqFilter();
        } else if(MemUriUniqFilter.class.getName().endsWith(testClass)) {
            // mem hashset
            uniq = new MemUriUniqFilter();
        } else if (FPUriUniqFilter.class.getName().endsWith(testClass)) {
            // mem fp set (open-addressing) setup
            uniq = new FPUriUniqFilter(new MemLongFPSet(21,0.75f));
        }
        uniq.setDestination(this);
        return uniq;
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.datamodel.UriUniqFilter.HasUriReceiver#receive(org.archive.crawler.datamodel.CandidateURI)
     */
    public void receive(CandidateURI item) {
        if(out!=null) {
            try {
                // we assume all tested filters are immediate passthrough so
                // we can use 'current'; a buffering filter would change this
                // assumption
                out.write(current);
                out.write("\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}