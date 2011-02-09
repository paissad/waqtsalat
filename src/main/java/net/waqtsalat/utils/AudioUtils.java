/*
 * WaqtSalat, for indicating the muslim prayers times in most cities.
 * Copyright (C) 2011  Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.
 * 
 */

package net.waqtsalat.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import net.waqtsalat.WaqtSalat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains some utilities related to audio files such as playing an audio file
 * for example.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class AudioUtils implements PlayerUtils {

	Logger logger = LoggerFactory.getLogger(WaqtSalat.class);

	protected File audioFile = null;
	private boolean stop = false; // if set to true, then do no play the audio
									// file !

	/**
	 * Constructs an {@link AudioUtils} object.
	 */
	public AudioUtils() {
	}

	// =======================================================================

	/**
	 * Construts an {@link AudioUtils} object from the specified file.
	 * 
	 * @param audioFile
	 *            File to use.
	 */
	public AudioUtils(File audioFile) {
		this.audioFile = audioFile;
	}

	// =======================================================================

	/**
	 * Constructs an {@link AudioUtils} object from the specified file.
	 * 
	 * @param audioFileName
	 *            Name of the file to use.
	 */
	public AudioUtils(String audioFileName) {
		this.audioFile = new File(audioFileName);
	}

	// =======================================================================

	/**
	 * Get the audio file for this object.
	 * 
	 * @return File of this object.
	 */
	public File getAudioFile() {
		return this.audioFile;
	}

	// =======================================================================

	/**
	 * Set the audio file to use.
	 * 
	 * @param audioFile
	 *            Audio file to use.
	 */
	public void setAudioFile(File audioFile) {
		this.audioFile = audioFile;
	}

	// =======================================================================

	/**
	 * Get all audio types supported by the system.
	 * 
	 * @return Return a String array containing the supported audio formats.
	 */
	public static String[] getSupportedTargetTypes() {

		ArrayList<String> list = new ArrayList<String>();
		for (AudioFileFormat.Type t : AudioSystem.getAudioFileTypes())
			list.add(t.getExtension());

		return list.toArray(new String[list.size()]);
	}

	// =======================================================================

	/**
	 * Trying to get an audio file type for the passed extension. This works by
	 * examining all available file types.<br />
	 * For each type, if the extension this type promisses to handle matches the
	 * extension we are trying to find a type for, this type is returned.<br />
	 * If no appropriate type is found, null is returned.
	 * 
	 * @param extension
	 * @return Return a String array containing supported extensions.
	 */
	public static AudioFileFormat.Type getSupportedExtensions(String extension) {
		AudioFileFormat.Type[] aTypes = AudioSystem.getAudioFileTypes();
		for (int i = 0; i < aTypes.length; i++) {
			if (aTypes[i].getExtension().equals(extension)) {
				return aTypes[i];
			}
		}
		return null;
	}

	// =======================================================================

	@Override
	public void stop() {
		stop = true;
	}

	// =======================================================================

	@Override
	public void play() {

		// run in new thread to play in background
		new Thread("audioPlayer") {
			public void run() {

				AudioInputStream audioInputStream = null;
				SourceDataLine line = null;
				try {
					audioInputStream = AudioSystem
							.getAudioInputStream(audioFile);

					AudioFormat audioFormat = audioInputStream.getFormat();
					logger.trace("AUDIO FORMAT: {}", audioFormat.toString());

					DataLine.Info info = new DataLine.Info(
							SourceDataLine.class, audioFormat);
					logger.trace("DATALINE INFO: {}", info.toString());

					line = (SourceDataLine) AudioSystem.getLine(info);
					logger.trace("LINE: {}", line.toString());

					stop = false;
					line.open(audioFormat); // Open the line with the right
											// audio format.
					line.start(); // Start the line (send the stream to the
									// sound card).

					// Now, we have to write to the line (like any other kind of
					// InputStream)
					int BUFFER = 4096;
					byte[] data = new byte[BUFFER];
					int bytesRead = 0;
					logger.debug("Playing audio file '{}'",
							audioFile.getAbsolutePath());
					while ((bytesRead = audioInputStream.read(data, 0,
							data.length)) != -1 && !stop) {
						line.write(data, 0, bytesRead);
					}

					line.close();
					stop = true;
					logger.debug("Finished playing audio file '{}'",
							audioFile.getAbsolutePath());

				} catch (Exception e) {
					logger.error("Problem while playing audio file"
							+ audioFile.getAbsolutePath());
					e.printStackTrace();
					try {
						if (audioInputStream != null)
							audioInputStream.close();

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					if (line != null)
						line.close();
					return;
				}
			}
		}.start();

	}
	// =======================================================================

}
