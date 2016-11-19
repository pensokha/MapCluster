package com.ashleyfigueira.loadinteractivetest.model;

/**
 * Created by ashleyfigueira on 19/11/16.
 */

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class EstablishmentsList
{
    @SerializedName("establishments")
    @Expose
    private List<Establishment> establishments = new ArrayList<Establishment>();

    /**
     *
     * @return
     * The establishments
     */
    public List<Establishment> getEstablishments() {
        return establishments;
    }

    /**
     *
     * @param establishments
     * The establishments
     */
    public void setEstablishments(List<Establishment> establishments) {
        this.establishments = establishments;
    }

}
