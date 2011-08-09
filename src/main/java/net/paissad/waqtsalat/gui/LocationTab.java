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

import static net.paissad.waqtsalat.gui.WaqtSalatPrefs.userPrefs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

import org.apache.lucene.queryParser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.timeZone;

import net.paissad.waqtsalat.Coordinates;
import net.paissad.waqtsalat.WSConstants;
import net.paissad.waqtsalat.gui.WaqtSalatPrefs.guiSettings;
import net.paissad.waqtsalat.gui.addons.JSearchTextField;
import net.paissad.waqtsalat.utils.CommonUtils;
import net.paissad.waqtsalat.utils.geoip.WorldCitiesDB;
import net.paissad.waqtsalat.utils.geoip.WorldCitiesLucene;

/**
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class LocationTab extends JPanel implements Observer {

    private static final long         serialVersionUID     = 1L;

    private static Logger             logger               = LoggerFactory.getLogger(LocationTab.class);

    private static Coordinates        _coordinates         = new Coordinates();
    private static String             _timezone            = userPrefs.get(
                                                                   guiSettings.LOCATION_TIMEZONE.toString(), TimeZone
                                                                           .getDefault().getID());
    private static LocationStateLabel _locationIconState   = new LocationStateLabel();
    private String                    locationSeparator    = ", ";
    private String                    errorLocMsg          = "Bad or unknown location !";
    private String                    locationFullName     = userPrefs.get(
                                                                   guiSettings.LOCATION_FULL_NAME.toString(), null);
    private static String             _countryCode         = userPrefs.get(
                                                                   guiSettings.LOCATION_COUNTRY_CODE.toString(), null);
    private boolean                   isUseSystemTimezone  = userPrefs.getBoolean(
                                                                   guiSettings.LOCATION_USE_SYS_TIMEZONE.toString(),
                                                                   true);
    private boolean                   isAutomaticLocation  = userPrefs.getBoolean(
                                                                   guiSettings.LOCATION_VIA_IP.toString(), false);
    private boolean                   isUseDayLightSavings = userPrefs.getBoolean(
                                                                   guiSettings.LOCATION_USE_DST.toString(), false);

    private JPanel                    panelLocation_1;
    private JPanel                    panelLocation_2;
    private JPanel                    panelTimezone;
    private JLabel                    lblCurrentLoc;
    private JLabel                    lblFlag;
    private JLabel                    lblCurrentTimezone;
    private JCheckBox                 chckbxIPLocation;
    private JLabel                    lblSearchLoc;
    private JScrollPane               scrollPane;
    private JSearchTextField          textFieldSearchLocation;
    private JList                     listLocation;
    private DefaultListModel          listModellocation;
    private JButton                   btnSetLocation;
    private JLabel                    lblTimeZone;
    private JCheckBox                 chckbxUseSystemTime;
    private JComboBox                 comboBoxTimezones;
    private JCheckBox                 chckbxDaylightSavings;
    private JSeparator                separator_1;
    private JSeparator                separator_2;

    public LocationTab() {

        _locationIconState.addObserver(this);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
        gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        panelLocation_1 = new JPanel();
        GridBagConstraints gbc_panelLocation_1 = new GridBagConstraints();
        gbc_panelLocation_1.gridwidth = 2;
        gbc_panelLocation_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelLocation_1.anchor = GridBagConstraints.BASELINE;
        gbc_panelLocation_1.insets = new Insets(5, 5, 5, 5);
        gbc_panelLocation_1.gridx = 0;
        gbc_panelLocation_1.gridy = 0;
        add(panelLocation_1, gbc_panelLocation_1);
        GridBagLayout gbl_panelLocation_1 = new GridBagLayout();
        gbl_panelLocation_1.columnWidths = new int[] { 0, 0, 0 };
        gbl_panelLocation_1.rowHeights = new int[] { 0, 0, 0, 0 };
        gbl_panelLocation_1.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panelLocation_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
        panelLocation_1.setLayout(gbl_panelLocation_1);

        lblCurrentLoc = new JLabel("Current location used.");
        lblCurrentLoc.setToolTipText("This is the current location used by the application to compute pray times.");
        lblCurrentLoc.setEnabled(true);
        GridBagConstraints gbc_lblCurrentLoc = new GridBagConstraints();
        gbc_lblCurrentLoc.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentLoc.weightx = 1.0;
        gbc_lblCurrentLoc.insets = new Insets(0, 0, 5, 5);
        gbc_lblCurrentLoc.gridx = 0;
        gbc_lblCurrentLoc.gridy = 0;
        lblCurrentLoc.setText(locationFullName);
        panelLocation_1.add(lblCurrentLoc, gbc_lblCurrentLoc);

        lblFlag = new JLabel("");
        lblFlag.setLabelFor(lblCurrentLoc);
        GridBagConstraints gbc_lblFlag = new GridBagConstraints();
        gbc_lblFlag.anchor = GridBagConstraints.WEST;
        gbc_lblFlag.insets = new Insets(0, 0, 5, 0);
        gbc_lblFlag.gridx = 1;
        gbc_lblFlag.gridy = 0;
        setIconFlagFromCountryName();
        panelLocation_1.add(lblFlag, gbc_lblFlag);

        lblCurrentTimezone = new JLabel("Current Timezone");
        lblCurrentTimezone
                .setToolTipText("This is the current time zone used by the application to compute pray times.");
        lblCurrentTimezone.setIcon(GuiConstants.ICON_LOCATION_TIMEZONE);
        lblCurrentTimezone.setText(_timezone);
        GridBagConstraints gbc_lblCurrentTimezone = new GridBagConstraints();
        gbc_lblCurrentTimezone.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentTimezone.gridx = 0;
        gbc_lblCurrentTimezone.gridy = 1;
        panelLocation_1.add(lblCurrentTimezone, gbc_lblCurrentTimezone);

        separator_1 = new JSeparator();
        separator_1.setForeground(Color.BLACK);
        GridBagConstraints gbc_separator_1 = new GridBagConstraints();
        gbc_separator_1.weightx = 1.0;
        gbc_separator_1.insets = new Insets(0, 5, 0, 5);
        gbc_separator_1.gridwidth = 2;
        gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_separator_1.gridx = 0;
        gbc_separator_1.gridy = 2;
        panelLocation_1.add(separator_1, gbc_separator_1);

        chckbxIPLocation = new JCheckBox("Get the location automatically. (Need internet connection !)");
        chckbxIPLocation.setToolTipText("It uses your public ip address to guess your current location.");
        chckbxIPLocation.setSelected(isAutomaticLocation);
        chckbxIPLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chckbxIPLocation_ActionPerformed(e);
                }
                catch (Exception exception) {
                    setErrorLabelLoc();
                }
            }
        });
        GridBagConstraints gbc_chckbxIPLocation = new GridBagConstraints();
        gbc_chckbxIPLocation.anchor = GridBagConstraints.WEST;
        gbc_chckbxIPLocation.insets = new Insets(0, 0, 0, 5);
        gbc_chckbxIPLocation.gridx = 0;
        gbc_chckbxIPLocation.gridy = 3;
        panelLocation_1.add(chckbxIPLocation, gbc_chckbxIPLocation);

        panelLocation_2 = new JPanel();
        GridBagConstraints gbc_panelLocation_2 = new GridBagConstraints();
        gbc_panelLocation_2.gridwidth = 2;
        gbc_panelLocation_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelLocation_2.anchor = GridBagConstraints.BASELINE;
        gbc_panelLocation_2.insets = new Insets(0, 5, 5, 5);
        gbc_panelLocation_2.gridx = 0;
        gbc_panelLocation_2.gridy = 1;
        add(panelLocation_2, gbc_panelLocation_2);
        GridBagLayout gbl_panelLocation_2 = new GridBagLayout();
        gbl_panelLocation_2.columnWidths = new int[] { 0, 0 };
        gbl_panelLocation_2.rowHeights = new int[] { 0, 0 };
        gbl_panelLocation_2.columnWeights = new double[] { 1.0, 0.0 };
        gbl_panelLocation_2.rowWeights = new double[] { 0.0, 0.0 };
        panelLocation_2.setLayout(gbl_panelLocation_2);

        lblSearchLoc = new JLabel("Search the location");
        lblSearchLoc.setFont(new Font("Calibri", lblSearchLoc.getFont().getStyle()
                | Font.BOLD | Font.ITALIC, 14));
        lblSearchLoc.setEnabled(!isAutomaticLocation);
        GridBagConstraints gbc_lblSearchLoc = new GridBagConstraints();
        gbc_lblSearchLoc.gridwidth = 2;
        gbc_lblSearchLoc.anchor = GridBagConstraints.WEST;
        gbc_lblSearchLoc.insets = new Insets(0, 0, 5, 0);
        gbc_lblSearchLoc.gridx = 0;
        gbc_lblSearchLoc.gridy = 0;
        panelLocation_2.add(lblSearchLoc, gbc_lblSearchLoc);

        scrollPane = new JScrollPane();
        scrollPane.setEnabled(!isAutomaticLocation);
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.anchor = GridBagConstraints.WEST;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        panelLocation_2.add(scrollPane, gbc_scrollPane);

        textFieldSearchLocation = new JSearchTextField();
        textFieldSearchLocation.setEnabled(!isAutomaticLocation);
        textFieldSearchLocation.setTextWhenNotFocused("Search ...");
        /*
         * FIXME: there is a problem with the apple look&feel, the text "search"
         * is under the icon, thus before solving that bug, we currently do not
         * set the icon search :(
         */
        if (!UIManager.getLookAndFeel().getID().equals("Aqua")) {
            textFieldSearchLocation.setIcon(GuiConstants.ICON_SEARCH_LOCATION);
        }
        textFieldSearchLocation.setColumns(10);
        textFieldSearchLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLocation_ActionPerformed(e);
            }
        });
        textFieldSearchLocation.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        textField_doLiveSearch(e);
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        textField_doLiveSearch(e);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                });
        scrollPane.setColumnHeaderView(textFieldSearchLocation);

        listLocation = new JList();
        listLocation.setEnabled(!isAutomaticLocation);
        listModellocation = new DefaultListModel();
        listLocation.setModel(listModellocation);
        listLocation.setVisibleRowCount(5);
        listLocation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listLocation.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int index = listLocation.getSelectedIndex();
                    if (index == -1) {
                        // No selection ...
                    } else {
                        textFieldSearchLocation.setText(
                                (String) listModellocation.elementAt(index));
                    }
                }
            }
        });
        scrollPane.setViewportView(listLocation);

        btnSetLocation = new JButton("Set");
        btnSetLocation.setEnabled(!isAutomaticLocation);
        GridBagConstraints gbc_btnSetLocation = new GridBagConstraints();
        gbc_btnSetLocation.insets = new Insets(0, 0, 5, 0);
        gbc_btnSetLocation.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnSetLocation.gridx = 1;
        gbc_btnSetLocation.gridy = 1;
        btnSetLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLocation_ActionPerformed(e);
            }
        });
        panelLocation_2.add(btnSetLocation, gbc_btnSetLocation);

        separator_2 = new JSeparator();
        separator_2.setForeground(Color.BLACK);
        GridBagConstraints gbc_separator_2 = new GridBagConstraints();
        gbc_separator_2.weightx = 1.0;
        gbc_separator_2.gridwidth = 2;
        gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_separator_2.insets = new Insets(0, 5, 0, 5);
        gbc_separator_2.gridx = 0;
        gbc_separator_2.gridy = 2;
        add(separator_2, gbc_separator_2);

        panelTimezone = new JPanel();
        GridBagConstraints gbc_panelTimezone = new GridBagConstraints();
        gbc_panelTimezone.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelTimezone.anchor = GridBagConstraints.BASELINE;
        gbc_panelTimezone.insets = new Insets(0, 5, 5, 5);
        gbc_panelTimezone.gridx = 0;
        gbc_panelTimezone.gridy = 3;
        add(panelTimezone, gbc_panelTimezone);
        GridBagLayout gbl_panelTimezone = new GridBagLayout();
        gbl_panelTimezone.columnWidths = new int[] { 0, 0, 0 };
        gbl_panelTimezone.rowHeights = new int[] { 0, 0, 0, 0 };
        gbl_panelTimezone.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panelTimezone.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panelTimezone.setLayout(gbl_panelTimezone);

        lblTimeZone = new JLabel("Time Zone:");
        GridBagConstraints gbc_lblTimeZone = new GridBagConstraints();
        gbc_lblTimeZone.anchor = GridBagConstraints.WEST;
        gbc_lblTimeZone.insets = new Insets(0, 0, 5, 5);
        gbc_lblTimeZone.gridx = 0;
        gbc_lblTimeZone.gridy = 0;
        panelTimezone.add(lblTimeZone, gbc_lblTimeZone);

        chckbxUseSystemTime = new JCheckBox("Use system time zone");
        chckbxUseSystemTime.setSelected(isUseSystemTimezone);
        GridBagConstraints gbc_chckbxUseSystemTime = new GridBagConstraints();
        gbc_chckbxUseSystemTime.anchor = GridBagConstraints.WEST;
        gbc_chckbxUseSystemTime.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxUseSystemTime.gridx = 1;
        gbc_chckbxUseSystemTime.gridy = 0;
        chckbxUseSystemTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chckbxUseSystemTime_actionPerformed(e);
            }
        });
        panelTimezone.add(chckbxUseSystemTime, gbc_chckbxUseSystemTime);

        comboBoxTimezones = new JComboBox();
        comboBoxTimezones.setEnabled(!isUseSystemTimezone);
        comboBoxTimezones.setMaximumRowCount(10);
        comboBoxTimezones.setModel(new DefaultComboBoxModel(CommonUtils.getAllTimezonesSorted()));
        comboBoxTimezones.setSelectedItem(_timezone);
        GridBagConstraints gbc_comboBoxTimezones = new GridBagConstraints();
        gbc_comboBoxTimezones.anchor = GridBagConstraints.WEST;
        gbc_comboBoxTimezones.insets = new Insets(0, 0, 5, 0);
        gbc_comboBoxTimezones.gridx = 1;
        gbc_comboBoxTimezones.gridy = 1;
        comboBoxTimezones.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                comboBoxTimezones_itemStateChanged(e);
            }
        });
        panelTimezone.add(comboBoxTimezones, gbc_comboBoxTimezones);

        chckbxDaylightSavings = new JCheckBox("Daylight Savings");
        chckbxDaylightSavings.setEnabled(!isUseSystemTimezone);
        chckbxDaylightSavings.setSelected(isUseDayLightSavings);
        GridBagConstraints gbc_chckbxDaylightSavings = new GridBagConstraints();
        gbc_chckbxDaylightSavings.anchor = GridBagConstraints.WEST;
        gbc_chckbxDaylightSavings.gridx = 1;
        gbc_chckbxDaylightSavings.gridy = 2;
        chckbxDaylightSavings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chckbxDaylightSavings_actionPerformed(e);
            }
        });
        panelTimezone.add(chckbxDaylightSavings, gbc_chckbxDaylightSavings);

    }

    // ======================================================================

    @Override
    public synchronized void update(Observable o, Object currentLocState) {
        Icon icon = null;
        LocationStateLabel.State s = (LocationStateLabel.State) currentLocState;
        switch (s) {
            case OK:
                icon = GuiConstants.ICON_LOCATION_OK;
                break;
            case NOT_OK:
                icon = GuiConstants.ICON_LOCATION_NOT_OK;
                break;
            case SEARCHING:
                icon = GuiConstants.ICON_LOCATION_SEARCHING;
                break;
            default:
                icon = GuiConstants.ICON_UNKNOWN_STATE;
                break;
        }
        lblCurrentLoc.setIcon(icon);
        setIconFlagFromCountryName();
    }

    // ======================================================================
    // ---------- ACTIONS ...

    private void chckbxIPLocation_ActionPerformed(ActionEvent e)
            throws IOException {
        boolean currentSelection = chckbxIPLocation.isSelected();
        try {
            lblSearchLoc.setEnabled(!currentSelection);
            textFieldSearchLocation.setEnabled(!currentSelection);
            listLocation.setEnabled(!currentSelection);
            btnSetLocation.setEnabled(!currentSelection);

            if (currentSelection == true) {
                try {
                    getIPLocation();
                } catch (IOException ioe) {
                    _locationIconState.setState(LocationStateLabel.State.NOT_OK);
                    logger.error(ioe.getMessage());
                    // ioe.printStackTrace();
                }
            } else { // (no GeoIp work ...) Search into the Lucene index.
                try {
                    String entry = textFieldSearchLocation.getText();
                    _locationIconState.setState(LocationStateLabel.State.SEARCHING);
                    searchWorldCitiesLucene(entry);
                    _locationIconState.setState(LocationStateLabel.State.OK);
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                } catch (ParseException pe) { /* pe.printStackTrace(); */
                }
            }
        } finally {
            userPrefs.putBoolean(
                    guiSettings.LOCATION_VIA_IP.toString(), currentSelection);
        }
    }

    // ------

    private void setLocation_ActionPerformed(ActionEvent e) {

        _locationIconState.setState(LocationStateLabel.State.SEARCHING);
        String[] country_city = textFieldSearchLocation.getText().split(locationSeparator);
        Coordinates coord = null;
        WorldCitiesDB db = new WorldCitiesDB();

        // Get the latitude and the longitude.
        try {
            if (country_city.length == 2) {
                String country = country_city[0].trim();
                String city = country_city[1].trim();
                coord = db.getCoordinates(country, city);
            } else {
                setErrorLabelLoc();
                return;
            }
        } catch (SQLException sqle) {
            setErrorLabelLoc();
            sqle.printStackTrace();
            return;
        }

        if (coord != null) {
            String locationString = textFieldSearchLocation.getText();
            setLocationFullName(locationString);
            _locationIconState.setState(LocationStateLabel.State.OK);
            logger.debug("Location  : {}", locationString);
            logger.debug("Latitude  : {}", coord.getLatitude());
            logger.debug("Longitude : {}", coord.getLongitude());
        } else {
            setErrorLabelLoc();
            return;
        }
    }

    // ------

    private synchronized void setLocationFullName(String name) {
        lblCurrentLoc.setText(name);
        userPrefs.put(
                guiSettings.LOCATION_FULL_NAME.toString(), name);
    }

    // ------

    private void setErrorLabelLoc() {
        _locationIconState.setState(LocationStateLabel.State.NOT_OK);
        lblCurrentLoc.setText(errorLocMsg);
        lblCurrentTimezone.setText(null);
        logger.error(errorLocMsg);
    }

    // ------

    private void textField_doLiveSearch(DocumentEvent e) {
        int currentTextLength = e.getDocument().getLength();
        Icon currentIcon = lblSearchLoc.getIcon();
        if (currentIcon == null && currentTextLength > 0) {
            new Thread() {
                @Override
                public void run() {
                    lblSearchLoc.setIcon(GuiConstants.ICON_LOCATION_SEARCHING);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                    }
                    lblSearchLoc.setIcon(null);
                }
            }.start();
        }

        if (currentTextLength < 2) {
            if (currentTextLength == 0) {
                listModellocation.removeAllElements();
            }
            return; // We should not loose time searching for an entry lesser
                    // than 2 characters ...
        }

        String currentText = "";
        try {
            currentText = e.getDocument().getText(0, currentTextLength);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        try {
            searchWorldCitiesLucene(currentText);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ParseException pe) {
        }
    }

    // ------

    private void chckbxUseSystemTime_actionPerformed(ActionEvent e) {
        boolean currentSelection = chckbxUseSystemTime.isSelected();
        comboBoxTimezones.setEnabled(!currentSelection);
        chckbxDaylightSavings.setEnabled(!currentSelection);
        setSelectedTimezone(TimeZone.getDefault().getID());
        if (currentSelection == false) {
            setSelectedTimezone((String) comboBoxTimezones.getSelectedItem());
        }
        userPrefs.putBoolean(
                guiSettings.LOCATION_USE_SYS_TIMEZONE.toString(), currentSelection);
    }

    // ------

    private void comboBoxTimezones_itemStateChanged(ItemEvent e) {
        if (comboBoxTimezones.isEnabled()) {
            String tmz = (String) e.getItem();
            setSelectedTimezone(tmz);
            userPrefs.put(guiSettings.LOCATION_TIMEZONE.toString(), tmz);
        }
    }

    // ------

    private void chckbxDaylightSavings_actionPerformed(ActionEvent e) {
        if (chckbxDaylightSavings.isEnabled()) {
            boolean currentSelection = chckbxDaylightSavings.isSelected();
            userPrefs.putBoolean(
                    guiSettings.LOCATION_USE_DST.toString(), currentSelection);
        }
    }

    // ======================================================================

    /**
     * http://lucene.apache.org/java/2_9_1/queryparsersyntax.html
     */
    private void searchWorldCitiesLucene(String entry) throws SQLException,
            IOException, ParseException {
        List<String[]> locations = new ArrayList<String[]>();
        entry = (entry.isEmpty() || entry == null)
                ? "_____" : entry;
        locations = WorldCitiesLucene.search(entry + "~"); // fuzzy search ...
        listModellocation.removeAllElements();
        for (int i = 0; i < locations.size(); i++) {
            String country = locations.get(i)[0];
            String city = locations.get(i)[1];
            listModellocation.addElement(country + locationSeparator + city);
        }
    }

    // ------

    private void getIPLocation() throws IOException {
        _locationIconState.setState(LocationStateLabel.State.SEARCHING);
        String _ip = CommonUtils.retreiveIpAddress();
        if (_ip.equals("-1"))
            logger.error("The Ip address cannot be '-1', an error occured.");
        Location location = new LookupService(WSConstants.GEOIP_DATABASE_FULLPATH).getLocation(_ip);
        String city = (location.city != null)
                ? location.city : "";
        String country = (location.countryName != null)
                ? location.countryName : "";
        String locationString = country + locationSeparator + city;
        setLocationFullName(locationString);
        if (textFieldSearchLocation != null) {
            textFieldSearchLocation.resetContent();
        }
        _coordinates.setLatitude(location.latitude);
        _coordinates.setLongitude(location.longitude);
        setSelectedTimezone(timeZone.timeZoneByCountryAndRegion(location.countryCode, location.region));
        _locationIconState.setState(LocationStateLabel.State.OK);
        logger.debug("Ip Address: {}", _ip);
        logger.debug("Location  : {}", locationString);
        logger.debug("Latitude  : {}", _coordinates.getLatitude());
        logger.debug("Longitude : {}", _coordinates.getLongitude());
    }

    // ======================================================================

    /**
     * Convenience class to use to set some kind of state for a component ...<br>
     * We will use it in order to set the icon (ok, bad, loading) while doing a
     * search for a location.
     */
    static class LocationStateLabel extends Observable {
        private static enum State {
            OK,
            NOT_OK,
            SEARCHING,
            UNKNOWN;
        }

        private static State  _state = State.UNKNOWN;
        private static Object lock   = new Object();

        public void setState(State newState) {
            synchronized (lock) {
                _state = newState;
            }
            setChanged();
            notifyObservers(_state);
        }

        public State getState() {
            synchronized (lock) {
                return _state;
            }
        }
    }

    // ======================================================================

    private synchronized void setIconFlagFromCountryName() {
        if (lblFlag == null)
            lblFlag = new JLabel();
        if (lblCurrentLoc == null)
            lblCurrentLoc = new JLabel();

        WorldCitiesDB db = new WorldCitiesDB();
        String cn = null; // 'cn' like country name && 'cc' like country code.
        String locationString = lblCurrentLoc.getText();
        locationString = (locationString == null)
                ? "" : locationString;
        cn = locationString.split(locationSeparator)[0].trim();
        try {
            String cc = db.getCountryCodeFromCountryName(cn);
            if (cc == null) {
                cc = "-";
            } else if (cc.isEmpty()) {
                cc = "unknown";
            }
            lblFlag.setIcon(new ImageIcon(
                    ClassLoader.getSystemResource(
                            GuiConstants.ICON_FLAGS_DIR + cc.toLowerCase()
                                    + ".gif")));
        } catch (SQLException e) {
            logger.error("Error while retreiving country from coordinates !!!");
            logger.error(e.getMessage());
        }
    }

    // ======================================================================
    // Getters / Setters ...

    public synchronized static Coordinates getCoordinates() {
        return _coordinates;
    }

    public synchronized static void setCoordinates(Coordinates coordinates) {
        _coordinates = coordinates;
    }

    public synchronized void setCountryCode(String countryCode) {
        _countryCode = countryCode;
        setIconFlagFromCountryName();
        userPrefs.put(
                guiSettings.LOCATION_COUNTRY_CODE.toString(), _countryCode);
    }

    public synchronized static String getCountryCode() {
        return _countryCode;
    }

    public synchronized static String getSelectedTimezone() {
        return _timezone;
    }

    public synchronized void setSelectedTimezone(String timezone) {
        _timezone = timezone;
        if (lblCurrentTimezone == null)
            lblCurrentTimezone = new JLabel();
        lblCurrentTimezone.setText(_timezone);
        userPrefs.put(
                guiSettings.LOCATION_TIMEZONE.toString(), _timezone);
    }

    // ======================================================================

}
