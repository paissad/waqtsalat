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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;
import net.waqtsalat.WaqtSalat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains some utilities such as playing a MP3 file.
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class Mp3Utils extends AudioUtils implements PlayerUtils {

	Logger logger = LoggerFactory.getLogger(WaqtSalat.class);

	private Player player;

	public Mp3Utils() {
		super();
	}

	// =======================================================================

	/**
	 * @param mp3File
	 *            Mp3 file to use.
	 */
	public Mp3Utils(File mp3File) {
		super(mp3File);
	}

	// =======================================================================

	@Override
	public void stop() {
		if (player != null)
			player.close();
	}

	// =======================================================================

	/**
	 * Play the MP3 file to the sound card.
	 */
	@Override
	public void play() {

		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(audioFile));
			player = new Player(bis);
		} catch (Exception e) {
			logger.error("Problem playing file '{}'",
					audioFile.getAbsolutePath());
			e.printStackTrace();
			return;
		}

		// run in new thread to play in background
		new Thread("mp3Player") {
			public void run() {
				try {
					logger.debug("Playing mp3 file '{}'",
							audioFile.getAbsolutePath());
					player.play();
					logger.debug("Finished playing mp3 file '{}'",
							audioFile.getAbsolutePath());
				} catch (Exception e) {
					logger.error("Error: '{}'", this.toString());
					e.printStackTrace();
				}
			}
		}.start();

	}

	// =======================================================================

	/**
	 * Get the mp3 file used for this object.
	 * 
	 * @return Return the mp3 file used for this object.
	 */
	public File getMp3File() {
		return audioFile;
	}

	// =======================================================================

	/**
	 * Set the mp3 file to use for this object.
	 * 
	 * @param mp3File
	 *            mp3 file to use.
	 */
	public void setMp3File(File mp3File) {
		this.audioFile = mp3File;
	}
	// =======================================================================

}
