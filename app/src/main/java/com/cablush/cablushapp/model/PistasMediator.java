package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
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
public class PistasMediator extends CablushMediator {

    public interface PistasMediatorListener {
        void onGetPistasResult(SearchResult result, List<Pista> pistas);
    }

    private WeakReference<PistasMediatorListener> mListener;
    private ApiPistas apiPistas;
    private PistaDAO pistaDAO;

    public PistasMediator(PistasMediatorListener listener, Context context) {
        super(context);
        this.mListener = new WeakReference<>(listener);
        this.apiPistas = RestServiceBuilder.createService(ApiPistas.class);
        this.pistaDAO = new PistaDAO(context);
    }

    public void getPistas(final String name, final String estado, final String esporte) {
        if (isOnline) {
            getPistasOnline(name, estado, esporte);
        } else {
            sendPistasResult(name, estado, esporte, SearchResult.SEARCH_OFF_LINE);
        }
    }

    private void getPistasOnline(final String name, final String estado, final String esporte) {
        apiPistas.getPistas(name, estado, esporte, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                if (!pistas.isEmpty()) {
                    pistaDAO.savePistas(pistas);
                }
                sendPistasResult(name, estado, esporte, SearchResult.SEARCH_ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting pistas online: " + error.getMessage());
                sendPistasResult(name, estado, esporte, SearchResult.SEARCH_ERROR);
            }
        });
    }

    private void sendPistasResult(final String name, final String estado, final String esporte,
                                  SearchResult result) {
        PistasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetPistasResult(result, pistaDAO.getPistas(name, estado, esporte));
        }
    }

    public void getMyPistas() {
        if (isOnline) {
            getMyPistasOnline();
        } else {
            sendPistasResult(SearchResult.SEARCH_OFF_LINE);
        }
    }

    private void getMyPistasOnline() {
        apiPistas.getPistas(new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                if (!pistas.isEmpty()) {
                    pistaDAO.savePistas(pistas);
                }
                sendPistasResult(SearchResult.SEARCH_ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting my pistas online: " + error.getMessage());
                sendPistasResult(SearchResult.SEARCH_ERROR);
            }
        });
    }

    private void sendPistasResult(SearchResult result) {
        PistasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetPistasResult(result, pistaDAO.getPistas(Usuario.LOGGED_USER.getUuid()));
        }
    }
}
