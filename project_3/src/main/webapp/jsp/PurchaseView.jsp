<%@page import="java.util.Map"%>
<%@page import="in.co.rays.project_3.controller.PurchaseCtl"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Validate.js"></script>
<style>
.hm {
    background-image: url('<%=ORSView.APP_CONTEXT%>/img/unsplash.jpg');
    background-size: cover;
    padding-top: 6%;
}
.input-group-addon {
    background-image: linear-gradient(to bottom right, orange, black);
    background-size: 100%;
    padding-bottom: 11px;
}
</style>
</head>
<body class="hm">
    <div class="header">
        <%@include file="Header.jsp"%>
        <%@include file="calendar.jsp"%>
    </div>
    <main>
        <form action="<%=ORSView.PURCHASE_CTL%>" method="post">
            <jsp:useBean id="dto" class="in.co.rays.project_3.dto.PurchaseDTO" scope="request" />
            
            <%
                Map map = (Map) request.getAttribute("purchase");
            %>

            <div class="row pt-3">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <div class="card input-group-addon">
                        <div class="card-body">
                            <h3 class="text-center text-primary">
                                <%= (dto.getId() != null && DataUtility.getLong(request.getParameter("id")) > 0) ? "Update Purchase" : "Add Purchase" %>
                            </h3>
                            <div>
                                <h4 align="center">
                                    <% if (!ServletUtility.getSuccessMessage(request).isEmpty()) { %>
                                    <div class="alert alert-success alert-dismissible">
                                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                                        <%=ServletUtility.getSuccessMessage(request)%>
                                    </div>
                                    <% } %>
                                </h4>
                                <h4 align="center">
                                    <% if (!ServletUtility.getErrorMessage(request).isEmpty()) { %>
                                    <div class="alert alert-danger alert-dismissible">
                                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                                        <%=ServletUtility.getErrorMessage(request)%>
                                    </div>
                                    <% } %>
                                </h4>
                                <input type="hidden" name="id" value="<%=dto.getId()%>">
                                <input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
                                <input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
                                <input type="hidden" name="createdDatetime" value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
                                <input type="hidden" name="modifiedDatetime" value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">
                            </div>
                            <div class="md-form">
                                <label>Total Quantity <span style="color: red;">*</span></label>
                                <input type="text" class="form-control" name="quantity" placeholder="Quantity" value="<%=DataUtility.getStringData(dto.getQuantity())%>">
                                <font color="red"> <%=ServletUtility.getErrorMessage("quantity", request)%></font>
                                
                                <label>Product <span style="color: red;">*</span></label>
                                <%=HTMLUtility.getList1("product", DataUtility.getStringData(dto.getProduct()), map)%>
                                <font color="red"> <%=ServletUtility.getErrorMessage("product", request)%></font>
                                
                                <label>Cost <span style="color: red;">*</span></label>
                                <input type="text" class="form-control" name="totalCost" placeholder="Cost" value="<%=DataUtility.getStringData(dto.getTotalCost())%>">
                                <font color="red"> <%=ServletUtility.getErrorMessage("totalCost", request)%></font>
                                
                                <label>Order Date <span style="color: red;">*</span></label>
                                <input type="text" id="datepicker" name="orderDate" class="form-control" placeholder="Order Date" readonly value="<%=DataUtility.getDateString(dto.getOrderDate())%>">
                                <font color="red"> <%=ServletUtility.getErrorMessage("orderDate", request)%></font>
                            </div>
                            <div class="text-center">
                                <% if (dto.getId() != null && DataUtility.getLong(request.getParameter("id")) > 0) { %>
                                    <input type="submit" name="operation" class="btn btn-success" value="<%=PurchaseCtl.OP_UPDATE%>">
                                    <input type="submit" name="operation" class="btn btn-warning" value="<%=PurchaseCtl.OP_CANCEL%>">
                                <% } else { %>
                                    <input type="submit" name="operation" class="btn btn-success" value="<%=PurchaseCtl.OP_SAVE%>">
                                    <input type="submit" name="operation" class="btn btn-warning" value="<%=PurchaseCtl.OP_RESET%>">
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4"></div>
            </div>
        </form>
    </main>
    <%@include file="FooterView.jsp"%>
</body>
</html>
