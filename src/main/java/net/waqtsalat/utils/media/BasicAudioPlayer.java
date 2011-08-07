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

package net.waqtsalat.utils.media;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Papa Issa DIAKHATE (paissad)
 */
class BasicAudioPlayer implements AudioPlayer {

    private static Logger logger = LoggerFactory.getLogger(BasicAudioPlayer.class);

    Clip                  clip   = null;

    /**
     * 
     * @param in
     * @param listener
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public BasicAudioPlayer(final InputStream in, final AudioPlayerListener listener) throws
            UnsupportedAudioFileException, IOException, LineUnavailableException {

        final AudioInputStream audioStream = AudioSystem.getAudioInputStream(in);
        final float sampleRate = 44100.0f;
        final int sampleSizeInBits = 8;
        final int channels = 2;
        final boolean signed = true;
        final boolean bigEndian = true;

        final AudioFormat audioFormat = new AudioFormat(
                sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        final int bufferSize = 4096;
        final DataLine.Info info = new DataLine.Info(Clip.class, audioFormat, bufferSize);
        this.clip = (Clip) AudioSystem.getLine(info);
        if (!AudioSystem.isLineSupported(info)) {
            logger.warn("The audio line is not supported : {}", info);
        }
        clip.open(audioStream);
    }

    @Override
    public void stop() {
        clip.drain();
        clip.stop();
        clip.close();
    }

    @Override
    public void play() {
        try {
            clip.start();
        } catch (Exception e) {
            logger.error("Error while playing the audio file : {}", e);
        } finally {
            clip.drain();
            clip.close();
        }
    }

}
