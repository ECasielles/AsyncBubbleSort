package com.usuario.asyncbubblesort;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity implements HiddenFragment.TaskCallBacks{
    public static final int LENGHT = 20000000;
    public static int mNumbers[] = new int[LENGHT];

    private TextView txvProgress;
    private Button btnInit;
    private Button btnCancel;
    private HiddenFragment fragment;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvProgress = (TextView) findViewById(R.id.txv_progress);
        btnInit = (Button) findViewById(R.id.btn_init);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        mProgressBar = (ProgressBar) findViewById(R.id.mainProgBar);

        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new HiddenFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(fragment, "FRAGMENT_HIDDEN");
                ft.commit();
            }
        });
        if (fragment == null)
            generateNumbers();
    }
    /**
     * Método que inicializa el array mNumbers con números aleatorios.
     */
    private void generateNumbers() {
        Random rnd = new Random();
        for (int i = 0; i < LENGHT; i++)
            mNumbers[i] = rnd.nextInt();
    }
    @Override
    public void onPreExecute() {
        txvProgress.setText("En espera");
        mProgressBar.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnInit.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onProgressUpdate(int progress) {
        mProgressBar.setProgress(progress);
        txvProgress.setText(progress+"%");
    }
    @Override
    public void onCancelled() {
        txvProgress.setText("Se ha cancelado la operación");
        btnCancel.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        btnInit.setEnabled(true);
    }
    @Override
    public void onPostExecute(Long time) {
        mProgressBar.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        btnInit.setEnabled(true);
        txvProgress.setText("Ha tardado: " + time + " segundos ");
    }
}
