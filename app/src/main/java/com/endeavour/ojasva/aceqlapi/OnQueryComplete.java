package com.endeavour.ojasva.aceqlapi;

import java.sql.SQLException;
import java.util.List;

public interface OnQueryComplete<T> {
    void onQueryComplete(List<List<T>> list, SQLException e1);
}
