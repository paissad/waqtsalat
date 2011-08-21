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

package net.paissad.waqtsalat.utils;

import java.awt.Component;

/**
 * @author Papa Issa DIAKHATE (paissad)
 */
public class SwingUtils {

    /**
     * Toggles the states of the components.
     * <p>
     * For example, if a component was enabled (when
     * {@link Component#isEnabled()} returns <tt>true</tt>), then it will be
     * disabled.
     * </p>
     * 
     * @param components
     *            - The component for which we toggle the states.
     */
    public static void toggleComponentStates(Component... components) {
        for (Component aComponent : components) {
            aComponent.setEnabled(!aComponent.isEnabled());
        }
    }

    /**
     * Sets one same states for all the specified <tt>Component</tt> objects.
     * 
     * @param state
     *            - The state to use.
     * @param components
     */
    public static void setComponentStates(final boolean state, Component... components) {
        for (Component aComponent : components) {
            aComponent.setEnabled(state);
        }
    }
}
