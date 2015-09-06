/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sliit.ead.assignment.ejb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Used for Update CustomerOrder entity - CustomerOrderLine List
 * @author Tharanga
 */
@MessageDriven(mappedName = "jms/COLOMessage", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class CustomerOrderLineOperationMessage implements MessageListener {
    
    @Resource
    private MessageDrivenContext mdc;

    @PersistenceContext(unitName = "EADAssignment-ejbPU")
    private EntityManager em;
    
    public CustomerOrderLineOperationMessage() {
    }
    
    @Override
    public void onMessage(Message message) {
        
        System.out.println("JMS : Fire");

        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                //get CustomerOrderEntity object for update
                CustomerOrderEntity entity = (CustomerOrderEntity) objectMessage.getObject();
                    
                updateCustomerOrderLine(entity);
                
                System.out.println("JMS : Success");

            } catch (JMSException e) {
                System.out.println("JMS : " + e.getMessage());
                mdc.setRollbackOnly();
            }
        }
        
    }
    
    /**
     * Update Entity
     *
     * @param entity
     */
    private void updateCustomerOrderLine(Object entity) {
        em.merge(entity);
    }
    
}
