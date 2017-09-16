package com.chen.mvp.module.manage.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import com.chen.mvp.R;
import com.chen.mvp.utils.ClearCacheUtils;
import com.chen.mvp.utils.PreferencesUtils;
import com.chen.mvp.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.model.Notice;

/**
 * Created by chen on 2017/9/15.
 */

public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    public static final String PREF_KEY_PROTOCOL = "pref_key_protocol";
    public static final String PREF_KEY_NO_IMG = "pref_key_no_image";
    public static final String PREF_KEY_CACHE = "pref_key_cache";
    public static final String PREF_KEY_CODE = "pref_key_code";
    public static final String NO_IMAGE_KEY = "setting_no_image";
    public static final String SAVE_PATH_KEY = "setting_save_path";
    public static final String DEFAULT_SAVE_PATH = "/storage/emulated/0/Mvp";
    private Preference cache;
    private Preference protocol;
    private SwitchPreference noImg;
    private Preference code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        cache = findPreference(PREF_KEY_CACHE);
        protocol = findPreference(PREF_KEY_PROTOCOL);
        noImg = (SwitchPreference) findPreference(PREF_KEY_NO_IMG);
        code = findPreference(PREF_KEY_CODE);

        cache.setOnPreferenceClickListener(this);
        protocol.setOnPreferenceClickListener(this);
        noImg.setOnPreferenceClickListener(this);
        code.setOnPreferenceClickListener(this);
        cache.setSummary(ClearCacheUtils.getCacheSize());
        noImg.setChecked(PreferencesUtils.getBoolean(this, NO_IMAGE_KEY));
    }


    public static void launch(Context context) {
        context.startActivities(new Intent[]{new Intent(context, SettingActivity.class)});
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case PREF_KEY_PROTOCOL:
                showLicensesDialog();
                break;
            case PREF_KEY_NO_IMG:
                if (noImg.isChecked()) {
                    PreferencesUtils.putBoolean(this, NO_IMAGE_KEY, true);
                } else {
                    PreferencesUtils.putBoolean(this, NO_IMAGE_KEY, false);
                }
                break;
            case PREF_KEY_CACHE:
                ClearCacheUtils.deleteDir(this.getCacheDir());
                cache.setSummary(ClearCacheUtils.getCacheSize());
                ToastUtil.showShortToast("清除缓存成功");
                break;
            case PREF_KEY_CODE:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/GG-chen/MVPApp");
                intent.setData(content_url);
                startActivity(intent);
                break;

        }
        return false;
    }

    private void showLicensesDialog() {
        final String name = "LicensesDialog";
        final String url = "https://github.com/GG-chen/MVPApp";
        final String copyright = "Copyright 2017 GG-chen <969097157@qq.com>";
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(this)
                .setNotices(notice)
                .build()
                .show();
    }
}
