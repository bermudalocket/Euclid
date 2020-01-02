package com.bermudalocket.euclid;

import java.util.HashMap;
import java.util.Iterator;

public class DelayedTaskPool {

    private static final HashMap<Runnable, Integer> TASKS = new HashMap<>();

    private DelayedTaskPool() { }
    
    public static void add(int tickDelay, Runnable runnable) {
        TASKS.put(runnable, tickDelay);
    }

    public static void tickAll() {
        Iterator<Runnable> iterator = TASKS.keySet().iterator();
        while (iterator.hasNext()) {
            Runnable runnable = iterator.next();
            int remainingDelay = TASKS.get(runnable);
            iterator.remove();
            if (remainingDelay == 0) {
                runnable.run();
            } else {
                TASKS.put(runnable, remainingDelay - 1);
            }
        }
    }

}
