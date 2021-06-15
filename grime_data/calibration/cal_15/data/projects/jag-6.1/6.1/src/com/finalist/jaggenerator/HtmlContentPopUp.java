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
package com.finalist.jaggenerator;

import com.finalist.jaggenerator.JagGenerator;

import javax.swing.text.html.HTMLDocument;
import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.net.URL;
import java.io.IOException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * A general purpose popup window for displaying hyperlinked HTML pages.  The window may be initialised
 * with either a URL or with a String containing HTML (but a HtmlContentPopUp initialised with a String does
 * not support hyperlinks).
 * <p>
 * The HtmlContentPopup allows hyperlinks to be followed and maintains a page history, which is navigatable
 * with a back and forward button.  The back and forward buttons also may be triggered by the familiar
 * keyboard shortcuts (Backspace or ALT-left_arrow for 'back', and ALT+right_arrow for 'forward'), and
 * the popup may be dismissed with the ESCAPE key.
 * <p>
 * The page history maintained by this component includes scrollbar position information. For example if you
 * hyperlink to document#2 from the <b>end</b> of document#1, and then navigate back to document#1:  you will
 * find yourself back at the very same place in document#1 where you left it (the end, in this case).
 * <p>
 * Any hyperlinked URLs that end in <code>!!!EXTERNAL!!!</code> (<code>HtmlContentPopup.EXTERNAL_TAG</code>)
 * will attempt to launch the link in an external browser - this is defaulted to Internet Explorer (sorry!).
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class HtmlContentPopUp extends javax.swing.JDialog {
   /** A return status code - returned if Cancel button has been pressed */
   public static final int RET_CANCEL = 0;
   /** A return status code - returned if OK button has been pressed */
   public static final int RET_OK = 1;
   private String externalBrowserCommand = "\"C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE\" ";
   private final ArrayList pageHistory = new ArrayList();
   private int historyPos = 0;
   private String initialContent;
   private static final String EXTERNAL_TAG = "!!!EXTERNAL!!!";
   private static final int WIDTH = 600;
   private static final int HEIGHT = 700;


   /**
    * Creates new form HtmlAboutPopUp with the specified content.
    *
    * @param parent the parent frame.
    * @param title the title for the popup.
    * @param modal whether or not the popup is modal.
    * @param html the HTML content for the popup.
    * @param navigation set to <code>true</code> if 'back' and 'forward' navigation buttons are required.
    */
   public HtmlContentPopUp(java.awt.Frame parent, String title, boolean modal, String html, boolean navigation) {
      this(parent, title, modal, html, null, navigation);
   }

   /**
    * Creates new form HtmlAboutPopUp with the specified content.
    *
    * @param parent the parent frame.
    * @param title the title for the popup.
    * @param modal whether or not the popup is modal.
    * @param html the HTML content for the popup.
    */
   public HtmlContentPopUp(java.awt.Frame parent, String title, boolean modal, String html) {
      this(parent, title, modal, html, null, true);
   }

   /**
    * Creates new form HtmlAboutPopUp with a specified URL.
    *
    * @param parent the parent frame.
    * @param title the title for the popup.
    * @param modal whether or not the popup is modal.
    * @param url the URL of the HTML content.
    */
   public HtmlContentPopUp(java.awt.Frame parent, String title, boolean modal, URL url) {
      this(parent, title, modal, null, url, true);
   }

   private HtmlContentPopUp(java.awt.Frame parent, String title, boolean modal,
                            String html, URL url, boolean navigation) {
      super(parent, modal);
      this.initialContent = html;
      initComponents();
      if (!navigation) {
         buttonPanel.remove(navigationPanel);
         //navigationPanel.setVisible(false);
      }

      jTextPane1.addHyperlinkListener(new HyperlinkListener() {
         public void hyperlinkUpdate(HyperlinkEvent e) {
            if (initialContent == null && e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
               try {
                  URL link = e.getURL();
                  int externalTagPos = link.toString().indexOf(EXTERNAL_TAG);
                  if (externalTagPos != -1) {
                     String realLink = link.toString().substring(0, externalTagPos);
                     JagGenerator.logToConsole("Launching external browser for " + realLink);
                     Runtime.getRuntime().exec(externalBrowserCommand + realLink);
                     return;
                  }
                  int size = pageHistory.size();
                  if (historyPos == size) {
                     pageHistory.add(new Bookmark());
                  } else {
                     updateCurrentBookmark();
                     for (int i = size - 1; i > historyPos; i--) {
                        pageHistory.remove(i);
                     }
                  }
                  jTextPane1.setPage(link);
                  historyPos++;
                  forwardButton.setEnabled(false);
                  backButton.setEnabled(true);
               } catch (IOException e1) {
                  //dead link - do nothing.
               }
            }
         }
      });

      setSize(WIDTH, HEIGHT);
      Dimension screenSize = (parent == null) ?
            new JFrame().getToolkit().getScreenSize() : parent.getToolkit().getScreenSize();
      this.setLocation(
            (int) ((screenSize.getWidth() / 2) - (WIDTH / 2)),
            (int) ((screenSize.getHeight() / 2) - (HEIGHT / 2)));

      setTitle(title);
      jTextPane1.setEditable(false);
      if (html == null) {
         try {
            jTextPane1.setPage(url);
            //pageHistory.add(new Bookmark());

         } catch (IOException e) {
            html = "Bad URL: " + url;
            JagGenerator.logToConsole(html);
            jTextPane1.setText(html);
         }
      } else {
         jTextPane1.setContentType("text/html");
         jTextPane1.setText(html);

      }
      jTextPane1.setCaretPosition(0);
   }


   /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
   public int getReturnStatus() {
      return returnStatus;
   }

   public String getExternalBrowserCommand() {
      return externalBrowserCommand;
   }

   public void setExternalBrowserCommand(String externalBrowserCommand) {
      this.externalBrowserCommand = externalBrowserCommand;
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   private void initComponents() {//GEN-BEGIN:initComponents
      buttonPanel = new javax.swing.JPanel();
      navigationPanel = new javax.swing.JPanel();
      backButton = new javax.swing.JButton();
      forwardButton = new javax.swing.JButton();
      okButtonPanel = new javax.swing.JPanel();
      okButton = new javax.swing.JButton();
      jScrollPane1 = new javax.swing.JScrollPane();
      jTextPane1 = new javax.swing.JTextPane();

      setTitle("");
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            closeDialog(evt);
         }
      });

      buttonPanel.setLayout(new java.awt.BorderLayout());

      backButton.setText("<<");
      backButton.setToolTipText("back");
      backButton.setEnabled(false);
      backButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            backButtonActionPerformed(evt);
         }
      });

      navigationPanel.add(backButton);

      forwardButton.setText(">>");
      forwardButton.setToolTipText("forward");
      forwardButton.setEnabled(false);
      forwardButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            forwardButtonActionPerformed(evt);
         }
      });

      navigationPanel.add(forwardButton);

      buttonPanel.add(navigationPanel, java.awt.BorderLayout.WEST);

      okButton.setText("OK");
      okButton.setSelected(true);
      okButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            okButtonActionPerformed(evt);
         }
      });

      okButtonPanel.add(okButton);

      buttonPanel.add(okButtonPanel, java.awt.BorderLayout.EAST);

      getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

      jTextPane1.setDocument(new HTMLDocument());
      jTextPane1.setEditable(false);
      jTextPane1.setFont(new java.awt.Font("Serif", 1, 14));
      jTextPane1.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyPressed(java.awt.event.KeyEvent evt) {
            shortcutKeyPressed(evt);
         }
      });

      jScrollPane1.setViewportView(jTextPane1);

      getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

      pack();
   }//GEN-END:initComponents

   private void shortcutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_shortcutKeyPressed
      if (backButton.isEnabled() &&
            (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
            (((evt.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) == KeyEvent.ALT_DOWN_MASK) &&
            evt.getKeyCode() == KeyEvent.VK_LEFT))) {
         backButtonActionPerformed(null);
      } else if (forwardButton.isEnabled() &&
            (((evt.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) == KeyEvent.ALT_DOWN_MASK) &&
            evt.getKeyCode() == KeyEvent.VK_RIGHT)) {
         forwardButtonActionPerformed(null);
      } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
         doClose(RET_CANCEL);
      }

   }//GEN-LAST:event_shortcutKeyPressed

   private void forwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardButtonActionPerformed
      try {
         updateCurrentBookmark();
         Bookmark next = (Bookmark) pageHistory.get(++historyPos);
         gotoBookmark(next);
         forwardButton.setEnabled(historyPos != (pageHistory.size() - 1));
         backButton.setEnabled(true);
      } catch (IOException e1) {
         //dead link - do nothing.
      }
   }//GEN-LAST:event_forwardButtonActionPerformed

   private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
      try {
         if (historyPos != pageHistory.size()) {
            updateCurrentBookmark();
         } else {
            pageHistory.add(new Bookmark());
         }

         Bookmark previous = (Bookmark) pageHistory.get(--historyPos);
         gotoBookmark(previous);
         forwardButton.setEnabled(true);
         backButton.setEnabled(historyPos != 0);

      } catch (IOException e1) {
         //dead link - do nothing.
      }
   }//GEN-LAST:event_backButtonActionPerformed

   private void gotoBookmark(final Bookmark next) throws IOException {
      jTextPane1.setPage(next.url);
      waitForSetPageDone(jTextPane1);
      jScrollPane1.getViewport().setViewPosition(next.pos);
      //jTextPane1.scrollRectToVisible(next.pos);

   }

   private void updateCurrentBookmark() {
      pageHistory.remove(historyPos);
      pageHistory.add(historyPos, new Bookmark());
   }

   private void waitForSetPageDone(final JTextPane pane) {
      /* After JTextPane.setPage(url), you need to wait until page is
       * fully loaded since setPage is asynchronous and returns
       * immediately! */
      synchronized (pane) {
         pane.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void changedUpdate(DocumentEvent evt) {
               synchronized(pane) {
                  pane.notify();
               }
            }
         });
         try {
            pane.wait(1000);
         }  // release lock, wait for notify
               // unblock after 1 sec in case hung
         catch (InterruptedException ie) {
            ie.printStackTrace();
         }
      }
   }

   private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
      doClose(RET_OK);
   }//GEN-LAST:event_okButtonActionPerformed

   /** Closes the dialog */
   private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
      doClose(RET_CANCEL);
   }//GEN-LAST:event_closeDialog

   private void doClose(int retStatus) {
      returnStatus = retStatus;
      setVisible(false);
      dispose();
   }


   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton backButton;
   private javax.swing.JPanel buttonPanel;
   private javax.swing.JButton forwardButton;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JTextPane jTextPane1;
   private javax.swing.JPanel navigationPanel;
   private javax.swing.JButton okButton;
   private javax.swing.JPanel okButtonPanel;
   // End of variables declaration//GEN-END:variables

   private int returnStatus = RET_CANCEL;


   class Bookmark {
      /**
       * A Bookmark is a URL of a HTML page, along with a record of the viewport upper-left corner.
       * This enables navigation to and from <i>a particular position</i> within a page.
       */
      public Bookmark() {
         this.url = jTextPane1.getPage();
         this.pos = jScrollPane1.getViewport().getViewPosition();
      }

      private URL url;
      private Point pos;

      public String toString() {
         return pos + "@" + url.toString().substring(url.toString().lastIndexOf('/'));
      }
   }

}


