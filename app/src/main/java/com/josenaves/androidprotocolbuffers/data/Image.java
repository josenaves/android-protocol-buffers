package com.josenaves.androidprotocolbuffers.data;

import java.sql.Date;
import java.util.Arrays;

public class Image {
    public long id;
    public String name;
    public String date;
    public byte[] imageData;

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", imageData=" + Arrays.toString(imageData) +
                '}';
    }
}
