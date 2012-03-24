<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script language="javascript" src="javascript/jquery-1.6.2.js"></script>
<script src="javascript/grid.locale-en.js" type="text/javascript"></script>
<script src="javascript/jquery.jqGrid.src.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" media="screen" href="css/ui.jqgrid.css" />
</head>
<body>
<input id="it" type="button" value="click" />
<span id="dinerData">
<table id="list" class="scroll"></table> 
<div id="pager" class="scroll" style="text-align:center;"></div> 
<script type="text/javascript">

$(document).ready(function() {
	  jQuery("#list").jqGrid({
	    url:'http://localhost:8080/DinerServices/DinerServices?latitude=42.11&longitude=-72.11&radius=100',
	    datatype: 'json',
	    mtype: 'GET',
	    colNames:['Name','Street', 'City','State','Zip','Web Site', 'Latitude', 'Longitude'],
	    colModel :[ 
	      {name:'name', index:'name', width:55}, 
	      {name:'street', index:'street', width:90}, 
	      {name:'city', index:'city', width:80, align:'right'}, 
	      {name:'state', index:'state', width:80, align:'right'}, 
	      {name:'zip', index:'zip', width:80, align:'right'}, 
	      {name:'webSite', index:'webSite', width:80, align:'right'},
	      {name:'latitude', index:'latitude', width:80, align:'right'},
	      {name:'longitude', index:'longitude', width:80, align:'right'}],
	    pager: jQuery('#pager'),
	    rowNum:10,
	    rowList:[10,20,30],
	    viewrecords: true,
	    jsonReader : [{
	    	  root: "serviceBeans",
	    	  page: "page",
	    	  total: "total",
	    	  cell: "serviceBean"
	    	}],
	    imgpath: 'themes/basic/images',
	    caption: 'My first grid'
	  }); 

	//jQuery("#list").jqGrid({ url: 'books.xml', datatype: "xml", colNames:["Author","Title", "Price", "Published Date"], colModel:[ {name:"Author",index:"Author", width:120, xmlmap:"ItemAttributes>Author"}, {name:"Title",index:"Title", width:180,xmlmap:"ItemAttributes>Title"}, {name:"Price",index:"Manufacturer", width:100, align:"right",xmlmap:"ItemAttributes>Price", sorttype:"float"}, {name:"DatePub",index:"ProductGroup", width:130,xmlmap:"ItemAttributes>DatePub",sorttype:"date"} ], height:250, rowNum:10, rowList:[10,20,30], viewrecords: true, loadonce: true, xmlReader: { root : "Items", row: "Item", repeatitems: false, id: "ASIN" }, caption: "XML Mapping example" });
	$('#it').click(function(){
		$.get( "http://localhost:8080/DinerServices/DinerServices", {latitude:"42.11",longitude:"-72.11",radius:"100"}, function(data){ $("dinerData").html( data ); alert("Ajax call complete." + data);});
	});
		
	 });

</script>
</span>
</body>
</html>