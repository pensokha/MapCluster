package com.ashleyfigueira.loadinteractivetest.model;

/**
 * Created by ashleyfigueira on 19/11/16.
 */

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("org.jsonschema2pojo")
public class Location{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lat")
    @Expose
    private float lat;
    @SerializedName("long")
    @Expose
    private float _long;

    public Location(String name, float lat, float _long)
    {
        this.name = name;
        this.lat = lat;
        this._long = _long;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The lat
     */
    public float getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(float lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     * The _long
     */
    public float getLong() {
        return _long;
    }

    /**
     *
     * @param _long
     * The long
     */
    public void setLong(float _long) {
        this._long = _long;
    }

}