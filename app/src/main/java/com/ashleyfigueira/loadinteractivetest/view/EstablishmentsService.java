package com.ashleyfigueira.loadinteractivetest.view;

import com.ashleyfigueira.loadinteractivetest.model.Establishment;
import com.ashleyfigueira.loadinteractivetest.model.EstablishmentsList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ashleyfigueira on 19/11/16.
 */

public interface EstablishmentsService
{
    @GET("/v2/5661d6ee1000007f3a8d920c")
    Call<EstablishmentsList> getEstablishments();
}
