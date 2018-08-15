var videoUtil = require('../../utils/util.js');
const app = getApp()

Page({
  data: {
    isMe: true,
    isFollow: false,
    faceUrl: "../resource/images/noneface.png" 
  },
  followMe:function(e){
    var me=this;
    var publisherId=me.data.publisherId;
    
    var user = app.getGlobalUserInfo();
    var userId = user.id;
    var fanId =userId;
     var folowType=e.currentTarget.dataset.followtype;//dataset-后面大写的 取的时候一律小写
     var url='';
     if(folowType=='0'){//已关注，下一步就是取消关注 
       url = '/user/dontBeYourFan?userId=' + publisherId+"&fanId="+fanId;
     }else{//未关注 下一步就是要关注 
       url = '/user/beYourFan?userId=' + publisherId + "&fanId=" + fanId;
     }
     wx.request({
       method:'post',
       url: app.serverUrl+url,
       header: {
         'content-type': 'application/json', // 默认值
         'userId': user.id,
         'userToken': user.userToken//这是用来传入后端 进行拦截判断
       },
       success:function(res){
         wx.showToast({
           title: res.data.data,
           icon: 'none',
           duration: 3000
         });
         if (folowType == '0') {//已关注，下一步就是取消关注 
           me.setData({
             isFollow: false,
             fansCounts: --me.data.fansCounts
           })
         } else {//未关注 下一步就是要关注 
           me.setData({
             isFollow: true,
             fansCounts: ++me.data.fansCounts
           })
         }
        
       }
     })
  },
  onLoad: function (params) {
   var that=this;
   //var user = app.userInfo;
   var user = app.getGlobalUserInfo();
   var userId=user.id;
   var publisherId = params.publisherId;
   if (publisherId!=null&&publisherId!=undefined&&publisherId!=''){
     if(userId==publisherId){
       that.setData({
         isMe:true,
         publisherId:publisherId
       })
     }else{
       that.setData({
          isMe:false,
          publisherId: publisherId
       })
     }
     userId=publisherId;
   }
   console.log(user);
   var serverUrl = app.serverUrl;
   wx.showLoading({
     title: '页面努力加载中...',
   });
   wx.request({
     url: serverUrl + '/user/query?userId=' + userId+'&fanId='+user.id,
     method: "POST",
     header: {
       'content-type': 'application/json', // 默认值
       'userId':user.id,
       'userToken': user.userToken//这是用来传入后端 进行拦截判断
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
           receiveLikeCounts: myUserInfo.receiveLikeCounts,
           isFollow: myUserInfo.follow
         })
       } else if (res.data.status == 500) {
         // 失败弹出框
         wx.showToast({
           title: res.data.msg,
           icon: 'none',
           duration: 3000
         })
       } else if (res.data.status == 502) {
         // 未登录提示框
         wx.showToast({
           title: res.data.msg,
           icon: 'none',
           duration: 3000,
           success:function(){
             wx.redirectTo({
               url: '../userLogin/login',
             })
           }
         })
       }
     }
   })
  },
  logout:function(){
    var user = app.getGlobalUserInfo();//app.userInfo;
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
            //app.userInfo = null;
            // fixme 修改原有的全局对象为本地缓存
            wx.removeStorageSync("userInfo");
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
    var userInfo = app.getGlobalUserInfo();
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
          url: serverUrl+'/user/uploadFace?userId='+userInfo.id,
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
    videoUtil.uploadVideo();
  }
})
