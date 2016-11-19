package com.ashleyfigueira.loadinteractivetest.geo;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by ashleyfigueira on 19/11/16.
 */

public class EstablishmentItem implements ClusterItem
{
    private String name;
    private String phone;
    private LatLng mPosition;
    private String address;
    private String description;

    public EstablishmentItem(String name, String phone, LatLng mPosition, String address, String description)
    {
        this.name = name;
        this.phone = phone;
        this.mPosition = mPosition;
        this.address = address;
        this.description = description;
    }

    @Override
    public LatLng getPosition()
    {
        return mPosition;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
