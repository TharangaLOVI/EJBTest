<%-- 
    Document   : form_customer
    Created on : Aug 8, 2015, 11:17:36 PM
    Author     : Tharanga
--%>

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
                        <h3 class="panel-title">Customer <%=(Integer.parseInt(request.getAttribute("isUpdate").toString()) == 0)?" Add ":" Update " %></h3>

                    </div>
                    <div class="panel-body">
                        <form class="form-horizontal" method="post">
                            <fieldset>
                                <input type="hidden" name="hdn_update_form" value="${isUpdate}" />
                                <div class="form-group">
                                    <label for="customerId" class="col-lg-2 control-label">Customer Id</label>
                                    <div class="col-lg-10">
                                        <input type="text"  
                                               class="form-control" 
                                               id="customerId" 
                                               name="customerId" 
                                               placeholder="Customer Id" 
                                               value="${customer.customerId}" 

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
                                    <label for="name" class="col-lg-2 control-label">Name</label>
                                    <div class="col-lg-10">
                                        <input type="text" class="form-control" id="name" name="name" placeholder="Name" value="${customer.name}">
                                        <span class="label label-danger">${errorMessage[1]}</span>
                                    </div>
                                </div>




                                <div class="form-group">
                                    <label for="address" class="col-lg-2 control-label">Address</label>
                                    <div class="col-lg-10">
                                        <textarea class="form-control" rows="3" id="address" name="address" >${customer.address}</textarea>
                                        <span class="help-block"></span>
                                        <span class="label label-danger">${errorMessage[2]}</span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="contactNo" class="col-lg-2 control-label">Contact No</label>
                                    <div class="col-lg-10">
                                        <input type="text" class="form-control" id="contactNo"  name="contactNo" placeholder="Contact No" value="${customer.contactNo}">
                                        <span class="label label-danger">${errorMessage[3]}</span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-lg-10 col-lg-offset-2">
                                        <button 
                                            type="button" 
                                            class="btn btn-default"
                                            onclick="window.location='${pageContext.request.contextPath}/customer'"
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
