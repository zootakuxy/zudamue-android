package st.domain.support.android.util;

import android.util.Log;

import java.util.Comparator;
import java.util.List;

/**
 *
 * Created by dchost on 29/01/17.
 */

public class ListOperation <T>{

    private final List<T> list;

    public ListOperation(List<T> list) {
        this.list = list;
    }

    public void sortAsc (Comparator<T> comparator ){
        for(int i = 0; i< this.list.size(); i++) {
            for (int j = i+1; j< this.list.size(); j++) {

                int result = comparator.compare(this.list.get(i), this.list.get(j));

                // if ti > tj {1}
                if (result >0)
                    troca(i, j);
            }
        }
    }

    private void troca(int i, int j) {
        T aux = this.list.get(i);
        this.list.set(i, this.list.get(j));
        this.list.set(j, aux);
        Log.i("Exc", "Trocando i:"+i+", j:"+j);
    }

    public void sortDesc (Comparator<T> comparator ){
        for(int i = 0; i< this.list.size(); i++) {
            for (int j = i+1; j< this.list.size(); j++) {
                int result = comparator.compare(this.list.get(i), this.list.get(j));

                // if ti > tj {1}
                if (result < 0)
                    troca(i, j);

            }
        }
    }
}
