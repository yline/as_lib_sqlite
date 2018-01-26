package com.yline.sqlite;

import com.yline.sqlite.dao.DaoManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 提供了一个 流工具类
 *
 * @author yline 2017/11/8 -- 16:44
 * @version 1.0.0
 */
public class SQLiteIOUtils {
    public static byte[] object2Byte(Object object) {
        if (null != object) { // && object instanceof Serializable
            ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(baoStream);
                objectOutputStream.writeObject(object);
                return baoStream.toByteArray();
            } catch (NotSerializableException e) {
                DaoManager.e("SQLiteIOUtils", "objectToByte NotSerializableException -> " + android.util.Log.getStackTraceString(e));
            } catch (Throwable e) {
                DaoManager.e("SQLiteIOUtils", "objectToByte Throwable -> " + android.util.Log.getStackTraceString(e));
            } finally {
                try {
                    if (null != objectOutputStream) {
                        objectOutputStream.close();
                    }
                    baoStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object byte2Object(byte[] bytes) {
        if (null != bytes && bytes.length != 0) {
            ByteArrayInputStream baiStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(baiStream);
                return objectInputStream.readObject();
            } catch (IOException e) {
                DaoManager.e("SQLiteIOUtils", "byteToObject IOException -> " + android.util.Log.getStackTraceString(e));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                DaoManager.e("SQLiteIOUtils", "byteToObject ClassNotFoundException -> " + android.util.Log.getStackTraceString(e));
            } finally {
                try {
                    if (null != objectInputStream) {
                        objectInputStream.close();
                    }
                    baiStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
