package net.paissad.waqtsalat.utils.api;

import net.paissad.waqtsalat.pray.PrayMessage;

public interface PrayListener {

    /**
     * 
     * @param o
     *            - The <code>PrayCalculator</code> object which send the
     *            message.
     * @param message
     *            - The Object message sent by the {@link PrayCalculator}.
     * @throws Exception
     */
    public void update(PrayCalculator o, PrayMessage message) throws Exception;

}
