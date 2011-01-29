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

package net.waqtsalat;

import net.waqtsalat.utils.AudioUtils;
import net.waqtsalat.utils.Mp3Utils;
import net.waqtsalat.utils.PlayerUtils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple player for MP3 & common audio files.
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class SimplePlayer implements PlayerUtils {

	Logger logger = LoggerFactory.getLogger(getClass());

	private File audioFile   = null;
	private AudioUtils audio = null;
	private Mp3Utils mp3     = null;

	//=======================================================================

	/**
	 * @param audioFile File to use.
	 */
	public SimplePlayer(File audioFile) {
		this.audioFile = audioFile;

		if(audioFile.getName().toLowerCase().endsWith(".mp3"))
			mp3 = new Mp3Utils(audioFile);
		else 
			audio = new AudioUtils(audioFile);
	}
	//=======================================================================

	/**
	 * @param audioFileName Name of the file to use.
	 */
	public SimplePlayer(String audioFileName) {
		new SimplePlayer(new File(audioFileName));
	}
	//=======================================================================

	/**
	 * Stop playing the audio file.
	 */
	public void stop() {
		if(audio != null)
			audio.stop();
		else if (mp3 != null)
			mp3.stop();
		else
			logger.error("Damn, what the hell do you want to stop !?");
	}
	//=======================================================================

	/**
	 * Start playing the audio file.
	 */
	public void play() {
		try {
			if(audio != null) {
				//logger.trace("Playing audio file '{}'", this.audioFile.getAbsolutePath());
				audio.play();
			}
			else if(mp3 != null) {
				//logger.trace("Playing mp3 file '{}'", this.audioFile.getAbsolutePath());
				mp3.play();
			}
			else {
				throw new Exception("PLAYER CANNOT BE NULL");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	//=======================================================================

	public File getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(File audioFile) {
		this.audioFile = audioFile;
	}

	public AudioUtils getAudio() {
		return audio;
	}

	public void setAudio(AudioUtils audio) {
		this.audio = audio;
	}

	public Mp3Utils getMp3() {
		return mp3;
	}

	public void setMp3(Mp3Utils mp3) {
		this.mp3 = mp3;
	}

	//=======================================================================

}