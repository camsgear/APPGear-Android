package com.camsgear.sdktest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.camsgear.camsgearlibrary.UserManager;
import com.camsgear.camsgearlibrary.image.model.ImageModel;
import com.camsgear.camsgearlibrary.image.model.ImageModelBean;
import com.camsgear.camsgearlibrary.personal.model.OssSTSBean;
import com.camsgear.camsgearlibrary.personal.model.UploadImagesToServerModel;
import com.camsgear.camsgearlibrary.personal.model.UploadToServerModel;
import com.camsgear.camsgearlibrary.personal.model.UploadVideoBean;


import com.camsgear.camsgearlibrary.utils.LogUtil;
import com.camsgear.camsgearlibrary.utils.MyVideoUtil;
import com.camsgear.camsgearlibrary.utils.StringUtil;
import com.camsgear.camsgearlibrary.utils.Utils;
import com.camsgear.camsgearlibrary.video.model.VideoModel;
import com.camsgear.camsgearlibrary.video.model.VideoModelBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{
    private final static String TAG = "MainActivity";

    OssSTSBean mOssSTSBean;
    List<VideoModelBean> mVideoList = new ArrayList<>();
    int limit = 10;//每次获取几条数据

    Button button1,button2,button3,button4,button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //+如果没有登录，创建一个匿名用户。在程序运行时就应该执行该操作
        String role = UserManager.getInstance().isLogin(this);
        if(role == null || role.equals("")){
            UserManager.getInstance().createAnonymous(this);
        }
        //-如果没有登录，创建一个匿名用户。在程序运行时就应该执行该操作

        button1 = (Button) findViewById(R.id.button_1);
        button2 = (Button) findViewById(R.id.button_2);
        button3 = (Button) findViewById(R.id.button_3);
        button4 = (Button) findViewById(R.id.button_4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        //设置App标识
        Utils.setAppKey("WkTU9McNCYKduv80Ld6OQLsW");
        //+++++开启log，开启后也会打印okhttp的Log，//
        // 所以要在app文件夹下的build.gradle文件中增加 compile 'com.squareup.okhttp3:logging-interceptor:3.3.1'
        LogUtil.setEnableDebug(true);
        //-----

    }

    public void showToast(String s){
        Toast.makeText(this.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    private void initData(){
        /**
         * MyVideoUtil.ALL 表示请求视频数据，MyVideoUtil.MY_PUBLISH = 1; 表示请求自己发布的数据 MyVideoUtil.MY_COLLECTION = 2;表示自己收藏的数据
         * limit 表示请求的数据个数
         * MyVideoUtil.DESC 表示根据时间倒序排序 MyVideoUtil.ASC 表示顺序排序
         */
        VideoModel.getInstance().initData(MyVideoUtil.ALL, this, limit, MyVideoUtil.DESC,
                new VideoModel.VideoListCallback() {
                    @Override
                    public void onSuccess(List<VideoModelBean> list) {
                        mVideoList.clear();
                        mVideoList.addAll(list);
                        showToast("初始化了"+list.size()+"条数据");
                    }

                    @Override
                    public void onFailure(int i) {

                    }
                });

    }

    private void refreshData(String id){
        /**
         * 刷新操作表示， 请求 id 之前的所有数据（已修改）
         * 3.0的api对刷新操作做了修改，获取数据时id可以为空，使用limit限制获得数据个数。
         * 现在刷新会一直获取最新的数据，已经不是id之前的数据了，所以显示刷新数据时要把list清空，
         * 否则数据会重复。所以现在的初始化操作就可以使用刷新代替了，initData和refreshData功能一样了。
         * 这里保留id字段是因为获取个人收藏、发布等数据时，获取的是id之前的最新数据。
         * 获取所有数据时（MyVideoUtil.ALL），id可以传空值。
         */
        VideoModel.getInstance().refreshData(MyVideoUtil.ALL, this, MyVideoUtil.DESC,id,limit,
                new VideoModel.VideoListCallback() {
                    @Override
                    public void onSuccess(List<VideoModelBean> list) {
                        showToast("刷新了"+list.size()+"条数据");
                        Log.d(TAG, "onSuccess: " + list.get(0));
                        //现在显示刷新数据要把原来的清空
                        mVideoList.clear();
                    }

                    @Override
                    public void onFailure(int i) {

                    }
                });
    }

    private void loadData(String id){
        /**
         * 加载操作表示，请求 id 之后的 limit 条数据
         */
        VideoModel.getInstance().loadData(MyVideoUtil.ALL, this, limit, MyVideoUtil.DESC, id, new VideoModel.VideoListCallback() {
            @Override
            public void onSuccess(List<VideoModelBean> list) {
                showToast("加载了"+list.size()+"条数据");
            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    /**
     * 获取临时凭证
     */
    private void getSTS(){
        UploadToServerModel.getInstance().getOssSTS(this, new UploadToServerModel.GetOssSTSCallback() {
            @Override
            public void onSuccess(OssSTSBean ossSTSBean) {
                mOssSTSBean = ossSTSBean;
                String id = UUID.randomUUID().toString();//视频的ID
                isBelongToMe(id);
            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    /**
     * 判断视频是否上传过，已经存在了
     */
    private void isBelongToMe(final String id){
        //获取视频是否存在
        UploadToServerModel.getInstance().isBelongToMe(this, id, new UploadToServerModel.IsBelongToMeCallback() {
            @Override
            public void onSuccess(boolean b) {
                if(b){
                    //视频已经存在，执行更新操作

                }else {
                    //视频不存在，执行创建操作
                    uploadVideo(id);//上传视频
                }
            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    void uploadVideo(final String id){
        String folderName = this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String videoPath = folderName + "/20180319184645_F.mkv";
        UploadToServerModel.getInstance().uploadVideo(this, mOssSTSBean, id + ".mkv", videoPath, new UploadToServerModel.UploadTaskCallback() {
            @Override
            public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
                Log.i(TAG,l+" "+l1);
            }

            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                UploadVideoBean body = new UploadVideoBean();
                body.setOriginId(id);
                body.setTitle("testMKVTitle");
                UploadVideoBean.CamdoraMediaInfoBean mediaInfoBean = new UploadVideoBean.CamdoraMediaInfoBean(1, 1080, 2160, 210, 0, 0, 1, 1, false);
                body.setCamdoraMediaInfo(mediaInfoBean);
                body.setPublished(true);
                body.setOrigin(new UploadVideoBean.OriginBean("videos/" + id + ".mp4"));
                body.setVideoType(StringUtil.getVideoType(StringUtil.VIDEO_TYPE_PANORAMA));
                UploadVideoBean.MetadataBean metadataBean = new UploadVideoBean.MetadataBean(720, 1280, 3, 300);
                body.setMetadata(metadataBean);
                createVideo(body);
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {

            }
        });
    }

    void createVideo(UploadVideoBean body){
        UploadToServerModel.getInstance().createVideos(this, body, new UploadToServerModel.UploadVideoCallback() {
            @Override
            public void onSuccess(VideoModelBean videoModelBean) {
                Log.d(TAG, "==============");
                Log.d(TAG, "onSuccess: " + videoModelBean);
            }

            @Override
            public void onFailure(int i) {

            }
        });

        //----------
        UploadImagesToServerModel.getInstance().isImagesBelongToMe(Utils.getServerAccessRetrofit(this), "id", new UploadImagesToServerModel.IsBelongToMeCallback() {
            @Override
            public void onSuccess(boolean b) {

            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        String id;
        Intent intent;
        switch (view.getId()) {
            case R.id.button_1:
                initData();
                break;
            case R.id.button_2:
                if(mVideoList.size() > 0){
                    id = mVideoList.get(0).getId();
                    refreshData(id);
                }else {
                    //如果没有数据，则执行初始化数据的操作
                    initData();
                }
                break;
            case R.id.button_3:
                if(mVideoList.size() > 0){
                    id = mVideoList.get(mVideoList.size() - 1).getId();
                    loadData(id);
                }
                break;
            case R.id.button_4:
                //这里只演示发布视频的流程,由于没有设置视频路径，所以不会执行成功
                getSTS();
                break;
            default:
                break;
        }
    }
}
