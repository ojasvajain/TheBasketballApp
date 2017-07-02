package com.endeavour.ojasva.aceqlapi;
import java.sql.ResultSet;

public interface ItemBuilder<T> {
    T buildItem(ResultSet rs);
}
