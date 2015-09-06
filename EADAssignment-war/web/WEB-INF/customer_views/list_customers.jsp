<%-- 
    Document   : list_customers
    Created on : Aug 8, 2015, 2:00:53 AM
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
                        <h3 class="panel-title">Customers List</h3>

                    </div>
                    <div class="panel-body">
                        <div class="alert alert-dismissable alert-success" id="div_message" style="display: none">

                            <h4>Message!</h4>
                            <p><a href="#" class="alert-link"><%=request.getParameter("msg")%></a>.</p>
                        </div>
                        <% if (request.getParameter("msg") != null) { %>
                        <script>showNotification()</script>
                        <% }%>
                        <a class="btn btn-info btn-raised" style="float: right" href="${pageContext.request.contextPath}/customer/form_customer"
                           data-toggle="modal" data-target="#studentAddModal"> New
                            Customer 
                        </a>


                        <% List<CustomerEntity> customerEntitys = (List<CustomerEntity>) request.getAttribute("customers");
                            if (customerEntitys.size() > 0) {
                        %>
                        <table class="table table-striped table-hover ">
                            <thead>
                                <tr>
                                    <th style="width: 100px">Update</th>
                                    <th style="width: 100px">Remove</th>
                                    <th style="width: 100px">Customer Id</th>
                                    <th>Name</th>
                                    <th>Address</th>
                                    <th>Contact No</th>

                                </tr>
                            </thead>
                            <tbody>
                                <%  int i = 0;
                                    for (CustomerEntity customerEntity : customerEntitys) {%>
                                <tr>
                                    <td><a href="${pageContext.request.contextPath}/customer/form_customer?id=<%=customerEntity.getCustomerId()%>">Update</a></td>
                                    <td>
                                        <script type="text/javascript">
                                            function formRemoveSubmit() {
                                                var r = confirm("Are you sure do you want to remove this customer ?");
                                                if (r == false) {
                                                    return;
                                                }

                                                document.getElementById("form_remove_<%=i %>").submit();
                                            }
                                        </script>
                                        <form action="${pageContext.request.contextPath}/customer/remove" id="form_remove_<%=i++ %>" method="post">
                                            <input type="hidden" name="id" value="<%=customerEntity.getCustomerId()%>" />
                                            <a href="javascript:formRemoveSubmit()">Remove</a>
                                        </form>
                                    </td>
                                    <td><%=customerEntity.getCustomerId()%></td>
                                    <td><%=customerEntity.getName()%></td>
                                    <td><%=customerEntity.getAddress()%></td>
                                    <td><%=customerEntity.getContactNo()%></td>

                                </tr>

                                <%}%>

                            </tbody>
                        </table>
                        <% } else {%>
                        <br><br><br>
                        <div class="alert alert-dismissable alert-warning">

                            <h4>Message!</h4>
                            <p><a href="javascript:void(0)" class="alert-link">Empty Customers</a>.</p>
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
