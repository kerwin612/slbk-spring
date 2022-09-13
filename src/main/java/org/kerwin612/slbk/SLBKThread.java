package org.kerwin612.slbk;

import static org.kerwin612.slbk.SLBK.clear;
import static org.kerwin612.slbk.SLBK.get;
import static org.kerwin612.slbk.SLBK.isBlank;
import static org.kerwin612.slbk.SLBK.put;

public class SLBKThread extends Thread {

    private String logKey;

    public SLBKThread(Runnable runnable) {
        this(null, runnable);
    }

    public SLBKThread(String logKey, Runnable runnable) {
        super(runnable);

        this.logKey = isBlank(logKey) ? get() : logKey;
    }

    @Override
    public void run() {
        put(this.logKey);
        super.run();
        clear();
    }

}
