/*
 * WaqtSalat, for indicating the muslim prayers times in most cities. Copyright
 * (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 */

package net.paissad.waqtsalat.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class WSMainFrame extends Composite {

    public WSMainFrame(Composite parent, int style) {
        
        super(parent, style);
        
        final Shell shell = new Shell();
        shell.setSize(520, 200);
        shell.setLayout(new RowLayout());

        //Composite parent = new Composite(shell, SWT.NONE);
        parent.setLayoutData(new RowData(515, 170));
        GridLayout gridLayout = new GridLayout(1, false);
        parent.setLayout(gridLayout);
        
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT | SWT.SHADOW_OUT);
        toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        
        ToolItem tltmGeneral = new ToolItem(toolBar, SWT.NONE);
        tltmGeneral.setImage(SWTResourceManager.getImage("/Users/paissad/Documents/workspace/waqtsalat/src/main/resources/icons/general_preferences.png"));
        tltmGeneral.setText("General");
        
        ToolItem tltmLocation = new ToolItem(toolBar, SWT.NONE);
        tltmLocation.setImage(SWTResourceManager.getImage("/Users/paissad/Documents/workspace/waqtsalat/src/main/resources/icons/earth.png"));
        tltmLocation.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        tltmLocation.setText("Location");
        
        ToolItem tltmPrayertimes = new ToolItem(toolBar, SWT.NONE);
        tltmPrayertimes.setImage(SWTResourceManager.getImage("/Users/paissad/Documents/workspace/waqtsalat/src/main/resources/icons/carpet.png"));
        tltmPrayertimes.setText("PrayerTimes");
        
        ToolItem tltmAlerts = new ToolItem(toolBar, SWT.NONE);
        tltmAlerts.setImage(SWTResourceManager.getImage("/Users/paissad/Documents/workspace/waqtsalat/src/main/resources/icons/alert.png"));
        tltmAlerts.setText("Alerts");
        
        ToolItem tltmAdvanced = new ToolItem(toolBar, SWT.NONE);
        tltmAdvanced.setImage(SWTResourceManager.getImage("/Users/paissad/Documents/workspace/waqtsalat/src/main/resources/icons/advanced_config.png"));
        tltmAdvanced.setText("Advanced");

    }
}
