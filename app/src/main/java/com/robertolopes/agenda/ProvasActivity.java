package com.robertolopes.agenda;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.robertolopes.agenda.modelo.Prova;

public class ProvasActivity extends AppCompatActivity {
    private Bundle parametros = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.replace(R.id.frame_principal, new ListaProvaFragment());

        if (getResources().getBoolean(R.bool.modoPaisagem))
            tx.replace(R.id.frame_secundario, new DetalhesProvaFragment());

        tx.addToBackStack(null);

        tx.commit();
    }

    public void selecionaProva(Prova prova) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!getResources().getBoolean(R.bool.modoPaisagem)) {

            DetalhesProvaFragment detalhesFragment = new DetalhesProvaFragment();
            parametros.putSerializable("prova", prova);
            detalhesFragment.setArguments(parametros);
            FragmentTransaction tx = fragmentManager.beginTransaction();
            tx.replace(R.id.frame_principal, detalhesFragment);
            tx.commit();
        } else {
            DetalhesProvaFragment detalhesFragment = (DetalhesProvaFragment) fragmentManager.findFragmentById(R.id.frame_secundario);
            detalhesFragment.populaCamposCom(prova);
        }
    }
}
