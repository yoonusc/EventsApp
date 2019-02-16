package mycodlabs.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yoonus on 5/23/2018.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }
}