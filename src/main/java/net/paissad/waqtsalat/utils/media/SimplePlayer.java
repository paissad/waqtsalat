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

package net.paissad.waqtsalat.utils.media;

import java.io.File;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.utils.media.AudioPlayerFactory.AudioType;

/**
 * A simple player for MP3 & common audio files.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 */
public class SimplePlayer implements AudioPlayer {

    private static Logger logger = LoggerFactory.getLogger(SimplePlayer.class);

    private String        filename;
    private AudioPlayer   player;

    /**
     * @param filename
     *            - The name of the file to use.
     * @throws Exception
     */
    public SimplePlayer(final String filename) throws Exception {
        this(new File(filename));
    }

    /**
     * @param file
     *            - File to use.
     * @throws Exception
     */
    public SimplePlayer(final File file) throws Exception {
        this(file, null);
    }

    /**
     * 
     * @param url
     *            - The url of the resource to play.
     * @throws Exception
     */
    public SimplePlayer(final URL url) throws Exception {
        try {
            this.filename = url.getPath();
            AudioType type = AudioPlayerFactory.guessTypeFromExtension(getFilename());
            this.player = AudioPlayerFactory.getInstance(url.openStream(), type, null);

        } catch (Exception e) {
            logger.error("Error while getting an instance of AudioPlayer : ", e);
            throw new Exception(e);
        }
    }

    /**
     * 
     * @param file
     *            - The file to use.
     * @param listener
     *            - The listener "connected" to the playback of the audio file.
     * @throws Exception
     */
    public SimplePlayer(final File file, final AudioPlayerListener listener) throws Exception {
        try {
            this.filename = file.getAbsolutePath();
            this.player = AudioPlayerFactory.getInstance(file, listener);

        } catch (Exception e) {
            String errMsg = "Error while getting an instance of AudioPlayer";
            logger.error(errMsg, e);
            throw new Exception(errMsg, e);
        }
    }

    @Override
    public void stop() throws Exception {
        player.stop();
    }

    @Override
    public void play() throws Exception {
        player.play();
    }

    private String getFilename() {
        return filename;
    }

    /*
     * For testing purpose only ! XXX
     */
    public static void main(String[] args) throws Exception {
        SimplePlayer player = new SimplePlayer("src/test/resources/sounds/alarm.mp3");
        player.play();
    }

}
