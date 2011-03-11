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

import static net.waqtsalat.WaqtSalat.logger;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

/**
 * Contains some utilities related to audio files such as playing an audio file
 * for example.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class AudioUtils implements PlayerUtils {

	protected File _audioFile = null;
	private AudioClip _audioClip = null;

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
		_audioFile = audioFile;
		try {
			_audioClip = Applet.newAudioClip(_audioFile.toURI().toURL());
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
			_audioClip = null;
		}
	}

	// =======================================================================

	/**
	 * Constructs an {@link AudioUtils} object from the specified file.
	 * 
	 * @param audioFileName
	 *            Name of the file to use.
	 */
	public AudioUtils(String audioFileName) {
		_audioFile = new File(audioFileName);
	}

	// =======================================================================

	public File getAudioFile() {
		return _audioFile;
	}

	public void setAudioFile(File audioFile) {
		_audioFile = audioFile;
	}

	// =======================================================================

	/**
	 * Gets all audio types supported by the system.
	 * 
	 * @return An array of <code>String</code> containing the supported audio
	 *         formats.
	 */
	public static String[] getSupportedTargetTypes() {

		ArrayList<String> list = new ArrayList<String>();
		for (AudioFileFormat.Type t : AudioSystem.getAudioFileTypes())
			list.add(t.getExtension().toLowerCase());
		if (list.contains("aif")) // let's add manually, the aiff extension 
			list.add("aiff");

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
	 * @return An array of <code>String</code> containing supported extensions.
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
		if (_audioClip != null)
			_audioClip.stop();
	}

	// =======================================================================

	@Override
	public void play() {
		logger.debug("Playing audio file '{}'.",
				_audioFile.getAbsolutePath());
		_audioClip.play();
		logger.debug("Finished playing audio file '{}'.",
				_audioFile.getAbsolutePath());
	}
	// =======================================================================

}
