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
import scalax.io._
import scalax.file._
val tests = Path("target/surefire-reports/") * "TEST-*"

implicit val c=Codec.UTF8

val report = """
%s
  tests: %s successes: %s failures: %s
  testNames:
%s

"""
val out = Path("target/surefire-reports/fullSummary.txt")
out.delete()
var suc = 0
var fail = 0
tests.foreach { f =>
	val results = xml.XML.loadFile(f.path)
	val t = results \\ "testsuite"
	val name = t \ "@name"
  val failures = (t \ "@failures").text.toInt + (t \ "@errors").text.toInt
  val successes = (t \ "@tests").text.toInt - failures
  suc += successes
  fail += failures
  val cases = t \\ "testcase" map { c =>
    val name = (c \\ "@name").text

    if (c \\ "error" nonEmpty) "X   "+name
    else "    "+name
  }
  val testNames = cases mkString "\n"
  out append report.format(name.text.split("\\.").last, successes+failures, successes, failures, testNames)
}

out.append("Totals: tests: "+(suc+fail)+" successes: "+suc+" failures: "+fail)