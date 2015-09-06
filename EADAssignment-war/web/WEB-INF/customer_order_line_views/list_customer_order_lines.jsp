<%-- 
    Document   : view_customer_order
    Created on : Aug 16, 2015, 11:28:07 AM
    Author     : Tharanga
--%>

<%@page import="sliit.ead.assignment.ejb.CustomerOrderLine"%>
<%@page import="sliit.ead.assignment.ejb.CustomerOrderEntity"%>
<%@page import="sliit.ead.assignment.ejb.CustomerEntity"%>
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
                        <h3 class="panel-title">Customer Order View</h3>

                    </div>
                    <div class="panel-body">
                        <div class="alert alert-dismissable alert-success" id="div_message" style="display: none">

                            <h4>Message!</h4>
                            <p><a href="#" class="alert-link"><%=request.getParameter("msg")%></a>.</p>
                        </div>
                        <% if (request.getParameter("msg") != null) { %>
                            <script>showNotification()</script>
                        <% }%>
                        <div class="form-horizontal" >
                            <fieldset>

                                <div class="form-group">
                                    <label for="orderNo" class="col-lg-2 control-label">Order No</label>
                                    <div class="col-lg-10">
                                        <input type="text"  
                                               class="form-control" 
                                               id="orderNo" 
                                               name="orderNo" 

                                               value="${customerOrder.orderNo}"
                                               readonly
                                               disabled
                                               />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="customer" class="col-lg-2 control-label">Customer Id</label>
                                    <div class="col-lg-10">
                                        <input type="text"  
                                               class="form-control" 
                                               id="customer" 
                                               name="customer" 
                                               value="${customerOrder.customer.customerId}"
                                               readonly
                                               disabled
                                               />
                                    </div>

                                </div>

                                <div class="form-group">
                                    <label for="customer" class="col-lg-2 control-label">Customer Name</label>
                                    <div class="col-lg-10">
                                        <input type="text"  
                                               class="form-control" 
                                               id="customer" 
                                               name="customer" 
                                               value="${customerOrder.customer.name}"
                                               readonly
                                               disabled
                                               />
                                    </div>
                                </div>             

                                <div class="form-group">
                                    <label for="dueDate" class="col-lg-2 control-label">Due Date</label>
                                    <div class="col-lg-10">
                                        <input 
                                            type="date" 
                                            class="form-control" 
                                            id="dueDate" 
                                            name="dueDate" 
                                            value="${customerOrder.dueDate}"
                                            readonly
                                            disabled
                                            >

                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="amount" class="col-lg-2 control-label">Amount</label>
                                    <div class="col-lg-10">
                                        <input 
                                            type="text" 
                                            class="form-control" 
                                            id="amount" 
                                            name="amount" 
                                            placeholder="Amount" 

                                            value="${customerOrder.amount}"
                                            readonly
                                            disabled
                                            >

                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="comment" class="col-lg-2 control-label">Comment</label>
                                    <div class="col-lg-10">
                                        <textarea class="form-control" rows="3" id="comment" name="comment" readonly disabled>${customerOrder.comment}</textarea>
                                        <span class="help-block"></span>

                                    </div>
                                </div>
                            </fieldset>
                        </div>
                        
                        <hr style="border-width:1px">
                        <div class="form-horizontal">                
                            
                            <%
                                List<CustomerOrderLine> customerOrderLines = ((CustomerOrderEntity) request.getAttribute("customerOrder")).getCustomerOrderLines();
                                if (customerOrderLines.size() > 0) {
                            %>
                            <table class="table table-striped table-hover ">
                                <thead>
                                    <tr>
                                        <th style="width: 100px">Update</th>
                                        <th style="width: 100px">Remove</th>
                                        <th>Line No</th>
                                        <th>Part No</th>
                                        <th>Amount</th>

                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                        int i = 0;
                                        for (CustomerOrderLine orderLine : customerOrderLines) {%>
                                    <tr>
                                        <td>
                                            <a 
                                                href="${pageContext.request.contextPath}/customer_order_line/form_order_line?order_no=${customerOrder.orderNo}&line_no=<%=orderLine.getLineNo()%>"
                                            >Update</a>
                                        </td>
                                        <td>
                                            <script type="text/javascript">
                                                function formRemoveSubmit() {
                                                    var r = confirm("Are you sure do you want to remove this customer order?");
                                                    if (r == false) {
                                                        return;
                                                    }

                                                    document.getElementById("form_remove_<%=i %>").submit();
                                                }
                                            </script>
                                            <form action="${pageContext.request.contextPath}/customer_order_line/remove" id="form_remove_<%=i++ %>" method="post">
                                                <input type="hidden" name="orderNo" value="${customerOrder.orderNo}" />
                                                <input type="hidden" name="lineNo" value="<%=orderLine.getLineNo()%>" />
                                                <a href="javascript:formRemoveSubmit()">Remove</a>
                                            </form>

                                        </td>

                                        <td><%=orderLine.getLineNo()%></td>
                                        <td><%=orderLine.getPartNo()%></td>
                                        <td><%=orderLine.getAmount()%></td>

                                    </tr>
                                    <% }%>
                                </tbody>
                            </table>


                            <%
                            } else {
                            %>
                            
                            <div class="alert alert-dismissable alert-warning">

                                <h4>Message!</h4>
                                <p><a href="javascript:void(0)" class="alert-link">Empty Customer Orders Lines</a>.</p>
                            </div>
                            <%
                                }
                            %>

                            <a 
                               class="btn btn-info btn-raised" 
                               style="float: right" 
                               href="${pageContext.request.contextPath}/customer_order_line/form_order_line?order_no=${customerOrder.orderNo}"
                               data-toggle="modal" data-target="#">Add New Customer Order Line
                            </a>
                        </div>
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