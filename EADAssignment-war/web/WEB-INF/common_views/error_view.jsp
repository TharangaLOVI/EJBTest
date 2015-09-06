<%-- 
    Document   : error_view
    Created on : Aug 17, 2015, 8:57:27 PM
    Author     : Tharanga
--%>
<%@include file="../includes/header.jsp"%>

<!-- 1 -->
<div class="col-lg-9 col-md-9">

    <!-- 1-1 -->
    <div class="container-fluid">

        <div class="row">
            <div class="col-lg-12 col-md-12">
                <div class="alert alert-dismissable alert-danger">
                    
                    <h2>Internal Error!</h2>
                    <p>${errorMessage}</p>

                </div>
                
            </div>
        </div>
    </div>
</div>

<%@include file="../includes/footer.jsp"%>
