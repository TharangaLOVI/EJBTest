<%-- 
    Document   : header
    Created on : Aug 8, 2015, 1:42:32 AM
    Author     : Tharanga
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EAD Assignment</title>

        <!-- bootstrap dependencies -->
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet"
              type="text/css" />

        <link href="${pageContext.request.contextPath}/css/bootstrap-material-datetimepicker.css"
              rel="stylesheet" type="text/css" />
        <link href="${pageContext.request.contextPath}/css/material-fullpalette.min.css" rel="stylesheet"
              type="text/css" />
        <link href="${pageContext.request.contextPath}/css/ripples.min.css" rel="stylesheet"
              type="text/css" />
        <link href="${pageContext.request.contextPath}/css/roboto.min.css" rel="stylesheet"
              type="text/css" /> 
        
        <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
        
        <script>
            
            function showNotification(){
                $('#div_message').slideDown("slow").delay(6000).slideUp("slow");
            }
            
        </script>

        <!-- end bootstrap dependencies -->
    </head>
    <body style="height: 3000px">
        <!-- navigation bar -->
        <div  class="navbar navbar-material-light-blue-500">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse"
                        data-target=".navbar-material-light-blue-collapse">
                    <span class="icon-bar"></span> <span class="icon-bar"></span> <span
                        class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="javascript:void(0)">EAD Assignment</a>
            </div>
            <div
                class="navbar-collapse collapse navbar-material-light-blue-collapse">

                <ul class="nav navbar-nav">


                </ul>


                <ul class="nav navbar-nav navbar-right">

                </ul>



            </div>
        </div>
        <!-- end navigation bar -->

        <div class="container">
            <div class="row">

                <!-- Left Menu -->
                <div class="col-lg-3 col-md-3">
                    <div class="panel panel-material-light-blue-500">
                        <div class="panel-heading">
                            <h3 class="panel-title">Menu</h3>
                        </div>
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li><a class="btn-material-light-blue-500" href="${pageContext.request.contextPath}/customer">Customer</a></li>
                                <li><a class="btn-material-light-blue-500" href="${pageContext.request.contextPath}/customer_order">Customer Order</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- Left Menu End-->
