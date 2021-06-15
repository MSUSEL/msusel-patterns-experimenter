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
package org.apache.hadoop.vaidya;

import org.apache.hadoop.vaidya.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * This is a base driver class for job diagnostics. Various specialty drivers that
 * tests specific aspects of job problems e.g. PostExPerformanceDiagnoser extends the
 * this base class.
 *
 */
public class JobDiagnoser {

  /*
   * XML document containing report elements, one for each rule tested
   */
  private Document _report;

  /*
   * @report : returns report document
   */
  public Document getReport() {
    return this._report;
  }
  

  /**
   * Constructor. It initializes the report document.
   */
  public JobDiagnoser () throws Exception {
    
    /*
     * Initialize the report document, make it ready to add the child report elements 
     */
    DocumentBuilder builder = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try{
      builder = factory.newDocumentBuilder();
      this._report = builder.newDocument();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
      
    // Insert Root Element
    Element root = (Element) this._report.createElement("PostExPerformanceDiagnosticReport");
    this._report.appendChild(root);
  }
  
  /*
   * Print the report document to console
   */
  public void printReport() {
    XMLUtils.printDOM(this._report);
  }
  
  /*
   * Save the report document the specified report file
   * @param reportfile : path of report file. 
   */
  public void saveReport(String filename) {
    XMLUtils.writeXmlToFile(filename, this._report);
  }
}
