package es.developer.achambi.tsproject.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import es.developer.achambi.coreframework.db.utils.DatabaseUtils;
import es.developer.achambi.tsproject.database.model.RowDao;
import es.developer.achambi.tsproject.database.model.data;

@Database(entities = {data.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static String DB_NAME = "test.db";

    public abstract RowDao getRowDao();
    /**
     * On first execution, copies initial database on the database application directory if needed,
     * Database is build after calling to this method
     * @param context
     * @return initialized database
     */
    public static AppDatabase buildDatabase(Context context) {
        try {
            DatabaseUtils.copyDataBase(context, AppDatabase.DB_NAME);
        } catch ( Exception e ){
            e.printStackTrace();
        }
        return  Room.databaseBuilder(
                context.getApplicationContext(), AppDatabase.class, DB_NAME).build();
    }
}
