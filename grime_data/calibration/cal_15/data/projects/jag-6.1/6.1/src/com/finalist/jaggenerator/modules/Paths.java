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
package com.finalist.jaggenerator.modules;

import com.finalist.jaggenerator.*;

import javax.swing.*;
import javax.swing.tree.*;

import org.w3c.dom.*;

/**
 *
 * @author  hillie
 */
public class Paths extends DefaultMutableTreeNode implements JagBean {


   /**
    * The following paths are predefined for non-velocity sources.
    */
   public final static String CONF_GENERAL_DIR = "/conf/conf-general";
   public final static String CONF_STRUTS_DIR = "/conf/conf-struts";
   public final static String CONF_SWING_DIR = "/conf/conf-swing";
   public final static String CONF_TAPESTRY4_DIR = "/conf/conf-tapestry4";

   public final static String JAVA_SERVICE_DIR = "/src/java-service";

   public final static String JAVA_STRUTS_DIR = "/src/java-struts";
   public final static String JAVA_TAPESTRY4_DIR = "/src/java-tapestry4";
   public final static String JAVA_WEB_STRUTS_DIR = "/src/web-struts";
   public final static String JAVA_WEB_TAPESTRY4_DIR = "/src/web-tapestry4";


   public final static String JAVA_EJB2_DIR = "/src/java-ejb2";
   public final static String JAVA_EJB3_DIR = "/src/java-ejb3";
   public final static String JAVA_HIBERNATE2_DIR = "/src/java-hibernate2";
   public final static String JAVA_HIBERNATE3_DIR = "/src/java-hibernate3";

   public final static String JAVA_TEST_DIR = "/src/test";
   public final static String JAVA_MOCK_DIR = "/src/java-mock";
   public final static String JAVA_SWING_DIR = "/src/java-swing";

   /** Creates new form BeanForm */
   public Paths() {
      initComponents();
   }

   public Paths(Element el) {
      initComponents();
      NodeList nl = el.getElementsByTagName("module-data");
      for (int i = 0; i < nl.getLength(); i++) {
         Element child = (Element) nl.item(i);
         String attName = child.getAttribute("name");
         String value = null;
         if (child.getFirstChild() == null)
            value = null;
         else
            value = child.getFirstChild().getNodeValue();
         if (value != null) {
             if (attName.equalsIgnoreCase("service_output")) {
                serviceOutText.setText(value);
                continue;
             }
            if (attName.equalsIgnoreCase("hibernate_output")) {
               hibernateOutText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("ejb_output")) {
               ejbOutText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("web_output")) {
               webOutText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("jsp_output")) {
               jspOutText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("config_output")) {
               configText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("test_output")) {
               testOutText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("mock_output")) {
               mockOutText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("swing_output")) {
               swingOutText.setText(value);
               continue;
            }
         }
      }
   }


   public String toString() {
      return "Paths";
   }

   public JPanel getPanel() {
      return panel;
   }

   public void getXML(Element el) {
      Document doc = el.getOwnerDocument();
      Element module = doc.createElement("module");
      module.setAttribute("name", "paths");

       Element serviceOut = doc.createElement("module-data");
       serviceOut.setAttribute("name", "service_output");
       if (serviceOutText.getText() != null) {
         serviceOut.appendChild(doc.createTextNode(serviceOutText.getText()));
       }
       module.appendChild(serviceOut);

      Element hibernateOut = doc.createElement("module-data");
      hibernateOut.setAttribute("name", "hibernate_output");
      if (hibernateOutText.getText() != null) {
        hibernateOut.appendChild(doc.createTextNode(hibernateOutText.getText()));
      }
      module.appendChild(hibernateOut);

      Element ejbOut = doc.createElement("module-data");
      ejbOut.setAttribute("name", "ejb_output");
      if (ejbOutText.getText() != null) {
        ejbOut.appendChild(doc.createTextNode(ejbOutText.getText()));
      }
      module.appendChild(ejbOut);

      Element webOut = doc.createElement("module-data");
      webOut.setAttribute("name", "web_output");
      if (webOutText.getText() != null) {
        webOut.appendChild(doc.createTextNode(webOutText.getText()));
      }
      module.appendChild(webOut);

      Element testOut = doc.createElement("module-data");
      testOut.setAttribute("name", "test_output");
      if (testOutText.getText() != null) {
        testOut.appendChild(doc.createTextNode(testOutText.getText()));
      }
      module.appendChild(testOut);

      Element jspOut = doc.createElement("module-data");
      jspOut.setAttribute("name", "jsp_output");
      if (jspOutText.getText() != null) {
        jspOut.appendChild(doc.createTextNode(jspOutText.getText()));
      }
      module.appendChild(jspOut);

      Element configXml = doc.createElement("module-data");
      configXml.setAttribute("name", "config_output");
      if (configText.getText() != null) {
        configXml.appendChild(doc.createTextNode(configText.getText()));
      }
      module.appendChild(configXml);

      Element mockXml = doc.createElement("module-data");
      mockXml.setAttribute("name", "mock_output");
      if (mockOutText.getText() != null) {
        mockXml.appendChild(doc.createTextNode(mockOutText.getText()));
      }
      module.appendChild(mockXml);

      Element swingXml = doc.createElement("module-data");
      swingXml.setAttribute("name", "swing_output");
      if (swingOutText.getText() != null) {
        swingXml.appendChild(doc.createTextNode(swingOutText.getText()));
      }
      module.appendChild(swingXml);
      el.appendChild(module);
   }

