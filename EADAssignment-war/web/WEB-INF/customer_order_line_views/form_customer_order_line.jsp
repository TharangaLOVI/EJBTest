<%-- 
    Document   : form_customer_order_line
    Created on : Aug 17, 2015, 8:36:37 AM
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
                        
                        
                        <h4 ><%=(Integer.parseInt(request.getAttribute("isUpdate").toString()) == 0)?"Add ":"Update " %> Customer Order Line </h4>
                        <form class="form-horizontal" method="post">
                            <fieldset>
                                
                                <input type="hidden" name="hdn_update_form" value="${isUpdate}" />
                                <input type="hidden" name="orderNo" value="${customerOrder.orderNo}" />
                                
                                <div class="form-group">
                                    <label for="lineNo" class="col-lg-2 control-label">Line No</label>
                                    <div class="col-lg-10">
                                        <input type="text"  
                                               class="form-control" 
                                               id="lineNo" 
                                               name="lineNo" 
                                               placeholder="Line No" 
                                               value="${customerOrderLine.lineNo}" 

                                               <%
                                                   if (Integer.parseInt(request.getAttribute("isUpdate").toString()) == 1) {
                                               %>
                                               readonly
                                               <% }%>
                                               >

                                        <span class="label label-danger">${errorMessage[0]}</span>
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <label for="partNo" class="col-lg-2 control-label">Part No</label>
                                    <div class="col-lg-10">
                                        <input type="text" class="form-control" id="partNo" name="partNo" placeholder="Part No" value="${customerOrderLine.partNo}">
                                        <span class="label label-danger">${errorMessage[1]}</span>
                                    </div>
                                </div>
                                    
                                <div class="form-group">
                                    <label for="amount" class="col-lg-2 control-label">Amount</label>
                                    <div class="col-lg-10">
                                        <input type="text" class="form-control" id="amount" name="amount" placeholder="Amount" value="${customerOrderLine.amount}">
                                        <span class="label label-danger">${errorMessage[2]}</span>
                                    </div>
                                </div>
                                    
                                <div class="form-group">
                                    <div class="col-lg-10 col-lg-offset-2">
                                        <button 
                                            type="button" 
                                            class="btn btn-default" 
                                            onclick="window.location='${pageContext.request.contextPath}/customer_order_line?id=${customerOrder.orderNo}'">
                                            Cancel
                                        </button>
                                        <button type="submit" class="btn btn-primary">Submit</button>
                                    </div>
                                </div>
                                
                            </fieldset>
                        </form>
                        
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