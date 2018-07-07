const app = getApp()

Page({
  data: {
    bgmList: [],
    serverUrl: "",
    videoParams: {}
  },

  onLoad: function (params) {
    var that=this;
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

  }
})

