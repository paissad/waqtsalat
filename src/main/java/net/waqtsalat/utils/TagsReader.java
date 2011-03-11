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

import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * 
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public interface TagsReader {

	public void initTagAndHeader();

	public Tag getTag();

	public AudioHeader getAudioHeader();

	// Let's provide directly some methods which return some specific informations,
	// so that we don't need to do if for each FielKey.xxxx ...
	public String getTag_Artist();

	public String getTag_Album();

	public String getTag_Title();

	public String getTag_Comment();

	public String getTag_Year();

	public String getTag_Track();
	
	public Artwork getTag_Artwork();
}
