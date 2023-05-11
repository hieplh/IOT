package com.example.aiot.thread;

import com.example.aiot.callback.IRenderUI;

public class Runnable implements java.lang.Runnable {

    private final IRenderUI renderUI;

    private boolean isStart = true;

    public Runnable(IRenderUI renderUI) {
        this.renderUI = renderUI;
    }

    @Override
    public void run() {
        while (isStart) {
            renderUI.render();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }
}
