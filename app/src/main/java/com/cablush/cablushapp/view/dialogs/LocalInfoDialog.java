package com.cablush.cablushapp.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.utils.ViewUtils;

import java.lang.ref.WeakReference;

/**
 * Created by oscar on 29/12/15.
 */
public class LocalInfoDialog<L extends Localizavel> extends DialogFragment {

    private static final String TAG = LocalInfoDialog.class.getSimpleName();

    private WeakReference<L> mLocalizavel;

    /**
     * Show the Login Dialog.
     *
     * @param fragmentManager
     */
    public static <L extends Localizavel> void showDialog(FragmentManager fragmentManager, L localizavel) {
        LocalInfoDialog dialog = new LocalInfoDialog();
        dialog.mLocalizavel = new WeakReference<>(localizavel);
        dialog.show(fragmentManager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(initializeView());

        // Set the dialog title
        builder.setCustomTitle(ViewUtils.getCustomTitleView(getActivity().getLayoutInflater(),
                mLocalizavel.get().getNome(),
                R.drawable.ic_mark_cablush_orange));

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_local_info, null);

        // TODO logo/foto

        TextView descricao = (TextView) view.findViewById(R.id.descricaoTextView);
        descricao.setText(mLocalizavel.get().getDescricao());

        TextView telefone = (TextView) view.findViewById(R.id.telefoneTextView);
        ImageView telefoneIcon = (ImageView) view.findViewById(R.id.imageViewTelefone);
        TextView email = (TextView) view.findViewById(R.id.emailTextView);
        ImageView emailIcon = (ImageView) view.findViewById(R.id.imageViewEmail);
        if (mLocalizavel.get() instanceof Loja) {
            Loja loja = (Loja) mLocalizavel.get();
            telefone.setText(loja.getTelefone());
            email.setText(loja.getEmail());
        } else {
            telefone.setVisibility(View.GONE);
            telefoneIcon.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            emailIcon.setVisibility(View.GONE);
        }

        // TODO facebook & site & directions && esportes

        TextView endereco = (TextView) view.findViewById(R.id.enderecoTextView);
        endereco.setText(mLocalizavel.get().getLocal().getEndereco());

        TextView cidadeEstado = (TextView) view.findViewById(R.id.cidadeEstadoTextView);
        cidadeEstado.setText(mLocalizavel.get().getLocal().getCidadeEstado());

        TextView cep = (TextView) view.findViewById(R.id.cepTextView);
        cep.setText(mLocalizavel.get().getLocal().getCep());

        return  view;
    }
}
