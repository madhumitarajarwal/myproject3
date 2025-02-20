<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.controller.ItemCtl"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User view</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Validate.js"></script>

<style type="text/css">
i.css {
	border: 2px solid #8080803b;
	padding-left: 10px;
	padding-bottom: 11px;
	background-color: #ebebe0;
}

.input-group-addon {
	/* box-shadow: 9px 8px 7px #001a33; */
	background-image: linear-gradient(to bottom right, orange, black);
	background-repeat: no repeat;
	background-size: 100%;
	padding-bottom: 11px;
}

.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/unsplash.jpg');
	background-size: cover;
	padding-top: 6%;
}
</style>

</head>
<body class="hm">
	<div class="header">
		<%@include file="Header.jsp"%>
		<%@include file="calendar.jsp"%>
	</div>
	<div>

		<main>
		<form action="<%=ORSView.ITEM_CTL%>" method="post">
			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.ItemDTO"
				scope="request"></jsp:useBean>
			<div class="row pt-3">
				<!-- Grid column -->
				<div class="col-md-4 mb-4"></div>
				<div class="col-md-4 mb-4">
					<div class="card input-group-addon">
						<div class="card-body">



							<%
								long id = DataUtility.getLong(request.getParameter("id"));

								if (dto.getId() != null && id > 0) {
							%>
							<h3 class="text-center default-text text-primary">Update
								Item</h3>
							<%
								} else {
							%>
							<h3 class="text-center default-text text-primary">Add Item</h3>
							<%
								}
							%>
							<!--Body-->
							<div>
								<%
									Map map = (Map) request.getAttribute("item");
								%>

								<H4 align="center">
									<%
										if (!ServletUtility.getSuccessMessage(request).equals("")) {
									%>
									<div class="alert alert-success alert-dismissible">
										<button type="button" class="close" data-dismiss="alert">&times;</button>
										<%=ServletUtility.getSuccessMessage(request)%>
									</div>
									<%
										}
									%>
								</H4>

								<H4 align="center">
									<%
										if (!ServletUtility.getErrorMessage(request).equals("")) {
									%>
									<div class="alert alert-danger alert-dismissible">
										<button type="button" class="close" data-dismiss="alert">&times;</button>
										<%=ServletUtility.getErrorMessage(request)%>
									</div>
									<%
										}
									%>

								</H4>

								<input type="hidden" name="id" value="<%=dto.getId()%>">
								<input type="hidden" name="createdBy"
									value="<%=dto.getCreatedBy()%>"> <input type="hidden"
									name="modifiedBy" value="<%=dto.getModifiedBy()%>"> <input
									type="hidden" name="createdDatetime"
									value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
								<input type="hidden" name="modifiedDatetime"
									value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">
							</div>

							<div class="md-form">

								<span class="pl-sm-5"><b>Title</b> <span
									style="color: red;">*</span></span> </br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-user-alt grey-text" style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control" name="title"
											placeholder="title"
											oninput=" handleLetterInput(this, 'titleError', 15)"
											onblur=" validateLetterInput(this, 'titleError', 15)"
											value="<%=DataUtility.getStringData(dto.getTitle())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5" id="titleError"> <%=ServletUtility.getErrorMessage("title", request)%></font></br>



								<span class="pl-sm-5"><b>Category</b> <span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-user-circle grey-text"
													style="font-size: 1rem;"></i>
											</div>
										</div>


										<%=HTMLUtility.getList1("category", DataUtility.getStringData(dto.getCategory()), map)%>

									</div>
								</div>
								<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("category", request)%></font></br>



								<%-- 	<span class="pl-sm-5"><b>Overview</b> <span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-user-circle grey-text"
													style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control"
											name="overView" placeholder="overView"
												oninput=" handleLetterInput(this, 'overViewError', 15)"
							                onblur=" validateLetterInput(this, 'overViewError', 15)"
											value="<%=DataUtility.getStringData(dto.getOverView())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5" id=""overView"Error"> <%=ServletUtility.getErrorMessage("overView", request)%></font></br>

 --%>

								<span class="pl-sm-5"><b>Overview</b> <span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-user-circle grey-text"
													style="font-size: 1rem;"></i>
											</div>
										</div>
										<textarea class="form-control" name="overView"
											placeholder="Enter overview"
											oninput="handleLetterInput(this, 'overViewError', 25)"
											onblur="validateLetterInput(this, 'overViewError', 25)"><%=DataUtility.getStringData(dto.getOverView())%></textarea>
									</div>
								</div>
								<font color="red" class="pl-sm-5" id="overViewError"> <%=ServletUtility.getErrorMessage("overView", request)%>
								
								
								
								</font></br> <span class="pl-sm-5"><b>Cost</b> <span
									style="color: red;">*</span></span> </br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-user-alt grey-text" style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" class="form-control" name="cost"
											placeholder="cost"
											oninput="handleIntegerInput(this, 'costError', 15)"
											onblur=" validateIntegerInput(this, 'costError', 15)"
											value="<%=DataUtility.getStringData(dto.getCost()==0?"":dto.getCost())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5" id="costError"> <%=ServletUtility.getErrorMessage("cost", request)%></font></br>





								<span class="pl-sm-5"><b>PurchaseDate</b> <span
									style="color: red;">*</span></span></br>
								<div class="col-sm-12">
									<div class="input-group">
										<div class="input-group-prepend">
											<div class="input-group-text">
												<i class="fa fa-calendar grey-text" style="font-size: 1rem;"></i>
											</div>
										</div>
										<input type="text" id="datepicker" name="date"
											class="form-control" placeholder="Purchase date"
											readonly="readonly"
											value="<%=DataUtility.getDateString(dto.getPurchaseDate())%>">
									</div>
								</div>
								<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("date", request)%></font></br>



								<%
									if (dto.getId() != null && id > 0) {
								%>

								<div class="text-center">

									<input type="submit" name="operation"
										class="btn btn-success btn-md" style="font-size: 17px"
										value="<%=ItemCtl.OP_UPDATE%>"> <input type="submit"
										name="operation" class="btn btn-warning btn-md"
										style="font-size: 17px" value="<%=ItemCtl.OP_CANCEL%>">

								</div>
								<%
									} else {
								%>
								<div class="text-center">

									<input type="submit" name="operation"
										class="btn btn-success btn-md" style="font-size: 17px"
										value="<%=ItemCtl.OP_SAVE%>"> <input type="submit"
										name="operation" class="btn btn-warning btn-md"
										style="font-size: 17px" value="<%=ItemCtl.OP_RESET%>">
								</div>

							</div>
							<%
								}
							%>
						</div>
					</div>
		</form>
		</main>
		<div class="col-md-4 mb-4"></div>

	</div>

</body>
<%@include file="FooterView.jsp"%>

</body>
</html>