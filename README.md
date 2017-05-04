## 配置
* 1.把aar文件复制到libs文件夹下，名字可以修改，默认名字是：camsgearlibrary-release.aar
* 2.在app 的gradle文件中增加引用：
```gradle
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    compile(name:'camsgearlibrary-release', ext:'aar')
    compile 'io.reactivex:rxjava:1.1.0'
    compile 'io.reactivex:rxandroid:1.1.0'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta4'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4'
}
```
* 3.在AndroidManifest.xml文件中增加权限：
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
## 功能分类
- [视频](#videos)
- [直播](#live)
- [用户](#user)
- [发布视频](#uploadvideo)
- [错误信息](#error)

## Videos
VideoModel.java 视频列表、点赞和详情接口
### 接口列表
- [initData](#initdata)
- [refreshData](#refreshdata)
- [loadData](#loaddata)
- [upVote](#upvote)
- [unVote](#unvote)
- [obtainVideoModeBean](#obtainvideomodebean)

#### initData
##### 描述:
获取视频列表信息，用于第一次请求视频列表数据。
##### 函数定义:
```java
public void initData(int myVideo, Context context, int limit, int orderBy, final VideoListCallback videoListCallback)
```
##### 参数说明：
- `myVideo` - 可取值为`Utils.ALL(获取视频数据)、 Utils.MY_PUBLISH（获取自己发布的数据）、 Utils.MY_COLLECTION（获取自己收藏的数据）`
- `context` - 上下文变量
- `limit` - 请求的数据个数
- `orderBy` - 可取值为`Utils.DESC(根据时间倒序排序)、 Utils.ASC（根据时间顺序排序）`
- `videoListCallback` - 回调接口，访问服务器成功调用`onSuccess(List<VideoModelBean> mData)`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  VideoModel.getInstance().initData(Utils.ALL, this, 10, Utils.DESC,
                new VideoModel.VideoListCallback() {
                    @Override
                    public void onSuccess(List<VideoModelBean> list) {
                        
                    }

                    @Override
                    public void onFailure(int i) {

                    }
                });
  ```
#### refreshData 
##### 描述:
刷新视频列表信息，用于获取最新的数据，会返回所有比传入的id号更新的数据
##### 函数定义:
```java
public void refreshData(int myVideo, Context context, int orderBy, String id,final VideoListCallback videoListCallback)
```
##### 参数说明：
- `myVideo` - 可取值为`Utils.ALL(获取视频数据)、 Utils.MY_PUBLISH（获取自己发布的数据）、 Utils.MY_COLLECTION（获取自己收藏的数据）`
- `context` - 上下文变量
- `orderBy` - 可取值为`Utils.DESC(根据时间倒序排序)、 Utils.ASC（根据时间顺序排序）`
- `id` - 刷新的参考值，获取所有比该id号更新的数据
- `videoListCallback` - 回调接口，访问服务器成功调用`onSuccess(List<VideoModelBean> mData)`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  VideoModel.getInstance().refreshData(Utils.ALL, this, Utils.DESC,id,
                new VideoModel.VideoListCallback() {
                    @Override
                    public void onSuccess(List<VideoModelBean> list) {
                        
                    }

                    @Override
                    public void onFailure(int i) {

                    }
                });
  ```
#### loadData
##### 描述:
加载视频列表信息，用于分页加载后面的数据，会返回比传入的id号更旧的数据
##### 函数定义:
```java
public void loadData(int myVideo, Context context, int limit, int orderBy, String id,final VideoListCallback videoListCallback)
```
##### 参数说明：
- `myVideo` - 可取值为`Utils.ALL(获取视频数据)、 Utils.MY_PUBLISH（获取自己发布的数据）、 Utils.MY_COLLECTION（获取自己收藏的数据）`
- `context` - 上下文变量
- `limit` - 请求的数据个数
- `orderBy` - 可取值为`Utils.DESC(根据时间倒序排序)、 Utils.ASC（根据时间顺序排序）`
- `id` - 加载的参考值，获取比该id号更旧的数据
- `videoListCallback` - 回调接口，访问服务器成功调用`onSuccess(List<VideoModelBean> mData)`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  VideoModel.getInstance().loadData(Utils.ALL, this, limit, Utils.DESC, id, new VideoModel.VideoListCallback() {
            @Override
            public void onSuccess(List<VideoModelBean> list) {

            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
#### upVote
##### 描述:
视频点赞
##### 函数定义:
```java
public void upVote(Context context, String sid, final VoteCallback voteCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `sid` - 给id号为`sid`的视频点赞
- `voteCallback` - 回调接口，访问服务器成功调用`onSuccess(VoteBean mData);`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  VideoModel.getInstance().upVote(this, sid, new VideoModel.VoteCallback() {
            @Override
            public void onSuccess(VoteBean voteBean) {
                
            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
#### unVote
##### 描述:
取消视频点赞
##### 函数定义:
```java
public void unVote(Context context, String sid, final VoteCallback voteCallback)
```
##### 参数说明：
- `context` - 上下文变量<br>
- `sid` - 取消id号为`sid`的视频点赞<br>
- `voteCallback` - 回调接口，访问服务器成功调用`onSuccess(VoteBean mData);`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  VideoModel.getInstance().unVote(this, sid, new VideoModel.VoteCallback() {
            @Override
            public void onSuccess(VoteBean voteBean) {

            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
#### obtainVideoModeBean
##### 描述:
获取id号为`videoId`的视频详情
##### 函数定义:
```java
public void obtainVideoModeBean(Context context, String videoId, final VideoCallback videoCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `videoId` - 获取该id号的数据详情
- `videoCallback` - 回调接口，访问服务器成功调用`onSuccess(VideoModelBean mData);`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  VideoModel.getInstance().obtainVideoModeBean(this, videoId, new VideoModel.VideoCallback() {
            @Override
            public void onSuccess(VideoModelBean videoModelBean) {
                
            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
## Live
LiveDetailModel.java 直播详情、观看人数接口
### 接口列表
- [getLiveDetail](#getlivedetail)
- [viewsCount](#viewscount)

#### getLiveDetail
##### 描述:
获取id号为`liveId`的直播详情。
##### 函数定义:
```java
public void getLiveDetail(Context context, String liveId, final GetLiveDetailCallback getLiveDetailCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `liveId` - 获取该id号的数据详情
- `getLiveDetailCallback` - 回调接口，访问服务器成功调用`onSuccess(LiveDetailBean liveDetailBean)`,liveDetailBean是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  LiveDetailModel.getInstance().getLiveDetail(this, liveId, new LiveDetailModel.GetLiveDetailCallback() {
            @Override
            public void onSuccess(LiveDetailBean liveDetailBean) {

            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
#### viewsCount
##### 描述:
获取id号为`liveId`的直播观看人数。
##### 函数定义:
```java
public void viewsCount(Context context, String liveId, final ViewsCountCallback viewsCountCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `liveId` - 获取该id号的直播观看人数
- `viewsCountCallback` - 回调接口，访问服务器成功调用`onSuccess(int i)`,i是观看人数，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  LiveDetailModel.getInstance().viewsCount(this, sid, new LiveDetailModel.ViewsCountCallback() {
            @Override
            public void onSuccess(int i) {
                
            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
## User
LoginAndRegisterModel.java 登录、获取验证码接口
### 接口列表
- [getVerificationCode](#getverificationcode)
- [loginOrRegister](#loginorregister)

#### getVerificationCode
##### 描述:
获取手机验证码。
##### 函数定义:
```java
 public void getVerificationCode(Context context, String phone, final LoginAndRegisterCallback loginAndRegisterCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `phone` - 手机号码
- `loginAndRegisterCallback` - 回调接口，访问服务器成功调用`onSuccess()`，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  LoginAndRegisterModel.getInstance().getVerificationCode(this, phone, new LoginAndRegisterModel.LoginAndRegisterCallback() {
            @Override
            public void onSuccess() {
                
            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
#### loginOrRegister
##### 描述:
使用手机号登录。
##### 函数定义:
```java
 public void loginOrRegister(final Context context, String name, String phone, String verifyingCodes, final LoginCallback loginCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `name` - 昵称（可以为""）
- `phone` - 手机号码
- `verifyingCodes` - 验证码
- `loginCallback` - 回调接口，访问服务器成功调用`onSuccess(UserBean userBean)`,userBean是用户信息，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  LoginAndRegisterModel.getInstance().loginOrRegister(this, name, phone, verifyingCodes, new LoginAndRegisterModel.LoginCallback() {
            @Override
            public void onSuccess(UserBean userBean) {
                
            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
PersonalModel.java 当前登录用户信息接口
### 接口列表
- [getUserInfor](#getuserinfor)

#### getUserInfor
##### 描述:
获取当前登录用户的详细信息。
##### 函数定义:
```java
 public void getUserInfor(Context context, final GetPersonalInforCallback getPersonalInforCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `getPersonalInforCallback` - 回调接口，访问服务器成功调用`onSuccess(UserInforBean userInforBean)`，userInforBean是用户的详细信息，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
##### 使用示例：
  ```java
  PersonalModel.getInstance().getUserInfor(this, new PersonalModel.GetPersonalInforCallback() {
            @Override
            public void onSuccess(UserInforBean userInforBean) {
                
            }

            @Override
            public void onFailure(int i) {

            }
        });
  ```
UserManager.java 获取登录状态、创建匿名用户接口
### 接口列表
- [isLogin](#islogin)
- [createAnonymous](#createanonymous)
- [signOut](#signout)

#### isLogin
##### 描述:
获取当前登录状态。
##### 函数定义:
```java
 public String isLogin(Context context)
```
##### 参数说明：
- `context` - 上下文变量
##### 返回值说明：
- `null`为没有登录，`UserManager.ROLE_ANON`为匿名登录，`UserManager.ROLE_USER`为注册用户登录。
##### 使用示例：
  ```java
  String role = UserManager.getInstance().isLogin(this.getActivity());
  if(role == null || role.equals("")){
        UserManager.getInstance().createAnonymous(this);
  }else if(role.equals(UserManager.ROLE_ANON)){
            
  }else if(role.equals(UserManager.ROLE_USER)){

  }
  ```
#### createAnonymous
##### 描述:
创建匿名用户。当前没有登录时应该创建匿名用户。
##### 函数定义:
```java
 public void createAnonymous(final Context context)
```
##### 参数说明：
- `context` - 上下文变量
##### 使用示例：
  ```java
  String role = UserManager.getInstance().isLogin(this.getActivity());
  if(role == null || role.equals("")){
        UserManager.getInstance().createAnonymous(this);
  }
  ```
#### signOut
##### 描述:
退出登录。执行该函数会清除登录信息并创建一个匿名用户。
##### 函数定义:
```java
 public void signOut(Context context)
```
##### 参数说明：
- `context` - 上下文变量
##### 使用示例：
  ```java
  UserManager.getInstance().signOut(this);
  ```
### 第三方登录（微信、QQ、新浪微博等）
#### 第三方授权使用友盟SDK，具体集成步骤请参考友盟社会化分享文档：<http://dev.umeng.com/social/android/quick-integration>
#### 第三方授权后，使用返回的授权信息登录
##### 使用示例（以微信登录为例）：
  ```java
  //调用微信授权借口
  mShareAPI = UMShareAPI.get(this);
  mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);

  //返回授权信息
  private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            LogUtil.i(TAG,"平台："+platform);

            if(platform == SHARE_MEDIA.WEIXIN){
                WechatLoginBean bean = new WechatLoginBean(data.get("unionid"),
                        data.get("gender"), data.get("screen_name"),data.get("openid"),
                        data.get("language"),data.get("profile_image_url"),
                        data.get("country"),data.get("city"),data.get("province"));
                //微信授权后登录
                wechatLogin(bean);
            }
            showToast(getString(R.string.authorize_succeed));
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast(getString(R.string.authorize_fail));
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast(getString(R.string.authorize_cancel));
        }
    };
    
  private void wechatLogin(WechatLoginBean mWechatLoginBean){
        LoginAndRegisterModel.getInstance().wechatLogin(this, mWechatLoginBean, new LoginAndRegisterModel.LoginCallback() {
            @Override
            public void onSuccess(UserBean userBean) {

            }

            @Override
            public void onFailure(int i) {

            }
        });
    }  
  ```
## UploadVideo
UploadToServerModel.java 发布、更新视频接口
### 发布流程（示例代码中有执行流程）
- 1.获取临时凭证
- 2.判断视频是否已经发布过
- 3.上传视频、封面
- 4.在服务器上创建或更新视频

### 接口列表
- [getOssSTS](#getosssts)
- [isBelongToMe](#isbelongtome)
- [uploadVideo](#uploadvideo)
- [uploadCover](#uploadcover)
- [createVideos](#createvideos)
- [updateVideos](#updatevideos)

#### getOssSTS
##### 描述:
获取获取临时凭证。
##### 函数定义:
```java
 public void getOssSTS(Context context, final GetOssSTSCallback getOssSTSCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `getOssSTSCallback` - 回调接口
##### 使用示例：
  ```java
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
  ```
  
#### isBelongToMe
##### 描述:
判断视频是否上传过。
##### 函数定义:
```java
 public void isBelongToMe(Context context, String originId, final IsBelongToMeCallback isBelongToMeCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `originId` - 视频的ID
- `isBelongToMeCallback` - 回调接口
##### 使用示例：
  ```java
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
  ```
  
#### uploadVideo
##### 描述:
上传视频。
##### 函数定义:
```java
  public void uploadVideo(Context context,final OssSTSBean bean,String mVideoName, String mVideoPath,final UploadTaskCallback uploadTaskCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `bean` - 临时凭证（使用getOssSTS获取）
- `mVideoName` - 视频的名字
- `mVideoPath` - 视频的绝对路径
- `uploadTaskCallback` - 回调接口
##### 使用示例：
  ```java
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
  ```
  
#### uploadCover
##### 描述:
上传封面。
##### 函数定义:
```java
public void uploadCover(Context context, final OssSTSBean bean, String mVideoThumbnailName, String mVideoThumbnailPath, final UploadTaskCallback uploadTaskCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `bean` - 临时凭证（使用getOssSTS获取）
- `mVideoThumbnailName` - 封面的名字
- `mVideoThumbnailPath` - 封面的绝对路径
- `uploadTaskCallback` - 回调接口

#### createVideos
##### 描述:
创建视频。
##### 函数定义:
```java
 public void createVideos(Context context, UploadVideoBean body, final UploadVideoCallback uploadVideoCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `body` - 视频的信息
- `uploadVideoCallback` - 回调接口
##### 使用示例：
  ```java
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
  ```
  
#### updateVideos
##### 描述:
更新视频。
##### 函数定义:
```java
 public void updateVideos(Context context, String originId, UploadVideoBean body, final UploadVideoCallback uploadVideoCallback)
```
##### 参数说明：
- `context` - 上下文变量
- `originId` - 视频的ID
- `body` - 视频的信息
- `uploadVideoCallback` - 回调接口

## Error
ErrorUtil.java 错误信息接口
##### 常量说明：
```java
    public final static int ACCESS_NETWORK_ERROR = -1;//访问网络出错。
    public final static int DATA_ERROR = -2;//返回的数据有问题，或许解析json出错
    public final static int BAD_REQUEST = 400;//错误的请求	该请求是无效的。相应的描述信息会说明原因。
    public final static int NOT_AUTHENTICATED = 401;//未验证	没有验证信息或者验证失败。
    public final static int FORBIDDEN = 403;//被拒绝	理解该请求，但不被接受。相应的描述信息会说明原因。
    public final static int NOT_FOUND = 404;//无法找到	资源不存在，请求的用户的不存在，请求的格式不被支持。
    public final static int METHOD_NOT_ALLOWED = 405;//请求方法不合适	该接口不支持该方法的请求。
    public final static int ACCOUNT_HAS_BEEN_BOUND = 409;//该账号已绑定其他用户，请先解绑
    public final static int ONLY_HAVE_ONE_CAN_LOGIN_ACCOUNT = 422;//您只有一个可登陆账号，不能再解绑
    public final static int TOO_MANY_REQUESTS = 429;//过多的请求	请求超出了频率限制。相应的描述信息会解释具体的原因。
    public final static int GENERAL_ERROR = 500;//内部服务错误	服务器内部出错了。请联系我们尽快解决问题。
    public final static int BAD_GATEWAY = 502;//无效代理	业务服务器下线了或者正在升级。请稍后重试。
    public final static int UNAVAILABLE = 503;//服务暂时失效	服务器无法响应请求。请稍后重试。
```
##### 显示错误信息示例：
```java
PersonalModel.getInstance().getUserInfor(this, new PersonalModel.GetPersonalInforCallback() {
            @Override
            public void onSuccess(UserInforBean userInforBean) {
                
            }

            @Override
            public void onFailure(int i) {
                ErrorUtil.showErrorInfo(this, i);//显示错误信息
            }
        });
```
