package ac.id.pnj.broadbandmultimedia.bm2015.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx,"ValueCalculate.db", null,1){
    companion object {
        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null){
                instance =
                    DatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as DatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            SavedResult.TABLE_SAVED,true,
            SavedResult.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            SavedResult.RANDOM_ID to TEXT,
            SavedResult.CONFIGURATION_GPON to TEXT,
            SavedResult.POWER_TRANSMITTER to TEXT,
            SavedResult.FIBER_LENGTH to TEXT,
            SavedResult.CONNECTOR_NUMBER to TEXT,
            SavedResult.SPLICING_NUMBER to TEXT,
            SavedResult.PHOTO_URI to TEXT,
            SavedResult.POWER_SPLITTERUSED to TEXT
            )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(SavedResult.TABLE_SAVED,true)
    }
}
val Context.database: DatabaseOpenHelper
get() = DatabaseOpenHelper.getInstance(applicationContext)
