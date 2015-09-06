/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sliit.ead.assignment.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tharanga
 */
@Stateless
public class CustomerEntityFacade extends AbstractFacade<CustomerEntity> {
    @PersistenceContext(unitName = "EADAssignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerEntityFacade() {
        super(CustomerEntity.class);
    }
    
}
