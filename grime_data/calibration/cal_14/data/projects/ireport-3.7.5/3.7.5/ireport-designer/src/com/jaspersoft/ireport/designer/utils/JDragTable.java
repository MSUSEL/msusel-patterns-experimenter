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
package com.jaspersoft.ireport.designer.utils;
import com.jaspersoft.ireport.designer.dnd.TransferableObject;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 *
 * @author  Administrator
 */
public class JDragTable extends org.jdesktop.swingx.JXTable implements DragGestureListener, DragSourceListener {    
   
     /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;
    
   public JDragTable() {
        
       super();
       
        DragSource dragSource = DragSource.getDefaultDragSource();

        // creating the recognizer is all that's necessary - it
        // does not need to be manipulated after creation
        dragSource.createDefaultDragGestureRecognizer(
         this, // component where drag originates
         DnDConstants.ACTION_COPY, // actions
         this); // drag gesture listener

        setColumnControlVisible(true); 
        
      }
      
       public void dragGestureRecognized(DragGestureEvent e) {
         // drag anything ...
         
         TransferableObject  to =   new TransferableObject(this.getValueAt(this.getSelectedRow(), this.getSelectedColumn() ));
         
         try{
         if (to != null)
         e.startDrag(DragSource.DefaultCopyDrop , // cursor
            to); //, // transferable
            //this); // drag source listener
         } catch (Exception ex) {

            ex.printStackTrace();

         }

      }

      public void dragDropEnd(DragSourceDropEvent e) {}
      public void dragEnter(DragSourceDragEvent e) {}
      public void dragExit(DragSourceEvent e) {}
      public void dragOver(DragSourceDragEvent e) {}
      public void dropActionChanged(DragSourceDragEvent e) {}

      
    @Override
    public void toggleSortOrder(int columnIndex) {
        super.toggleSortOrder(columnIndex);
        
        int index = convertColumnIndexToModel(columnIndex);
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }

        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so) );
    }

    @Override
    public void toggleSortOrder(Object identifier) {
        super.toggleSortOrder(identifier);
        
        TableColumnExt columnExt = getColumnExt(identifier);
        int index = columnExt.getModelIndex();
        
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so ) );
    }

    @Override
    public void setSortOrder(Object identifier, SortOrder arg1) {
        super.setSortOrder(identifier, arg1);
        
        TableColumnExt columnExt = getColumnExt(identifier);
        int index = columnExt.getModelIndex();
        
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so ) );
    }
    
    
    @Override
    public void resetSortOrder() {
        super.resetSortOrder();
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, -1, SortOrder.UNSORTED ) );
    }

      
     

    /**
     * Registers TabPaneChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addSortChangedListener(SortChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (SortChangedListener.class, listener);
    }

    /**
     * Removes TabPaneChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeSortChangedListener(SortChangedListener listener) {

        listenerList.remove (SortChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireSortChangedListenerSortChanged(SortChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==SortChangedListener.class) {
                ((SortChangedListener)listeners[i+1]).sortChanged (event);
            }
        }
    }
}
