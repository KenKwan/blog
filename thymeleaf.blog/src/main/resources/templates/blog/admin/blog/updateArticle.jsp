<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
    <title>index</title>
    <%@ include file="../inc/head.inc"%>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <script type="text/javascript" charset="utf-8" src="<%= path%>/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%= path%>/ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="<%= path%>/ueditor/lang/zh-cn/zh-cn.js"></script>
</head>
<body>
    <%@ include file="../header.jsp"%>
    <div class="container">
        <div class="row">
            <div id="content" class="col-md-12">
                <!-- content starts -->
                <section>
                    <input id="_articleId"  type="hidden" name="title">
                    <div class="col-md-7">
                        <div class="input-group">
                            <span class="input-group-addon">文章标题</span>
                            <input id="_articleTitle" type="text" class="form-control" placeholder="输入标题...">
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="btn-group">
                            <button type="button" class="btn">文章类型</button>
                            <button type="button" id="_articleToggleBtn" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            选择分类<span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a onclick="toggleSelect('_articleToggleBtn',1,'Article','_articleType')">Article</a></li>
                                <li><a onclick="toggleSelect('_articleToggleBtn',2, 'Subjtct','_articleType')">Subjtct</a></li>
                            </ul>
                            <input id="_articleType" type="hidden" name="tagType"/>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <button type="button" id="_articleUpdateBtn" class="btn btn-primary pull-right">
                        <span class="glyphicon glyphicon-ok" aria-hidden="true"></span> 发布更新</button>
                    </div>
                    <div class="col-md-12">
                        <div class="input-group">
                            <span class="input-group-addon">文章摘要</span>
                            <textarea  id="_summary" name="summary" rows="3" class="form-control" placeholder="文章摘要..."></textarea>
                        </div>
                    </div>
                    <div class="container row well">
                        <div class="col-md-12">
                            <button type="button" class="btn btn-primary pull-right" data-toggle="modal" data-target="#_tagModal">
                            <span class="glyphicon glyphicon-tags" aria-hidden="true"></span> 添加标签
                            </button>
                        </div>
                        <div class="col-md-6 ">
                            <div class="label-group" id="_labelGroup"></div>
                        </div>
                        <div class="col-md-6 ">
                            <div class="label-group" id="_labelGroup2"></div>
                        </div>
                    </div>
                </section>
                <div class="row">
                    <script id="_articleEditor" type="text/plain" style="width:100%;height:500px;"></script>
                </div>
                <div id="_tempContent" style="display:none;">${article.content}</div>
                <!-- content ends -->
            </div>
        </div>
    </div>
    
    <!-- Modal -->
    <div class="modal fade" id="_tagModal" tabindex="-1" role="dialog" aria-labelledby="_tagModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="_tagModalLabel">添加新标签</h4>
                </div>
                <div class="modal-body">
                    <!-- modal begin -->
                    <div class="row" id="_addTagDiv">
                        <div class="col-md-8">
                            <div class="input-group">
                                <span class="input-group-addon">标签名称</span>
                                <input id="_newTagName" type="text" name="tagName" class="form-control" placeholder="输入标签名称...">
                            </div>
                        </div>
                        <div class="col-md-4 pull-right">
                            <div class="btn-group">
                                <button type="button" id="_typeToggleBtn" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                标签分类<span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a onclick="toggleSelect('_typeToggleBtn',1,'Article','_newTagType')">Article</a></li>
                                    <li><a onclick="toggleSelect('_typeToggleBtn',2, 'Subjtct','_newTagType')">Subjtct</a></li>
                                </ul>
                                <input id="_newTagType" type="hidden" name="tagType"/>
                            </div>
                        </div>
                    </div>
                    <!-- modal end -->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" tagFun="tagClick" id="_newTagBtn">添加</button>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="../footer.jsp"%>
    </div><!--/container-->

<script type="text/javascript" charset="utf-8" src="<%= path%>/static/js/admin/blog/publish.js"></script>
<script type="text/javascript">
    var ue;
    $().ready(function(){
        
        ue = initUE();
        initArticlePage();

        $("#_articleUpdateBtn").on("click",function(){
            var title = $("#_articleTitle").val()
            var summary = $("#_summary").val()
            var articleType = $("#_articleType").val()
            var content = UE.getEditor('_articleEditor').getContent()
            
            if(title == "" || summary =="" || articleType =="" || content == "") {
                alert("Blank ...");
                return;
            }

            var article = {
                "articleId": $("#_articleId").val(),
                "title": title,
                "articleType": articleType,
                "summary": summary,
                "tags": tagIdStr,
                "content": content
            };

            $.ajax({
                beforeSend:function(){$('.table').html("正在保存，请稍候……");},
                type: "post",
                dataType: "json",
                url: PRE_URI_AA + "/updateArticle",
                data: article,
                success: function(res){
                    alert(res.retMsg);
                }
            });
        });
    });

    function initArticlePage() {
        var title = '${article.title}';
        var articleId = '${article.articleId}';
        // tagIdStr = '${article.tags}';
        var articleType = '${article.articleType}';
        
        var articleTypeName = articleType == 1? 'Article':'Subject';
        
        var tagList = JSON.parse('${article.tagList}');
        for (var i = 0; i < tagList.length; i++) {
            var tag = tagList[i]
           
            tagIdStr += tag.tagId + ",";
        };

        $("#_articleId").val(articleId);
        $("#_articleTitle").val(title);
        $("#_summary").val('${article.summary}');

        toggleSelect('_articleToggleBtn',articleType,articleTypeName,'_articleType')

        initLabel("tagClick");
        
        ue.ready(function() {
            this.setContent($("#_tempContent").html());
            $("#_tempContent").empty();
        })
    }
</script>
</body>
</html>