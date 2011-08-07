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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.waqtsalat.utils.CommonUtils;
import net.waqtsalat.utils.media.SimplePlayer;
import net.waqtsalat.utils.tags.BasicTagsReader;

/**
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class AdhanComboBox extends JComboBox {

    private static final long serialVersionUID = 1L;

    private static Logger     logger           = LoggerFactory.getLogger(AdhanComboBox.class);

    public static enum Selection {

        NONE("None"),
        DEFAULT_ADHAN_SOUND("Default adhan"),
        SELECT_FILE("Select file ...");

        private String value;

        private Selection(String valueName) {
            value = valueName;
        }

        public String getDescName() {
            return value;
        }
    };

    private HashMap<Selection, String> _possibleSelections = new HashMap<AdhanComboBox.Selection, String>();

    private String                     defaultAdhan;
    private String                     defaultSelectDir;
    private JFileChooser               adhanChooser;

    // ======================================================================
    // Constructors ...

    public AdhanComboBox() {
        this(GuiConstants.DEFAULT_ADHAN_SOUND);
    }

    public AdhanComboBox(String defaultAdhanSound) {

        initSelection_Names();
        setModel(new DefaultComboBoxModel(
                _possibleSelections.values().toArray(new String[_possibleSelections.size()])));
        setSelectedItem(Selection.NONE.getDescName()); // default item to use
                                                       // ...
        this.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adhanListSelection_actionPerformed(e);
            }
        });
    }

    // ======================================================================

    private void adhanListSelection_actionPerformed(ItemEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String currentSeletion = (String) cb.getSelectedItem();
        if (currentSeletion.equals(Selection.NONE.getDescName())) { // TODO ...

        } else if (currentSeletion.equals(Selection.DEFAULT_ADHAN_SOUND.getDescName())) { // TODO
                                                                                          // ...

        } else if (currentSeletion.equals(Selection.SELECT_FILE.getDescName())) { // TODO
                                                                                  // ...
            System.out.println("selection ...");
            adhanChooser = new adhanJFileChooser();
            adhanChooser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (JFileChooser.CANCEL_SELECTION.equals(e.getActionCommand())) {
                        System.out.println("selected -> "
                                + ((File) e.getSource()).getName()); // TODO
                        // adhanChooser.setVisible(false); // TODO ...
                    } else if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
                        System.out.println("canceled ..."); // TODO
                        // adhanChooser.setVisible(false); // TODO ...
                    } else { // TODO
                        System.out.println("unknown response ...");
                    }
                }
            });
            adhanChooser.showOpenDialog(getParent());

        } else {
            // Not known selection ...
        }
    }

    // ======================================================================

    class adhanJFileChooser extends JFileChooser {

        private static final long serialVersionUID = 1L;

        public adhanJFileChooser() {
            super();
            setDefaultSelectDir(GuiConstants.DEFAULT_ADHAN_DIR);
            setCurrentDirectory(new File(getDefaultSelectDir()));
            setFileSelectionMode(JFileChooser.FILES_ONLY);
            addChoosableFileFilter(new AdhanFileFilter());
            setFileHidingEnabled(true);
            AudioAccessory adhanAccessory = new AudioAccessory();
            setAccessory(adhanAccessory);
            addPropertyChangeListener(adhanAccessory); // to receive selection
                                                       // changes
            addActionListener(adhanAccessory); // to receive approve/cancel
                                               // button events
            setApproveButtonText("Select");
        }
    }

    // ======================================================================

    /**
     * Represents the {@link FileFilter} for choosing an Adhan ( which is an
     * audio file).
     */
    class AdhanFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            } else {
                String extension = CommonUtils.getFilenameExtension(f.getName());
                if (extension != null && !extension.isEmpty()) {
                    extension = extension.toLowerCase();
                }
                return isExtensionSupported(extension);
            }
        }

        // -----------------------------------------------

        @Override
        public String getDescription() {
            // TODO Auto-generated method stub
            return null;
        }
        // -----------------------------------------------
    }

    // ======================================================================

    /**
     * Represents the Accessory for <code>JFileChooser</code> for playing audio
     * files.<br>
     * Got some inspiration from this <a href=
     * "http://www.java2s.com/Code/Java/Swing-JFC/JFileChooserclassinactionwithanaccessory.htm"
     * >link</a>.
     */
    class AudioAccessory extends JPanel implements PropertyChangeListener, ActionListener {

        private static final long serialVersionUID = 1L;

        private SimplePlayer      audioPlayer;
        private String            currentFileName  = new String();
        private JPanel            topPanel;
        private JLabel            fileLabel;
        private JLabel            tagsLabel;
        private JPanel            playerPanel;
        private JButton           playButton, stopButton;

        // -----------------------------------------------

        /**
         * Default constructor.
         */
        public AudioAccessory() {

            GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
            gridBagLayout.rowHeights = new int[] { 0, 0 };
            gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
            gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
            setLayout(gridBagLayout);

            topPanel = new JPanel();
            GridBagConstraints gbc_topPanel = new GridBagConstraints();
            gbc_topPanel.insets = new Insets(0, 5, 5, 5);
            gbc_topPanel.anchor = GridBagConstraints.NORTHWEST;
            gbc_topPanel.gridx = 1;
            gbc_topPanel.gridy = 0;
            add(topPanel, gbc_topPanel);
            GridBagLayout gbl_topPanel = new GridBagLayout();
            gbl_topPanel.columnWidths = new int[] { 0, 0 };
            gbl_topPanel.rowHeights = new int[] { 0, 0, 0, 0 };
            gbl_topPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
            gbl_topPanel.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
            topPanel.setLayout(gbl_topPanel);

            fileLabel = new JLabel("Audio File", JLabel.CENTER);
            GridBagConstraints gbc_fileLabel = new GridBagConstraints();
            gbc_fileLabel.insets = new Insets(0, 0, 5, 0);
            gbc_fileLabel.gridx = 0;
            gbc_fileLabel.gridy = 0;
            gbc_fileLabel.anchor = GridBagConstraints.NORTHWEST;
            topPanel.add(fileLabel, gbc_fileLabel);

            playerPanel = new JPanel();
            GridBagConstraints gbc_playerPanel = new GridBagConstraints();
            gbc_playerPanel.insets = new Insets(0, 0, 5, 0);
            gbc_playerPanel.fill = GridBagConstraints.BOTH;
            gbc_playerPanel.gridx = 0;
            gbc_playerPanel.gridy = 1;
            gbc_playerPanel.anchor = GridBagConstraints.WEST;
            topPanel.add(playerPanel, gbc_playerPanel);

            playButton = new JButton("Play");
            playButton.setIcon(GuiConstants.ICON_SOUND_PLAY);
            playButton.setEnabled(false);
            playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (audioPlayer != null) {
                        audioPlayer.stop();
                        audioPlayer.play();
                    }
                }
            });
            playerPanel.add(playButton);

            stopButton = new JButton("Stop");
            stopButton.setIcon(GuiConstants.ICON_SOUND_STOP);
            stopButton.setEnabled(false);
            stopButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (audioPlayer != null) {
                        audioPlayer.stop();
                    }
                }
            });
            playerPanel.add(stopButton);

            tagsLabel = new JLabel("");
            tagsLabel.setFont(new Font("Calibri", Font.PLAIN, 13));
            GridBagConstraints gbc_tagsLabel = new GridBagConstraints();
            gbc_tagsLabel.gridx = 0;
            gbc_tagsLabel.gridy = 2;
            gbc_tagsLabel.anchor = GridBagConstraints.SOUTHWEST;
            tagsLabel.setHorizontalAlignment(SwingConstants.LEADING);
            topPanel.add(tagsLabel, gbc_tagsLabel);
        }

        // -----------------------------------------------

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(propertyName)) {
                File f = (File) evt.getNewValue();
                String audioExtension = CommonUtils.getFilenameExtension(f.getName());
                if (audioExtension != null && !audioExtension.isEmpty()) {
                    audioExtension = audioExtension.toLowerCase();
                }

                // Make reasonably sure it's an audio file
                if (isExtensionSupported(audioExtension)) {
                    setCurrentClip(f);
                    tagsLabel.setText(getTagsInfos(f));
                } else {
                    setCurrentClip(null);
                }
            } else {
                setCurrentClip(null);
            }
        }

        // -----------------------------------------------

        private void setCurrentClip(File audioFile) {
            if (audioPlayer != null)
                audioPlayer.stop();

            // Make sure we have a real file, otherwise, disable the buttons
            if (audioFile == null || audioFile.getName() == null
                    || audioFile.isDirectory()) {
                fileLabel.setText("No audio selected");
                tagsLabel.setText(null);
                playButton.setEnabled(false);
                stopButton.setEnabled(false);
                return;
            }

            // Ok, seems the audio file is real, so load it and enable the
            // buttons
            String fileName = audioFile.getName();
            fileLabel.setText(trimTextWithDots(fileName));
            if (fileName.equals(currentFileName)) { // same file ...
                playButton.setEnabled(true);
                stopButton.setEnabled(true);
                return;
            }

            currentFileName = fileName;
            try {
                audioPlayer = new SimplePlayer(audioFile);
            } catch (Exception e) {
                logger.error("Error while getting an instance of SimplePlayer : ", e);
            }
            fileLabel.setText(trimTextWithDots(fileName));
            playButton.setEnabled(true);
            stopButton.setEnabled(true);
        }

        // -----------------------------------------------

        private String getTagsInfos(File file) {
            String format = "%-12s : %s<br>";
            BasicTagsReader tagger = null;
            try {
                tagger = new BasicTagsReader(file);
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            }

            StringBuilder infos = new StringBuilder();

            infos.append("<html>");
            infos.append(String.format(format, "Title", tagger.getTitle()));
            infos.append(String.format(format, "Artist", tagger.getArtist()));
            infos.append(String.format(format, "Album", tagger.getAlbum()));
            String comment = tagger.getComment();
            comment = (comment.equals("0"))
                    ? "" : comment;
            infos.append(String.format(format, "Comment", comment));
            infos.append(String.format(format, "Year", tagger.getYear()));

            AudioHeader audioHeader = tagger.getAudioHeader();

            infos.append(String.format(format, "Duration",
                    CommonUtils.formatDuration(audioHeader.getTrackLength() * 1000L)));
            String bitRate = audioHeader.getBitRate() + " kbit/s";
            infos.append(String.format(format, "Bitrate", bitRate));
            infos.append(String.format(format, "Channels", audioHeader.getChannels()));
            infos.append(String.format(format, "Encoding", audioHeader.getEncodingType()));
            infos.append(String.format(format, "Format", audioHeader.getFormat()));
            String sampleRate = audioHeader.getSampleRate() + " Hz";
            infos.append(String.format(format, "SampleRate", sampleRate));
            infos.append(String.format(format, "Size",
                    CommonUtils.humanReadableByteCount((file.length()), false)));

            infos.append("</html>");
            return infos.toString();
        }

        // -----------------------------------------------

        @Override
        public void actionPerformed(ActionEvent e) {
            // Be a little cavalier here...we're assuming the dialog was just
            // approved or cancelled so we should stop any playing clip
            if (audioPlayer != null) {
                audioPlayer.stop();
            }
        }

        // -----------------------------------------------

        private String trimTextWithDots(String text) {
            if (text != null && text.length() > 25) {
                text = text.substring(0, 25) + "...";
            }
            return text;
        }

        // -----------------------------------------------

    }

    // ======================================================================

    /**
     * Checks whether or not a file is eligible for reading/playing like an
     * audio file.
     * 
     * @param extension
     * @return <code>true</code> if the extension is known and supported,
     *         <code>false</code> otherwise or when extension is
     *         <code>null</code>.
     */
    public boolean isExtensionSupported(final String extension) {
        if (extension == null) {
            return false;
        }

        List<String> knownExtensions = CommonUtils.getSupportedAudioTargetTypes();
        for (String ext : GuiConstants.ADDITIONAL_AUDIO_EXTENSIONS) {
            knownExtensions.add(ext);
        }
        return knownExtensions.contains(extension);
    }

    // ======================================================================

    private void initSelection_Names() {
        Selection[] allSelections = Selection.values();
        for (int i = 0; i < allSelections.length; i++) {
            _possibleSelections.put(allSelections[i], allSelections[i].getDescName());
        }
    }

    // ======================================================================
    // Getters / Setters ...

    public void setDefaultAdhan(String defaultAdhan) {
        this.defaultAdhan = defaultAdhan;
    }

    public String getDefaultAdhan() {
        return defaultAdhan;
    }

    public void setDefaultSelectDir(String defaultSelectDir) {
        this.defaultSelectDir = defaultSelectDir;
    }

    public String getDefaultSelectDir() {
        return defaultSelectDir;
    }

    // ======================================================================

}
