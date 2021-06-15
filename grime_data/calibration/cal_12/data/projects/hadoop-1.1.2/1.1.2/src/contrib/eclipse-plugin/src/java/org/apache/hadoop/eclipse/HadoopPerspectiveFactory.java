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
package org.apache.hadoop.eclipse;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * Creates links to the new MapReduce-based wizards and views for a MapReduce
 * perspective
 * 
 */

public class HadoopPerspectiveFactory implements IPerspectiveFactory {

  public void createInitialLayout(IPageLayout layout) {
    layout.addNewWizardShortcut("org.apache.hadoop.eclipse.NewDriverWizard");
    layout.addNewWizardShortcut("org.apache.hadoop.eclipse.NewMapperWizard");
    layout
        .addNewWizardShortcut("org.apache.hadoop.eclipse.NewReducerWizard");

    IFolderLayout left =
        layout.createFolder("org.apache.hadoop.eclipse.perspective.left",
            IPageLayout.LEFT, 0.2f, layout.getEditorArea());
    left.addView("org.eclipse.ui.navigator.ProjectExplorer");

    IFolderLayout bottom =
        layout.createFolder("org.apache.hadoop.eclipse.perspective.bottom",
            IPageLayout.BOTTOM, 0.7f, layout.getEditorArea());
    bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
    bottom.addView(IPageLayout.ID_TASK_LIST);
    bottom.addView(JavaUI.ID_JAVADOC_VIEW);
    bottom.addView("org.apache.hadoop.eclipse.view.servers");
    bottom.addPlaceholder(JavaUI.ID_SOURCE_VIEW);
    //bottom.addPlaceholder(IPageLayout.ID_PROGRESS_VIEW); //QualitasCorpus.class: There is no such attribute
    bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
    bottom.addPlaceholder(IPageLayout.ID_BOOKMARKS);

    IFolderLayout right =
        layout.createFolder("org.apache.hadoop.eclipse.perspective.right",
            IPageLayout.RIGHT, 0.8f, layout.getEditorArea());
    right.addView(IPageLayout.ID_OUTLINE);
    right.addView("org.eclipse.ui.cheatsheets.views.CheatSheetView");
    // right.addView(layout.ID); .. cheat sheet here

    layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
    layout.addActionSet(JavaUI.ID_ACTION_SET);
    layout.addActionSet(JavaUI.ID_CODING_ACTION_SET);
    layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
    layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
    layout.addActionSet(JavaUI.ID_SEARCH_ACTION_SET);

    layout
        .addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");
    layout
        .addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard");
    layout
        .addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard");
    layout
        .addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard");
    layout
        .addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard");
    layout
        .addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");
    layout
        .addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard");
    layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
    layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
    layout
        .addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");

    // CheatSheetViewerFactory.createCheatSheetView().setInput("org.apache.hadoop.eclipse.cheatsheet");
  }

}
