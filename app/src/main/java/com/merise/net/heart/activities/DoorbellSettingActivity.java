package com.merise.net.heart.activities;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.framewok.util.TLog;
import com.merise.net.heart.R;
import com.merise.net.heart.base.BaseActivity;
import com.merise.net.heart.bean.Sound;
import com.merise.net.heart.operate.AppOperate;
import com.merise.net.heart.operate.interfaces.Report;
import com.merise.net.heart.utils.SnackbarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/29.
 */
public class DoorbellSettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.setting_sounds_seekbar)
    SeekBar settingSoundsSeekbar;
    @BindView(R.id.sounds_lv)
    ListView soundsLv;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.sounds_persent_tv)
    TextView soundsPersentTv;
    @BindView(R.id.root)
    LinearLayout root;
    private AudioManager audioManager;
    private int maxVolume, currentVolume;// 最大音量，当前音量
    private int currentVolumeID = 5;// 当前铃声ID
    private String deviceID;
    //    private boolean isGet;// 判断是否获取到数据
    private soundsSettingAdapter adapter;

    private static final int INIT = 0x01;
    private MediaPlayer player;
    /**
     * 被选中的项
     */
    private Map<Integer, Boolean> isSelected;
    private String[] mData = {"经典一", "经典二", "经典三", "优雅", "风铃", "欢乐", "角落",
            "卡农", "飘零", "童真"};
    private Integer[] mSounds = {R.raw.goldenfst, R.raw.goldensnd,
            R.raw.goldenthd, R.raw.elegant, R.raw.wind_chime, R.raw.happy,
            R.raw.corner, R.raw.cannon, R.raw.wandering, R.raw.naivete,};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_doorbell_setting;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setSeekBarProcess();
            initSelected();
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void initView() {
        ButterKnife.bind(this);
        deviceID = String.valueOf(app.devices.get(app.currentIndex).getId());
        titleText.setText(R.string.doorbell_setting);
        settingSoundsSeekbar.setOnSeekBarChangeListener(this);
        initSeekBarMax();
        setSeekBarProcess();
        adapter = new soundsSettingAdapter();
        initSelected();
        soundsLv.setAdapter(adapter);
        soundsLv.setOnItemClickListener(this);
    }

    private void initSelected() {
        if (isSelected != null) {
            isSelected = null;
        }
        isSelected = new HashMap<>();
        /**
         * 将所有checkBox置为未被选中(false状态)
         */
        for (int i = 0; i < mData.length; i++) {
            isSelected.put(i, false);
        }
        isSelected.put(currentVolumeID - 1, true);
    }

    /**
     * 开始播放
     *
     * @param position
     */
    private void startPlayer(int position) {
        if (player != null) {
            Log.i(TAG, player.toString() + "即将释放");
            player.release();
            player = null;
        }
        String uriStr = "android.resource://" + getPackageName() + "/"
                + mSounds[position];
        player = MediaPlayer.create(DoorbellSettingActivity.this,
                Uri.parse(uriStr));
        player.start();
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.reset();
                return false;
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
            }
        });
    }

    @Override
    public void initData() {
        getSoundVolume(deviceID);
    }


    @OnClick({R.id.back, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.finish:
                setSoundVolume(deviceID, currentVolumeID, currentVolume * 2 < 5 ? 5 : currentVolume * 2);
                break;
        }
    }

    /**
     * 获取门铃信息
     *
     * @param deviceID
     */
    private void getSoundVolume(String deviceID) {
        AppOperate.getSoundVolume(deviceID, this, new Report() {
            @Override
            public void onSucces(Object o) {
                Sound sound = (Sound) o;
                currentVolume = sound.getVolume() / 2;
                currentVolumeID = sound.getSound();
                handler.sendEmptyMessage(INIT);
            }

            @Override
            public void onError(Object o) {
                handler.sendEmptyMessage(INIT);
            }
        });
    }

    private void setSoundVolume(String deviceID, int sound, int volume) {
        AppOperate.setSoundVolume(deviceID, sound, volume, this, new Report() {
            @Override
            public void onSucces(Object o) {
                SnackbarUtil.LongSnackbar(root, getResources().getString(R.string.modify_success), SnackbarUtil.Confirm).show();
            }

            @Override
            public void onError(Object o) {
                SnackbarUtil.LongSnackbar(root, (String) o, SnackbarUtil.Alert).show();
            }
        });
    }

    /**
     * 初始化声音调节按妞
     */
    private void initSeekBarMax() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取系统最大音量
        settingSoundsSeekbar.setMax(maxVolume);// 拖动条的最大值与系统声音的最大值匹配
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
    }

    /**
     * 设置seekbar的进度
     */
    private void setSeekBarProcess() {
        TLog.log(TAG, "当前音量:" + currentVolume + "当前id:" + currentVolumeID);
        settingSoundsSeekbar.setProgress(currentVolume);
        soundsPersentTv.setText(currentVolume * 100 / maxVolume + " %");
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        Log.i(TAG, "系统音量：" + progress);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
        setSeekBarProcess();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentVolumeID = position + 1;
        /**
         * 全部置为未选择状态
         */
        for (int i = 0; i < mData.length; i++) {
            isSelected.put(i, false);
        }
        isSelected.put(position, true);// 将当前点击的按钮设置为选择状态
        adapter.notifyDataSetChanged();
        startPlayer(position);
    }

    class soundsSettingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public Object getItem(int position) {
            return mData[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder holder;
            if (convertView == null) {
                holder = new viewHolder();
                convertView = View.inflate(DoorbellSettingActivity.this,
                        R.layout.item_sounds_setting, null);
                holder.checkbox = (CheckBox) convertView
                        .findViewById(R.id.checkbox);
                holder.sound_tv = (TextView) convertView
                        .findViewById(R.id.sound_tv);
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }
            holder.sound_tv.setText(mData[position]);
            holder.checkbox.setChecked(isSelected.get(position));
            return convertView;
        }

    }

    static class viewHolder {
        CheckBox checkbox;
        TextView sound_tv;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int currentVolume = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (currentVolume < maxVolume) {
                currentVolume = currentVolume + 1;
            }
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, 0);// 第三个flags是一些附加参数,只介绍两个常用的
            // FLAG_PLAY_SOUND 调整音量时播放声音
            // FLAG_SHOW_UI
            // 调整时显示音量条,就是按音量键出现的那个
            // 0表示什么也没有

            settingSoundsSeekbar.setProgress(currentVolume);
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (currentVolume > 0) {
                currentVolume = currentVolume - 1;
            }
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, 0);
            settingSoundsSeekbar.setProgress(currentVolume);
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
