/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.automovilesoracle.persistence;

/**
 *
 * @author josediaz
 */
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/*")
public class OpenSessionAndTransactionInView implements Filter {

    @Override
    public void destroy() {
        JPAUtil.closeEntityManagerFactory();

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //inicia a transação antes de processar o request
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            //processa a requisição
            chain.doFilter(request, response);

            //faz commit
            tx.commit();
        } catch (Exception e) { //ou em caso de erro faz o rollback
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
//			em.close();
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //não precisa fazer nada
    }
}
