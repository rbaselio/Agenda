package com.robertolopes.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.robertolopes.agenda.R;
import com.robertolopes.agenda.modelo.Aluno;

import java.util.List;

public class AlunosAdapter extends BaseAdapter {
    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) {
        this.alunos = alunos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Aluno aluno = alunos.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        TextView campoNome = (TextView) view.findViewById(R.id.item_nome);
        campoNome.setText(aluno.getNome());

        TextView campoTelefone = (TextView) view.findViewById(R.id.item_telefone);
        campoTelefone.setText(aluno.getTelefone());

        String caminhoFoto = aluno.getCaminhoFoto();
        if (caminhoFoto != null) {
            ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapreduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapreduzido);
        }
        return view;
    }

}
