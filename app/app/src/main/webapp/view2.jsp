<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="java.util.*"
	import="jsoup.ItemBean"
	import="config.Config_iroiro"
	import="java.time.LocalDateTime"
	import="java.time.format.DateTimeFormatter" %>
<% ItemBean bean = (ItemBean) request.getAttribute("bean");
	DateTimeFormatter df = DateTimeFormatter.ofPattern(Config_iroiro.StrFormatDataTime);%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width">
<link rel="stylesheet" href="view2.css" type="text/css">
<title>Insert title here</title>
</head>
<body>
<!-- ヘッダ -->
	<div id="header"><h1><a href="index.html">MyGRL</a></h1></div>

	<!-- コンテンツ -->
	<div id="container">
		<div id="imgBx">
			<img src="<%=bean.getPic_url()%>" width="360" height="486">
			<div id="content">
				<h2><%=bean.getItem_name()%></h2>
				<%
				for (int i = 0; i < bean.getPrice_list().size(); i++) {
				%>
				<p>
					価格 :
					<%=bean.getPriceYen(bean.getPrice_list().get(i))%>
					[<%=df.format(bean.getTime_list().get(i))%>]
				</p>
				<%
				}
				%>
			</div>
		</div>
	</div>
</body>
</html>