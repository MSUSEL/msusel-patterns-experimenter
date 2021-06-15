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
/*
 * Created on 26.03.2005
 */
package net.sourceforge.ganttproject.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.eclipse.core.runtime.Platform;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.language.GanttLanguage.Event;

/**
 * @author bard
 */
public abstract class GPAction extends AbstractAction implements
        RolloverAction, GanttLanguage.Listener {
    public static final int MENU_MASK = Toolkit.getDefaultToolkit()
            .getMenuShortcutKeyMask();

    protected boolean iconVisible = true;
    private Icon myIcon = null;
    
    protected GPAction() {
        this(null, "16");
    }

    protected GPAction(String name, String iconSize) {
        super(name);
        setIconSize(iconSize);
        updateName();
        updateTooltip();
        GanttLanguage.getInstance().addListener(this);
    }

    public GPAction(String name) {
        this(name, "16");
    }

    public Icon getIconOnMouseOver() {
        return (Icon) getValue(Action.SMALL_ICON);
    }

    public void setIconSize(String iconSize) {
        Icon icon = createIcon(iconSize);
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
            myIcon = icon;
        }
    }

    protected Icon createIcon(String iconSize) {
        if (iconSize == null || false==iconVisible) {
            return null;
        }
        URL resource = getClass().getResource(
                getIconFileDirectory() + "/" + getIconFilePrefix() + iconSize
                + ".gif");
        return resource==null ? null : new ImageIcon(resource);
    }

    protected String getIconFileDirectory() {
        return "/icons";
    }

    protected String getLocalizedName() {
        return "";
    }
    
    protected String getTooltipText() {
    	String localizedName = getLocalizedName();
    	return localizedName==null ? "" : GanttLanguage.getInstance().correctLabel(getLocalizedName());
    }

    protected String getI18n(String key) {
        return GanttLanguage.getInstance().getText(key);
    }
    protected abstract String getIconFilePrefix();

    public void setIconVisible(boolean isVisible) {
        iconVisible = isVisible;
        putValue(Action.SMALL_ICON, iconVisible ? myIcon : null);
    }

    private void updateName() {
        String localizedName = getLocalizedName();
        if (localizedName == null) {
            localizedName = String.valueOf(getValue(Action.NAME));
        }
        if (localizedName != null) {
            int bucksPos = localizedName.indexOf('$');
            if (bucksPos>=0) {
                localizedName = new StringBuffer(localizedName).deleteCharAt(bucksPos).toString();
            }
            putValue(Action.NAME, localizedName);
            if (bucksPos>=0) {
                putValue(Action.MNEMONIC_KEY, new Integer(Character.toLowerCase(localizedName.charAt(bucksPos))));
            }
        }        
    }
    
    private void updateTooltip() {
        putValue(Action.SHORT_DESCRIPTION, "<html><body bgcolor=#EAEAEA>" + getTooltipText() + "</body></html>");        
    }
    public void isIconVisible(boolean isNull) {
        setIconVisible(isNull);
    }

    public void languageChanged(Event event) {
        updateName();
        updateTooltip();
    }
    
    public static KeyStroke getKeyStroke(String keystrokeID) {
        if (ourProperties==null) {
            ourProperties = new Properties();
            URL url = GPAction.class.getResource("/keyboard.properties");
            if (url==null) {
                return null;
            }
            URL resolvedUrl;
            try {
                resolvedUrl = Platform.resolve(url);
                ourProperties.load(resolvedUrl.openStream());
            } catch (IOException e) {
                if (!GPLogger.log(e)) {
                	e.printStackTrace(System.err);
                }
                return null;
            }            
        }
        String stringKeyStroke = (String) ourProperties.get(keystrokeID);
        return KeyStroke.getKeyStroke(stringKeyStroke);
    }
    
    private static Properties ourProperties;
}
