package es.developer.achambi.tsproject.database.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RowDao {
    @Query("select * from data")
    List<data> queryAll();
}