   public String getSwingOutput() {
      return swingOutText.getText();
   }
   public String getMockOutput() {
      return mockOutText.getText();
   }

    public String getServiceOutput() {
       return serviceOutText.getText();
    }

   public String getHibernateOutput() {
      return hibernateOutText.getText();
   }

   public String getConfigOutput() {
      return configText.getText();
   }

   public String getJspOutput() {
      return jspOutText.getText();
   }

   public String getTestOutput() {
      return testOutText.getText();
   }

   public String getWebOutput() {
      return webOutText.getText();
   }

   public String getEjbOutput() {
      return ejbOutText.getText();
   }

   public void setSwingOutput(String text) {
      swingOutText.setText(text);
   }

   public void setMockOutput(String text) {
      mockOutText.setText(text);
   }

    public void setServiceOutput(String text) {
       serviceOutText.setText(text);
    }

   public void setHibernateOutput(String text) {
      hibernateOutText.setText(text);
   }

   public void setConfigOutput(String text) {
      configText.setText(text);
   }

   public void setJspOutput(String text) {
      jspOutText.setText(text);
   }

   public void setTestOutput(String text) {
      testOutText.setText(text);
   }

   public void setWebOutput(String text) {
      webOutText.setText(text);
   }

   public void setEjbOutput(String text) {
      ejbOutText.setText(text);
   }


