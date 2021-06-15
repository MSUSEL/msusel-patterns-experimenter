====
    The MIT License (MIT)

    MSUSEL Arc Framework
    Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
    Software Engineering Laboratory and Idaho State University, Informatics and
    Computer Science, Empirical Software Engineering Laboratory

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
====

*************************************************
*** Input Files (Note: tab-separated columns) ***
*************************************************
[:~]$ cat datajoin/input/A
A.a11   A.a12
A.a21   A.a22
B.a21   A.a32
A.a31   A.a32
B.a31   A.a32

[:~]$ cat datajoin/input/B
A.a11   B.a12
A.a11   B.a13
B.a11   B.a12
B.a21   B.a22
A.a31   B.a32
B.a31   B.a32


*****************************
*** Invoke SampleDataJoin ***
*****************************
[:~]$ $HADOOP_HOME/bin/hadoop jar hadoop-datajoin-examples.jar org.apache.hadoop.contrib.utils.join.DataJoinJob datajoin/input datajoin/output Text 1 org.apache.hadoop.contrib.utils.join.SampleDataJoinMapper org.apache.hadoop.contrib.utils.join.SampleDataJoinReducer org.apache.hadoop.contrib.utils.join.SampleTaggedMapOutput Text
Using TextInputFormat: Text
Using TextOutputFormat: Text
07/06/01 19:58:23 INFO mapred.FileInputFormat: Total input paths to process : 2
Job job_kkzk08 is submitted
Job job_kkzk08 is still running.
07/06/01 19:58:24 INFO mapred.LocalJobRunner: collectedCount    5
totalCount      5

07/06/01 19:58:24 INFO mapred.LocalJobRunner: collectedCount    6
totalCount      6

07/06/01 19:58:24 INFO datajoin.job: key: A.a11 this.largestNumOfValues: 3
07/06/01 19:58:24 INFO mapred.LocalJobRunner: actuallyCollectedCount    5
collectedCount  7
groupCount      6
 > reduce


*******************
*** Output File ***
*******************
[:~]$ cat datajoin/output/part-00000  
A.a11   A.a12   B.a12
A.a11   A.a12   B.a13
A.a31   A.a32   B.a32
B.a21   A.a32   B.a22
B.a31   A.a32   B.a32
