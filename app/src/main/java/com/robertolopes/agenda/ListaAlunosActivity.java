package com.robertolopes.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.robertolopes.agenda.adapter.AlunosAdapter;
import com.robertolopes.agenda.dao.AlunoDAO;
import com.robertolopes.agenda.modelo.Aluno;

import java.util.List;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        Button botaoSalvar = (Button) findViewById(R.id.novo_aluno);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(vaiProFormulario);
            }
        });

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);
                Toast.makeText(ListaAlunosActivity.this, "Aluno: " + aluno.getNome() + " Editar", Toast.LENGTH_SHORT).show();
                Intent vaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                vaiProFormulario.putExtra("aluno", aluno);
                startActivity(vaiProFormulario);

            }
        });
        registerForContextMenu(listaAlunos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem itemLigar = menu.add("Ligar");
        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            123);
                } else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    String ligar = "tel:" + aluno.getTelefone();
                    intentLigar.setData(Uri.parse(ligar));
                    startActivity(intentLigar);
                }
                return false;
            }
        });

        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        String sms = "sms:" + aluno.getTelefone();
        intentSMS.setData(Uri.parse(sms));
        itemSMS.setIntent(intentSMS);

        MenuItem itemMapa = menu.add("Vizualiar no Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        String mapa = "geo:0,0?q=" + aluno.getEndereco();
        intentMapa.setData(Uri.parse(mapa));
        itemMapa.setIntent(intentMapa);

        MenuItem itemSite = menu.add("Visitar Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String site = aluno.getSite();
        if (!site.startsWith("http://")) site = "http://" + site;
        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);

        MenuItem remover = menu.add("Remover");
        remover.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(ListaAlunosActivity.this, "Aluno: " + aluno.getNome() + " Deletar", Toast.LENGTH_SHORT).show();
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deletar(aluno);
                dao.close();
                carregaLista();
                return false;
            }
        });

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAluno();
        dao.close();
        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAlunos.setAdapter(adapter);
    }

}
