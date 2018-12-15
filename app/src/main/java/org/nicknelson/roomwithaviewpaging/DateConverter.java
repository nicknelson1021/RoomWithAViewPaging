package org.nicknelson.roomwithaviewpaging;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

class DateConverter {

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
