<%@ page language="java" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>

<script type="text/javascript" src="/static/js/jquery.min.js"></script>

<body>
<p>
    <input type="text" placeholder="书名" name="bookName" id="bookName" />
    <input type="text" placeholder="章节" name="chapter" id="chapter" />

    <input type="button" value="查看" onclick="findChapter()" id="find"/>
</p>

<h2></h2>
<p id="content">

</p>
</body>
</html>
<script>
    function findChapter(){
        $.ajax({
            url:"/seeBook",
            data:{
                bookName:$("#bookName").val(),
                chapter:$("#chapter").val()
            },
            success:function (data) {
                console.log(data);
                $("h2").text(data.title);
                $("#content").html(data.content);
            }
        })
    }
</script>