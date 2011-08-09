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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.paissad.waqtsalat.utils.CommonUtils;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class AudioPlayerFactory {

    private static Logger logger = LoggerFactory.getLogger(AudioPlayerFactory.class);

    public static enum AudioType {
        BASIC,
        MP3
    }

    private AudioPlayerFactory() {
    }

    /**
     * 
     * @param file
     * @param listener
     * @return An instance of {@link AudioPlayer}.
     * @throws Exception
     */
    public static AudioPlayer getInstance(final File file, final AudioPlayerListener listener) throws Exception {

        String errMsg;
        if (file == null) {
            errMsg = "The audio file to read cannot be null.";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        String extension = CommonUtils
                .getFilenameExtension(file.getName())
                .toLowerCase();

        AudioType type;

        if (extension.equals(".mp3")) {
            type = AudioType.MP3;

        } else if (CommonUtils.getSupportedAudioTargetTypes().contains(extension)) {
            type = AudioType.BASIC;

        } else {
            logger.warn("The extension '{}' of the audio file is unknown, an error may occur.", extension);
            type = AudioType.BASIC;
        }

        InputStream in = new BufferedInputStream(new FileInputStream(file));
        return getInstance(in, type, listener);
    }

    /**
     * 
     * @param in
     *            - The audio file.
     * @param type
     *            - The type of the file.
     * @param listener
     * @return An instance of {@link AudioPlayer}.
     * @throws Exception
     * @see AudioType
     */
    public static AudioPlayer getInstance(
            final InputStream in,
            final AudioType type,
            final AudioPlayerListener listener) throws Exception {

        String errMsg;
        if (in == null) {
            errMsg = "The specified InputStream cannot be null";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        switch (type) {

            case MP3:
                return new Mp3Player(in, listener);

            case BASIC:
                return new BasicAudioPlayer(in, listener);

            default:
                errMsg = "The specified audio type + (" + type + ") is not supported !";
                logger.error(errMsg);
                throw new Exception(errMsg);
        }
    }

}
