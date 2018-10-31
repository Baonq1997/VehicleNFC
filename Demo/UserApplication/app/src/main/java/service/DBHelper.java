package service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import model.Order;
import sqliteModel.History;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ParkingWithNFC.db";
    public static final String CONTACTS_TABLE_NAME = "tbl_order";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_CHECK_IN_DATE = "check_in_date";
    public static final String COLUMN_CHECK_OUT_DATE = "check_out_date";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_ALLOWED_PARKING_FROM = "allowed_parking_from";
    public static final String COLUMN_ALLOWED_PARKING_TO = "allowed_parking_to";
    public static final String COLUMN_TBL_ORDER_STATUS_ID = "tbl_order_status_id";
    public static final String COLUMN_TBL_USER_ID = "username";
    public static final String COLUMN_TBL_VEHICAL_NAME = "vehical_name";
    public static final String COLUMN_TBL_VEHICAL_ID = "vehical_id";
    public static final String COLUMN_TBL_LOCATION_ID = "tbl_location_id";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table tbl_order (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_TOTAL + " DOUBLE, " +
                        COLUMN_CHECK_IN_DATE + " INTEGER," +
                        COLUMN_CHECK_OUT_DATE + " INTEGER, " +
                        COLUMN_DURATION + " INTEGER," +
                        COLUMN_ALLOWED_PARKING_FROM + " INTEGER," +
                        COLUMN_ALLOWED_PARKING_TO + " INTEGER," +
                        COLUMN_TBL_ORDER_STATUS_ID + " TEXT," +
                        COLUMN_TBL_USER_ID + " TEXT," +
                        COLUMN_TBL_VEHICAL_NAME + " TEXT," +
                        COLUMN_TBL_VEHICAL_ID + " TEXT, " +
                        COLUMN_TBL_LOCATION_ID + " TEXT " +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS tbl_order");
        onCreate(db);
    }


    public boolean insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID, order.getId());
        contentValues.put(COLUMN_TOTAL, order.getTotal());
        contentValues.put(COLUMN_CHECK_IN_DATE, order.getCheckInDate());
        contentValues.put(COLUMN_CHECK_OUT_DATE, order.getCheckOutDate());
        contentValues.put(COLUMN_DURATION, order.getDuration());
        contentValues.put(COLUMN_ALLOWED_PARKING_FROM, order.getAllowedParkingFrom());
        contentValues.put(COLUMN_ALLOWED_PARKING_TO, order.getAllowedParkingTo());
        contentValues.put(COLUMN_TBL_ORDER_STATUS_ID, order.getOrderStatus().getName());
        contentValues.put(COLUMN_TBL_USER_ID, order.getUser().getLastName() + order.getUser().getFirstName());
        if (order.getUser().getVehicle() != null) {
            contentValues.put(COLUMN_TBL_VEHICAL_NAME, order.getUser().getVehicle().getVehicleTypeId().getName());
            contentValues.put(COLUMN_TBL_VEHICAL_ID, order.getUser().getVehicle().getVehicleNumber());
        } else {
            contentValues.put(COLUMN_TBL_VEHICAL_NAME, "");
            contentValues.put(COLUMN_TBL_VEHICAL_ID, "");
        }
        contentValues.put(COLUMN_TBL_LOCATION_ID, order.getLocation().getLocation());

        db.insert("tbl_order", null, contentValues);
        return true;
    }

    public Integer deleteAllContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tbl_order", null, null);
    }

    public ArrayList<History> getAllOrder() {

        ArrayList<History> array_list = new ArrayList<History>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from tbl_order ORDER BY check_in_date DESC;", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            int id = res.getInt(res.getColumnIndex(COLUMN_ID));
            double total = res.getDouble(res.getColumnIndex(COLUMN_TOTAL));
            long indate = res.getLong(res.getColumnIndex(COLUMN_CHECK_IN_DATE));
            long outdate = res.getLong(res.getColumnIndex(COLUMN_CHECK_OUT_DATE));
            long duration = res.getLong(res.getColumnIndex(COLUMN_DURATION));
            long parkfrom = res.getLong(res.getColumnIndex(COLUMN_ALLOWED_PARKING_FROM));
            long parkto = res.getLong(res.getColumnIndex(COLUMN_ALLOWED_PARKING_TO));
            String orderstatus = res.getString(res.getColumnIndex(COLUMN_TBL_ORDER_STATUS_ID));
            String username = res.getString(res.getColumnIndex(COLUMN_TBL_USER_ID));
            String vehicalname = res.getString(res.getColumnIndex(COLUMN_TBL_VEHICAL_NAME));
            String vehicalid = res.getString(res.getColumnIndex(COLUMN_TBL_VEHICAL_ID));
            String location = res.getString(res.getColumnIndex(COLUMN_TBL_LOCATION_ID));

            History his = new History(id, total, indate, outdate, duration, parkfrom, parkto, orderstatus, username, vehicalname, vehicalid, location);
            array_list.add(his);
            res.moveToNext();
        }
        return array_list;
    }

}
