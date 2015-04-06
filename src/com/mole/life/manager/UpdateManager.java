package com.mole.life.manager;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import com.lidroid.xutils.DbUtils;
import com.mole.life.R;
import com.mole.life.model.SysConfig;
import com.mole.life.util.VersionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by redmole on 2015/3/15.
 */
public class UpdateManager {

        private Context mContext;

        private DbUtils db;
        private SysConfig sysConfig;

        //返回的安装包url
        private String apkUrl;

        private String apkVersion;

        private Dialog noticeDialog;

        private Dialog downloadDialog;
        /* 下载包安装路径 */
        private static final String savePath = "/sdcard/TheLife/";

        private static final String saveFileName = savePath + "TheLife.apk";

        /* 进度条与通知ui刷新的handler和msg常量 */
        private ProgressBar mProgress;


        private static final int DOWN_UPDATE = 1;

        private static final int DOWN_OVER = 2;

        private int progress;

        private Thread downLoadThread;

        private boolean interceptFlag = false;

        private Handler mHandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWN_UPDATE:
                        mProgress.setProgress(progress);
                        break;
                    case DOWN_OVER:

                        installApk();
                        break;
                    default:
                        break;
                }
            };
        };

        public UpdateManager(Context context) {
            this.mContext = context;
            try {
                db = DbUtils.create(context);
                sysConfig = db.findById(SysConfig.class,1);
                if(sysConfig!=null){
                    apkUrl = sysConfig.getDonwloadUrl();
                    apkVersion = sysConfig.getNewWersion();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //外部接口让主Activity调用
        public void checkUpdateInfo(){
            if(sysConfig!=null&& VersionUtil.hasNewVersion(mContext.getString(R.string.app_version), sysConfig.getVersion())){
                showNoticeDialog();
            }
        }


        private void showNoticeDialog(){
            Builder builder = new Builder(mContext);
            builder.setTitle(mContext.getString(R.string.upgrade_warning_title));
            builder.setMessage(mContext.getString(R.string.upgrade_warning));
            builder.setPositiveButton(mContext.getString(R.string.download_select), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showDownloadDialog();
                }
            });
            builder.setNegativeButton(mContext.getString(R.string.ignore_select), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            noticeDialog = builder.create();
            noticeDialog.show();
        }

        private void showDownloadDialog(){
            Builder builder = new Builder(mContext);
            builder.setTitle("The Life "+apkVersion+" downloading...");

            final LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.progress, null);
            mProgress = (ProgressBar)v.findViewById(R.id.progress);

            builder.setView(v);
            builder.setNegativeButton(mContext.getString(R.string.remove), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    interceptFlag = true;
                }
            });
            downloadDialog = builder.create();
            downloadDialog.show();

            downloadApk();
        }

        private Runnable mdownApkRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    if(!file.exists()){
                        file.mkdir();
                    }
                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    do{
                        int numread = is.read(buf);
                        count += numread;
                        progress =(int)(((float)count / length) * 100);
                        //更新进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if(numread <= 0){
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }
                        fos.write(buf,0,numread);
                    }while(!interceptFlag);//点击取消就停止下载.

                    fos.close();
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                }

            }
        };

        /**
         * 下载apk
         */

        private void downloadApk(){
            downLoadThread = new Thread(mdownApkRunnable);
            downLoadThread.start();
        }
        /**
         * 安装apk
         */
        private void installApk(){
            File apkfile = new File(saveFileName);
            if (!apkfile.exists()) {
                return;
            }
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            mContext.startActivity(i);

        }
    }
