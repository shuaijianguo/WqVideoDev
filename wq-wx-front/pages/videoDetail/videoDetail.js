// pages/videoDetail/videoDetail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cover: "cover"
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
  },
  showSearch:function(){
    wx.navigateTo({
      url: '../searchView/searchView',
    })
  }
})