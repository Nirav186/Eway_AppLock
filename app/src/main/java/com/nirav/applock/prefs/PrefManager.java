package com.nirav.applock.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "appppp";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setChargeRemoval(boolean a) {
        editor.putBoolean("charge_removal", a);
        editor.commit();
    }

    public boolean isChargeRemoval() {
        return pref.getBoolean("charge_removal", false);
    }

    public void setMoveAlert(boolean b) {
        editor.putBoolean("move_alert", b);
        editor.commit();
    }

    public boolean isMoveAlert() {
        return pref.getBoolean("move_alert", false);
    }
}