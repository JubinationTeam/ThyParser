package com.jubination.io.parser.init;





import com.jubination.io.parser.model.dao.ReportDAOImpl;


/**
 *
 * @author Welcome
 */
public class Init {
   
    public static void main(String[] args) {
       

        ReportDAOImpl reportDao =new ReportDAOImpl();
        reportDao.setSessionFactory(HibernateUtil.getSessionFactory());
          

        System.err.println("Constructed:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        
     
        
        
    }
}
