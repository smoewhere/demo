package org.fan.demo.dynamic.config.datasource;

import org.fan.demo.dynamic.enums.DataBaseType;

/**
 * @author Fan
 * @version 1.0
 * @date 2020-05-22 10:15
 */
public class DataSourceType {


    private static final ThreadLocal<DataBaseType> TYPE = new ThreadLocal<DataBaseType>();

    public static void setDataBaseType(DataBaseType dataBaseType) {
        if (dataBaseType == null) {
            throw new NullPointerException();
        }
        TYPE.set(dataBaseType);
    }

    public static DataBaseType getDataBaseType() {
        return TYPE.get() == null ? DataBaseType.DS2 : TYPE.get();
    }

    public static void clearDataBaseType() {
        TYPE.remove();
    }
}
