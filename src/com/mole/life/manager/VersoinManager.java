package com.mole.life.manager;

import android.content.Context;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mole.life.R;
import com.mole.life.model.SysConfig;
import com.mole.life.util.VersionUtil;

public class VersoinManager {
	Context context;
    String jsonStr;
    Gson gson = new Gson();
    SysConfig sysConfig;
    SysConfig newSysConfig;
    DbUtils db;

    public VersoinManager(Context context) {
		this.context = context;
	}

	// 判断网络是否可用的方法
	public boolean hasNewVersoin() {
        boolean flag = false;
        db = DbUtils.create(context);
        try {
            sysConfig = db.findById(SysConfig.class, 1);
            if(sysConfig==null){
                sysConfig = new SysConfig();
                sysConfig.setId(1);
                sysConfig.setVersion(context.getString(R.string.app_version));
                db.save(sysConfig);
            }else {
                String url = context.getString(R.string.app_url);
                HttpUtils http = new HttpUtils();
                http.configCurrentHttpCacheExpiry(1000 * 10);
                http.send(HttpRequest.HttpMethod.GET,
                        url,
                        //params,
                        new RequestCallBack<String>() {

                            @Override
                            public void onStart() {
//                                resultText.setText("conn...");
                            }

                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
//                                resultText.setText(current + "/" + total);
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                try {
                                    jsonStr = responseInfo.result;
                                    if (jsonStr != null) {
                                        newSysConfig = gson.fromJson(jsonStr, SysConfig.class);
                                        if( VersionUtil.hasNewVersion(context.getString(R.string.app_version), newSysConfig.getVersion())){
                                            sysConfig.setNewWersion(newSysConfig.getVersion());
                                            sysConfig.setDonwloadUrl(newSysConfig.getDonwloadUrl());
                                            sysConfig.setVersion(newSysConfig.getVersion());
                                        }
                                    }
                                    db.update(sysConfig);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }


                            @Override
                            public void onFailure(HttpException error, String msg) {
//                                resultText.setText(msg);
                            }
                        });

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
	}

}
