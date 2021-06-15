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
import com.finalist.jaggenerator.ForeignKey;
import com.finalist.jaggenerator.JagGenerator;
import com.finalist.jaggenerator.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * This class models a container-managed relation.  A Relation maintains three views: a DefaultMutableTreeNode,
 * an XML view and a Swing JPanel.  Unfortunately this class is kind of a Model, View and Controller rolled into one,
 * but that's just the way JagBeans have been designed...
 * <p/>
 * The relation data is initially generated using foreign key information read from a database table (or just
 * filled in by hand from the GUI).
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class Relation extends DefaultMutableTreeNode implements JagBean {

    private String name = "new relation";
    private String fieldName;
    private String targetName;
    private String foreignTable;
    private String foreignPkFieldName;
    private String foreignColumn;
    private String localColumn;
    private RelationPanel panelView;
    private Field fieldObject;
    private Field foreignPkField;
    private Entity localEntity;
    private boolean targetMultiple = true;    //not yet implemented
    private boolean bidirectional = false;    //not yet implemented

    /**
     * Constructs a new Relation from scratch.
     *
     * @param localEntity the parent entity bean on the local side of this relation.
     */
    public Relation(Entity localEntity) {
        this.localEntity = localEntity;
        panelView = new RelationPanel(this, false);
    }

    /**
     * Constructs a Relation from a ForeignKey object.
     *
     * @param localEntity the parent entity bean on the local side of this relation.
     * @param fk          the foreign key.
     */
    public Relation(Entity localEntity, ForeignKey fk) {
        this(localEntity, fk, true);
    }

    /**
     * Constructs a Relation from a ForeignKey object.
     *
     * @param localEntity       the parent entity bean on the local side of this relation.
     * @param fk                the foreign key.
     * @param waitForInitSignal if <code>true</code> panel delays initialisation until notified.
     */
    public Relation(Entity localEntity, ForeignKey fk, boolean waitForInitSignal) {
        this.localEntity = localEntity;
        String thisTable = Utils.format(localEntity.getLocalTableName().toString());
        String thatTable = Utils.format(fk.getPkTableName());
        name = thisTable + '-' + thatTable;
        targetName = thatTable + '-' + thisTable;
        foreignTable = fk.getPkTableName();
        fieldName = fk.getFkName() == null ? Utils.format(fk.getFkColumnName()) : fk.getFkName();

        foreignPkFieldName = Utils.format(fk.getPkColumnName());
        foreignColumn = fk.getPkColumnName();
        localColumn = fk.getFkColumnName();
        panelView = new RelationPanel(this, waitForInitSignal);


    }


    /**
     * (Re-)Constructs a Relation from an XML element.
     *
     * @param localEntity the parent entity bean on the local side of this relation.
     * @param el          the XML element.
     */
    public Relation(Entity localEntity, Element el) {
        this.localEntity = localEntity;
        NodeList nl = el.getElementsByTagName("module-data");

        for (int i = 0; i < nl.getLength(); i++) {
            Element child = (Element) nl.item(i);
            String attName = child.getAttribute("name");
            String value = null;
            if (child.getFirstChild() != null) {
                value = child.getFirstChild().getNodeValue();
            }
            if (value != null) {
                if (attName.equalsIgnoreCase("name")) {
                    name = value;
                    continue;
                }
                if (attName.equalsIgnoreCase("field-name")) {
                    fieldName = Utils.firstToLowerCase(value);
                    continue;
                }
                if (attName.equalsIgnoreCase("target-name")) {
                    targetName = value;
                    continue;
                }
                if (attName.equalsIgnoreCase("target-multiple")) {
                    targetMultiple = "true".equals(value.trim().toLowerCase());
                    continue;
                }
                if (attName.equalsIgnoreCase("bidirectional")) {
                    bidirectional = "true".equals(value.trim().toLowerCase());
                    continue;
                }
                if (attName.equalsIgnoreCase("field")) {        //for backwards compatibility (pre v.2.3)
                    fieldName = Utils.firstToLowerCase(value);
                    continue;
                }
                if (attName.equalsIgnoreCase("foreign-table")) {
                    foreignTable = value;
                    continue;
                }
                if (attName.equalsIgnoreCase("foreign-column")) {
                    foreignColumn = value;
                    continue;
                }
                if (attName.equalsIgnoreCase("local-column")) {
                    localColumn = value;
                    continue;
                }
                if (attName.equalsIgnoreCase("foreign-field")) {
                    foreignPkFieldName = value;
                    continue;
                }
            }
        }

        panelView = new RelationPanel(this, true);
    }


    public String getRefName() {
        return name;
    }

    /**
     * Gets the Swing JPanel view of this relation.
     *
     * @return the JPanel.
     */
    public JPanel getPanel() {
        return panelView;
    }

    /**
     * Creates the XML view of this relation and appends it as a new child to the specified XML element.
     *
     * @param parent the XML element to become parent to this relation child.
     */
    public void getXML(Element parent) {
        Document doc = parent.getOwnerDocument();
        Element newModule = doc.createElement("module-data");
        newModule.setAttribute("name", "relation");

        newModule.appendChild(createElement(doc, "name", name));
        newModule.appendChild(createElement(doc, "field-name", fieldName));
        newModule.appendChild(createElement(doc, "local-column", localColumn));
        newModule.appendChild(createElement(doc, "target-name", targetName));
        newModule.appendChild(createElement(doc, "target-multiple", "" + targetMultiple));
        newModule.appendChild(createElement(doc, "bidirectional", "" + bidirectional));
        newModule.appendChild(createElement(doc, "foreign-table", foreignTable));
        newModule.appendChild(createElement(doc, "foreign-column", foreignColumn));
        newModule.appendChild(createElement(doc, "foreign-field", foreignPkFieldName));

        parent.appendChild(newModule);
    }

    /**
     * Gets 'name' : the name of this relation.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        panelView.setName(name);
    }

    /**
     * Gets the name of the imported foreign key field in the parent entity bean on the 'local' side of this relation.
     *
     * @return field
     */
    public TemplateString getFieldName() {
        return new TemplateString(fieldName);
    }

    public void setFieldName(String fieldName) {
        this.fieldName = Utils.firstToLowerCase(fieldName);
    }

    /**
     * Gets 'targetName' : the name given to the reciprocal end of this relation (if it is bidirectional).
     *
     * @return targetName
     */
    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * Gets 'targetMultiple' : whether or not this relation maps to multiple entities at the 'foreign' end.
     *
     * @return targetMultiple
     */
    public boolean isTargetMultiple() {
        return targetMultiple;
    }

    public void setTargetMultiple(boolean targetMultiple) {
        this.targetMultiple = targetMultiple;
    }

    /**
     * Gets 'bidirectional' : whether or not this relation is also navigable the other way round.
     *
     * @return bidirectional
     */
    public boolean isBidirectional() {
        return bidirectional;
    }

    public void setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    /**
     * Gets 'foreignTable' : the name of the table at the other end of the relation.
     *
     * @return foreignTable
     */
    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    /**
     * Gets the name of the exported primary key field at the other end of the relation.
     *
     * @return foreignPkFieldName
     */
    public TemplateString getForeignPkFieldName() {
        return new TemplateString(foreignPkFieldName);
    }

    public void setForeignPkFieldName(String foreignField) {
        this.foreignPkFieldName = foreignField;
    }

    public Field getForeignPkField() {
        return foreignPkField;
    }

    public void setForeignPkField(Field foreignPkField) {
        this.foreignPkField = foreignPkField;
    }

    /**
     * Gets 'foreignColumn' : the name of the primary key column at the other end of the relation.
     *
     * @return foreignTable
     */
    public String getForeignColumn() {
        return foreignColumn;
    }

    public void setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;
    }

    /**
     * Gets 'localColumn' : the name of the column at the local end of the relation.
     *
     * @return String with local column.
     */
    public String getLocalColumn() {
        return localColumn;
    }

    public void setLocalColumn(String localColumn) {
        this.localColumn = localColumn;
    }

    /**
     * Gets the Entity object that this Relation relates to.
     *
     * @return Entity
     */
    public Entity getRelatedEntity() {
        return JagGenerator.getEntityByTableName(foreignTable);
    }

    /**
     * The relation is a many to many relation if the related entity is an assocation table.
     *
     * @return string with 'true' if the relation is a many to many relation. 'false' if not.
     */
    public String isManyToManyRelation() {
        return getRelatedEntity().getIsAssociationEntity();
    }

    public String toString() {
        return name;               // the name used to display this relation in the tree view.
    }

    public void setFkField(Field field) {
        fieldObject = field;
    }

    public Field getFkField() {
        return fieldObject;
    }

    public Entity getLocalEntity() {
        return localEntity;
    }

    /**
     * RelationPanels can't finish initialising themselves until the local-side entity is completely generated
     * (until all the entity's fields are generated).  Call this method to wake up the sleeping initialisers.
     */
    public void notifyLocalEntityIsComplete() {
        synchronized (panelView) {
            panelView.notifyAll();
        }
    }


    public void notifyFieldNameChanged(String oldName, String text) {
        panelView.updateFieldName(oldName, text);
    }

    private Element createElement(Document doc, String name, String value) {
        Element newElement = doc.createElement("module-data");
        newElement.setAttribute("name", name);
        if (value != null) {
            newElement.appendChild(doc.createTextNode(value));
        }
        return newElement;
   }

}