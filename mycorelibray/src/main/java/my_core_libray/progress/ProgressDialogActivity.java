package my_core_libray.progress;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import mobile.almaviva.it.mycorelibray.R;

public class ProgressDialogActivity extends Dialog {

    public Context context;

    public TextView textProgress;
    public String textString;

    public ProgressDialogActivity(@NonNull Context context, String textString) {
        super(context);
        this.context = context;
        this.textString = textString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog);

        textProgress = findViewById(R.id.textDialogProgress);
        textProgress.setText(textString);
    }
}
