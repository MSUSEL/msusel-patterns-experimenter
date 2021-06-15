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
package org.apache.hadoop.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.TaskTrackerStatus;
import org.junit.Test;

/**
 * A JUnit test to test {@link LinuxResourceCalculatorPlugin}
 * Create the fake /proc/ information and verify the parsing and calculation
 */
public class TestLinuxResourceCalculatorPlugin extends TestCase {
  /**
   * LinuxResourceCalculatorPlugin with a fake timer
   */
  static class FakeLinuxResourceCalculatorPlugin extends
      LinuxResourceCalculatorPlugin {
    
	  long currentTime = 0;
	  public FakeLinuxResourceCalculatorPlugin(String procfsMemFile,
			                                       String procfsCpuFile,
			                                       String procfsStatFile,
			                                       long jiffyLengthInMillis) {
	    super(procfsMemFile, procfsCpuFile, procfsStatFile, jiffyLengthInMillis);
	  }
	  @Override
	  long getCurrentTime() {
	    return currentTime;
	  }
	  public void advanceTime(long adv) {
	    currentTime += adv * jiffyLengthInMillis;
	  }
  }
  private static final FakeLinuxResourceCalculatorPlugin plugin;
  private static String TEST_ROOT_DIR = new Path(System.getProperty(
         "test.build.data", "/tmp")).toString().replace(' ', '+');
  private static final String FAKE_MEMFILE;
  private static final String FAKE_CPUFILE;
  private static final String FAKE_STATFILE;
  private static final long FAKE_JIFFY_LENGTH = 10L;
  static {
    int randomNum = (new Random()).nextInt(1000000000);
    FAKE_MEMFILE = TEST_ROOT_DIR + File.separator + "MEMINFO_" + randomNum;
    FAKE_CPUFILE = TEST_ROOT_DIR + File.separator + "CPUINFO_" + randomNum;
    FAKE_STATFILE = TEST_ROOT_DIR + File.separator + "STATINFO_" + randomNum;
    plugin = new FakeLinuxResourceCalculatorPlugin(FAKE_MEMFILE, FAKE_CPUFILE,
                                                   FAKE_STATFILE,
                                                   FAKE_JIFFY_LENGTH);
  }
  static final String MEMINFO_FORMAT = 
	  "MemTotal:      %d kB\n" +
	  "MemFree:         %d kB\n" +
	  "Buffers:        138244 kB\n" +
	  "Cached:         947780 kB\n" +
	  "SwapCached:     142880 kB\n" +
	  "Active:        3229888 kB\n" +
	  "Inactive:       %d kB\n" +
	  "SwapTotal:     %d kB\n" +
	  "SwapFree:      %d kB\n" +
	  "Dirty:          122012 kB\n" +
	  "Writeback:           0 kB\n" +
	  "AnonPages:     2710792 kB\n" +
	  "Mapped:          24740 kB\n" +
	  "Slab:           132528 kB\n" +
	  "SReclaimable:   105096 kB\n" +
	  "SUnreclaim:      27432 kB\n" +
	  "PageTables:      11448 kB\n" +
	  "NFS_Unstable:        0 kB\n" +
	  "Bounce:              0 kB\n" +
	  "CommitLimit:   4125904 kB\n" +
	  "Committed_AS:  4143556 kB\n" +
	  "VmallocTotal: 34359738367 kB\n" +
	  "VmallocUsed:      1632 kB\n" +
	  "VmallocChunk: 34359736375 kB\n" +
	  "HugePages_Total:     0\n" +
	  "HugePages_Free:      0\n" +
	  "HugePages_Rsvd:      0\n" +
	  "Hugepagesize:     2048 kB";
  
  static final String CPUINFO_FORMAT =
    "processor : %s\n" +
    "vendor_id : AuthenticAMD\n" +
    "cpu family  : 15\n" +
    "model   : 33\n" +
    "model name  : Dual Core AMD Opteron(tm) Processor 280\n" +
    "stepping  : 2\n" +
    "cpu MHz   : %f\n" +
    "cache size  : 1024 KB\n" +
    "physical id : 0\n" +
    "siblings  : 2\n" +
    "core id   : 0\n" +
    "cpu cores : 2\n" +
    "fpu   : yes\n" +
    "fpu_exception : yes\n" +
    "cpuid level : 1\n" +
    "wp    : yes\n" +
    "flags   : fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov " +
    "pat pse36 clflush mmx fxsr sse sse2 ht syscall nx mmxext fxsr_opt lm " +
    "3dnowext 3dnow pni lahf_lm cmp_legacy\n" +
    "bogomips  : 4792.41\n" +
    "TLB size  : 1024 4K pages\n" +
    "clflush size  : 64\n" +
    "cache_alignment : 64\n" +
    "address sizes : 40 bits physical, 48 bits virtual\n" +
    "power management: ts fid vid ttp";
  
