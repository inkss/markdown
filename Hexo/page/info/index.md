---
seo_title: 留言板
date: 2019/11/20 10:49
updated: 2020/07/28 16:22
valine:
  placeholder: 有什么想对我说的呢？
meta:
  header: [author, counter]
  footer: []
sidebar: []
top_meta: []
bottom_meta: []
---

<p class="p center logo ultra">{% iconfont huli- %}</p>

*{% p center logo small gray, 请遵守相关法律法规，文明灌水，谢谢合作~ %}*

<div style="margin-top: -10px"></div>

<!-- <script>
setTimeout(() => {
  var AV = window.AV;
  if (AV == undefined) {
    document.getElementById("showTimes").style.display = 'none';
    document.getElementById("showComments").style.display = 'none';
    return;
  }

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
    pjax.refresh(document.querySelector("#showTimes"));
  }).catch(ex => {
    document.getElementById("showTimes").style.display = 'none';
  });

  var innerHtmlComments = "<li>";
  var queryComments = new AV.Query('Comment');
  queryComments.descending('createdAt');
  queryComments.limit(10);
  queryComments.find().then(ret => {
    ret.forEach((item, index) => {
      innerHtmlComments += "<a class='flat-box' title='" + item.attributes.nick + "' href='" + item.attributes.url + "#" + item.id + "'>";
      innerHtmlComments += "  <div class='name' style='word-break: break-all'><b>" + item.attributes.nick + "：</b>" + item.attributes.comment.replace(/<[^>]+>/g,"") + "</div>";
      innerHtmlComments += "</a>";
    });
    innerHtmlComments += "</li>";
    document.getElementById("showComments").innerHTML = innerHtmlComments;
    pjax.refresh(document.querySelector("#showComments"));
  }).catch(ex => {
    document.getElementById("showComments").style.display = 'none';
  });
}, 3000);
</script> -->