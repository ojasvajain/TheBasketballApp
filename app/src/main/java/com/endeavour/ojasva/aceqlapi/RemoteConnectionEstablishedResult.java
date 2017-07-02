package com.endeavour.ojasva.aceqlapi;
import java.sql.SQLException;

public class RemoteConnectionEstablishedResult {
    public final BackendConnection remoteConnection;
    public final SQLException sqlException;

    public RemoteConnectionEstablishedResult(BackendConnection remoteConnection, SQLException e) {
        this.remoteConnection = remoteConnection;
        this.sqlException = e;
    }
}
