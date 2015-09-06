<%-- 
    Document   : list_customer_orders
    Created on : Aug 11, 2015, 11:00:26 PM
    Author     : Tharanga
--%>

<%@page import="sliit.ead.assignment.ejb.CustomerOrderEntity"%>
<%@page import="java.util.List"%>
<%@include file="../includes/header.jsp"%>

<!-- 1 -->
<div class="col-lg-9 col-md-9">

    <!-- 1-1 -->
    <div class="container-fluid">

        <div class="row">
            <div class="col-lg-12 col-md-12">

                <!-- Content -->
                <div class="panel panel-material-light-blue-500">
                    <div class="panel-heading">
                        <h3 class="panel-title">Customer Orders List</h3>

                    </div>
                    <div class="panel-body">
                        <div class="alert alert-dismissable alert-success" id="div_message" style="display: none">

                            <h4>Message!</h4>
                            <p><a href="#" class="alert-link"><%=request.getParameter("msg")%></a>.</p>
                        </div>
                        <% if (request.getParameter("msg") != null) { %>
                            <script>showNotification()</script>
                        <% }%>
                        <a class="btn btn-info btn-raised" style="float: right" href="${pageContext.request.contextPath}/customer_order/form_order"
                           data-toggle="modal" data-target="#"> New
                            Customer Order
                        </a>


                        <% List<CustomerOrderEntity> customerOrders = (List<CustomerOrderEntity>) request.getAttribute("customerOrders");
                            if (customerOrders.size() > 0) {
                        %>
                        <table class="table table-striped table-hover ">
                            <thead>
                                <tr>
                                    <th style="width: 100px">Update</th>
                                    <th >Order No</th>
                                    <th>Customer Id</th>
                                    <th>Customer Name</th>
                                    <th>Amount</th>
                                    <th>Due Date</th>
                                    <th>Comment</th>
                                    
                                </tr>
                            </thead>
                            <tbody>
                                <% for (CustomerOrderEntity customerOrderEntity : customerOrders) {%>
                                <tr>
                                    <td><a href="${pageContext.request.contextPath}/customer_order/form_order?id=<%=customerOrderEntity.getOrderNo()%>">Update</a></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/customer_order_line?id=<%=customerOrderEntity.getOrderNo()%>">
                                            <%=customerOrderEntity.getOrderNo() %>
                                        </a>
                                        
                                    
                                    </td>
                                    
                                    <td><%=customerOrderEntity.getCustomer().getCustomerId() %></td>
                                    <td><%=customerOrderEntity.getCustomer().getName() %></td>
                                    <td><%=customerOrderEntity.getAmount() %></td>
                                    <td><%=customerOrderEntity.getDueDate() %></td>
                                    <td><%=customerOrderEntity.getComment() %></td>
                                    
                                    
                                </tr>

                                <% } %>

                            </tbody>
                        </table>
                        <% } else {%>
                        <br><br><br>
                        <div class="alert alert-dismissable alert-warning">

                            <h4>Message!</h4>
                            <p><a href="javascript:void(0)" class="alert-link">Empty Customer Orders</a>.</p>
                        </div>
                        <% }%>
                    </div>
                </div>


            </div>
        </div>
        <!-- row end -->

    </div>
    <!-- 1-1 End-->
</div>
<!-- 1 End-->



<%@include file="../includes/footer.jsp"%>

