// pages/videoDetail/videoDetail.js
const app=getApp()
var videoUtil=require('../../utils/util.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cover: "cover",
    videoId:"",
    src:"",
    videoInfo:{},
   // userLikeVideo:true
  },

 videoCtx:{

 },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    var me=this;
    me.videoCtx=wx.createVideoContext("myVideo",me);
    //获取上个页面传入的数据
    var videoInfo=JSON.parse(params.videoInfo);
    //判断宽和高设置cover 默认是"cover"
    var height=videoInfo.videoHeight;
    var width=videoInfo.videoWidth;
    var cover="cover";
    if(width>=height){
      //不要拉伸
      cover="";
    }

    me.setData({
      videoId: videoInfo.id,
      src: app.serverUrl+videoInfo.videoPath,
      videoInfo: videoInfo,
      cover:cover
    })
  },
  onShow:function(){
    var me=this;
    me.videoCtx.play();//页面显示时就播放
  },
  onHide:function(){
     var me=this;
     me.videoCtx.pause();//隐藏时就暂停视频播放
  },
  showSearch:function(){
    wx.navigateTo({
      url: '../searchView/searchView',
    })
  },
  upload:function(){
    //获取当前路径，以前当前视频信息，这样确保在用户登录完能重定向到此视频页面
    var me=this;
    var user = app.getGlobalUserInfo();
    var videoInfo = JSON.stringify(me.data.videoInfo);
    var realUrl = '../videoDetail/videoDetail#videoInfo@' + videoInfo;//把问号转井号，等号转艾特符号，因为在下面拼的时候会把问号等号过滤掉
    
    if (user == null || user == undefined || user == '') {
      //表示还没登录 不能上传
      wx.navigateTo({
        url: '../userLogin/login?redirectUrl=' + realUrl,
      })
    }else{
      //页面绑定的上传视频事件
      videoUtil.uploadVideo();
    }
  },
  showIndex:function(){
    wx.redirectTo({
      url: '../index/index',
    })
  },
  showMine:function(){
    var user = app.getGlobalUserInfo();
    if(user==null||user==undefined||user==''){
      //表示还没登录
      wx.navigateTo({
        url: '../userLogin/login',
      })
    }else{
      wx.navigateTo({
        url: '../mine/mine',
      })
    }
  },
  likeVideoOrNot:function(){
    var me=this;
    var user = app.getGlobalUserInfo();
    var videoInfo = me.data.videoInfo;
    if (user == null || user == undefined || user == '') {
      //表示还没登录
      wx.navigateTo({
        url: '../userLogin/login',
      })
    } else {
      var userLikeVideo = me.data.userLikeVideo;
      var url = "/video/like?userId=" + user.id + "&videoId=" + videoInfo.id + "&creatorId=" + videoInfo.userId;//默认请求url是喜欢
     if(userLikeVideo){
       url = "/video/unlike?userId=" + user.id + "&videoId=" + videoInfo.id + "&creatorId=" + videoInfo.userId;
       
     }
    wx.request({
      url: app.serverUrl+url,
      method:"post",
      header: {
        'content-type': 'application/json', // 默认值
        'userId': user.id,
        'userToken': user.userToken//这是用来传入后端 进行拦截判断
      },
      success:function(res){
        me.setData({
          userLikeVideo: !userLikeVideo
        })
      }

    })

    }
  }
})