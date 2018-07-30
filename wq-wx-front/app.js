//app.js ctrl+shift+f全局搜索
App({
 // serverUrl:"http://192.168.1.107:8081",
  serverUrl: "http://10.10.100.95:8081",
 userInfo:null,
 setGlobalUserInfo:function(userInfo){
   wx.setStorageSync("userInfo",userInfo);
 },
  getGlobalUserInfo:function(){
    return wx.getStorageSync("userInfo");
  }
})