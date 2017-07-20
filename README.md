一、简介

  该款APP是一个后台基于bmob后端云的校园社交APP，后台采用bmob云存储技术。界面采用了谷歌的matrial design设计，框架基于MD+Rxjava+retrofit+MVP架构。
到目前为止，已经完成的功能模块有单聊，群聊，附近人搜索，开心时刻，天气预报，朋友圈发表和个人信息编辑展示等7大功能模块。
  
  首先郑重声明下，该聊天功能的实现并不是调用官方的即时通讯API，而是本人自己结合官方提供的推送功能和实时同步的功能，按照自己的逻辑来实现的，所以内部聊天信息的逻辑处理过程源码是开放的，希望对想学习Android聊天框架的同学有所帮助。
  
  
  
   项目详解地址:http://www.jianshu.com/p/2d76430617ae

二、screenshot

   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170609-153556.jpg
 width=250 height=450>
 <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-153501.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70611-240903.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-160929.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-160840.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-160131.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-160046.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-154410.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-154406.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-154358.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-154355.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/S70610-154347.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-154938.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-155450.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-155556.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-161608.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-161817.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-161924.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-162019.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-162025.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-162038.jpg
 width=250 height=450>
   <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-235352.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170610-235657.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170611-104346.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170611-104502.jpg
 width=250 height=450>
  <image src=https://github.com/HelloChenJinJun/TestChat/blob/master/screenshots/Screenshot_20170611-104528.jpg
 width=250 height=450>

 
 

 
