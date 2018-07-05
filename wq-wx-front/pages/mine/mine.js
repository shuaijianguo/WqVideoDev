//var videoUtil = require('../../utils/videoUtil.js')

const app = getApp()

Page({
  data: {
   // isMe:true,
    faceUrl: "../resource/images/noneface.png" 
  },

  onLoad: function (params) {
   
  },
  logout:function(){
      var user=app.userInfo;
      var serverUrl = app.serverUrl;
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
  }
})
