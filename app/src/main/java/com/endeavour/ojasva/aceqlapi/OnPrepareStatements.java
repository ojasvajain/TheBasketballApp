package com.endeavour.ojasva.aceqlapi;
import java.sql.PreparedStatement;

public interface OnPrepareStatements {
    PreparedStatement[] onGetPreparedStatementListener(BackendConnection remoteConnection);
}
