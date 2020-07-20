---
title: 留言板
valine:
  placeholder: 有什么想对我说的呢？
meta:
  header: [author, counter]
  footer: []
sidebar: [notice, showText, showTimes, showComments]  
---

{% p center logo ultra, '<i class="fad fa-star-of-david" style="color: #a6d5fa" title="爱我"></i>' %}

*{% p center logo small gray, 请遵守相关法律法规，文明灌水，谢谢合作~ <br><span class="bb_spoiler" title="这是真的，不骗你">ヾ(*´▽‘*)ﾉ 无关评论会被删掉的~</span> %}*


<div style="margin-top: 10px"></div>

<div id="showTop10"></div>

<script>
setTimeout(() => {
  var AV = window.AV;
  if (AV == undefined) {return;}

  var innerHtmlTimes = "<li>";
  var queryTimes = new AV.Query('Counter');
  queryTimes.contains('url', '/article/');
  queryTimes.descending('time');
  queryTimes.limit(10);
  queryTimes.find().then(ret => {
    ret.forEach((item, index) => {
      innerHtmlTimes += "<a class='flat-box' title='" + item.attributes.title + "' href='" + item.attributes.url + "'>";
      innerHtmlTimes += "  <div class='name' style='white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>" + item.attributes.title + "</div>";
      innerHtmlTimes += "  <div class='badge'>" + item.attributes.time + "</div>";
      innerHtmlTimes += "</a>";
    });
    innerHtmlTimes += "</li>";
    document.getElementById("showTimes").innerHTML = innerHtmlTimes;
  });

  var innerHtmlComments = "<li>";
  var queryComments = new AV.Query('Comment');
  queryComments.descending('updatedAt');
  queryComments.limit(10);
  queryComments.find().then(ret => {
    console.log(ret);
    ret.forEach((item, index) => {
      innerHtmlComments += "<a class='flat-box' title='" + item.attributes.ip + "' href='" + item.attributes.url + "#" + item.id + "'>";
      innerHtmlComments += "  <div class='name'><b>" + item.attributes.nick + "：</b>" + item.attributes.comment.replace(/<[^>]+>/g,"") + "</div>";
      innerHtmlComments += "</a>";
    });
    innerHtmlComments += "</li>";
    document.getElementById("showComments").innerHTML = innerHtmlComments;
  });
}, 3000);
</script>

<div style="margin-top: -10px"></div>