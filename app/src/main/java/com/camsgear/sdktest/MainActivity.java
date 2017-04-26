package com.camsgear.sdktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.camsgear.camsgearlibrary.UserManager;
import com.camsgear.camsgearlibrary.personal.model.OssSTSBean;
import com.camsgear.camsgearlibrary.personal.model.UploadToServerModel;
import com.camsgear.camsgearlibrary.personal.model.UploadVideoBean;
import com.camsgear.camsgearlibrary.utils.Utils;
import com.camsgear.camsgearlibrary.video.model.VideoModel;
import com.camsgear.camsgearlibrary.video.model.VideoModelBean;

import java.util.ArrayList;
import java.util.List;

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
    }

    public void showToast(String s){
        Toast.makeText(this.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    private void initData(){
        /**
         * Utils.ALL 表示请求视频数据，Utils.MY_PUBLISH = 1; 表示请求自己发布的数据 Utils.MY_COLLECTION = 2;表示自己收藏的数据
         * limit 表示请求的数据个数
         * Utils.DESC 表示根据时间倒序排序 Utils.ASC 表示顺序排序
         */
        VideoModel.getInstance().initData(Utils.ALL, this, limit, Utils.DESC,
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
         * 刷新操作表示， 请求 id 之前的所有数据
         */
        VideoModel.getInstance().refreshData(Utils.ALL, this, Utils.DESC,id,
                new VideoModel.VideoListCallback() {
                    @Override
                    public void onSuccess(List<VideoModelBean> list) {
                        showToast("刷新了"+list.size()+"条数据");
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
        VideoModel.getInstance().loadData(Utils.ALL, this, limit, Utils.DESC, id, new VideoModel.VideoListCallback() {
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
                String id = "";//视频的ID
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
    private void isBelongToMe(String id){
        //获取视频是否存在
        UploadToServerModel.getInstance().isBelongToMe(this, id, new UploadToServerModel.IsBelongToMeCallback() {
            @Override
            public void onSuccess(boolean b) {
                if(b){
                    //视频已经存在，执行更新操作

                }else {
                    //视频不存在，执行创建操作
                    uploadVideo();//上传视频
                }
            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    void uploadVideo(){
        UploadToServerModel.getInstance().uploadVideo(this, mOssSTSBean, "", "", new UploadToServerModel.UploadTaskCallback() {
            @Override
            public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
                Log.i(TAG,l+" "+l1);
            }

            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                UploadVideoBean body = null;
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

            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        String id;
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
