/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sliit.ead.assignment.ejb;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Tharanga
 */
@Entity
public class CustomerOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String orderNo;
    private Date dueDate;
    private String comment;
    private double amount;
    
    @OneToOne
    private CustomerEntity customer;
    
    @ElementCollection
    private List<CustomerOrderLine> customerOrderLines;

    public CustomerOrderEntity(){
        
    }
    
    public CustomerOrderEntity(List<CustomerOrderLine> customerOrderLines){
        this.customerOrderLines = customerOrderLines;
    }
    
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public List<CustomerOrderLine> getCustomerOrderLines() {
        return customerOrderLines;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderNo != null ? orderNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerOrderEntity)) {
            return false;
        }
        CustomerOrderEntity other = (CustomerOrderEntity) object;
        if ((this.orderNo == null && other.orderNo != null) || (this.orderNo != null && !this.orderNo.equals(other.orderNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sliit.ead.assignment.ejb.CustomerOrderEntity[ id=" + orderNo + " ]";
    }
    
}
