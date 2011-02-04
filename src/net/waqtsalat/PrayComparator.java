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

import java.util.Comparator;

/**
 * Comparator for the Pray object types.
 * @author Papa Issa DIAKHATE (<a href="mailto:paissad@gmail.com">paissad</a>)
 */
public class PrayComparator implements Comparator<Pray> {

	@Override
	public int compare(Pray pray1, Pray pray2) {
		String prayTime1 = pray1.getTime();
		String prayTime2 = pray2.getTime();
		if(prayTime1 != null && prayTime2 != null)
			return prayTime1.compareTo(prayTime2); // We only compare the hours of each pray time.

		// We assume that the pray names are null, then the pray ranks aren't.
		else {
			Integer prayRank1 = pray1.getRank();
			Integer prayRank2 = pray2.getRank();
			return  prayRank1.compareTo(prayRank2);
		}
	}
}
