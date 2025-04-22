package org.gp3;

public interface Callback<P, F>{
    void onStarted();
    void onProgress(P progress);
    void onFinished(F parameter);
}
