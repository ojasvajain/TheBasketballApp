package com.endeavour.ojasva.aceqlapi;
import java.sql.SQLException;

public interface OnRemoteConnectionEstablishedListener {

    void onRemoteConnectionEstablishedListener(BackendConnection remoteConnection, SQLException e);
}
