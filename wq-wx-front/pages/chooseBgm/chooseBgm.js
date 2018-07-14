const app = getApp()

Page({
  data: {
    bgmList: [],
    serverUrl: "",
    videoParams: {}
  },

  onLoad: function (params) {
    //接收上一个页面传来的参数
    console.log(params);
    var that=this;
    that.setData({
      videoParams:params
    })
    //加载背景音乐 列表 
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待...',
    });
    wx.request({
      url: serverUrl + '/bgm/list',
      method: "POST",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        console.log(res.data);
        wx.hideLoading();
        var status = res.data.status;
        if (status == 200) {
          var bgmList=res.data.data;
          that.setData({
            bgmList:bgmList,
            serverUrl:serverUrl
          })
        }
      }
    })
  },

  upload: function (e) {
    var that=this;
    var serverUrl=app.serverUrl;
    var prePageParams = that.data.videoParams;
    //获取本页用户选择的背景音乐和输入的视频描述信息 
     var bgmId= e.detail.value.bgmId;
     var desc=e.detail.value.desc;
    //获取上一页传来的视频相关参数
     var videoSeconds = prePageParams.duration;
     var videoHeight = prePageParams.tmpHeight;
     var videoWidth = prePageParams.tmpWidth;
     var tmpVideoUrl = prePageParams.tmpVideoUrl;
     var tmpCoverUrl = prePageParams.tmpCoverUrl;
     wx.showLoading({
       title: '上传中...',
     });
     wx.uploadFile({
       url: serverUrl + '/video/upload' ,
       filePath: tmpVideoUrl,
       name: 'file',//后端定义的key
       header: {
         'content-type': 'application/json' // 默认值
       },
       formData:{
         userId: app.userInfo.id,
         videoSeconds: videoSeconds,
         videoHeight: videoHeight,
         videoWidth: videoWidth,
         bgmId: bgmId,
         desc: desc
       },
       success: function (res) {
         var data = JSON.parse(res.data);
         var status = data.status;
         console.log(data);
         var videoId=data.data;
         wx.hideLoading();
         if (status == 200) {
           wx.showLoading({
             title: '上传中...',
           });
          //第二部分:上传封面
           wx.uploadFile({
             url: serverUrl + '/video/uploadCover',
             filePath: tmpCoverUrl,
             name: 'file',//后端定义的key
             header: {
               'content-type': 'application/json' // 默认值
             },
             formData: {
               userId: app.userInfo.id,
               videoId: videoId
             },
             success: function (res) {
               var data = JSON.parse(res.data);
               var status = data.status;
               console.log(data);
               wx.hideLoading();
               if (status == 200) {
                 wx.showToast({
                   title: "恭喜您,上传成功！",
                   icon: 'none',
                   duration: 3000
                 });
                
                 wx.navigateBack({
                   delta: 1,
                 });

               } else if (status == 500) {
                 wx.showToast({
                   title: data.msg
                 })
               }
             }
           });
          //  wx.showToast({
          //    title: "恭喜您,上传成功！",
          //    icon: 'none',
          //    duration: 3000
          //  });
          
         } else if (status == 500) {
           wx.showToast({
             title: data.msg
           })
         }
       }
     })
  }
})

