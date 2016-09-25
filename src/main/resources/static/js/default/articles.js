$().ready(function(){
    initTitle();

    var indexMore = $("button[id=index-more]");
    indexMore.attr("disabled", "true");
    indexMore.text("正在加载");
    var articleVO = {
        "articleType": 1
    };
    initPage("/article/list", "_news_content", articleVO, function(flag){
        if(flag){
            indexMore.text("没有更多的文章")
        }else{
            indexMore.removeAttr("disabled");
            indexMore.text("加载更多")
        }
    });
    listTitle("/article/hot", articleVO, '_ranking');

    myTags(1);
});

var initTitle = function(){
    var tagName = "${tagName}";
    if(tagName != null && tagName != "") {
        $("#_tagTitle").removeAttr("display");
    }else {
        $("#_tagTitle").attr("display", "none");
    }
}