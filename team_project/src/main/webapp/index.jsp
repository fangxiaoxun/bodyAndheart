<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - 你好</title>
</head>
<body>
<h1><%= "Hello 你好" %>
</h1>
<br/>
<form action="file/uploadVoice" method="post" id="iconEdit" enctype="multipart/form-data">
    <label>岛id：</label> <input type="text" name="belong">
    <label for="file">头像:</label><input type="file" id="file" accept="image/jpeg,image/gif,image/png" name="icon" multiple>
    <input type="submit" value="提交">
</form>
</body>
</html>