<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*" 
    import="jsoup.ItemBean" 
%>
<% ArrayList<ItemBean> beans = (ArrayList<ItemBean>) request.getAttribute("beans"); %>
<!DOCTYPE html>
<html lang="ja">
<head>
<link rel="stylesheet" href="view1.css" type="text/css">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
<title>商品<%=beans.size() %>件</title>
</head>
<body>
<!-- ヘッダ -->
	<div id="header"><h1><a href="index.html">MyGRL</a></h1></div>

<!-- コンテンツ -->
<form action="viewdetail" method="post">
<% int k=0; %>
<% for (int i = 0; i < beans.size() / 4; i++) {%>
<div>
	<% for (int j = 0; j < 4; j++) { %>
	<div class="product"><button type="submit" name="item_id" value="<%= beans.get(k).getItem_id() %>">
	<div class="ranking">No.<%=k+1 %></div>
	<img src="<%=beans.get(k).getPic_url() %>">
	<p class="item"><%= beans.get(k).getItem_NameProc() %></p>
	<div class="price"><%=beans.get(k).getPriceYen(beans.get(k).getPrice()) %></div></button></div>
	<% k++; %>
	<% } %>
</div>
<% } %>
</form>
<!-- フッタ -->
	<div id="footer"></div>
</body>
</html>