  static final String STAT_FILE_FORMAT = 
    "cpu  %d %d %d 1646495089 831319 48713 164346 0\n" +
    "cpu0 15096055 30805 3823005 411456015 206027 13 14269 0\n" +
    "cpu1 14760561 89890 6432036 408707910 456857 48074 130857 0\n" +
    "cpu2 12761169 20842 3758639 413976772 98028 411 10288 0\n" +
    "cpu3 12355207 47322 5789691 412354390 70406 213 8931 0\n" +
    "intr 114648668 20010764 2 0 945665 2 0 0 0 0 0 0 0 4 0 0 0 0 0 0\n" +
    "ctxt 242017731764\n" +
    "btime 1257808753\n" +
    "processes 26414943\n" +
    "procs_running 1\n" +
    "procs_blocked 0\n";
  
  /**
   * Test parsing /proc/stat and /proc/cpuinfo
   * @throws IOException
   */
  public void testParsingProcStatAndCpuFile() throws IOException {
    // Write fake /proc/cpuinfo file.
    long numProcessors = 8;
    long cpuFrequencyKHz = 2392781;
    String fileContent = "";
    for (int i = 0; i < numProcessors; i++) {
      fileContent += String.format(CPUINFO_FORMAT, i, cpuFrequencyKHz / 1000D) +
                     "\n";
    }
    File tempFile = new File(FAKE_CPUFILE);
    tempFile.deleteOnExit();
    FileWriter fWriter = new FileWriter(FAKE_CPUFILE);
    fWriter.write(fileContent);
    fWriter.close();
    assertEquals(plugin.getNumProcessors(), numProcessors);
    assertEquals(plugin.getCpuFrequency(), cpuFrequencyKHz);
    
    // Write fake /proc/stat file.
    long uTime = 54972994;
    long nTime = 188860;
    long sTime = 19803373;
    tempFile = new File(FAKE_STATFILE);
    tempFile.deleteOnExit();
    updateStatFile(uTime, nTime, sTime);
    assertEquals(plugin.getCumulativeCpuTime(),
                 FAKE_JIFFY_LENGTH * (uTime + nTime + sTime));
    assertEquals(plugin.getCpuUsage(), (float)(TaskTrackerStatus.UNAVAILABLE));
    
    // Advance the time and sample again to test the CPU usage calculation
    uTime += 100L;
    plugin.advanceTime(200L);
    updateStatFile(uTime, nTime, sTime);
    assertEquals(plugin.getCumulativeCpuTime(),
                 FAKE_JIFFY_LENGTH * (uTime + nTime + sTime));
    assertEquals(plugin.getCpuUsage(), 6.25F);
    
    // Advance the time and sample again. This time, we call getCpuUsage() only.
    uTime += 600L;
    plugin.advanceTime(300L);
    updateStatFile(uTime, nTime, sTime);
    assertEquals(plugin.getCpuUsage(), 25F);
    
    // Advance very short period of time (one jiffy length).
    // In this case, CPU usage should not be updated.
    uTime += 1L;
    plugin.advanceTime(1L);
    updateStatFile(uTime, nTime, sTime);
    assertEquals(plugin.getCumulativeCpuTime(),
                 FAKE_JIFFY_LENGTH * (uTime + nTime + sTime));
    assertEquals(plugin.getCpuUsage(), 25F); // CPU usage is not updated.
  }
  
  /**
   * Write information to fake /proc/stat file
   */
  private void updateStatFile(long uTime, long nTime, long sTime)
    throws IOException {
    FileWriter fWriter = new FileWriter(FAKE_STATFILE);
    fWriter.write(String.format(STAT_FILE_FORMAT, uTime, nTime, sTime));
    fWriter.close();
  }
  
  /**
   * Test parsing /proc/meminfo
   * @throws IOException
   */
  public void testParsingProcMemFile() throws IOException {
    long memTotal = 4058864L;
    long memFree = 99632L;
    long inactive = 567732L;
    long swapTotal = 2096472L;
    long swapFree = 1818480L;
    File tempFile = new File(FAKE_MEMFILE);
    tempFile.deleteOnExit();
    FileWriter fWriter = new FileWriter(FAKE_MEMFILE);
    fWriter.write(String.format(MEMINFO_FORMAT,
      memTotal, memFree, inactive, swapTotal, swapFree));
    
    fWriter.close();
    assertEquals(plugin.getAvailablePhysicalMemorySize(),
                 1024L * (memFree + inactive));
    assertEquals(plugin.getAvailableVirtualMemorySize(),
                 1024L * (memFree + inactive + swapFree));
    assertEquals(plugin.getPhysicalMemorySize(), 1024L * memTotal);
    assertEquals(plugin.getVirtualMemorySize(), 1024L * (memTotal + swapTotal));
  }
}
