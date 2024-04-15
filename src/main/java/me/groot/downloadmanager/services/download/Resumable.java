package me.groot.downloadmanager.services.download;

public interface Resumable {

    boolean isPaused();

    void pause();

    void resume();

}
