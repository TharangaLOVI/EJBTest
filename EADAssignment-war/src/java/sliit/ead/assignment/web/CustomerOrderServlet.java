/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sliit.ead.assignment.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sliit.ead.assignment.ejb.CustomerEntity;
import sliit.ead.assignment.ejb.CustomerEntityFacade;
import sliit.ead.assignment.ejb.CustomerOrderEntity;
import sliit.ead.assignment.ejb.CustomerOrderEntityFacade;

/**
 *
 * @author Tharanga
 */
@WebServlet(name = "CustomerOrder", urlPatterns = {"/customer_order/*"})
public class CustomerOrderServlet extends HttpServlet {

    @EJB
    private CustomerEntityFacade customerEntityFacade;
    @EJB
    private CustomerOrderEntityFacade customerOrderEntityFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String requestPath = checkRequestPath(request);
            System.out.println("Req : " + requestPath);

            switch (request.getMethod()) {
                case "GET":

                    switch (requestPath) {
                        case "":
                            listCustomerOrder(request, response);//list all the customer orders
                            break;
                        case "form_order":
                            formCustomerOrder(request, response);//load form add new customer order | load for edit new customer order
                            break;
                        default:
                            throw new Exception("Unable to find Resources");//PageNo found Error View
                    }
                    break;
                case "POST":

                    switch (requestPath) {
                        case "form_order":
                            insertORUpdateCustomerOrder(request, response);//insert or update customer order
                            break;
                        case "remove":
                            removeCustomerOrder(request, response);//remove the customer order
                            break;
                        default:
                            throw new Exception("Unable to find Resources");//PageNo found Error View
                    }
                    break;

            }

        } catch (Exception e) {
            loadErrorView(request, response, e.getMessage());
        }
    }

    /**
     * Requested URL mapping
     *
     * @param request
     * @return
     */
    private String checkRequestPath(HttpServletRequest request) {

        try {
            String pathInfo = request.getPathInfo(); // /customer/path
            System.out.println("pathInfo : " + pathInfo); // print => /path
            if (pathInfo.length() >= 2) {
                return pathInfo.split("/")[1];
            } else {
                return "";
            }
        } catch (Exception e) {
            //if path request if 'customer' request.getPathInfo() can't access path details.
            //because of it does contain only 'customer/'.
            return "";
        }

    }

    /**
     * List all the customers data
     *
     * @param request
     * @param response
     */
    private void listCustomerOrder(HttpServletRequest request, HttpServletResponse response) {
        try {
            String meassage = "";
            if (request.getParameter("msg") != null) {
                meassage = request.getParameter("msg");
            }
            List<CustomerOrderEntity> customerOrders = customerOrderEntityFacade.findAll();
            request.setAttribute("customerOrders", customerOrders);//setting customerOrders,for accessing in JSP
            request.setAttribute("meassage", meassage);//setting messsage,for accessing in JSP

            getServletContext().getRequestDispatcher("/WEB-INF/customer_order_views/list_customer_orders.jsp").forward(request, response);

        } catch (Exception e) {
            loadErrorView(request, response, "List Customers Orders- " + e.getMessage());
        }
    }

    /**
     * View form for Insert CustomerServlet data or Update CustomerServlet data
     * When update operation,id parameter come as Query parameter. user id
     * parameter load customer data
     *
     * @param request
     * @param response
     */
    private void formCustomerOrder(HttpServletRequest request, HttpServletResponse response) {
        try {

            String id = request.getParameter("id");//id query parameter(customer order id) can access when update form load
            if (id != null) {
                CustomerOrderEntity customerOrderEntity = customerOrderEntityFacade.find(id);

                if (customerOrderEntity == null) {
                    response.sendRedirect("");
                    return;
                }

                request.setAttribute("isUpdate", 1);//setting update form indicator(mention this is a update form)
                request.setAttribute("customerOrder", customerOrderEntity);//setting customerOrder details
            } else {
                request.setAttribute("isUpdate", 0);//setting update form indicator(mention this is not a update form)

                CustomerOrderEntity newCustomerOrderEntity = new CustomerOrderEntity();
                newCustomerOrderEntity.setAmount(0.00);//seting default customerOrder amount
                request.setAttribute("customerOrder", newCustomerOrderEntity);

            }
            request.setAttribute("customers", customerEntityFacade.findAll());//seting customers

            getServletContext().getRequestDispatcher("/WEB-INF/customer_order_views/form_customer_order.jsp").forward(request, response);
        } catch (Exception e) {
            loadErrorView(request, response, "Form Customer Order - " + e.getMessage());
        }
    }

    /**
     * Insert new CustomerServlet or Update CustomerServlet Details by using
     * hdn_update_form parameter can decide incoming request is for Insert Data
     * Operation or Update Operation
     *
     * @param request
     * @param response
     */
    private void insertORUpdateCustomerOrder(HttpServletRequest request, HttpServletResponse response) {
        try {
            String successMessage;
            //access query parameters
            String orderNo = request.getParameter("orderNo");
            String dueDate = request.getParameter("dueDate");
            String comment = request.getParameter("comment");
            String amount = request.getParameter("amount");
            String customer = request.getParameter("customer");
            String hdn_update_form = request.getParameter("hdn_update_form");

            CustomerOrderEntity customerOrderEntity = new CustomerOrderEntity();
            String[] errorMessage = new String[5];
            boolean isValidate = true;

            //Start - Validation
            //Start - orderNo
            if (orderNo.equals("")) {

                errorMessage[0] = "Please fill orderNo";
                isValidate = false;
            } else if (customerOrderEntityFacade.find(orderNo) != null && Integer.parseInt(hdn_update_form) == 0) {

                errorMessage[0] = "Duplicate orderNo";
                isValidate = false;
            } else {
                customerOrderEntity.setOrderNo(orderNo);
            }
            //End - orderNo
            
            //Start - dueDate
            if (dueDate.equals("")) {

                errorMessage[1] = "Please fill due date";
                isValidate = false;

            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    java.util.Date date = sdf.parse(dueDate);
                    java.sql.Date sqlDate = new Date(date.getTime());
                    customerOrderEntity.setDueDate(sqlDate);
                } catch (ParseException ex) {
                    errorMessage[1] = "Invalide due date format";
                    isValidate = false;
                }
            }
            //End - dueDate
            
            //Start - comment
            if (comment.equals("")) {

                errorMessage[2] = "Please fill comment";
                isValidate = false;

            } else {
                customerOrderEntity.setComment(comment);
            }
            //End - comment
            
            //Start - customer
            if (customer == null) {
                errorMessage[4] = "Please fill customer";
                isValidate = false;
            } else {
                CustomerEntity customerEntity = new CustomerEntity();
                customerEntity.setCustomerId(Integer.parseInt(customer));
                customerOrderEntity.setCustomer(customerEntity);
            }
            //End - customer
            //End Validation

            //if validation fail,reload the form again
            if (!isValidate) {
                request.setAttribute("customerOrder", customerOrderEntity);
                request.setAttribute("customers", customerEntityFacade.findAll());
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("isUpdate", hdn_update_form);
                getServletContext().getRequestDispatcher("/WEB-INF/customer_order_views/form_customer_order.jsp").forward(request, response);
                return;
            }

            if (Integer.parseInt(hdn_update_form) == 1) {
                //request is update
                customerOrderEntityFacade.edit(customerOrderEntity);
                successMessage = "Successfully update customer order details";
            } else {
                //request is add new customer
                customerOrderEntityFacade.create(customerOrderEntity);
                successMessage = "Successfully insert customer order details";
            }
            response.sendRedirect("?msg=" + successMessage);

        } catch (Exception e) {
            loadErrorView(request, response, e.getMessage());
        }

    }

    /**
     * Remove the customer for id
     *
     * @param request
     * @param response
     * @throws Exception
     */
    private void removeCustomerOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String id = request.getParameter("id");
            if (id == null) {
                throw new Exception("Unable to find input parameter id");
            }

            CustomerOrderEntity customerOrderEntity = new CustomerOrderEntity();
            customerOrderEntity.setOrderNo(id);
            customerOrderEntityFacade.remove(customerOrderEntity);
            response.sendRedirect("?msg=Successfully remove customer order");

        } catch (Exception e) {
            loadErrorView(request, response, "Delete Customer Order - " + e.getMessage());
        }
    }

    /**
     * Error Page
     *
     * @param request
     * @param response
     * @param errorMessage
     */
    private void loadErrorView(HttpServletRequest request, HttpServletResponse response, String errorMessage) {

        try {
            request.setAttribute("errorMessage", errorMessage);
            getServletContext().getRequestDispatcher("/WEB-INF/common_views/error_view.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(CustomerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * If input string is a Integer -> return true,otherwise return false
     * @param str
     * @return 
     */
    private boolean isDouble(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
