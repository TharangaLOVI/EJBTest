<%-- 
    Document   : form_customer_order
    Created on : Aug 12, 2015, 2:11:28 AM
    Author     : Tharanga
--%>

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
                        <h3 class="panel-title">Customer Order <%=(Integer.parseInt(request.getAttribute("isUpdate").toString()) == 0)?" Add ":" Update " %></h3>

                    </div>
                    <div class="panel-body">
                        <form class="form-horizontal" method="post">
                            <% CustomerOrderEntity cuo = (CustomerOrderEntity)request.getAttribute("customerOrder");  %>
                            <fieldset>
                                <input type="hidden" name="hdn_update_form" value="${isUpdate}" />
                                <div class="form-group">
                                    <label for="orderNo" class="col-lg-2 control-label">Order No</label>
                                    <div class="col-lg-10">
                                        <input type="text"  
                                               class="form-control" 
                                               id="orderNo" 
                                               name="orderNo" 
                                               placeholder="Order No" 
                                               value="${customerOrder.orderNo}" 

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
                                    <label for="customer" class="col-lg-2 control-label">Customer</label>
                                    <div class="col-lg-10">
                                        
                                        <% 
                                            CustomerEntity selectedCustomer = null;
                                             
                                            if(cuo != null){
                                                selectedCustomer = cuo.getCustomer();
                                            }
                                        %>
                                        <select id="customer" name="customer" class="form-control">
                                            <option disabled="disabled" selected="selected" value="">Select customer</option>
                                            <%
                                                List<CustomerEntity> customerEntitys = (List<CustomerEntity>) request.getAttribute("customers");
                                                if (customerEntitys != null) {
                                                    for (CustomerEntity customerEntity : customerEntitys) {
                                            %>
                                                        <option
                                                            
                                                            <%
                                                                if(selectedCustomer != null && selectedCustomer.getCustomerId() == customerEntity.getCustomerId()){
                                                                    out.print(" selected='selected' ");
                                                                }
                                                            %>
                                                            
                                                            value="<%=customerEntity.getCustomerId()%>"
                                                        >
                                                            <%=customerEntity.getName()%>
                                                        </option>
                                            <% }
                                                }%>


                                        </select>
                                        <span class="label label-danger">${errorMessage[4]}</span>
                                    </div>
                                </div>                


                                <div class="form-group">
                                    <label for="dueDate" class="col-lg-2 control-label">Due Date</label>
                                    <div class="col-lg-10">
                                        <input type="date" class="form-control" id="dueDate" name="dueDate" placeholder="Due Date" value="${customerOrder.dueDate}">
                                        <span class="label label-danger">${errorMessage[1]}</span>
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
                                            readonly 
                                            value="${customerOrder.amount}"
                                            >
                                        <span class="label label-danger">${errorMessage[3]}</span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="comment" class="col-lg-2 control-label">Comment</label>
                                    <div class="col-lg-10">
                                        <textarea class="form-control" rows="3" id="comment" name="comment" >${customerOrder.comment}</textarea>
                                        <span class="help-block"></span>
                                        <span class="label label-danger">${errorMessage[2]}</span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-lg-10 col-lg-offset-2">
                                        <button 
                                            type="button" 
                                            class="btn btn-default"
                                            onclick="window.location='${pageContext.request.contextPath}/customer_order'"
                                        >
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
