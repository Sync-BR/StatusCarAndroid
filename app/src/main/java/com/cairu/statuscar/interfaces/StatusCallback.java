package com.cairu.statuscar.interfaces;

import com.cairu.statuscar.model.StatusModel;

import java.io.IOException;

public interface StatusCallback {
    void onStatusReceived(StatusModel status);
    void onFailure(IOException e);
}
