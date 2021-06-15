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
package com.jaspersoft.ireport.designer.editor;

import com.jaspersoft.ireport.designer.editor.functions.FunctionsUtils;
import bsh.ParseException;
import bsh.Parser;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.editor.functions.Function;
import com.jaspersoft.ireport.designer.editor.functions.FunctionAndExpObjectCellRenderer;
import com.jaspersoft.ireport.designer.editor.functions.FunctionPanel;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import org.jdesktop.swingx.MultiSplitLayout;


/**
 *
 * @author  gtoffoli
 */
public class ExpressionEditor2 extends javax.swing.JPanel {
    
    public static final String USER_DEFINED_EXPRESSIONS = "USER_DEFINED_EXPRESSIONS";
    public static final String PARAMETERS = "PARAMETERS";
    public static final String FIELDS = "FIELDS";
    public static final String VARIABLES = "VARIABLES";
    public static final String RECENT_EXPRESSIONS = "RECENT_EXPRESSIONS";
    public static final String WIZARDS = "WIZARDS";
    public static final String FUNCTIONS = "FUNCTIONS";
    public static final String FUNCTIONS_CATEGORY = "FUNCTIONS_CATEGORY";
    
    private ExpressionContext expressionContext = null;
    private JDialog dialog = null;
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    private boolean refreshingContext = false;
    private FunctionPanel functionPanel = null;

    private JPanel thirdColumnPanel = null;

    private static DefaultMutableTreeNode functionsNode = null;

    
    
    public String getExpression() {
        return jEditorPane1.getText();
    }

    public void setExpression(String expression) {
        jEditorPane1.setText(expression);
        checkSyntax();
    }

    public static ArrayList<String> getPredefinedExpressions() {


        ArrayList<String> exps = new ArrayList<String>();
        Preferences pref = IReportManager.getPreferences();
        if (pref.getBoolean("custom_expressions_set", false))
        {
            for (int i=0; pref.get("customexpression."+i, null) != null; ++i)
            {
                exps.add(pref.get("customexpression."+i, ""));
            }
        }
        else
        {
            exps.addAll(getDefaultPredefinedExpressions());
        }
        return exps;
    }

    public static ArrayList<String> getDefaultPredefinedExpressions() {
        return defaultExpressions;
    }

    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
        this.jEditorPane1.setExpressionContext(expressionContext);

         refreshContext();

        
    }
    
    private static java.util.ArrayList<String> recentExpressions = new java.util.ArrayList<String>();
    private static java.util.ArrayList<String> defaultExpressions = null;
    
    static {
        defaultExpressions = new java.util.ArrayList<String>();
        defaultExpressions.add("( <condition> ? exp1 : exp2 )");
        defaultExpressions.add("msg(<pattern>, <arg0>)");
        defaultExpressions.add("msg(<pattern>, <arg0>, <arg1>)");
        defaultExpressions.add("msg(<pattern>, <arg0>, <arg1>, <arg2>)");
        defaultExpressions.add("str(<key>)");
        defaultExpressions.add("((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).subDataSource(<select expression>)");
        defaultExpressions.add("((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).dataSource(<select expression>)");
    }

    
    
    /** Creates new form ExpressionEditorPanel */
    public ExpressionEditor2() {
        initComponents();
        
        jButtonApply.setVisible(false);
        jButtonCancel.setVisible(false);
        
        jTree1.setCellRenderer(new NamedIconTreeCellRenderer());
        jList2.setModel(new DefaultListModel());
        jList3.setModel(new DefaultListModel());

        
        jList2.setCellRenderer(new FunctionAndExpObjectCellRenderer(jList2));
        jList3.setCellRenderer(new MethodsListCellRenderer((jList3)));

        //jList4.setModel(new DefaultListModel());

        /*
        jEditorPane1.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                refreshTokensList();
            }

            public void removeUpdate(DocumentEvent e) {
                refreshTokensList();
            }

            public void changedUpdate(DocumentEvent e) {
                refreshTokensList();
            }
        });
        */

        thirdColumnPanel = new JPanel();
        thirdColumnPanel.setLayout(new BorderLayout());
        thirdColumnPanel.setPreferredSize(new Dimension(250,10));

        String layoutDef = "(COLUMN (LEAF name=top weight=0.4) (ROW weight=0.6 (LEAF name=list1 weight=0.25) (LEAF name=list2 weight=0.25) (LEAF name=list3 weight=0.50)))";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);

