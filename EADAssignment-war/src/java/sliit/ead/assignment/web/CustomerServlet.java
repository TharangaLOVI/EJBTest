/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sliit.ead.assignment.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

/**
 *
 * @author Tharanga
 */
@WebServlet(name = "Customer", urlPatterns = {"/customer/*"})
public class CustomerServlet extends HttpServlet {

    @EJB
    private CustomerEntityFacade customerEntityFacade;

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
            //requested url path
            String requestPath = checkRequestPath(request);
            System.out.println("requestPath : " + requestPath);
            switch (request.getMethod()) {
                case "GET":

                    switch (requestPath) {
                        case "":
                            listCustomers(request, response);//list all customers
                            break;
                        case "form_customer":
                            formCustomer(request, response);//load form add new customer | load for edit new customer
                            break;
                        default:
                            throw new Exception("Unable to found Resources");//PageNo found Error View
                    }
                    break;

                case "POST":

                    switch (requestPath) {
                        case "form_customer":
                            insertORUpdateCustomer(request, response);//insert or update customer
                            break;
                        case "remove":
                            removeCustomer(request, response);//remove the customer
                            break;
                        default:
                            throw new Exception("Unable to found Resources");//PageNo found Error View
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
    private void listCustomers(HttpServletRequest request, HttpServletResponse response) {
        try {
            String meassage = "";
            if (request.getParameter("msg") != null) {
                meassage = request.getParameter("msg");
            }
            List<CustomerEntity> customers = customerEntityFacade.findAll();
            request.setAttribute("customers", customers);//setting customers,for accessing in JSP
            request.setAttribute("meassage", meassage);//setting messsage,for accessing in JSP

            getServletContext().getRequestDispatcher("/WEB-INF/customer_views/list_customers.jsp").forward(request, response);

        } catch (Exception e) {
            loadErrorView(request, response, "List Customers - " + e.getMessage());
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
    private void formCustomer(HttpServletRequest request, HttpServletResponse response) {
        try {

            String id = request.getParameter("id");//id query parameter(customer id) can access when update form load
            if (id != null) {

                if (!isInteger(id)) {
                    response.sendRedirect("");//query parameter id must be integer
                    return;
                }

                //get customer for given id
                CustomerEntity customerEntity = customerEntityFacade.find(Integer.parseInt(id));

                if (customerEntity == null) {
                    //if unable to fi=ound customer
                    throw new Exception("Unable to found customer");
                }

                request.setAttribute("isUpdate", 1);//setting update form indicator(mention this is a update form)
                request.setAttribute("customer", customerEntity);//setting customer details

            } else {
                request.setAttribute("isUpdate", 0);//setting update form indicator(mention this is not a update form)
            }

            getServletContext().getRequestDispatcher("/WEB-INF/customer_views/form_customer.jsp").forward(request, response);
        } catch (Exception e) {
            loadErrorView(request, response, "Form Customer - " + e.getMessage());
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
    private void insertORUpdateCustomer(HttpServletRequest request, HttpServletResponse response) {
        try {
            String successMessage;
            
            //access query parameters
            String customerId = request.getParameter("customerId");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String contactNo = request.getParameter("contactNo");
            String hdn_update_form = request.getParameter("hdn_update_form");

            CustomerEntity customerEntity = new CustomerEntity();
            String[] errorMessage = new String[4];
            boolean isValidate = true;

            //Start - Validation
            //Start - customerId
            if (customerId.equals("")) {

                errorMessage[0] = "Please fill customer id";
                isValidate = false;
            } else if (!isInteger(customerId)) {

                errorMessage[0] = "Customer id must be Integer";
                isValidate = false;
            } else if (customerEntityFacade.find(Integer.parseInt(customerId)) != null && Integer.parseInt(hdn_update_form) == 0) {

                errorMessage[0] = "Duplicate customer id";
                isValidate = false;
            } else {
                customerEntity.setCustomerId(Integer.parseInt(customerId));
            }
            //End - customerId
            
            //Start - name
            if (name.equals("")) {

                errorMessage[1] = "Please fill name";
                isValidate = false;

            } else {
                customerEntity.setName(name);
            }
            //End - name
            
            //Start - address
            if (address.equals("")) {

                errorMessage[2] = "Please fill address";
                isValidate = false;

            } else {
                customerEntity.setAddress(address);
            }
            //End - address
            
            //Start - contactNo
            if (contactNo.equals("")) {

                errorMessage[3] = "Please fill contactNo";
                isValidate = false;

            } else {
                customerEntity.setContactNo(contactNo);
            }
            //End - contactNo
            //End Validation

            //if validation fail,reload the form again
            if (!isValidate) {
                request.setAttribute("customer", customerEntity);
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("isUpdate", hdn_update_form);
                getServletContext().getRequestDispatcher("/WEB-INF/customer_views/form_customer.jsp").forward(request, response);
                return;
            }

            
            if (Integer.parseInt(hdn_update_form) == 1) {
                //request is update
                customerEntityFacade.edit(customerEntity);
                successMessage = "Successfully update customer details";
            } else {
                //request is add new customer
                customerEntityFacade.create(customerEntity);
                successMessage = "Successfully insert customer details";
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
    private void removeCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            String id = request.getParameter("id");
            if (id == null) {
                throw new Exception("Unable to found input parameter");
            }

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setCustomerId(Integer.parseInt(id));
            customerEntityFacade.remove(customerEntity);
            response.sendRedirect("?msg=Successfully remove the customer");

        } catch (Exception e) {
            loadErrorView(request, response, "Remove Customer - " + e.getMessage());
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
            request.setAttribute("errorMessage", errorMessage);//setting error message
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
    private boolean isInteger(String str) {
        try {
            int d = Integer.parseInt(str);
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
