//var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
    isMe:true,
    faceUrl: "../resource/images/noneface.png" 
  },

  onLoad: function (params) {
   
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
            // app.userInfo = res.data.data;
            // fixme 修改原有的全局对象为本地缓存
            //app.setGlobalUserInfo(res.data.data);
            // 页面跳转
            // wx.redirectTo({
            //   url: '../mine/mine',
            // })
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
  }
})
