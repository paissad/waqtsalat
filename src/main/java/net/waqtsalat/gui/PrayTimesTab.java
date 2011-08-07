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

package net.waqtsalat.gui;

import static net.waqtsalat.gui.WaqtSalatPrefs.userPrefs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.waqtsalat.ComputePrayTimes;
import net.waqtsalat.ComputePrayTimes.CalculationMethod;
import net.waqtsalat.ComputePrayTimes.JuristicMethod;
import net.waqtsalat.I18N;
import net.waqtsalat.PrayName;
import net.waqtsalat.gui.WaqtSalatPrefs.guiSettings;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class PrayTimesTab extends JPanel implements ActionListener {

    private static final long              serialVersionUID = 1L;

    private static Logger                  logger           = LoggerFactory.getLogger(PrayTimesTab.class);

    private Map<JuristicMethod, String>    _madhabs         =
                                                                    new HashMap<ComputePrayTimes.JuristicMethod, String>();
    private Map<CalculationMethod, String> _methods         =
                                                                    new TreeMap<ComputePrayTimes.CalculationMethod, String>();

    private static String                  _currentMadhab_String;
    private static String                  _currentMethod_String;

    private JPanel                         madhabPanel;
    private JPanel                         methodPanel;
    private JLabel                         lblMadhab;
    private JLabel                         lblMethod;
    private ButtonGroup                    madhabBtnGrp;
    private JRadioButton                   rdbtnShafii;
    private JRadioButton                   rdbtnHanafi;
    private JComboBox                      comboBoxMethod;
    private JSeparator                     separator;
    private JTable                         prayTable;
    private JLabel                         lblIconKaba;
    private JPanel                         prayPanel;

    public PrayTimesTab() {

        initMadhab_Names();
        initMethod_Names();

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        madhabPanel = new JPanel();
        GridBagConstraints gbc_madhabPanel = new GridBagConstraints();
        gbc_madhabPanel.anchor = GridBagConstraints.WEST;
        gbc_madhabPanel.insets = new Insets(5, 15, 5, 15);
        gbc_madhabPanel.gridx = 0;
        gbc_madhabPanel.gridy = 0;
        add(madhabPanel, gbc_madhabPanel);
        GridBagLayout gbl_madhabPanel = new GridBagLayout();
        madhabPanel.setLayout(gbl_madhabPanel);

        lblMadhab = new JLabel("Madhab:");
        GridBagConstraints gbc_lblMadhab = new GridBagConstraints();
        gbc_lblMadhab.insets = new Insets(0, 0, 5, 5);
        gbc_lblMadhab.gridx = 0;
        gbc_lblMadhab.gridy = 0;
        madhabPanel.add(lblMadhab, gbc_lblMadhab);

        rdbtnShafii = new JRadioButton(JuristicMethod.SHAFII.getName());
        rdbtnShafii.setActionCommand(JuristicMethod.SHAFII.toString());
        rdbtnShafii.setHorizontalAlignment(SwingConstants.LEFT);
        rdbtnShafii.addActionListener(this);
        GridBagConstraints gbc_rdbtnShafii = new GridBagConstraints();
        gbc_rdbtnShafii.weightx = 1.0;
        gbc_rdbtnShafii.anchor = GridBagConstraints.WEST;
        gbc_rdbtnShafii.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnShafii.gridx = 1;
        gbc_rdbtnShafii.gridy = 0;
        madhabPanel.add(rdbtnShafii, gbc_rdbtnShafii);

        rdbtnHanafi = new JRadioButton(JuristicMethod.HANAFI.getName());
        rdbtnHanafi.setActionCommand(JuristicMethod.HANAFI.toString());
        rdbtnHanafi.setHorizontalAlignment(SwingConstants.LEFT);
        rdbtnHanafi.addActionListener(this);
        GridBagConstraints gbc_rdbtnHanafi = new GridBagConstraints();
        gbc_rdbtnHanafi.weightx = 1.0;
        gbc_rdbtnHanafi.anchor = GridBagConstraints.WEST;
        gbc_rdbtnHanafi.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnHanafi.gridx = 1;
        gbc_rdbtnHanafi.gridy = 1;
        madhabPanel.add(rdbtnHanafi, gbc_rdbtnHanafi);

        chooseMadhabToSelectFromPrefs(); // Be aware: should/must be called
                                         // before adding buttons to ButtonGroup
                                         // ...

        madhabBtnGrp = new ButtonGroup();
        madhabBtnGrp.add(rdbtnShafii);
        madhabBtnGrp.add(rdbtnHanafi);

        separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        separator.setOrientation(SwingConstants.VERTICAL);
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.anchor = GridBagConstraints.LINE_END;
        gbc_separator.fill = GridBagConstraints.VERTICAL;
        gbc_separator.gridheight = 2;
        gbc_separator.insets = new Insets(5, 5, 0, 5);
        gbc_separator.gridx = 0;
        gbc_separator.gridy = 0;
        add(separator, gbc_separator);

        prayPanel = new JPanel();
        GridBagConstraints gbc_prayPanel = new GridBagConstraints();
        gbc_prayPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_prayPanel.insets = new Insets(5, 5, 5, 10);
        gbc_prayPanel.gridx = 1;
        gbc_prayPanel.gridy = 0;
        add(prayPanel, gbc_prayPanel);
        prayPanel.setLayout(new BorderLayout());

        prayTable = new JTable();
        prayTable.setBorder(new CompoundBorder(
                new EtchedBorder(EtchedBorder.LOWERED, null, null),
                new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0))));
        prayTable.setBackground(Color.LIGHT_GRAY);
        prayTable.setFillsViewportHeight(true);
        prayTable.setColumnSelectionAllowed(true);
        prayTable.setCellSelectionEnabled(true);
        prayTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        prayTable.setGridColor(Color.BLACK);
        DefaultTableCellRenderer atcr = new AdhanTableCellRenderer();
        prayTable.setModel(new AdhanDefaultTableModel(
                new Object[][] {
                        { null, null },
                        { null, null },
                        { null, null },
                        { null, null },
                        { null, null },
                        { null, null },
                        { null, null },
                },
                new String[] {
                        I18N.getString("output.PRAYS"), I18N.getString("output.TIMES")
                }
                ));
        TableColumn col;
        for (int i = 0; i < prayTable.getColumnCount(); i++) {
            col = prayTable.getColumn(prayTable.getColumnName(i));
            col.setCellRenderer(atcr);
            col = null;
        }
        this.updatePrayTableContent();

        JTableHeader th = prayTable.getTableHeader();
        th.setBorder(prayTable.getBorder());
        DefaultTableCellRenderer htcr = new HeaderTableCellRenderer();
        th.setDefaultRenderer(htcr);
        prayPanel.add(th, BorderLayout.NORTH);
        prayPanel.add(prayTable, BorderLayout.CENTER);

        lblIconKaba = new JLabel("");
        lblIconKaba.setIcon(new ImageIcon(PrayTimesTab.class.getResource("/images/kaba_128x128.png")));
        GridBagConstraints gbc_lblIconKaba = new GridBagConstraints();
        gbc_lblIconKaba.insets = new Insets(0, 5, 10, 0);
        gbc_lblIconKaba.gridx = 1;
        gbc_lblIconKaba.gridy = 1;
        add(lblIconKaba, gbc_lblIconKaba);

        methodPanel = new JPanel();
        GridBagConstraints gbc_methodPanel = new GridBagConstraints();
        gbc_methodPanel.anchor = GridBagConstraints.WEST;
        gbc_methodPanel.insets = new Insets(0, 15, 10, 15);
        gbc_methodPanel.gridx = 0;
        gbc_methodPanel.gridy = 1;
        add(methodPanel, gbc_methodPanel);
        GridBagLayout gbl_methodPanel = new GridBagLayout();
        gbl_methodPanel.columnWidths = new int[] { 0, 0, 0 };
        gbl_methodPanel.rowHeights = new int[] { 0, 0, 0 };
        methodPanel.setLayout(gbl_methodPanel);

        lblMethod = new JLabel("Method:");
        GridBagConstraints gbc_lblMethod = new GridBagConstraints();
        gbc_lblMethod.insets = new Insets(0, 0, 5, 5);
        gbc_lblMethod.gridx = 0;
        gbc_lblMethod.gridy = 0;
        methodPanel.add(lblMethod, gbc_lblMethod);

        comboBoxMethod = new JComboBox();
        comboBoxMethod.setModel(new DefaultComboBoxModel(
                _methods.values().toArray(new String[_methods.size()])
                ));
        comboBoxMethod.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBoxMethod_itemStateChanged(e);
            }
        });
        chooseMethodToSelectFromPrefs(); // Be aware: Must be called after the
                                         // creation/initialisation of ComboBox
                                         // of Methods ...
        GridBagConstraints gbc_comboBoxMethod = new GridBagConstraints();
        gbc_comboBoxMethod.weightx = 1.0;
        gbc_comboBoxMethod.anchor = GridBagConstraints.WEST;
        gbc_comboBoxMethod.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxMethod.gridx = 1;
        gbc_comboBoxMethod.gridy = 0;
        methodPanel.add(comboBoxMethod, gbc_comboBoxMethod);

        String praytimesSettingsNotice_11 = "Please, select the pray time settings so that";
        String praytimesSettingsNotice_12 = "it matches the prayer times of your Masjid as closely as possible.";
        JLabel lblNotice = new JLabel("<html>"
                + praytimesSettingsNotice_11 + "<br>"
                + praytimesSettingsNotice_12 + "</html>"
                );
        lblNotice.setFont(new Font("Calibri", Font.BOLD | Font.ITALIC, 13));
        GridBagConstraints gbc_lblNotice = new GridBagConstraints();
        gbc_lblNotice.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNotice.anchor = GridBagConstraints.WEST;
        gbc_lblNotice.gridwidth = 2;
        gbc_lblNotice.insets = new Insets(0, 0, 0, 5);
        gbc_lblNotice.gridx = 0;
        gbc_lblNotice.gridy = 1;
        methodPanel.add(lblNotice, gbc_lblNotice);
    }

    // ======================================================================

    private void initMadhab_Names() {
        JuristicMethod[] allMadhabs = JuristicMethod.values();
        for (int i = 0; i < allMadhabs.length; i++) {
            _madhabs.put(allMadhabs[i], allMadhabs[i].toString());
        }
    }

    private void initMethod_Names() {
        CalculationMethod[] allMethods = CalculationMethod.values();
        for (int i = 0; i < allMethods.length; i++) {
            _methods.put(allMethods[i], allMethods[i].getName());
        }

        // Let's remove method 'CUSTOM' from the ComboBox,
        // we may add it in the future, but not now ...
        _methods.remove(CalculationMethod.CUSTOM);
    }

    // ======================================================================

    private void chooseMadhabToSelectFromPrefs() {
        JuristicMethod default_madhab = JuristicMethod.SHAFII;
        _currentMadhab_String = userPrefs.get(
                guiSettings.PRAYTIME_MADHAB.toString(), default_madhab.toString());

        if (_madhabs.containsValue(_currentMadhab_String) == false) {
            _currentMadhab_String = default_madhab.toString();
        }

        if (_currentMadhab_String.equals(JuristicMethod.SHAFII.toString())) {
            rdbtnShafii.setSelected(true);
        } else if (_currentMadhab_String.equals(JuristicMethod.HANAFI.toString())) {
            rdbtnHanafi.setSelected(true);
        } else {
            logger.error("What the hell is going wrong with madhab selection ???");
            logger.error("Really bad ... :-/");
        }
    }

    // ======================================================================

    private void chooseMethodToSelectFromPrefs() {
        CalculationMethod default_method = CalculationMethod.EGYPT;
        _currentMethod_String = userPrefs.get(
                guiSettings.PRAYTIME_METHOD.toString(), default_method.toString());

        Iterator<CalculationMethod> known_methods = _methods.keySet().iterator();
        int index = 0;
        while (known_methods.hasNext()) {
            String methodString = known_methods.next().toString();
            if (methodString.equals(_currentMethod_String)) {
                break;
            }
            index++;
        }
        if (index >= _methods.size()) {
            logger.error("What the hell happened with method selection ????");
            logger.error("Not good ... :-/");
            setSelectedMethod(CalculationMethod.EGYPT.toString());
        } else {
            comboBoxMethod.setSelectedIndex(index);
        }
    }

    private void updatePrayTableContent() { // TODO: to complete ...
        ArrayList<String> prayNameList = PrayName.getNamesList();
        for (int i = 0; i < prayNameList.size(); i++) {
            prayTable.setValueAt(prayNameList.get(i), i, 0);
        }
    }

    // ======================================================================
    // Actions ...

    @Override
    public void actionPerformed(ActionEvent e) {
        String source = e.getActionCommand();
        if (source.equals(JuristicMethod.SHAFII.toString()) ||
                source.equals(JuristicMethod.HANAFI.toString())) {
            String selected_madhab = ((JRadioButton) e.getSource()).getActionCommand();
            setSelectedMadhab(selected_madhab);
        }
    }

    private void comboBoxMethod_itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String selected_item = (String) e.getItem();
            // Let's retrieve the 'raw' name of the method instead of the
            // "full description name"
            Iterator<CalculationMethod> methods_keys = _methods.keySet().iterator();

            int i = 0;
            while (methods_keys.hasNext()) {
                CalculationMethod m = methods_keys.next();
                if (m.getName().equals(selected_item)) {
                    setSelectedMethod(m.toString());
                    break;
                }
                i++;
            }
        }
    }

    // ======================================================================

    class AdhanDefaultTableModel extends DefaultTableModel {

        private static final long serialVersionUID = 1L;

        AdhanDefaultTableModel(Object[][] objects, String[] strings) {
            super(objects, strings);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    class AdhanTableCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        public AdhanTableCellRenderer() {
            super();
            setHorizontalAlignment(SwingConstants.CENTER);
            setHorizontalTextPosition(DefaultTableCellRenderer.CENTER);
        }
    }

    class HeaderTableCellRenderer extends AdhanTableCellRenderer {
        private static final long serialVersionUID = 1L;

        public HeaderTableCellRenderer() {
            super();
            this.setFont(new Font(
                    getFont().getName(), Font.BOLD, getFont().getSize()));
        }
    }

    // ======================================================================
    // Getters / Setters ...

    public synchronized void setSelectedMadhab(String madhab) {
        logger.info("Selecting madhab : {}", madhab);
        _currentMadhab_String = madhab;
        userPrefs.put(
                guiSettings.PRAYTIME_MADHAB.toString(), madhab);

    }

    public synchronized static String getSelectedMadhab() {
        return _currentMadhab_String;
    }

    /**
     * Be aware:<br>
     * Use the raw name of the method, but not the description name. Example:<br>
     * use CalculationMethod.EGYPT but not
     * "Egyptian General Authority of Survey" ...
     * 
     * @param method
     *            The 'raw' name of the method to use.
     */
    public synchronized void setSelectedMethod(String method) {
        logger.info("Selection method : {}", method);
        _currentMethod_String = method;
        userPrefs.put(
                guiSettings.PRAYTIME_METHOD.toString(), method);
    }

    public synchronized static String getSelectedMethod() {
        return _currentMethod_String;
    }
}
