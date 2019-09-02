package es.developer.achambi.tsproject.database.model;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RowDao {
    @Query("select * from data")
    List<data> queryAll();
}
