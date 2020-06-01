package org.fan.demo.dynamic.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.fan.demo.dynamic.enums.DataBaseType;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Fan
 * @version 1.0
 * @date 2020-05-22 10:20
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DataBaseType> TYPE = new ThreadLocal<DataBaseType>();

    public static void setDataBaseType(DataBaseType dataBaseType) {
        if (dataBaseType == null) {
            throw new NullPointerException();
        }
        TYPE.set(dataBaseType);
        log.info("dataSource changed choose db {}",dataBaseType);
    }

    public static DataBaseType getDataBaseType() {
        return TYPE.get() == null ? DataBaseType.DS2 : TYPE.get();
    }

    public static void clearDataBaseType() {
        TYPE.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataBaseType();
    }
}
