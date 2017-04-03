package com.halloworkd.zsfssdd.notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by zsfss on 2017/3/12.
 */

public class Strings extends PreferenceFragment {

    Context ctx;
    CheckBoxPreference password_CBP;
    Preference out;
    boolean isD;
    boolean isS;
    EditText et;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ctx = MyApplication.getContext();
        isD = true;
        isS = true;

        password_CBP = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.save_password_mode));
        out = (Preference) getPreferenceManager().findPreference("totxt");
        
        password_CBP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                
                boolean checked = Boolean.valueOf(newValue.toString());
                prefUtils.putBoolean(MyApplication.getContext().getString(R.string.settings_password), checked, MyApplication.getContext());
                Log.d("pc", "onCreate: " + String.valueOf(prefUtils.getBoolean(MyApplication.getContext().getString(R.string.settings_password), false, ctx)));
                Log.d("pref", "onPreferenceChange: " + String.valueOf(checked));
                if (checked){
                    if (isS){
                        MyApplication.setisLock(true);
                        setPassword();
                    }else {
                        isS = true;
                    }
                }else {
                    if (isD){
                        delPassword();
                    }else {
                        isD = true;
                    }
                }
                return true;
            }
        });
        Log.d("pc", "onCreate: " + String.valueOf(prefUtils.getBoolean(MyApplication.getContext().getString(R.string.settings_password), true, ctx)));

        out.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (Totxt.txt()){
                    Toast.makeText(MyApplication.getSettingctx(), getString(R.string.txtyes), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyApplication.getSettingctx(), getString(R.string.txtno), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    public void setPassword(){
        
        AlertDialog.Builder ifset = new AlertDialog.Builder(MyApplication.getSettingctx());
        ifset.setTitle(R.string.hint);
        ifset.setMessage(R.string.hint_set_password);
        ifset.setCancelable(false);
        ifset.setPositiveButton(ctx.getString(R.string.agree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                
                et = new EditText(MyApplication.getSettingctx());
                AlertDialog.Builder setpassword = new AlertDialog.Builder(MyApplication.getSettingctx());
                setpassword.setTitle(ctx.getString(R.string.password_maxlength));
                setpassword.setView(et);
                setpassword.setCancelable(false);
                
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                setpassword.setPositiveButton(ctx.getString(R.string.agree), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (et.getText().toString().length() == 0){
                            Toast.makeText(MyApplication.getSettingctx(), ctx.getString(R.string.password_is_null), Toast.LENGTH_SHORT).show();
                            password_CBP.setChecked(false);
                        }else {
                            prefUtils.putString("password", et.getText().toString(), ctx);
                        }
                    }
                });
                setpassword.setNegativeButton(ctx.getString(R.string.disagree), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefUtils.putBoolean(MyApplication.getContext().getString(R.string.settings_password), false, MyApplication.getContext());
                        password_CBP.setChecked(false);
                    }
                });
                setpassword.show();
            }
        });
        ifset.setNegativeButton(ctx.getString(R.string.disagree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                password_CBP.setChecked(false);
            }
        });
        ifset.show();
    }

    public void delPassword(){
        
        AlertDialog.Builder delpassword = new AlertDialog.Builder(MyApplication.getSettingctx());
        delpassword.setTitle(R.string.hint);
        delpassword.setMessage(R.string.hint_dle_password);
        delpassword.setCancelable(false);
        delpassword.setPositiveButton(ctx.getString(R.string.agree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prefUtils.remove("password", ctx);
            }
        });
        delpassword.setNegativeButton(ctx.getString(R.string.disagree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isS = true;
                password_CBP.setChecked(true);
            }
        });
        delpassword.show();
    }

    private View getPasscheckedview(AlertDialog dialog){
        LayoutInflater inflater = (LayoutInflater) MyApplication.getSettingctx().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.password_check, null);

        return rootView;
    }
}
