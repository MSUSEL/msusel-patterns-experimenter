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

import com.finalist.jag.util.TemplateString;
import com.finalist.jaggenerator.JagGenerator;
import com.finalist.jaggenerator.SelectTablesDialog;
import com.finalist.jaggenerator.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author hillie
 */
public class Entity extends DefaultMutableTreeNode implements JagBean, ActionListener {
    private boolean relationsEnabled;
    static Log log = LogFactory.getLog(JagGenerator.class);

    /**
     * Creates new form BeanForm
     */
    public Entity(String rootPackage, String tableName, String pKey) {
        initComponents();
        String tableClassName = Utils.toClassName(tableName);
        rootPackageText.setText(rootPackage + ".entity." + tableClassName.toLowerCase());
        tableNameText.setText(tableName);
        nameText.setText(tableClassName);
        descriptionText.setText(tableClassName);
        refNameText.setText(tableClassName);
        pKeyText.setText(pKey);
        JagGenerator.addEntity(getRefName(), this);
        //a little sequencing hack.. the last-created relation can still be initialising itself when
        //this point is reached - so we pause here a little while: otherwise we'll notify before it begins waiting!
        synchronized (this) {
            try {
                wait(100);
            } catch (InterruptedException e) {
            }
        }
        notifyRelationsThatConstructionIsFinished();
        SelectTablesDialog.getAlreadyselected().add(tableName);
    }

