const app = getApp()

Page({
  data: {
    // 用于分页的属性
    totalPage: 1,
    page: 1,
    videoList: [],

    screenWidth: 350,
    serverUrl: "",

    searchContent: ""
  },

  onLoad: function(params) {//
    var me = this;
    var screenWidth = wx.getSystemInfoSync().screenWidth;
    me.setData({
      screenWidth: screenWidth,
    });
    //获取查询页输入的查询内容
    var searchContent=params.search;
    var isSaveRecord=params.isSaveRecord;
    //判空处理
    if (searchContent == null || searchContent == '' || searchContent == undefined) {
      searchContent = "";
    }
    if(isSaveRecord==null||isSaveRecord==''||isSaveRecord==undefined){
      isSaveRecord=0;
    }
    me.setData({
      searchContent: searchContent
    })
    wx.showLoading({
      title: '请等待,正在加载中...',
    })
    var page = me.data.page;//获取当前的分页数
    console.log(me.data);
    me.getAllVideoList(page,isSaveRecord);
  },

  onPullDownRefresh: function() {//下拉刷新
    wx.showNavigationBarLoading();
    this.getAllVideoList(1,0);//下拉不需要保存热搜词所以给定值为0
  },

  onReachBottom: function() {//上拉加载更多
    var me = this;
    var currentPage = me.data.page;
    var totalPage = me.data.totalPage;

    // 判断当前页数和总页数是否相等，如果想的则无需查询
    if (currentPage === totalPage) {
      wx.showToast({
        title: '已经没有视频啦~~',
        icon: "none"
      })
      return;
    }

    var page = currentPage + 1;

    me.getAllVideoList(page,0);
  },

  getAllVideoList: function (page, isSaveRecord) {
    var me = this;
    // 获取data中的content
    var searchContent=me.data.searchContent;
    var serverUrl = app.serverUrl;
    wx.request({
      url: serverUrl + '/video/showAll?page=' + page + "&isSaveRecord=" + isSaveRecord,
      method: "POST",
      data: {
        videoDesc: searchContent
      },
      success: function(res) {
        wx.hideLoading();
        wx.hideNavigationBarLoading();//隐藏导航条动画
        wx.stopPullDownRefresh();//停止下拉的刷新 
        console.log(res.data);
        if (page == 1) {
          me.setData({
            videoList: []
          })
        }
        var backVideoList = res.data.data.rows; //后端上拉或下拉加载的视频信息
        var pageVideoList = me.data.videoList; //页面上
        me.setData({
          videoList: pageVideoList.concat(backVideoList),
          page: page,
          totalPage: res.data.data.total,
          serverUrl: serverUrl
        })
      }
    });

  },
  showVideoInfo: function(e) {
    var me = this;
    var videoList = me.data.videoList;
    var arrindex = e.target.dataset.arrindex;
    var videoInfo = JSON.stringify(videoList[arrindex]);//对象转化为字符串这样就能传到下个页面中去了

    wx.redirectTo({
      url: '../videoDetail/videoDetail?videoInfo=' + videoInfo
    })
  }

})