package com.PGA.sectaxi;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);

		//se possivel abrir BT aqui
		
		Button bStart = (Button) findViewById(R.id.bStart);
        Button bSair   = (Button) findViewById(R.id.bSair);
        bStart.setOnClickListener(this);
        bSair.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick(View v) {
        Intent i = new Intent(MainActivity.this, Corrida.class);
        switch (v.getId()) {
        case R.id.bStart:
            startActivity(i);
            break;
        case R.id.bSair:
            finish();
        }
    }

}


/*

TESTE DE MUDANÇA


testando denovo
 */
