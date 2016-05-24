package com.PGA.sectaxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Trouble extends Activity implements OnClickListener{

	String conteudo;
	Cliente cliente;
	String relatorio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.troubleint);
		
		Button bFinish1 = (Button) findViewById(R.id.bFinish1);
        bFinish1.setOnClickListener(this);
		Button bCalote = (Button) findViewById(R.id.bCalote);
        bCalote.setOnClickListener(this);
		Button bFuel = (Button) findViewById(R.id.bFuel);
        bFuel.setOnClickListener(this);
		Button bEngine = (Button) findViewById(R.id.bEngine);
        bEngine.setOnClickListener(this);
		Button bTire = (Button) findViewById(R.id.bTire);
        bTire.setOnClickListener(this);
		Button bTire1 = (Button) findViewById(R.id.bTire1);
        bTire1.setOnClickListener(this);
        Intent i = getIntent();
        conteudo = i.getStringExtra("problemas");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bFinish1:
            Log.i("info", "Finaliza relat�rio");
        	finish();
            break;
        case R.id.bCalote:
        	relatorio = ("Calote detectado,ultima corrida" + "\n" + conteudo);
        	//cliente = new Cliente("tro","/Notes/trouble.txt",relatorio);
            break;
        case R.id.bFuel:
        	relatorio = ("Pane seca, necessito nova unidade e assistencia em:" + "\n" + conteudo);
        	//cliente = new Cliente("tro","/Notes/trouble.txt",relatorio);
        	break;
        case R.id.bEngine:
        	relatorio = ("Problemas no motor, necessito nova unidade e assistencia" + "\n" + conteudo);
        	//cliente = new Cliente("tro","/Notes/trouble.txt",relatorio);
            break;
        case R.id.bTire:
        	relatorio = ("Pneu danificado, necessito nova unidade e assistencia" + "\n" + conteudo);
        	//cliente = new Cliente("tro","/Notes/trouble.txt",relatorio);
        	break;
        case R.id.bTire1:
        	relatorio = ("Pneu reparado");
        	//cliente = new Cliente("tro","/Notes/trouble.txt",relatorio);
        	break;
        }            
    }
}
