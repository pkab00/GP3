package org.gp3.utils;

public interface Callback<P, F>{
    void onStarted();
    void onProgress(P progress);
    void onFinished(F parameter);
}
