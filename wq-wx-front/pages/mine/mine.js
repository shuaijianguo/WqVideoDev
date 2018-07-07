//var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    isMe:true,
    faceUrl: "../resource/images/noneface.png" 
  },

  onLoad: function (params) {
   var that=this;
   var user = app.userInfo;
   var serverUrl = app.serverUrl;
   wx.showLoading({
     title: '页面努力加载中...',
   });
   wx.request({
     url: serverUrl + '/user/query?userId=' + user.id,
     method: "POST",
     header: {
       'content-type': 'application/json' // 默认值
     },
     success: function (res) {
       console.log(res.data);
       wx.hideLoading();
       var status = res.data.status;
       if (status == 200) {
         var myUserInfo=res.data.data;
         var faceUrl ='../resource/images/noneface.png';
         if (myUserInfo.faceImage != null && myUserInfo.faceImage != '' && myUserInfo.faceImage!=undefined){
           faceUrl = serverUrl + myUserInfo.faceImage;
         }
         that.setData({
           faceUrl: faceUrl,
           nickname: myUserInfo.nickname,
           fansCounts: myUserInfo.fansCounts,
           followCounts: myUserInfo.followCounts,
           receiveLikeCounts: myUserInfo.receiveLikeCounts
         })
       }
     }
   })
  },
  logout:function(){
      var user=app.userInfo;
      var serverUrl = app.serverUrl;
      wx.showLoading({
        title: '请等待...',
      });
      wx.request({
        url: serverUrl + '/logout?userId='+user.id,
        method: "POST",
       
        header: {
          'content-type': 'application/json' // 默认值
        },
        success: function (res) {
          console.log(res.data);
          wx.hideLoading();
          var status = res.data.status;
          if (status == 200) {
            wx.showToast({
              title: "恭喜您，注销成功！",
              icon: 'none',
              duration: 3000
            })
            app.userInfo = null;
            // fixme 修改原有的全局对象为本地缓存
            //app.setGlobalUserInfo(res.data.data);
            // 页面跳转
             wx.redirectTo({
              url: '../userLogin/login',
             })
          } else if (status == 500) {
            wx.showToast({
              title: res.data.msg,
              icon: 'none',
              duration: 3000
            })
          }
        }
      })
  },
  changeFace:function(){
    var that=this;
    wx.chooseImage({
      count:1,
      sizeType:['compressed'],
      sourceType:['album'],
      success: function(res) {
        var tempPaths=res.tempFilePaths;
        var serverUrl=app.serverUrl;
        console.log(tempPaths[0]);
        wx.showLoading({
          title: '上传中...',
        });
        wx.uploadFile({
          url: serverUrl+'/user/uploadFace?userId='+app.userInfo.id,
          filePath: tempPaths[0],
          name: 'file',//后端定义的key
          header: {
            'content-type': 'application/json' // 默认值
          },
          success:function(res){
            var data=JSON.parse(res.data);
            var status = data.status;
            console.log(data);
            wx.hideLoading();
            if (status == 200) {
              wx.showToast({
                title: "恭喜您,上传成功！",
                icon: 'none',
                duration: 3000
              });
              var myFaceUrl=data.data;
              that.setData({
                faceUrl:serverUrl+myFaceUrl
              });
            } else if (status ==500) {
              wx.showToast({
                title: data.msg
              })
            }
          }
        })
      },
    })
  },
  uploadVideo:function(){
    var that = this;
    wx.chooseVideo({
      sourceType: ['album'],
      success: function (res) {
        console.log(res);

        var duration = res.duration;
        var tmpHeight = res.height;
        var tmpWidth = res.width;
        var tmpVideoUrl = res.tempFilePath;
        var tmpCoverUrl = res.thumbTempFilePath;

        if (duration > 11) {
          wx.showToast({
            title: '视频长度不能超过10秒...',
            icon: "none",
            duration: 2500
          })
        } else if (duration < 1) {
          wx.showToast({
            title: '视频长度太短，请上传超过1秒的视频...',
            icon: "none",
            duration: 2500
          })
        } else {
          // 打开选择bgm的页面
          wx.navigateTo({
            url: '../chooseBgm/chooseBgm?duration=' + duration
            + "&tmpHeight=" + tmpHeight
            + "&tmpWidth=" + tmpWidth
            + "&tmpVideoUrl=" + tmpVideoUrl
            + "&tmpCoverUrl=" + tmpCoverUrl
            ,
          })
        }

      }
    })

  }
})
