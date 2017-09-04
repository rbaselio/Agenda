package com.robertolopes.agenda;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.robertolopes.agenda.dao.AlunoDAO;
import com.robertolopes.agenda.modelo.Aluno;

import java.io.IOException;
import java.util.List;

public class MapaFragment extends MapFragment implements OnMapReadyCallback {
    private GoogleMap mapa;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mapa = googleMap;  // guardamos o mapa

        LatLng posicaoInicial = pegaCoordenadas("Rua Antonio Provati 381, Pedreira");
        if (posicaoInicial != null) {
            centralizaEm(posicaoInicial);  // usamos um método aqui agora
        }

        AlunoDAO alunoDao = new AlunoDAO(getActivity());
        for (Aluno aluno : alunoDao.buscaAluno()) {
            LatLng coordenada = pegaCoordenadas(aluno.getEndereco());
            if (coordenada != null) {
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(coordenada);
                marcador.title(aluno.getNome());
                marcador.snippet(String.valueOf(aluno.getNota()));
                mapa.addMarker(marcador);
            }
        }
        alunoDao.close();
    }

    public void centralizaEm(LatLng posicao) {
        if (posicao != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicao, 17);
            mapa.moveCamera(update);
        }
    }

    private LatLng pegaCoordenadas(String endereco) {
        try {
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> resultado = geocoder.getFromLocationName(endereco, 1);
            if (!resultado.isEmpty()) {
                LatLng posicao = new LatLng(resultado.get(0).getLatitude(), resultado.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
