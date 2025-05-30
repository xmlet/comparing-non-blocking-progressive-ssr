<!DOCTYPE html>
<html>
<head>
<title>Stock Prices</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="/images/favicon.ico">
<link rel="stylesheet" type="text/css" href="/css/style.css" media="all">
<script type="text/javascript" src="/js/util.js"></script>
<style>
/*<![CDATA[*/
body {
	color: #333333;
	line-height: 150%;
}

thead {
	font-weight: bold;
	background-color: #CCCCCC;
}

.odd {
	background-color: #FFCCCC;
}

.even {
	background-color: #CCCCFF;
}

.minus {
	color: #FF0000;
}

/*]]>*/
</style>

</head>

<body>

	<h1>Stock Prices - Freemarker</h1>

	<table>
		<thead>
			<tr>
				<th>#</th>
				<th>symbol</th>
				<th>name</th>
				<th>price</th>
				<th>change</th>
				<th>ratio</th>
			</tr>
		</thead>
		<tbody>
			<#list stocks as stock>
			<tr class="${["even", "odd"][(stock_index+1) %2]}">
				<td>${stock_index + 1}</td>
				<td><a href="/stocks/${stock.getSymbol()}">${stock.getSymbol()}</a></td>
				<td><a href="${stock.getUrl()}">${stock.getName()}</a></td>
				<td><strong>${stock.getPrice()}</strong></td><#if (stock.getChange() < 0.0)>
				<td class="minus">${stock.getChange()}</td>
				<td class="minus">${stock.getRatio()}</td><#else>
				<td>${stock.getChange()}</td>
				<td>${stock.getRatio()}</td></#if>
			</tr>
			</#list>
		</tbody>
	</table>

</body>
</html>
