/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sliit.ead.assignment.web;

import com.sun.jmx.snmp.BerDecoder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sliit.ead.assignment.ejb.CustomerEntity;
import sliit.ead.assignment.ejb.CustomerEntityFacade;
import sliit.ead.assignment.ejb.CustomerOrderEntity;
import sliit.ead.assignment.ejb.CustomerOrderEntityFacade;
import sliit.ead.assignment.ejb.CustomerOrderLine;

/**
 *
 * @author Tharanga
 */
@WebServlet(name = "CustomerOrderLineServlet", urlPatterns = {"/customer_order_line/*"})
public class CustomerOrderLineServlet extends HttpServlet {

    @EJB
    private CustomerOrderEntityFacade customerOrderEntityFacade;
    @EJB
    private CustomerEntityFacade customerEntityFacade;

    @Resource(mappedName = "jms/COLOMessageFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "jms/COLOMessage")
    private Queue queue;

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
                            viewCustomerOrder(request, response);//view customer orders details

                            break;
                        case "form_order_line":
                            formCustomerOrderLine(request, response);//load form add new customer order line| load for edit new customer order line
                            break;

                        default:
                            throw new Exception("Unable to find Resources");//PageNo found Error View
                    }
                    break;
                case "POST":
                    switch (requestPath) {
                        case "form_order_line":
                            insertOrUpdateCustomerOrderLine(request, response);//insert or update customer order line
                            break;
                        case "remove":
                            removeCustomerOrderLine(request, response);//remove customer order line
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
            //because of it does contain only 'customer_order_line/'.
            return "";
        }

    }

    /**
     * Load CustomerOrder details with OrderLines
     * @param request
     * @param response 
     */
    private void viewCustomerOrder(HttpServletRequest request, HttpServletResponse response) {
        try {

            String id = request.getParameter("id");//query parameter - cstomerOrderId

            if (id == null) {
                throw new Exception("Parameter null");
            }

            CustomerOrderEntity customerOrderEntity = customerOrderEntityFacade.find(id);//get customerOrder for requested orderId

            if (customerOrderEntity == null) {
                throw new Exception("Unable to found CustomerOrder");
            }

            request.setAttribute("customerOrder", customerOrderEntity);//setting customerOrder

            getServletContext().getRequestDispatcher("/WEB-INF/customer_order_line_views/list_customer_order_lines.jsp").forward(request, response);

        } catch (Exception ex) {
            loadErrorView(request, response, ex.getMessage());
        }
    }

    /**
     * Load Form CustomerOrderLine Insert/Update view
     * @param request
     * @param response 
     */
    private void formCustomerOrderLine(HttpServletRequest request, HttpServletResponse response) {
        try {
            //request parameters
            String orderNo = request.getParameter("order_no");
            String lineNo = request.getParameter("line_no");

            if (orderNo == null) {
                throw new Exception("order no parameter empty");
            }

            CustomerOrderEntity customerOrderEntity = customerOrderEntityFacade.find(orderNo);

            if (customerOrderEntity == null) {
                throw new Exception("Unable to found CustomerOrder");
            }

            if (lineNo != null) {//update order line

                for (CustomerOrderLine customerOrderLine : customerOrderEntity.getCustomerOrderLines()) {

                    if (Integer.parseInt(lineNo) == customerOrderLine.getLineNo()) {
                        request.setAttribute("customerOrderLine", customerOrderLine);//setting customerOrderLine object dor update
                        request.setAttribute("customerOrder", customerOrderEntity);//setting customerOrder object
                        request.setAttribute("isUpdate", 1);//setting update form indicator(mention this is a update form)

                        getServletContext().getRequestDispatcher("/WEB-INF/customer_order_line_views/form_customer_order_line.jsp").forward(request, response);
                        return;
                    }

                }
                throw new Exception("Unable to found Line No");

            } else {
                request.setAttribute("customerOrder", customerOrderEntity);//setting customerOrder object
                request.setAttribute("isUpdate", 0);//setting update form indicator(mention this is not a update form)
                getServletContext().getRequestDispatcher("/WEB-INF/customer_order_line_views/form_customer_order_line.jsp").forward(request, response);
            }

        } catch (Exception ex) {
            loadErrorView(request, response, ex.getMessage());
        }
    }

    /**
     * Add new CustomerOrderLine object into the CustomerOrder's order line list |
     * Update existing CustomerOrderLine object of CustomerOrder's order line list
     * @param request
     * @param response 
     */
    private void insertOrUpdateCustomerOrderLine(HttpServletRequest request, HttpServletResponse response) {

        String successMessage;

        //request parameters
        String orderNo = request.getParameter("orderNo");
        String lineNo = request.getParameter("lineNo");
        String partNo = request.getParameter("partNo");
        String amount = request.getParameter("amount");
        String hdn_update_form = request.getParameter("hdn_update_form");

        try {

            CustomerOrderEntity currentCustomerOrderEntity = customerOrderEntityFacade.find(orderNo);

            if (currentCustomerOrderEntity == null) {
                throw new Exception("Unable to found CustomerOrder");
            }

            CustomerOrderLine customerOrderLine = new CustomerOrderLine();
            String[] errorMessage = new String[3];
            boolean isValidate = true;

            //Validation
            //Start - lineNo
            if (lineNo.equals("")) {
                errorMessage[0] = "Please fill line no";
                isValidate = false;
            } else if (!isInteger(lineNo)) {
                errorMessage[0] = "Line No must be Integer";
                isValidate = false;
            } else if (Integer.parseInt(lineNo) <= 0) {
                errorMessage[0] = "Line No must be greather than 0 ";
                isValidate = false;
            } else if (isLineNoExists(Integer.parseInt(lineNo), currentCustomerOrderEntity) && Integer.parseInt(hdn_update_form) == 0) {
                errorMessage[0] = "Line No already exists for this order";
                isValidate = false;
            } else {
                customerOrderLine.setLineNo(Integer.parseInt(lineNo));
            }
            //End - lineNo

            //Start - partNo
            if (partNo.equals("")) {
                errorMessage[1] = "Please fill part no";
                isValidate = false;
            } else {
                customerOrderLine.setPartNo(partNo);
            }
            //End - partNo

            //Start - amount
            if (amount.equals("")) {
                errorMessage[2] = "Please fill amount";
                isValidate = false;
            } else if (!isDouble(amount)) {
                errorMessage[2] = "Amount must be Integer";
                isValidate = false;
            } else if (Double.parseDouble(amount) <= 0.0) {
                errorMessage[2] = "Amount must be greather than 0.00 ";
                isValidate = false;
            } else {
                customerOrderLine.setAmount(Double.parseDouble(amount));
            }
            //End - amount
            //End Validation

            //if validation fail,reload the form again
            if (!isValidate) {
                request.setAttribute("customerOrder", currentCustomerOrderEntity);
                request.setAttribute("customerOrderLine", customerOrderLine);
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("isUpdate", hdn_update_form);
                getServletContext().getRequestDispatcher("/WEB-INF/customer_order_line_views/form_customer_order_line.jsp").forward(request, response);
                return;
            }
            List<CustomerOrderLine> customerOrderLines = currentCustomerOrderEntity.getCustomerOrderLines();
            if (Integer.parseInt(hdn_update_form) == 1) {//update order line
                //request is update - customer order line
                int i = 0;
                for(CustomerOrderLine tmpOrderLine : customerOrderLines){
                    
                    if(Integer.parseInt(lineNo) == tmpOrderLine.getLineNo()){
                        customerOrderLines.remove(i);
                        customerOrderLines.add(i, customerOrderLine);
                        break;
                    }
                    i++;
                }
                successMessage = "Successfully update order line details";
                
                
            } else {
                //add customerOrderLine into customerOrderLines list
                customerOrderLines.add(customerOrderLine);

                successMessage = "Successfully insert order line";
            }
            //set OrderLines
            CustomerOrderEntity updatedCustomerOrderEntity = new CustomerOrderEntity(customerOrderLines);

            //set current CustomerOrder details into new CustomerOrder obj
            updatedCustomerOrderEntity.setOrderNo(currentCustomerOrderEntity.getOrderNo());
            updatedCustomerOrderEntity.setAmount(calculateTotalOrderAmount(customerOrderLines));
            updatedCustomerOrderEntity.setComment(currentCustomerOrderEntity.getComment());
            updatedCustomerOrderEntity.setDueDate(currentCustomerOrderEntity.getDueDate());
            updatedCustomerOrderEntity.setCustomer(currentCustomerOrderEntity.getCustomer());

            //jmsUpdateCustomerOrderLine(updatedCustomerOrderEntity);
            customerOrderEntityFacade.edit(updatedCustomerOrderEntity);

            response.sendRedirect("?id=" + orderNo + "&msg=" + successMessage);
        } catch (Exception e) {
            loadErrorView(request, response, e.getMessage());
        }

    }

    /**
     * Remove CustomerOrderLine object from CustomerOrder's order line list
     * @param request
     * @param response 
     */
    private void removeCustomerOrderLine(HttpServletRequest request, HttpServletResponse response) {
        String orderNo = request.getParameter("orderNo");
        String lineNo = request.getParameter("lineNo");

        try {
            CustomerOrderEntity currentCustomerOrderEntity = customerOrderEntityFacade.find(orderNo);

            if (currentCustomerOrderEntity == null) {
                throw new Exception("Unable to found CustomerOrder");
            }
            
            int i = 0;
            List<CustomerOrderLine> newOrderLines = currentCustomerOrderEntity.getCustomerOrderLines();
            for (CustomerOrderLine customerOrderLine : newOrderLines) {

                if (Integer.parseInt(lineNo) == customerOrderLine.getLineNo()) {
                    newOrderLines.remove(i);
                    break;
                }
                i++;
            }

            CustomerOrderEntity updatedCustomerOrderEntity = new CustomerOrderEntity(newOrderLines);

            //set current CustomerOrder details into new CustomerOrder obj
            updatedCustomerOrderEntity.setOrderNo(currentCustomerOrderEntity.getOrderNo());
            updatedCustomerOrderEntity.setAmount(calculateTotalOrderAmount(newOrderLines));
            updatedCustomerOrderEntity.setComment(currentCustomerOrderEntity.getComment());
            updatedCustomerOrderEntity.setDueDate(currentCustomerOrderEntity.getDueDate());
            updatedCustomerOrderEntity.setCustomer(currentCustomerOrderEntity.getCustomer());

            customerOrderEntityFacade.edit(updatedCustomerOrderEntity);
            
            response.sendRedirect("?id=" + orderNo + "&msg=Successfully remove order line");

        } catch (Exception e) {
            loadErrorView(request, response, e.getMessage());
        }
    }

    /**
     * Calculate total sum of amount for CustomerOrderLine list 
     * @param customerOrderLines
     * @return 
     */
    private double calculateTotalOrderAmount(List<CustomerOrderLine> customerOrderLines) {
        double totalOrderAmount = 0.0;

        for (CustomerOrderLine orderLine : customerOrderLines) {
            totalOrderAmount += orderLine.getAmount();
        }

        return totalOrderAmount;
    }

    /**
     * JMS message prepare for update CustomerOrderLine object put into CustomerOrder Entity
     * @param customerOrderEntity
     * @throws JMSException 
     */
    private void jmsUpdateCustomerOrderLine(CustomerOrderEntity customerOrderEntity) throws JMSException {

        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer = session.createProducer(queue);

        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setObject(customerOrderEntity);

        messageProducer.send(objectMessage);
        messageProducer.close();
        connection.close();

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

    private boolean isInteger(String str) {
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isDouble(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * If lineNo exists for the given order return true.otherwise return false
     *
     * @param lineNo
     * @param currentCustomerOrderEntity
     * @return
     */
    private boolean isLineNoExists(int lineNo, CustomerOrderEntity currentCustomerOrderEntity) {

        for (CustomerOrderLine orderLine : currentCustomerOrderEntity.getCustomerOrderLines()) {
            if (lineNo == orderLine.getLineNo()) {
                return true;
            }
        }

        return false;
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
