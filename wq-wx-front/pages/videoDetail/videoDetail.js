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
    videoInfo:{}
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
    //me.videoCtx.play();//页面显示时就播放
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
    //页面绑定的上传视频事件
    videoUtil.uploadVideo();
  },
  showIndex:function(){
    wx.redirectTo({
      url: '../index/index',
    })
  }
})