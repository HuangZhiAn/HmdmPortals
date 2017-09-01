<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/static/css/indexDivCss.css">
<script type="text/javascript" src="<%=path%>/static/js/indexDivJs.js"></script>
</head>
<body>
	<div class="img-div">
		<img class="index-top-bg" alt=""
			src="<%=path%>/static/img/webp/index_top_bg.webp">
		<div class="text-div">
			<div class="text-content">
				<div class="product-div">
					<div class="product-info">
						<p>HMDM is the master data management platform developed by
							HAND in order to meet the needs of unified management of master
							data. It aims to help enterprises solve the information island
							problem with new technical methods.</p>
						<p>Based on the HAP framework of HAND, the integration of
							Spring, Spring MVC, MyBatis and other open source technologies,
							using REST, SOA micro-service architecture, through Maven,
							Liquibase, Jenkins technology to achieve cross-platform, cross
							DB, continuous deployment</p>
						<p>The use of load balancing, reverse proxy, server cluster,
							database master-slave replication, cache, Zookeeper and other
							technologies, the system using WebService, ESB, MQ and other
							technologies to achieve data exchange between systems</p>
					</div>
					<a href="javascript:void(0);">DownLoad</a>
				</div>
				<img class="introduce-img"
					src="<%=path%>/static/img/home_HMDM_introduce.jpg">
			</div>
		</div>
	</div>

	<div class="content-div">
		<div class="bg-div">
			<img src="<%=path%>/static/img/index_test_bg.png">
		</div>
		<div class="message-div">
			<div class="message-center">
				<div class="message-text">
					<div class="message-title">Master Data Definition</div>
					<div class="message-content">
						<div>
							<p>HMDM can manage any type of master data according to enterprise management needs, business needs, system integration needs, including but not limited to: customer, vendor, project, mater, account, organization, staff, price, etc.</p>
							<p>The system administrator can customize the master data type, classification, attribute, attribute value set in HMDM, etc.</p>
							<p>Custom Encoder: The system administrator can customize the encoding and description rules, the system automatically generated by the rules of coding or description</p>
							<p>Pre-configured data attributes: Pre-configured data attributes for ORACEL EBS, SAP ECC</p>
						</div>
						<a href="javascript:void(0);">More+</a>
					</div>
					<div class="message-others"></div>
				</div>
				<div class="message-img">
					<img src="<%=path%>/static/img/index_test_bg2.jpg">
				</div>
			</div>
		</div>
	</div>
	<div class="content-div">
		<div class="bg-div">
			<img src="<%=path%>/static/img/index_test_bg.png">
		</div>
		<div class="message-div">
			<div class="message-center">
				<div class="message-text">
					<div class="message-title">Master Data Definition</div>
					<div class="message-content">
						<div>
							<p>HMDM can manage any type of master data according to enterprise management needs, business needs, system integration needs, including but not limited to: customer, vendor, project, mater, account, organization, staff, price, etc.</p>
							<p>The system administrator can customize the master data type, classification, attribute, attribute value set in HMDM, etc.</p>
							<p>Custom Encoder: The system administrator can customize the encoding and description rules, the system automatically generated by the rules of coding or description</p>
							<p>Pre-configured data attributes: Pre-configured data attributes for ORACEL EBS, SAP ECC</p>
						</div>
						<a href="javascript:void(0);">More+</a>
					</div>
					<div class="message-others"></div>
				</div>
				<div class="message-img">
					<img src="<%=path%>/static/img/index_test_bg2.jpg">
				</div>
			</div>
		</div>
	</div>
</body>
</html>