jXMultiSplitPane1.setDividerSize(10);
        jXMultiSplitPane1.getMultiSplitLayout().setModel(modelRoot);
        jXMultiSplitPane1.add(jPanelEditorArea, "top");
        jXMultiSplitPane1.add(jScrollPanecolumn1, "list1");
        jXMultiSplitPane1.add(jScrollPaneColumn2, "list2");
        jXMultiSplitPane1.add(thirdColumnPanel, "list3");

        thirdColumnPanel.add(jScrollPaneColumn3);

        jEditorPane1.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                //refreshTokensList();
                int y = 0;
                int x = 0;
                try {
                    String text = jEditorPane1.getText(0, jEditorPane1.getCaretPosition());
                    String[] lines = text.split("[\\r\\n]+");
                    y = lines.length;
                    x = lines[lines.length-1].length();
                } catch (Exception ex){}
                // Calculate caret position...
                jLabelCaretPosition.setText( "Ln " + y + ", Col " + x);
            }
        });


        jEditorPane1.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                checkSyntax();
            }

            public void removeUpdate(DocumentEvent e) {
                checkSyntax();
            }

            public void changedUpdate(DocumentEvent e) {
                checkSyntax();
            }
        } );

        refreshContext();
    }


    public void checkSyntax()
    {
        jLabelErrors.setForeground(Color.BLACK);
        jLabelErrors.setText("The expression syntax is correct");
        String exp = jEditorPane1.getText();

        while (exp.length() > 0 && Character.isWhitespace(  exp.charAt(exp.length()-1)) )
        {
            exp = exp.substring(0,exp.length()-1);
        }
        if (exp.endsWith(";"))
        {
            Point p = findCaretPosition(exp);
            jLabelErrors.setForeground(Color.red.darker());
            jLabelErrors.setText("Invalid character ';' at line: " + p.y + ", column: " + p.x);
        }

        // Replace all the $x{xxx} with a valid variable name one by one
            // to keep the width...
        String[] patterns = new String[]{"$P{", "$V{", "$F{", "$R{"};
        String[] patternNames = new String[]{"Parameter", "Variable", "Field", "Resource"};

        for (int k=0; k<patterns.length; ++k)
        {
            while (exp.indexOf(patterns[k]) >= 0)
            {
                int initialIndex = exp.indexOf(patterns[k]);
                boolean endFound = false;
                for (int index = initialIndex; index >=0 && index < exp.length(); ++index)
                {
                    char c = exp.charAt(index);
                    exp = exp.substring(0,index) + "X" + exp.substring(index+1);

                    if (c == '}')
                    {
                        endFound = true;
                        break;
                    }
                }
                if (!endFound)
                {
                    String text = jEditorPane1.getText();
                    Point p = findCaretPosition(text, initialIndex);
                    jLabelErrors.setForeground(Color.red.darker());
                    jLabelErrors.setText("Report " + patternNames[k] + " reference not closed at line: " + p.y + ", column: " + p.x);
                    return;
                }
           }
        }

        exp += ";";
        Parser parser = new Parser(new StringReader(exp));
        try {
            
//            interpreter.eval(exp);
            parser.Line();

        } catch (ParseException  ex)
        {
            bsh.Token errorToken = null;
            bsh.Token tmp = parser.getToken(0);
            for (int i=1; tmp != null; i++)
            {
                errorToken = tmp;
                tmp = tmp.next;
            }
            String message = ex.getMessage() +  "\n";
            if (errorToken != null)
            {

                Point p = findCaretPosition(exp);
                if (p.y == errorToken.beginLine && p.x == errorToken.beginColumn)
                {
                    message = "Incomplete expression.";
                }
            }
            jLabelErrors.setForeground(Color.red.darker());
            jLabelErrors.setText(message);

        } catch (Throwable  ex) {}
    }

    /**
     * Return column/line
     * @param text
     * @param position
     * @return
     */
    private Point findCaretPosition(String text)
    {
        Point p = new Point(0,1);
        String[] lines = text.split("[\\r\\n]+");
        p.y = lines.length;
        p.x = lines[lines.length-1].length();
        return p;
    }

    /**
     * Return column/line
     * @param text
     * @param position
     * @return
     */
    private Point findCaretPosition(String text, int position)
    {
        return findCaretPosition(text.substring(0,position));
    }

    /*
    public void refreshTokensList()
    {
        DefaultListModel model = (DefaultListModel)jList4.getModel();
        model.removeAllElements();

        AbstractDocument document = (AbstractDocument)jEditorPane1.getDocument();
        document.readLock();
        try {

            TokenHierarchy th = TokenHierarchy.get(document);
            TokenSequence ts = th.tokenSequence();

            int caretPos = jEditorPane1.getCaretPosition();
            model.addElement("Current position: "  + jEditorPane1.getCaretPosition());

            Token tokenAtPosition = null;
            Token previousPositionToken = null;
            String textFromLastValidToked = "";
            try {
                textFromLastValidToked = document.getText(0, caretPos);
            } catch (BadLocationException ex) {
                //Exceptions.printStackTrace(ex);
            }

            ts.moveStart();
            int pos = 0;
            while (ts.moveNext())
            {
                Token t = ts.token();
                if (t.length() <= 0) continue; // skip null tokens...
                int t_start = pos;
                int t_end = pos + t.length();
                pos = t_end;

                if (caretPos > t_start && caretPos <= t_end)
                {
                    tokenAtPosition = t;
                    model.addElement("**" + t.id().name() + " " + t.text() + " {"  + t_start + " - " + t_end + "}");
                }
                else
                {
                    model.addElement(t.id().name() + " " + t.text() + " {"  + t_start + " - " + t_end + "}");
                }
                
                if (caretPos > t_end)
                {
                    previousPositionToken = t;
                    try {
                        textFromLastValidToked = document.getText(t_end, caretPos - t_end);
                    } catch (BadLocationException ex) {
                        //Exceptions.printStackTrace(ex);
                    }
                }
            }

            if (previousPositionToken != null)
            {
                model.insertElementAt("PT: " + previousPositionToken.id().name() + " " + previousPositionToken.text(),1);
            }
            else
            {
                model.insertElementAt( "NO PT",1);
            }
            if (tokenAtPosition != null)
            {
                model.insertElementAt("CT: " + tokenAtPosition.id().name() + " " + tokenAtPosition.text(),1);
            }
            else
            {
                model.insertElementAt("NO CT",1);
            }
            
            model.insertElementAt("Pre text:" + textFromLastValidToked,1);

        } finally {
            document.readUnlock();
        }


    }
    */
    
    /**
     *  Refresh the content of the expression editor based on the current
     *  ExpressionContext.
     */
    public void refreshContext()
    {
        setRefreshingContext(true);
        jList2.removeAll();
        jList3.removeAll();
        ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).removeAllChildren();
        
        if (getExpressionContext() != null)
        {
            // Aggregate the datasets....
            if (getExpressionContext().getDatasets().size() > 0)
            {
                JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).add(new DefaultMutableTreeNode(new NamedIconItem(PARAMETERS, I18n.getString("ExpressionEditor.IconName.Parameters"), NamedIconItem.ICON_FOLDER_PARAMETERS)));
                ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).add(new DefaultMutableTreeNode(new NamedIconItem(FIELDS, I18n.getString("ExpressionEditor.IconName.Fields"), NamedIconItem.ICON_FOLDER_FIELDS)));
                ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).add(new DefaultMutableTreeNode(new NamedIconItem(VARIABLES, I18n.getString("ExpressionEditor.IconName.Variables"), NamedIconItem.ICON_FOLDER_VARIABLES)));
            }
            
            int i = 0;
            for (JRDesignCrosstab crosstab : getExpressionContext().getCrosstabs())
            {
                i++;
                String key = crosstab.getKey();
                if (key == null) key = "";
                ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).add(new DefaultMutableTreeNode(new NamedIconItem(crosstab, I18n.getString("ExpressionEditor.IconName.Crosstab") + i + ") " + key, NamedIconItem.ICON_CROSSTAB)));
            }
        }

        ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).add(getFunctionsNode());
        
        ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).add(new DefaultMutableTreeNode(new NamedIconItem(USER_DEFINED_EXPRESSIONS, I18n.getString("ExpressionEditor.IconName.UserDefinedExpr"), NamedIconItem.ICON_FOLDER_FORMULAS )));
        ((DefaultMutableTreeNode)jTree1.getModel().getRoot()).add(new DefaultMutableTreeNode(new NamedIconItem(RECENT_EXPRESSIONS, I18n.getString("ExpressionEditor.IconName.RecentExpr"), NamedIconItem.ICON_FOLDER_RECENT_EXPRESSIONS )));
        //((DefaultMutableTreeNode)jTree1.getModel().getRoot()).add(new DefaultMutableTreeNode(new NamedIconItem(WIZARDS, I18n.getString("ExpressionEditor.IconName.ExprWiz"), NamedIconItem.ICON_FOLDER_WIZARDS )));
        
        jTree1.updateUI();

        setRefreshingContext(false);
        // If there are fields, select the fields node by default
        /*
        try {
            if (dlm1.getSize() > 0)
            {
                if (((NamedIconItem)(dlm1.getElementAt(1))).getItem().equals(FIELDS))
                {
                    JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                    if (ds.getFieldsList().size() > 0)
                    {
                        jList1.setSelectedIndex(1);
                    }
                    else
                    {
                        jList1.setSelectedIndex(0);
                    }
                }
                else
                {
                    jList1.setSelectedIndex(0);
                }
            }
        } catch (Exception ex) {}       
        */
    }
    
    
    
    public String getPrintableTypeName( String type )
    {
            if (type == null) return "void";

            if (type.endsWith(";")) type = type.substring(0,type.length()-1);
    
            while (type.startsWith("["))
            {
                type = type.substring(1) + "[]";
                if (type.startsWith("[")) continue;
                if (type.startsWith("L")) type = type.substring(1);
                if (type.startsWith("Z")) type = "boolean" + type.substring(1);
                if (type.startsWith("B")) type = "byte" + type.substring(1);
                if (type.startsWith("C")) type = "char" + type.substring(1);
                if (type.startsWith("D")) type = "double" + type.substring(1);
                if (type.startsWith("F")) type = "float" + type.substring(1);
                if (type.startsWith("I")) type = "int" + type.substring(1);
                if (type.startsWith("J")) type = "long" + type.substring(1);
                if (type.startsWith("S")) type = "short" + type.substring(1);
            }
            
            if (type.startsWith("java.lang."))
            {
                type = type.substring("java.lang.".length());
                if (type.indexOf(".") > 0)
                {
                    type = "java.lang." + type;
                }
            }
            return type;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPanecolumn1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanelEditorArea = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new com.jaspersoft.ireport.designer.editor.ExpressionEditorPane();
        jPanel3 = new javax.swing.JPanel();
        jLabelErrors = new javax.swing.JLabel();
        jLabelCaretPosition = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jScrollPaneColumn2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jScrollPaneColumn3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jButtonImport = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonApply = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jXMultiSplitPane1 = new org.jdesktop.swingx.JXMultiSplitPane();

        jTree1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTree1.setRootVisible(false);
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPanecolumn1.setViewportView(jTree1);

        jPanelEditorArea.setLayout(new java.awt.BorderLayout());

        jEditorPane1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jEditorPane1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jScrollPane1.setViewportView(jEditorPane1);

        jPanelEditorArea.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabelErrors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/resources/errorhandler/accept.png"))); // NOI18N
        jLabelErrors.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jLabelErrors.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jLabelErrors, gridBagConstraints);

        jLabelCaretPosition.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jLabelCaretPosition.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(jLabelCaretPosition, gridBagConstraints);

        jButton1.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton1.text")); // NOI18N
        jButton1.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton1.toolTipText")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton2.text")); // NOI18N
        jButton2.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton2.toolTipText")); // NOI18N
        jButton2.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton3.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton3.text")); // NOI18N
        jButton3.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton3.toolTipText")); // NOI18N
        jButton3.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton4.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton4.text")); // NOI18N
        jButton4.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton4.toolTipText")); // NOI18N
        jButton4.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton5.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton5.text")); // NOI18N
        jButton5.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton6.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton6.text")); // NOI18N
        jButton6.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton7.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton7.text")); // NOI18N
        jButton7.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton7.toolTipText")); // NOI18N
        jButton7.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton8.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton8.text")); // NOI18N
        jButton8.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton8.toolTipText")); // NOI18N
        jButton8.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton9.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton9.text")); // NOI18N
        jButton9.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton9.toolTipText")); // NOI18N
        jButton9.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton10.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton10.text")); // NOI18N
        jButton10.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton11.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton11.text")); // NOI18N
        jButton11.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jButton12.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton12.text")); // NOI18N
        jButton12.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton12.toolTipText")); // NOI18N
        jButton12.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton13.text")); // NOI18N
        jButton13.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton13.toolTipText")); // NOI18N
        jButton13.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton14.text")); // NOI18N
        jButton14.setToolTipText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButton14.toolTipText")); // NOI18N
        jButton14.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jButton1)
                .add(2, 2, 2)
                .add(jButton2)
                .add(2, 2, 2)
                .add(jButton3)
                .add(2, 2, 2)
                .add(jButton4)
                .add(2, 2, 2)
                .add(jButton5)
                .add(2, 2, 2)
                .add(jButton6)
                .add(2, 2, 2)
                .add(jButton7)
                .add(2, 2, 2)
                .add(jButton8)
                .add(2, 2, 2)
                .add(jButton9)
                .add(2, 2, 2)
                .add(jButton10)
                .add(2, 2, 2)
                .add(jButton11)
                .add(2, 2, 2)
                .add(jButton12)
                .add(2, 2, 2)
                .add(jButton13)
                .add(2, 2, 2)
                .add(jButton14))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jButton1)
                .add(jButton2)
                .add(jButton3)
                .add(jButton4)
                .add(jButton7)
                .add(jButton8)
                .add(jButton9)
                .add(jButton10)
                .add(jButton11)
                .add(jButton12)
                .add(jButton13)
                .add(jButton5)
                .add(jButton6)
                .add(jButton14))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(jPanel4, gridBagConstraints);

        jPanelEditorArea.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jList2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPaneColumn2.setViewportView(jList2);

        jList3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList3MouseClicked(evt);
            }
        });
        jScrollPaneColumn3.setViewportView(jList3);

        setPreferredSize(new java.awt.Dimension(750, 650));

        jButtonImport.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButtonImport.text")); // NOI18N
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });

        jButtonExport.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButtonExport.text")); // NOI18N
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        jButtonCancel.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButtonCancel.text")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonApply.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jButtonApply.text")); // NOI18N
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ExpressionEditor2.class, "ExpressionEditor2.jLabel1.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jXMultiSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jButtonImport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonExport)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 440, Short.MAX_VALUE)
                        .add(jButtonApply)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonCancel))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jXMultiSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonImport)
                    .add(jButtonExport)
                    .add(jButtonCancel)
                    .add(jButtonApply))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged

        if (isRefreshingContext()) return;

        DefaultListModel dlm = (DefaultListModel)jList3.getModel();
        dlm.removeAllElements();
        
        Class clazz = null; //getSelectedObjectClass();

        if (jList2.getSelectedValue() instanceof ExpObject)
        {




            //if (jSplitPane3.getRightComponent() != jScrollPaneColumn2)
            //{
            //    jSplitPane3.setRightComponent(jScrollPaneColumn2);
            //    jSplitPane3.updateUI();
            //}

            
            Component c = thirdColumnPanel.getComponent(0);

            if (c != jScrollPaneColumn3)
            {
                thirdColumnPanel.removeAll();
                thirdColumnPanel.add(jScrollPaneColumn3, BorderLayout.CENTER);
                thirdColumnPanel.updateUI();
            }


            try {
                clazz = IReportManager.getReportClassLoader().loadClass( ((ExpObject)jList2.getSelectedValue()).getClassType());
        
            } 
            catch (Throwable ex3)
            {
                
            }


            if (clazz != null)
            {
                java.lang.reflect.Method[] methods = clazz.getMethods();
                for (int i=0; i<methods.length; ++i)
                {
                    if ((methods[i].getModifiers() & java.lang.reflect.Modifier.PUBLIC) != 0 )
                    {
                        String method_firm = methods[i].getName() + "(";
                        Class[] params = methods[i].getParameterTypes();
                        int j=0;
                        for (j=0; j<params.length; ++j)
                        {

                            if (j > 0) method_firm +=", ";
                            else method_firm +=" ";
                            method_firm +=  getPrintableTypeName( params[j].getName() );
                        }
                        if (j>0) method_firm+=" ";
                        method_firm += ") ";

                        String rname = methods[i].getReturnType().getName();
                        if (rname.equals("void")) continue; // we have to return something always!
                        method_firm += getPrintableTypeName( rname);
                        dlm.addElement( method_firm );
                    }
                }
            }
        }
        else if (jList2.getSelectedValue() instanceof Function)
        {

            Component c = thirdColumnPanel.getComponent(0);

            if (c != functionPanel || functionPanel == null)
            {
                if (functionPanel == null)
                {
                    functionPanel = new FunctionPanel();
                }

                thirdColumnPanel.removeAll();
                thirdColumnPanel.add(functionPanel, BorderLayout.CENTER);
                thirdColumnPanel.updateUI();
            }

//            if (jSplitPane3.getRightComponent() != functionPanel || functionPanel == null)
//            {
//                if (functionPanel == null)
//                {
//                    functionPanel = new FunctionPanel();
//                }
//                jSplitPane3.setRightComponent(functionPanel);
//                jSplitPane3.updateUI();
//            }

            functionPanel.setFunction( (Function)jList2.getSelectedValue() );

        }
        
        
        
    }//GEN-LAST:event_jList2ValueChanged

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
       
        if (dialog != null && dialog.isVisible()) 
        {
            dialogResult = JOptionPane.CANCEL_OPTION;
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jList3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList3MouseClicked
       if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2)
        {
            if (jList2.getSelectedValue() != null && jList3.getSelectedValue() != null)
            {
                try {
                    
                    String objName = "";
                    if (jList2.getSelectedValue() instanceof ExpObject)
                    {
                        objName = ((ExpObject)jList2.getSelectedValue()).getExpression();
                    }
                    else
                    {
                        objName = ""+jList2.getSelectedValue();
                    }
                    
                    String method = (jList3.getSelectedValue()+"");
                    method = method.substring(0, method.lastIndexOf(")")+1);
                    // Remove selected text...
                    jEditorPane1.replaceSelection(objName+"."+method);
            } catch (Exception ex){}
            }
        }
    }//GEN-LAST:event_jList3MouseClicked

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2)
        {
            try {
                    String objName = "";
                    if (jList2.getSelectedValue() instanceof ExpObject)
                    {
                        objName = ((ExpObject)jList2.getSelectedValue()).getExpression();
                    }
                    else
                    {
                        objName = ""+jList2.getSelectedValue();
                    }
                    
                    jEditorPane1.replaceSelection(objName+"");
             } catch (Exception ex){}
        }
    }//GEN-LAST:event_jList2MouseClicked

    private void jButtonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed

        if (dialog != null && dialog.isVisible())
        {
            // Add the expression to the recent expressions list...
            String exp = getExpression();
            recentExpressions.remove(exp);
            recentExpressions.add(0,exp);

            dialogResult = JOptionPane.OK_OPTION;
            dialog.setVisible(false);
            dialog.dispose();
        }
        
    }//GEN-LAST:event_jButtonApplyActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed
        String expression = Misc.loadExpression(this);

        if (expression != null) {
            jEditorPane1.setText(expression);
        }
    }//GEN-LAST:event_jButtonImportActionPerformed

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed
        Misc.saveExpression( jEditorPane1.getText(), this );
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jEditorPane1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jEditorPane1CaretPositionChanged

    }//GEN-LAST:event_jEditorPane1CaretPositionChanged

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged

        Object obj = jTree1.getSelectionPath().getLastPathComponent();
        if (obj != null && obj instanceof DefaultMutableTreeNode &&
            ((DefaultMutableTreeNode)obj).getUserObject() instanceof NamedIconItem)
        {

            NamedIconItem item = (NamedIconItem) ((DefaultMutableTreeNode)obj).getUserObject();

            if (isRefreshingContext()) return;

            setRefreshingContext(true);
            DefaultListModel dlm2 = new DefaultListModel(); //(DefaultListModel)jList2.getModel();
            DefaultListModel dlm3 = (DefaultListModel)jList3.getModel();

            dlm2.removeAllElements();
            dlm3.removeAllElements();

            if (item != null) {
                if (item.getItem() instanceof Tag && ((Tag)item.getItem()).getValue() instanceof String)
                {
                    List<Function> exps = FunctionsUtils.getFunctionsByCategory( (String)((Tag)item.getItem()).getValue() );
                    for (Function s : exps) dlm2.addElement(s);
                }
                else if (item.getItem().equals( USER_DEFINED_EXPRESSIONS)) {
                    ArrayList<String> exps = getPredefinedExpressions();
                    for (String s : exps) dlm2.addElement(s);
                } else if (item.getItem().equals( RECENT_EXPRESSIONS)) {
                    for (String s : recentExpressions) dlm2.addElement(s);
                } else if (item.getItem().equals( PARAMETERS)) {
                    JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                    Iterator parameters = ds.getParametersList().iterator();
                    while (parameters.hasNext()) {
                        dlm2.addElement(new ExpObject(parameters.next()));
                    }
                } else if (item.getItem().equals( FIELDS)) {
                    JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                    Iterator fields = ds.getFieldsList().iterator();
                    while (fields.hasNext()) {
                        ExpObject eo = new ExpObject(fields.next());
                        dlm2.addElement(eo);
                    }
                } else if (item.getItem().equals( VARIABLES)) {
                    JRDesignDataset ds = getExpressionContext().getDatasets().get(0);
                    Iterator variables = ds.getVariablesList().iterator();
                    while (variables.hasNext()) {
                        dlm2.addElement(new ExpObject(variables.next()));
                    }
                } else if (item.getItem() instanceof JRDesignCrosstab) {
                    JRDesignCrosstab crosstab = (JRDesignCrosstab)item.getItem();
                    List rowGroups = crosstab.getRowGroupsList();
                    List columnGroups = crosstab.getColumnGroupsList();

                    Iterator measures = crosstab.getMesuresList().iterator();
                    while (measures.hasNext()) {
                        JRDesignCrosstabMeasure measure = (JRDesignCrosstabMeasure)measures.next();
                        dlm2.addElement(new ExpObject(measure.getVariable()));

                        for (int i=0; i<rowGroups.size(); ++i) {
                            JRDesignCrosstabRowGroup rowGroup = (JRDesignCrosstabRowGroup)rowGroups.get(i);
                            dlm2.addElement(new CrosstabTotalVariable(measure, rowGroup, null));


                            for (int j=0; j<columnGroups.size(); ++j) {
                                JRDesignCrosstabColumnGroup columnGroup = (JRDesignCrosstabColumnGroup)columnGroups.get(j);
                                if (j==0) {
                                    dlm2.addElement(new CrosstabTotalVariable(measure, null, columnGroup));
                                }

                                dlm2.addElement(new CrosstabTotalVariable(measure, rowGroup, columnGroup));
                            }
                        }
                    }

                    for (int i=0; i<rowGroups.size(); ++i) {
                        JRDesignCrosstabRowGroup rowGroup = (JRDesignCrosstabRowGroup)rowGroups.get(i);
                        dlm2.addElement(new ExpObject(rowGroup.getVariable()));
                    }

                    for (int i=0; i<columnGroups.size(); ++i) {
                        JRDesignCrosstabColumnGroup columnGroup = (JRDesignCrosstabColumnGroup)columnGroups.get(i);
                        dlm2.addElement(new ExpObject(columnGroup.getVariable()));
                    }

                    List crosstabParameters = crosstab.getParametersList();
                    for (int i=0; i<crosstabParameters.size(); ++i) {
                        JRDesignCrosstabParameter parameter = (JRDesignCrosstabParameter)crosstabParameters.get(i);
                        dlm2.addElement(new ExpObject(parameter));
                    }

                }
                // TODO -> Wizards
                jList2.setModel(dlm2);

                setRefreshingContext(false);

                if (dlm2.size() > 0) {
                    jList2.setSelectedIndex(0);
                }

            }



        }

    }//GEN-LAST:event_jTree1ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jButton14ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonImport;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCaretPosition;
    private javax.swing.JLabel jLabelErrors;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelEditorArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneColumn2;
    private javax.swing.JScrollPane jScrollPaneColumn3;
    private javax.swing.JScrollPane jScrollPanecolumn1;
    private javax.swing.JTree jTree1;
    private org.jdesktop.swingx.JXMultiSplitPane jXMultiSplitPane1;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Show the dialog to edit the expression.
     * If parent is not null, it is used to find a parent Window.
     */
    public int showDialog(Component parent)
    {
        jButtonApply.setVisible(true);
        jButtonCancel.setVisible(true);
        
        Window pWin = (parent != null) ? SwingUtilities.windowForComponent(parent) : null;
        
        if (pWin instanceof Dialog) dialog = new JDialog((Dialog)pWin);
        else if (pWin instanceof Frame) dialog = new JDialog((Frame)pWin);
        else dialog = new JDialog();
        
        dialog.setModal(true);
        

        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };

        dialog.getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        dialog.getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);


        //to make the default button ...
        dialog.getRootPane().setDefaultButton(this.jButtonApply);

        dialog.getContentPane().add(this);
        dialog.pack();
        dialogResult = JOptionPane.CANCEL_OPTION;
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setTitle(I18n.getString("ExpressionEditor.Title.ExpressionEditor"));
        dialog.setVisible(true);

        return dialogResult;
    }
 
    @Override
    public void addNotify () {
        super.addNotify();
        //force focus to the editable area
        if (isEnabled() && isFocusable()) {
            jEditorPane1.requestFocus();
        }
    }

    /**
     * @return the refreshingContext
     */
    public boolean isRefreshingContext() {
        return refreshingContext;
    }

    /**
     * @param refreshingContext the refreshingContext to set
     */
    public void setRefreshingContext(boolean refreshingContext) {
        this.refreshingContext = refreshingContext;
    }




    public DefaultMutableTreeNode getFunctionsNode()
    {
        if (functionsNode == null)
        {
            functionsNode = new DefaultMutableTreeNode(new NamedIconItem(FUNCTIONS, "Built-in functions", NamedIconItem.ICON_FOLDER_FORMULAS ));

            // Add the function categories to this node...
            List<Tag> categories = FunctionsUtils.getFunctionCategories();

           for (Tag t : categories)
           {
               functionsNode.add(new DefaultMutableTreeNode(new NamedIconItem(t, ""+t, NamedIconItem.ICON_FOLDER_FORMULAS )));
           }
        }
        return functionsNode;
    }
}

