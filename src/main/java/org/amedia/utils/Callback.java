package org.amedia.utils;

public interface Callback<P, F>{
    void onStarted();
    void onProgress(P progress);
    void onFinished(F parameter);
}
