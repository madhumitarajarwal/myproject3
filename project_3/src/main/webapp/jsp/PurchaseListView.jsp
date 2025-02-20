<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="in.co.rays.project_3.dto.PurchaseDTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.model.ModelFactory"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.PurchaseListCtl"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Purchase List</title>

<!-- Include JavaScript Files -->
<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Validate.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.hm {
    background-image: url('<%=ORSView.APP_CONTEXT%>/img/rain.jpg');
    background-size: cover;
    background-repeat: no-repeat;
    padding-top: 6%;
}
.text {
    text-align: center;
}
</style>
</head>

<body class="hm">
    <%@include file="Header.jsp"%>
    <%@include file="calendar.jsp"%>

    <form action="<%=ORSView.PURCHASE_LIST_CTL%>" method="post">
        <jsp:useBean id="dto" class="in.co.rays.project_3.dto.PurchaseDTO" scope="request"></jsp:useBean>

        <%
            Map<String, String> map = (Map<String, String>) request.getAttribute("purchase");
            int pageNo = ServletUtility.getPageNo(request);
            int pageSize = ServletUtility.getPageSize(request);
            int index = ((pageNo - 1) * pageSize) + 1;
            int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

            List<PurchaseDTO> list = ServletUtility.getList(request);
            Iterator<PurchaseDTO> it = list.iterator();
        %>

        <center>
            <h1 class="text-primary font-weight-bold pt-3"><u>Purchase List</u></h1>
        </center>

        <!-- Display Success Message -->
        <% if (!ServletUtility.getSuccessMessage(request).isEmpty()) { %>
        <div class="alert alert-success alert-dismissible">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <h4><%=ServletUtility.getSuccessMessage(request)%></h4>
        </div>
        <% } %>

        <!-- Display Error Message -->
        <% if (!ServletUtility.getErrorMessage(request).isEmpty()) { %>
        <div class="alert alert-danger alert-dismissible">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <h4><%=ServletUtility.getErrorMessage(request)%></h4>
        </div>
        <% } %>

        <!-- Filter Section -->
        <div class="row">
            <div class="col-sm-2"></div>
            <div class="col-sm-2">
                <input type="text" name="totalCost" placeholder="Enter totalCost" class="form-control" 
                       value="<%=ServletUtility.getParameter("totalCost", request)%>">
            </div>
            <div class="col-sm-2">
                <input type="text" name="orderDate" id="datepicker" placeholder="Enter order Date" class="form-control"
                       readonly value="<%=ServletUtility.getParameter("orderDate", request)%>">
            </div>
            <div class="col-sm-2">
                <input type="submit" class="btn btn-primary btn-md" name="operation" value="<%=PurchaseListCtl.OP_SEARCH%>">
                <input type="submit" class="btn btn-dark btn-md" name="operation" value="<%=PurchaseListCtl.OP_RESET%>">
            </div>
            <div class="col-sm-2"></div>
        </div>

        <br>

        <!-- Table to Display Purchase List -->
        <% if (!list.isEmpty()) { %>
        <div class="table-responsive">
            <table class="table table-bordered table-dark table-hover">
                <thead>
                    <tr style="background-color: blue;">
                        <th width="10%"><input type="checkbox" id="select_all"> Select All</th>
                        <th width="5%" class="text">S.NO</th>
                        <th width="15%" class="text">Product</th>
                        <th width="15%" class="text">Quantity</th>
                        <th width="20%" class="text">Cost</th>
                        <th width="20%" class="text">Order Date</th>
                        <th width="5%" class="text">Edit</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        while (it.hasNext()) {
                            dto = it.next();
                            String formattedDate = (dto.getOrderDate() != null) ? sdf.format(dto.getOrderDate()) : "N/A";
                    %>
                    <tr>
                        <td align="center"><input type="checkbox" class="checkbox" name="ids" value="<%=dto.getId()%>"></td>
                        <td class="text"><%=index++%></td>
                        <td class="text"><%= map.getOrDefault(dto.getProduct(), "N/A") %></td>
                        <td class="text"><%= dto.getQuantity() %></td>
                        <td class="text"><%= map.getOrDefault(dto.getTotalCost(), "N/A") %></td>
                        <td class="text"><%= formattedDate %></td>
                        <td class="text"><a href="PurchaseCtl?id=<%= dto.getId() %>">Edit</a></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <!-- Pagination and Action Buttons -->
        <table width="100%">
            <tr>
                <td><input type="submit" name="operation" class="btn btn-warning btn-md" value="<%=PurchaseListCtl.OP_PREVIOUS%>" <%= (pageNo > 1) ? "" : "disabled" %>></td>
                <td><input type="submit" name="operation" class="btn btn-primary btn-md" value="<%=PurchaseListCtl.OP_NEW%>"></td>
                <td><input type="submit" name="operation" class="btn btn-danger btn-md" value="<%=PurchaseListCtl.OP_DELETE%>"></td>
                <td align="right"><input type="submit" name="operation" class="btn btn-warning btn-md" value="<%=PurchaseListCtl.OP_NEXT%>" <%= (nextPageSize != 0) ? "" : "disabled" %>></td>
            </tr>
        </table>

        <% } else { %>
        <center><h3>No purchases found.</h3></center>
        <% } %>

        <input type="hidden" name="pageNo" value="<%=pageNo%>">
        <input type="hidden" name="pageSize" value="<%=pageSize%>">
    </form>

    <%@include file="FooterView.jsp"%>
</body>
</html>
