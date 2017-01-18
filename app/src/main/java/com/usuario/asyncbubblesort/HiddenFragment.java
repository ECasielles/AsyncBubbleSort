package com.usuario.asyncbubblesort;


import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HiddenFragment extends Fragment {
    private TaskCallBacks mCallBacks;
    public BubbleNumberTask bubbleNumberTask;

    public BubbleNumberTask getBubbleNumberTask() {
        return bubbleNumberTask;
    }

    interface TaskCallBacks {
        void onPreExecute();        //Configurar la visibilidad de los botones.
        void onProgressUpdate(int progress);    //Actualiza el progreso de la barra.
        void onCancelled();
        void onPostExecute(Long time);
    }

    public HiddenFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //setContentView();
        bubbleNumberTask = new BubbleNumberTask();
        bubbleNumberTask.execute();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hidden, container, false);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBacks = (TaskCallBacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getLocalClassName() + " must  implement interface TaskCallBacks");
        }
    }

    class BubbleNumberTask extends AsyncTask<Long, Integer, Long> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCallBacks.onPreExecute();
        }
        @Override
        protected Long doInBackground(Long... longs) {
            long t0 = System.currentTimeMillis();
            int aux;
            int numbers[] = MainActivity.mNumbers;

            for (int i = 0; i < numbers.length - 1; i++) {
                for (int j = 0; j < numbers.length -1; j++)
                    if (numbers[j] > numbers[j+1]) {
                        aux          = numbers[j];
                        numbers[j]   = numbers[j+1];
                        numbers[j+1] = aux;
                    }
                if(!isCancelled())
                    publishProgress((int)(((i+1)/(float)(numbers.length-1))*100));
                else break;
            }
            return t0;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mCallBacks.onProgressUpdate(values[0]);
        }
        @Override
        protected void onPostExecute(Long time) {
            if (mCallBacks != null)
                mCallBacks.onPostExecute(time);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            mCallBacks.onCancelled();
        }

    }

}
