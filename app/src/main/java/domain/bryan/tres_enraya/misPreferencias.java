package domain.bryan.tres_enraya;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class misPreferencias extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_mis_preferencias);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
