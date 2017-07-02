package com.endeavour.ojasva.aceqlapi;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteQueryResult {
    final ResultSet[] resultSets;
    final SQLException sqlException;

    public ExecuteQueryResult(ResultSet[] resultSets, SQLException e) {
        this.resultSets = resultSets;
        this.sqlException = e;
    }
}