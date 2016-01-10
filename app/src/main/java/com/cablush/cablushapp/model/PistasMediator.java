package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.persistence.PistaDAO;
import com.cablush.cablushapp.model.rest.ApiPistas;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.domain.Pista;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class PistasMediator {

    private static final String TAG = PistasMediator.class.getSimpleName();

    public interface PistasMediatorListener {
        void onGetPistasSucess(List<Pista> pistas);
        void onGetPistasFail(String message);
    }

    private WeakReference<PistasMediatorListener> listener;
    private WeakReference<Context> context;
    private ApiPistas apiPistas;
    private PistaDAO pistaDAO;

    public PistasMediator(PistasMediatorListener listener, Context context) {
        this.listener = new WeakReference<>(listener);
        this.context = new WeakReference<>(context);
        this.apiPistas = RestServiceBuilder.createService(ApiPistas.class);
        this.pistaDAO = new PistaDAO(context);
    }

    public void getPistas(final String name, final String estado, final String esporte) {
        apiPistas.getPistas(name, estado, esporte, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                if (!pistas.isEmpty()) {
                    pistaDAO.savePistas(pistas);
                }
                listener.get().onGetPistasSucess(pistaDAO.getPistas(name, estado, esporte));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting pistas. " + error.getMessage());
                listener.get().onGetPistasFail(context.get().getString(R.string.error_no_connection));
            }
        });

    }
}
