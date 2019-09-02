package es.developer.achambi.tsproject.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class data {
    @ColumnInfo(name="field1")
    public String marca;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="field2")
    public String modelo;
    @ColumnInfo(name="field3")
    public String periodo;
    @ColumnInfo(name="field4")
    public String cc;
    @ColumnInfo(name="field5")
    public String cilindros;
    @ColumnInfo(name="field6")
    public String gD;
    @ColumnInfo(name="field7")
    public String potencia;
    @ColumnInfo(name="field8")
    public String cvf;
    @ColumnInfo(name="field9")
    public String cv;
    @ColumnInfo(name="field10")
    public String valor;
}