   public String getRefName() {
      return "paths";
   }


   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        panel = new javax.swing.JPanel();
        ejbOutLabel = new javax.swing.JLabel();
        webOutLabel = new javax.swing.JLabel();
        jspOutLabel = new javax.swing.JLabel();
        testNameLabel = new javax.swing.JLabel();
        configOutLabel = new javax.swing.JLabel();
        ejbOutText = new javax.swing.JTextField();
        webOutText = new javax.swing.JTextField();
        jspOutText = new javax.swing.JTextField();
        testOutText = new javax.swing.JTextField();
        configText = new javax.swing.JTextField();
        serviceOutLabel = new javax.swing.JLabel();
        serviceOutText = new javax.swing.JTextField();
        hibernateOutLabel = new javax.swing.JLabel();
        hibernateOutText = new javax.swing.JTextField();
        mockOutLabel = new javax.swing.JLabel();
        mockOutText = new javax.swing.JTextField();
        swingOutLabel = new javax.swing.JLabel();
        swingOutText = new javax.swing.JTextField();

        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ejbOutLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ejbOutLabel.setText("EJB dir:");
        panel.add(ejbOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, -1));

        webOutLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        webOutLabel.setText("Web dir:");
        panel.add(webOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 100, -1));

        jspOutLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jspOutLabel.setText("JSP dir:");
        panel.add(jspOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 100, -1));

        testNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        testNameLabel.setText("Test dir:");
        panel.add(testNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 100, -1));

        configOutLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        configOutLabel.setText("Config dir:");
        panel.add(configOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 100, -1));

        ejbOutText.setText("./src/java-ejb/");
        ejbOutText.setToolTipText("Target directory for ejb sources");
        ejbOutText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ejbOutTextFocusLost(evt);
            }
        });

        panel.add(ejbOutText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 260, -1));

        webOutText.setText("./src/java-web/");
        webOutText.setToolTipText("Target directory for web based source");
        webOutText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                webOutTextFocusLost(evt);
            }
        });

        panel.add(webOutText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 260, -1));

        jspOutText.setText("./src/web/");
        jspOutText.setToolTipText("Target directory for JSPs, HTML, images etc.");
        jspOutText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jspOutTextFocusLost(evt);
            }
        });

        panel.add(jspOutText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 260, -1));

        testOutText.setText("./src/java-test/");
        testOutText.setToolTipText("Target directory for JUnit tests");
        testOutText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                testOutTextFocusLost(evt);
            }
        });

        panel.add(testOutText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 260, -1));

        configText.setText("./conf/");
        configText.setToolTipText("Target directory for configuration files");
        configText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                configTextFocusLost(evt);
            }
        });

        panel.add(configText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 260, -1));

        serviceOutLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        serviceOutLabel.setText("Service dir:");
        panel.add(serviceOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 100, -1));

        serviceOutText.setText("./src/java-service/");
        serviceOutText.setToolTipText("Target directory for service interfaces and related sources");
        serviceOutText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceOutTextActionPerformed(evt);
            }
        });

        panel.add(serviceOutText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 260, -1));

        hibernateOutLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        hibernateOutLabel.setText("Hibernate dir:");
        panel.add(hibernateOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 100, -1));

        hibernateOutText.setText("./src/java-hibernate/");
        hibernateOutText.setToolTipText("Target directory for hibernate sources");
        hibernateOutText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hibernateOutTextActionPerformed(evt);
            }
        });

        panel.add(hibernateOutText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 260, -1));

        /*
        mockOutLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        mockOutLabel.setText("Mock dir:");
        panel.add(mockOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 100, -1));

        mockOutText.setText("./src/java-mock/");
        mockOutText.setToolTipText("Target directory for mock implementation sources");
        panel.add(mockOutText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 260, -1));
        */

        swingOutLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        swingOutLabel.setText("Swing dir:");
        panel.add(swingOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 100, -1));

        swingOutText.setText("./src/java-swing/");
        swingOutText.setToolTipText("Target directory for java swing client sources");
        panel.add(swingOutText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, 260, -1));

    }
    // </editor-fold>//GEN-END:initComponents

    private void hibernateOutTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hibernateOutTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hibernateOutTextActionPerformed

    private void serviceOutTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceOutTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serviceOutTextActionPerformed

    private void configTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_configTextFocusLost
       JagGenerator.stateChanged(false);
    }//GEN-LAST:event_configTextFocusLost

    private void testOutTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_testOutTextFocusLost
       JagGenerator.stateChanged(false);
    }//GEN-LAST:event_testOutTextFocusLost

    private void jspOutTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jspOutTextFocusLost
       JagGenerator.stateChanged(false);
    }//GEN-LAST:event_jspOutTextFocusLost

    private void webOutTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_webOutTextFocusLost
       JagGenerator.stateChanged(false);
    }//GEN-LAST:event_webOutTextFocusLost

    private void ejbOutTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ejbOutTextFocusLost
       JagGenerator.stateChanged(false);
    }//GEN-LAST:event_ejbOutTextFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel configOutLabel;
    private javax.swing.JTextField configText;
    private javax.swing.JLabel ejbOutLabel;
    private javax.swing.JTextField ejbOutText;
    private javax.swing.JLabel hibernateOutLabel;
    public javax.swing.JTextField hibernateOutText;
    private javax.swing.JLabel jspOutLabel;
    private javax.swing.JTextField jspOutText;
    private javax.swing.JLabel mockOutLabel;
    public javax.swing.JTextField mockOutText;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel serviceOutLabel;
    private javax.swing.JTextField serviceOutText;
    private javax.swing.JLabel swingOutLabel;
    private javax.swing.JTextField swingOutText;
    private javax.swing.JLabel testNameLabel;
    private javax.swing.JTextField testOutText;
    private javax.swing.JLabel webOutLabel;
    private javax.swing.JTextField webOutText;
    // End of variables declaration//GEN-END:variables
}