    public Entity(Element el) {
        relationsEnabled = !JagGenerator.isRelationsEnabled();
        initComponents();
        NodeList nl = el.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == n.ELEMENT_NODE) {
                Element child = (Element) n;
                String attName = child.getAttribute("name");
                try {
                    Node first = child.getFirstChild();
                    if (first == null) continue;
                    String value = first.getNodeValue();
                    if (value != null) {
                        if (attName.equalsIgnoreCase("name")) {
                            nameText.setText(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("display-name")) {
                            displayNameText.setText(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("description")) {
                            descriptionText.setText(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("root-package")) {
                            rootPackageText.setText(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("table-name")) {
                            tableNameText.setText(value);
                            SelectTablesDialog.getAlreadyselected().add(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("primary-key")) {
                            pKeyText.setText(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("primary-key-type")) {
                            pKeyTypeText.setText(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("is-composite")) {
                            isCompositeCombo.setSelectedItem(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("is-association")) {
                            isAssociationEntity.setSelectedItem(value);
                            continue;
                        }
                        if (attName.equalsIgnoreCase("field")) {
                            add(new Field(this, child));
                            continue;
                        }
                        if (attName.equalsIgnoreCase("relation")) {
                            if (warningNeeded()) {
                                JOptionPane.showMessageDialog(getPanel(),
                                        "The application file you have opened contains relations, but you have disabled relations support. \n" +
                                                "If you later save this file or generate an application from this file, the original relations information will be lost.\n" +
                                                "\n" +
                                                "To avoid this, either enable relations support (Options->Enable relations) and re-open the file, or save the file under a different name.",
                                        "Container-managed relations disabled!",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else if (relationsEnabled) {
                                add(new Relation(this, child));
                                continue;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JagGenerator.logToConsole("Error constructing " + this.toString() + ": " + e);
                }
            }
        }
        nl = el.getElementsByTagName("ref-name");
        if (nl.getLength() > 0) {
            Node node = nl.item(0).getFirstChild();
            if (node != null) {
                refNameText.setText(node.getNodeValue());
            }
        }

        JagGenerator.addEntity(getRefName(), this);

        //a little sequencing hack.. the last-created relation can still be initialising itself when
        //this point is reached - so we pause here a little while: otherwise we'll notify before it begins waiting!
        synchronized (this) {
            try {
                wait(100);
            } catch (InterruptedException e) {
            }
        }
        notifyRelationsThatConstructionIsFinished();
    }


    public JPanel getPanel() {
        return panel;
    }

    public String getRefName() {
        return refNameText.getText();
    }

    public void setRefName(String text) {
        refNameText.setText(text);
    }


    public void setPKeyType(String pKeyType) {
        pKeyTypeText.setText(pKeyType);
    }

    public void getXML(Element el) throws ParserConfigurationException {
        Document doc = el.getOwnerDocument();
        Element module = doc.createElement("module");
        module.setAttribute("name", "entity");

        Element name = doc.createElement("module-data");
        name.setAttribute("name", "name");
        if (nameText.getText() != null) {
            name.appendChild(doc.createTextNode(nameText.getText()));
        }
        module.appendChild(name);

        Element displayname = doc.createElement("module-data");
        displayname.setAttribute("name", "display-name");
        if (displayNameText.getText() != null) {
            displayname.appendChild(doc.createTextNode(displayNameText.getText()));
        }
        module.appendChild(displayname);


        Element description = doc.createElement("module-data");
        description.setAttribute("name", "description");
        if (descriptionText.getText() != null) {
            description.appendChild(doc.createTextNode(descriptionText.getText()));
        }
        module.appendChild(description);

        Element rootPackage = doc.createElement("module-data");
        rootPackage.setAttribute("name", "root-package");
        if (rootPackageText.getText() != null) {
            rootPackage.appendChild(doc.createTextNode(rootPackageText.getText()));
        }
        module.appendChild(rootPackage);

        Element tableName = doc.createElement("module-data");
        tableName.setAttribute("name", "table-name");
        if (tableNameText.getText() != null) {
            tableName.appendChild(doc.createTextNode(tableNameText.getText()));
        }
        module.appendChild(tableName);

        Element pKey = doc.createElement("module-data");
        pKey.setAttribute("name", "primary-key");
        pKey.appendChild(doc.createTextNode((isCompositeKey() ? "" : pKeyText.getText())));
        module.appendChild(pKey);

        Element pKeyType = doc.createElement("module-data");
        pKeyType.setAttribute("name", "primary-key-type");
        if (pKeyTypeText.getText() != null) {
            pKeyType.appendChild(doc.createTextNode(pKeyTypeText.getText()));
        }

        module.appendChild(pKeyType);
        Element isComposite = doc.createElement("module-data");
        isComposite.setAttribute("name", "is-composite");
        isComposite.appendChild(doc.createTextNode((String) isCompositeCombo.getSelectedItem()));
        module.appendChild(isComposite);


        Element isAssociation = doc.createElement("module-data");
        isAssociation.setAttribute("name", "is-association");
        isAssociation.appendChild(doc.createTextNode((String) isAssociationEntity.getSelectedItem()));
        module.appendChild(isAssociation);

        Element refName = doc.createElement("ref-name");
        if (refNameText.getText() != null) {
            refName.appendChild(doc.createTextNode(refNameText.getText()));
        }
        module.appendChild(refName);

        //..and finally getXML() from the children: fields and relations
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            child.getXML(module);
        }

        el.appendChild(module);
    }

    public TemplateString getName() {
        return new TemplateString(nameText.getText());
    }

    public void setName(String text) {
        nameText.setText(text);
    }

    /**
     * Display name is used to determine which field to render in drop downl lists.
     * if not set, the primary key is used.
     * @return the name of the field to use to display an entity in a dropdown list.
     */
    public TemplateString getDisplayName() {
        if (displayNameText.getText() == null || "".equals(displayNameText.getText())) {
            if (getPrimaryKey() == null) {
                return new TemplateString("");
            } else {
                return new TemplateString(getPrimaryKey().toString());
            }
        }
        return new TemplateString(displayNameText.getText());
    }

    public void setDisplayName(String text) {
        displayNameText.setText(text);
    }

    public TemplateString getDescription() {
        return new TemplateString(descriptionText.getText());
    }

    public void setDescription(String text) {
        descriptionText.setText(text);
    }

    public TemplateString getRootPackage() {
        return new TemplateString(rootPackageText.getText());
    }

    public void setRootPackage(String text) {
        rootPackageText.setText(text);
    }


    public Field getPrimaryKey() {
        return isCompositeKey() ? null : (Field) getPkFields().get(0);
    }

    /**
     * Called by a field when it has been selected as primary key in the GUI.
     *
     * @param field
     */
    public void setPrimaryKey(Field field) {
        if ("false".equals(isCompositeCombo.getSelectedItem())) {
            if ("".equals(pKeyText.getText())) {
                pKeyText.setText(field.getName().toString());
                pKeyTypeText.setText(field.getType().toString());

            } else {
                isCompositeCombo.setSelectedItem("true");
                pKeyText.setText("");
                pKeyTypeText.setText(rootPackageText.getText() + '.' + getName() + "PK");
            }
        }
    }

    /**
     * Called by a field when it has been de-selected as primary key in the GUI.
     *
     * @param field
     */
    public void unsetPrimaryKey(Field field) {
        if ("false".equals(isCompositeCombo.getSelectedItem())) {
            pKeyText.setText("");
            pKeyTypeText.setText("");

        } else {
            List pkFields = getPkFields();
            if (pkFields.size() == 1) {
                isCompositeCombo.setSelectedItem("false");
                pKeyText.setText(((Field) pkFields.get(0)).getName().toString());
                pKeyTypeText.setText(((Field) pkFields.get(0)).getType().toString());
            }
        }
    }

    public TemplateString getPrimaryKeyType() {
        return new TemplateString(pKeyTypeText.getText());
    }

    public TemplateString getPrimaryKeyName() {
        return new TemplateString(pKeyText.getText());
    }

    public String getIsComposite() {
        return (String) isCompositeCombo.getSelectedItem();
    }

    public void setIsComposite(String composite) {
        isCompositeCombo.setSelectedItem(composite);
    }

    public String getIsAssociationEntity() {
        return (String) isAssociationEntity.getSelectedItem();
    }

    public void setIsAssociationEntity(String associationEntity) {
        isAssociationEntity.setSelectedItem(associationEntity);
    }

    public String getRootPath() {
        return getRootPackage().toString().replace('.', '/');
    }

    public List getRelations() {
        ArrayList relations = new ArrayList();
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            if (child instanceof Relation) {
                relations.add(child);
            }
        }
        return relations;
    }

    /**
     * Get a list of all Relations to this entity that are not assocation relations.
     *
     * @return list with non-assocation relations.
     */
    public List getEntitiesRelations() {
        ArrayList result = new ArrayList();
        List entities = JagGenerator.getObjectsFromTree(Entity.class);
        for (int i = 0; i < entities.size(); i++) {
            Entity relatedEntity = (Entity) entities.get(i);
            List relations = relatedEntity.getRelations();
            for (int j = 0; j < relations.size(); j++) {
                Relation rel = (Relation) relations.get(j);
                Entity en = rel.getRelatedEntity();
                // Don't add assocation entities.
                if ("false".equals(relatedEntity.getIsAssociationEntity()) && en.getName().equals(this.getName())) {
                    result.add(rel);
                }
            }
        }
        return result;
    }

    /**
     * Get a list of all assocation Relations to this entity.
     *
     * @return list with relations.
     */
    public List getEntitiesAssocationRelations() {
        ArrayList result = new ArrayList();
        List entities = JagGenerator.getObjectsFromTree(Entity.class);
        for (int i = 0; i < entities.size(); i++) {
            Entity relatedEntity = (Entity) entities.get(i);
            List relations = relatedEntity.getRelations();
            for (int j = 0; j < relations.size(); j++) {
                Relation rel = (Relation) relations.get(j);
                // Only add relations that are association entities.
                Entity en = rel.getRelatedEntity();
                if (en.getName().equals(this.getName())) {
                    if ("true".equals(relatedEntity.getIsAssociationEntity())) {
                        result.add(rel);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get the Entity that is associated
     *
     * @param relationName
     * @return description
     */
    public Entity getAssocationEntity(String relationName) {
        //
        return null;
    }

    public List getRelatedEntities() {
        ArrayList relatedEntities = new ArrayList();
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            if (child instanceof Relation) {
                relatedEntities.add(((Relation) child).getRelatedEntity());
            }
        }
        return relatedEntities;
    }

    /**
     * Gets the set of field names within this entity, which are used as relations to other CMR-related entity beans.
     *
     * @return a Set of TemplateString objects.
     */
    public List getRelationFieldNames() {
        ArrayList relationFieldNames = new ArrayList();
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            if (child instanceof Relation) {
                relationFieldNames.add(((Relation) child).getFieldName());
            }
        }
        return relationFieldNames;
    }

    public boolean getHasRelations() {
        return !getRelations().isEmpty();
    }

    public List getFields() {
        ArrayList fields = new ArrayList();
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            if (child instanceof Field) {
                fields.add(child);
            }
        }
        return fields;
    }

    public List getNonFkFields() {
        ArrayList fields = new ArrayList();
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            if (child instanceof Field &&
                    !((Field) child).isForeignKey()) {
                fields.add(child);
            }
        }
        return fields;
    }

    public List getFkFields() {
        ArrayList fields = new ArrayList();
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            if (child instanceof Field &&
                    ((Field) child).isForeignKey()) {
                fields.add(child);
            }
        }
        return fields;
    }


    public List getNonRelationFields() {
        ArrayList fields = new ArrayList();
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            if (child instanceof Field &&
                    (((Field) child).getRelation() == null)) {
                fields.add(child);
            }
        }
        return fields;
    }

    public List getPkFields() {
        ArrayList fields = new ArrayList();
        Enumeration children = children();
        while (children.hasMoreElements()) {
            JagBean child = (JagBean) children.nextElement();
            if (child instanceof Field &&
                    ((Field) child).isPrimaryKey()) {
                fields.add(child);
            }
        }
        return fields;
    }

    public List getNonPkFields() {
        List nonPkFields = getFields();
        nonPkFields.removeAll(getPkFields());
        return nonPkFields;
    }

    /**
     * Gets the fields that are neither primary keys, nor a foreign key involved in a container-managed relation.
     *
     * @return List
     */
    public List getNonPkRelationFields() {
        List nonPkRelationFields = getNonRelationFields();
        nonPkRelationFields.removeAll(getPkFields());
        return nonPkRelationFields;
    }

    /**
     * Iterates over all the table columns and determines whether the table has a single key or a composite key. The
     * key is said to be composite when more than one column is part of the key.
     *
     * @return <code>true</code> only when more the table has a composite key else <code>false</code>
     */
    public boolean isCompositeKey() {
        int countPrimaryKeys = countPrimaryKeyFields();
        return countPrimaryKeys > 1;
    }

    /**
     * Count the number of primary key fields.
     *
     * @return <code>int</code> with the number of primary keys
     */
    public int countPrimaryKeyFields() {
        Enumeration children = children();
        JagBean bean = null;
        Field field = null;
        int countPrimaryKeys = 0;
        while (children.hasMoreElements()) {
            bean = (JagBean) children.nextElement();
            if (bean instanceof Field) {
                field = (Field) bean;
                if (field.isPrimaryKey()) countPrimaryKeys++;
            }
        }
        return countPrimaryKeys;
    }

    /**
     * Returns the class that represents the primary key type
     *
     * @return the class that represents the primary key type
     */
    public String getPrimaryKeyClass() {
        Enumeration children = children();
        JagBean bean = null;
        Field field = null;
        String getPrimaryKeyClass = null;
        while (children.hasMoreElements()) {
            bean = (JagBean) children.nextElement();
            if (bean instanceof Field) {
                field = (Field) bean;
                if (field.isPrimaryKey()) getPrimaryKeyClass = field.getType();
            }
        }
        return getPrimaryKeyClass;
    }

    /**
     * Returns the First primary key field
     *
     * @return the field name of the first primary key field or an empty string if nothing was found.
     */
    public String getFirstPrimaryKeyFieldName() {
        Enumeration children = children();
        JagBean bean = null;
        Field field = null;
        String getPrimaryKeyFieldName = "";
        while (children.hasMoreElements()) {
            bean = (JagBean) children.nextElement();
            if (bean instanceof Field) {
                field = (Field) bean;
                if (field.isPrimaryKey()) getPrimaryKeyFieldName = field.getName().toString();
            }
        }
        return getPrimaryKeyFieldName;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("GET_PRIMARY_KEY")) {
            String key = null;
            if (isCompositeKey())
                key = rootPackageText.getText() + "." + nameText.getText() + "PK";
            else
                key = getPrimaryKeyClass();
            pKeyTypeText.setText(key);
        }
    }

    /**
     * Gets the name of the table represented by this Entity.
     *
     * @return the table name.
     */
    public TemplateString getLocalTableName() {
        return new TemplateString(tableNameText.getText());
    }

    public void setTableName(String table) {
        tableNameText.setText(table);
    }

    public String getTableName() {
        return tableNameText.getText();
    }

    /**
     * While an entity is being created, its constituent relations have to wait for the entity to finish being
     * constructed before they can finish initialising themselves.  Call this method when the Entity is ready.
     */
    public void notifyRelationsThatConstructionIsFinished() {
        Iterator relations = getRelations().iterator();
        while (relations.hasNext()) {
            Relation relation = (Relation) relations.next();
            relation.notifyLocalEntityIsComplete();
        }
    }

    /**
     * When a field name is changed, call this to tell all relations to update their lists.
     *
     * @param oldName
     * @param text
     */
    public void notifyRelationsThatFieldNameChanged(String oldName, String text) {
        Iterator relations = getRelations().iterator();
        while (relations.hasNext()) {
            Relation relation = (Relation) relations.next();
            relation.notifyFieldNameChanged(oldName, text);
        }

    }

    public String toString() {
        return "Entity - " + getRefName();
    }

    /**
     * Inserts a relation in the correct position (relations are always positioned first
     * amongst an Entity's children, because it looks nicer!).
     *
     * @param relation
     */
    public void addRelation(Relation relation) {
        TreeNode lastRelly = null;
        Enumeration kids = children();
        while (kids.hasMoreElements()) {
            TreeNode kid = (TreeNode) kids.nextElement();
            if (kid instanceof Relation) {
                lastRelly = kid;
            }
        }
        int insertPos = 0;
        if (lastRelly == null) {
            insertPos = getIndex(getFirstChild());
        } else {
            insertPos = getIndex(lastRelly) + 1;
        }
        insert(relation, insertPos);
    }


   /**
    * Check if one of the fields needs a sequence.
    * If so, return true.
    * @return true if a sequence is needed.
    */
    public boolean isSequenceEntity() {
       List fields = getFields();
       for (int i=0; i < getFields().size(); i++) {
         Field field = (Field) fields.get(i);
          if (field.isSequenceField()) {
             return true;
          }
       }
       return false;
    }
   /**
    * Get a session that contains this Entity.
    * @return Session
    */
   public Session getFirstSession() {
      List services = JagGenerator.getObjectsFromTree(Session.class);
      for (int i=0; i<services.size(); i++) {
         Session s = (Session) services.get(i);
         if (s.getEntities().contains(this)) {
            return s;
         }
      }
      System.out.println("No session found for entity:" + getName());
      return null;
   }

    private boolean warningNeeded() {
        if (relationsEnabled != JagGenerator.isRelationsEnabled()) {
            relationsEnabled = JagGenerator.isRelationsEnabled();
            return !relationsEnabled;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        panel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        tableNameLabel = new javax.swing.JLabel();
        pKeyLabel = new javax.swing.JLabel();
        pKeyTypeLabel = new javax.swing.JLabel();
        desciptionLabel = new javax.swing.JLabel();
        rootPackageLabel = new javax.swing.JLabel();
        refNameLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        tableNameText = new javax.swing.JTextField();
        pKeyText = new javax.swing.JTextField();
        pKeyTypeText = new javax.swing.JTextField();
        descriptionText = new javax.swing.JTextField();
        rootPackageText = new javax.swing.JTextField();
        refNameText = new javax.swing.JTextField();
        isCompositeLabel = new javax.swing.JLabel();
        isCompositeCombo = new javax.swing.JComboBox();
        nameLabel1 = new javax.swing.JLabel();
        displayNameText = new javax.swing.JTextField();
        isCompositeLabel1 = new javax.swing.JLabel();
        isAssociationEntity = new javax.swing.JComboBox();

        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameLabel.setText("Name: ");
        nameLabel.setToolTipText("Name of the entity");
        panel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 90, -1));

        tableNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        tableNameLabel.setText("Table name: ");
        tableNameLabel.setToolTipText("Physical table name the entity is mapped to");
        panel.add(tableNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, -1));

        pKeyLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        pKeyLabel.setText("Primary key: ");
        pKeyLabel.setToolTipText("Primary key field of the entity");
        panel.add(pKeyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, -1));

        pKeyTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        pKeyTypeLabel.setText("Primary key class: ");
        pKeyTypeLabel.setToolTipText("Primary key class in case of a composite key");
        panel.add(pKeyTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 110, -1));

        desciptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        desciptionLabel.setText("Description: ");
        desciptionLabel.setToolTipText("Description of the entity");
        panel.add(desciptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 90, -1));

        rootPackageLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        rootPackageLabel.setText("Root-package: ");
        rootPackageLabel.setToolTipText("Root package for the entity");
        panel.add(rootPackageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 90, -1));

        refNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        refNameLabel.setText("Ref-name: ");
        refNameLabel.setToolTipText("Reference name for the entity");
        panel.add(refNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 90, -1));

        nameText.setToolTipText("Name of the entity");
        nameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextActionPerformed(evt);
            }
        });
        nameText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextFocusLost(evt);
            }
        });

        panel.add(nameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 260, -1));

        tableNameText.setToolTipText("Physical table name the entity is mapped to");
        tableNameText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tableNameTextFocusLost(evt);
            }
        });

        panel.add(tableNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 260, -1));

        pKeyText.setToolTipText("Primary key field of the entity");
        pKeyText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pKeyTextFocusLost(evt);
            }
        });

        panel.add(pKeyText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 260, -1));

        pKeyTypeText.setToolTipText("Primary key class in case of a composite key");
        pKeyTypeText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pKeyTypeTextFocusLost(evt);
            }
        });

        panel.add(pKeyTypeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 260, -1));

        descriptionText.setToolTipText("Description of the entity");
        descriptionText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                descriptionTextFocusLost(evt);
            }
        });

        panel.add(descriptionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 260, -1));

        rootPackageText.setToolTipText("Root package for the entity");
        rootPackageText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rootPackageTextFocusLost(evt);
            }
        });

        panel.add(rootPackageText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 260, -1));

        refNameText.setToolTipText("Reference name for the entity");
        refNameText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                refNameTextFocusLost(evt);
            }
        });
        refNameText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                refNameTextMouseReleased(evt);
            }
        });
        refNameText.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                refNameTextHierarchyChanged(evt);
            }
        });

        panel.add(refNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 280, 260, -1));

        isCompositeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        isCompositeLabel.setText("Composite key:");
        isCompositeLabel.setToolTipText("Set to true in case of a composite key");
        panel.add(isCompositeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 110, -1));

        isCompositeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "false", "true" }));
        isCompositeCombo.setToolTipText("Set to true in case of a composite key");
        isCompositeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isCompositeComboActionPerformed(evt);
            }
        });

        panel.add(isCompositeCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 260, -1));

        nameLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameLabel1.setText("Display name: ");
        nameLabel1.setToolTipText("Display name for the entity. Should be one of the entity fields.");
        panel.add(nameLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));

        displayNameText.setToolTipText("Display name for the entity");
        panel.add(displayNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 260, -1));

        isCompositeLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        isCompositeLabel1.setText("Association entity:");
        isCompositeLabel1.setToolTipText("Entity used for many to many relations");
        panel.add(isCompositeLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 110, -1));

        isAssociationEntity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "false", "true" }));
        isAssociationEntity.setToolTipText("Entity used for many to many relations");
        isAssociationEntity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isAssociationEntityActionPerformed(evt);
            }
        });

        panel.add(isAssociationEntity, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 260, -1));

    }// </editor-fold>//GEN-END:initComponents

    private void isAssociationEntityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isAssociationEntityActionPerformed
        if ("true".equals(this.isAssociationEntity.getSelectedItem().toString())) {
            this.pKeyTypeText.setEnabled(false);
            this.pKeyText.setEnabled(false);
            this.isCompositeCombo.setEnabled(false);
        } else {
            this.pKeyText.setEnabled(true);
            this.pKeyTypeText.setEnabled(true);
            this.isCompositeCombo.setEnabled(true);
        }
    }//GEN-LAST:event_isAssociationEntityActionPerformed

    private void nameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_nameTextActionPerformed

    private void refNameTextHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_refNameTextHierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_refNameTextHierarchyChanged

    private void refNameTextMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refNameTextMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_refNameTextMouseReleased

    private void refNameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_refNameTextFocusLost
        System.out.println("Focus was lost for refname");
        JagGenerator.stateChanged(true);
    }//GEN-LAST:event_refNameTextFocusLost

    private void rootPackageTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rootPackageTextFocusLost
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_rootPackageTextFocusLost

    private void descriptionTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionTextFocusLost
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_descriptionTextFocusLost

    private void isCompositeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isCompositeComboActionPerformed
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_isCompositeComboActionPerformed

    private void pKeyTypeTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pKeyTypeTextFocusLost
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_pKeyTypeTextFocusLost

    private void pKeyTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pKeyTextFocusLost
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_pKeyTextFocusLost

    private void tableNameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tableNameTextFocusLost
        JagGenerator.stateChanged(false);
        JagGenerator.entityHasupdatedTableName(nameText.getText(), tableNameText.getText());
    }//GEN-LAST:event_tableNameTextFocusLost

    private void nameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFocusLost
        // Add your handling code here:
        evt = null;
        if ((nameText.getText() != null) && (nameText.getText().length() > 0)) {
            nameText.setText(Utils.initCap(nameText.getText()));
            if ((refNameText.getText() == null) || (refNameText.getText().length() == 0)) {
                // Only change the refname, if it hasn't been set before.
                refNameText.setText(nameText.getText());
            }
        }
        JagGenerator.stateChanged(true);
    }//GEN-LAST:event_nameTextFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel desciptionLabel;
    private javax.swing.JTextField descriptionText;
    public javax.swing.JTextField displayNameText;
    public javax.swing.JComboBox isAssociationEntity;
    public javax.swing.JComboBox isCompositeCombo;
    private javax.swing.JLabel isCompositeLabel;
    private javax.swing.JLabel isCompositeLabel1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel nameLabel1;
    public javax.swing.JTextField nameText;
    private javax.swing.JLabel pKeyLabel;
    public javax.swing.JTextField pKeyText;
    private javax.swing.JLabel pKeyTypeLabel;
    public javax.swing.JTextField pKeyTypeText;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel refNameLabel;
    private javax.swing.JTextField refNameText;
    private javax.swing.JLabel rootPackageLabel;
    public javax.swing.JTextField rootPackageText;
    private javax.swing.JLabel tableNameLabel;
    private javax.swing.JTextField tableNameText;
    // End of variables declaration//GEN-END:variables
}

