/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sliit.ead.assignment.ejb;
import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author Tharanga
 */
@Embeddable
public class CustomerOrderLine implements Serializable{
    
    private int lineNo;
    private String partNo;
    private double amount;

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
