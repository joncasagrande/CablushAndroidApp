package com.cablush.cablushandroidapp.model.rest;

import com.cablush.cablushandroidapp.model.domain.Evento;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiEventos {

    @GET("/eventos")
    void getEventos(@Query("name") String name, @Query("estado") String estado, @Query("esporte") String esporte, Callback<List<Evento>> listCallback);

    @POST("/eventos")
    void createEvento(@Body Evento evento, Callback<Evento> savedEvento);
    
    @PUT("/eventos")
    void updateEvento(@Body Evento evento, Callback<Evento> savedEvento);

}