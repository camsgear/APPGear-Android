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


## Videos
VideoModel.java 视频列表、点赞和详情接口
### 接口列表
- [initData](#initdata)


#### initData
##### 函数签名:
```java
initData(int myVideo, Context context, int limit, int orderBy, final VideoListCallback videoListCallback)
```
##### 描述:
获取视频列表信息，用于第一次请求视频列表数据。
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
#### `refreshData`(int myVideo, Context context, int orderBy, String id,final VideoListCallback videoListCallback)刷新视频列表信息，用于获取最新的数据，会返回所有比传入的id号更新的数据。参数说明：<br>
##### 参数说明：
  `myVideo`：可取值为`Utils.ALL(获取视频数据)、 Utils.MY_PUBLISH（获取自己发布的数据）、 Utils.MY_COLLECTION（获取自己收藏的数据）`<br>
  `context`：上下文变量<br>
  `orderBy`：可取值为`Utils.DESC(根据时间倒序排序)、 Utils.ASC（根据时间顺序排序）`<br>
  `id`：刷新的参考值，获取所有比该id号更新的数据<br>
  `videoListCallback`:回调接口，访问服务器成功调用`onSuccess(List<VideoModelBean> mData)`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
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
#### `loadData`(int myVideo, Context context, int limit, int orderBy, String id,final VideoListCallback videoListCallback)加载视频列表信息，用于分页加载后面的数据，会返回比传入的id号更旧的数据。参数说明：<br>
##### 参数说明：
  `myVideo`：可取值为`Utils.ALL(获取视频数据)、 Utils.MY_PUBLISH（获取自己发布的数据）、 Utils.MY_COLLECTION（获取自己收藏的数据）`<br>
  `context`：上下文变量<br>
  `limit`：请求的数据个数<br>
  `orderBy`：可取值为`Utils.DESC(根据时间倒序排序)、 Utils.ASC（根据时间顺序排序）`<br>
  `id`：加载的参考值，获取比该id号更旧的数据<br>
  `videoListCallback`:回调接口，访问服务器成功调用`onSuccess(List<VideoModelBean> mData)`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
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
  * 视频点赞。参数说明：<br>
  `context`：上下文变量<br>
  `sid`：给id号为`sid`的视频点赞<br>
  `voteCallback`:回调接口，访问服务器成功调用`onSuccess(VoteBean mData);`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
  ```java
  public void upVote(Context context, String sid,
                       final VoteCallback voteCallback)
  ```
  * 视频取消点赞。参数说明：<br>
  `context`：上下文变量<br>
  `sid`：取消id号为`sid`的视频点赞<br>
  `voteCallback`:回调接口，访问服务器成功调用`onSuccess(VoteBean mData);`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
  ```java
  public void unVote(Context context, String sid,
                       final VoteCallback voteCallback)
  ```
  * 获取id号为`videoId`的视频详情。参数说明：<br>
  `context`：上下文变量<br>
  `videoId`：获取该id号的数据详情<br>
  `videoCallback`:回调接口，访问服务器成功调用`onSuccess(VideoModelBean mData);`,mData是返回的数据，访问服务器失败调用`onFailure(int code)`，code表示失败的信息，详情请参考`ErrorUtil.java`
  ```java
  public void obtainVideoModeBean(Context context, String videoId,
                                    final VideoCallback videoCallback)
  ```
* VideoCommentModel.java 视频评论列表、点赞接口

  * 获取某个视频的评论列表信息，用于第一次请求视频评论列表数据。参数说明：<br>
  `context`：上下文变量<br>
  `sid`：获取id号为`sid`的视频评论<br>
  `limit`：请求的数据个数<br>
  `orderBy`：可取值为`Utils.DESC(根据时间倒序排序)、 Utils.ASC（根据时间顺序排序）`<br>
  `videoCommentListCallback`:回调接口
  ```java
  public void initVideoComment(Context context, String sid, int limit, int orderBy,
                                 final VideoCommentListCallback videoCommentListCallback)
  ```
