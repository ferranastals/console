package ohi.andre.consolelauncher.commands.main.raw;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;

/**
 * Created by andre on 03/12/15.
 */
public class airplane implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            boolean isEnabled = isEnabled(info.context);
            Settings.System.putInt(info.context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);

            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", !isEnabled);
            info.context.sendBroadcast(intent);

            return info.res.getString(R.string.output_airplane) + !isEnabled;
        } else
            return info.res.getString(R.string.output_nofeature);
    }

    private boolean isEnabled(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public int minArgs() {
        return 0;
    }

    @Override
    public int maxArgs() {
        return 0;
    }

    @Override
    public int[] argType() {
        return null;
    }

    @Override
    public String[] parameters() {
        return null;
    }

    @Override
    public int helpRes() {
        return R.string.help_airplane;
    }

    @Override
    public String onArgNotFound(ExecutePack info) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }
}