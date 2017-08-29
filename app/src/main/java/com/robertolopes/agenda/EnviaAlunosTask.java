package com.robertolopes.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.robertolopes.agenda.converter.AlunoConverter;
import com.robertolopes.agenda.dao.AlunoDAO;
import com.robertolopes.agenda.modelo.Aluno;

import java.util.List;

public class EnviaAlunosTask extends AsyncTask<Void, Void, String> {
    ProgressDialog dialog;
    private Context context;

    public EnviaAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde", "Enviando Alunos...", true, true);
    }

    @Override
    protected String doInBackground(Void[] params) {

        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscaAluno();
        dao.close();

        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.converterParaJSON(alunos);

        WebClient client = new WebClient();

        return client.post(json);
    }

    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }
}
