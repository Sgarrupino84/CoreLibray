package my_core_libray.progress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class ProgressDialogFragment extends DialogFragment {
    private static final String KEY_MSG = "KEY_MSG";
    private static boolean wantToCloseDialog = false;
    private ProgressDialog pd;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(getArguments().getInt(KEY_MSG)));
        setCancelable(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        return pd;
    }

    public void updateMessage(String msg) {
        pd.setMessage(msg);
    }

    public static void wantToCloseDialog(boolean wantToCloseDialog) {
        ProgressDialogFragment.wantToCloseDialog = wantToCloseDialog;
    }

    public void show(FragmentActivity context, int resId) {
        try {
            Bundle arguments = new Bundle(1);
            arguments.putInt(KEY_MSG, resId);
            setArguments(arguments);
            if (!ProgressDialogFragment.wantToCloseDialog) {
                show(context.getSupportFragmentManager(), ProgressDialogFragment.class.getSimpleName());
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void show(FragmentManager fragmentManager, int resId) {
        try {
            Bundle arguments = new Bundle(1);
            arguments.putInt(KEY_MSG, resId);
            setArguments(arguments);
            if (!ProgressDialogFragment.wantToCloseDialog) {
                show(fragmentManager, ProgressDialogFragment.class.getSimpleName());
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
