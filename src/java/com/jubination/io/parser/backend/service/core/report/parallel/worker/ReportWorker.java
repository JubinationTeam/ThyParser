/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jubination.io.parser.backend.service.core.report.parallel.worker;

import com.jubination.io.parser.backend.pojo.thyrocare.report.ReportMessage;
import com.jubination.io.parser.backend.service.io.parser.thyrocare.report.parallel.worker.PDFBackendProcess;
import com.jubination.io.parser.backend.service.io.parser.thyrocare.report.parallel.worker.XMLBackendProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author MumbaiZone
 */
@Component
@Scope("prototype")
public class ReportWorker implements Runnable{
       

      
   private  boolean status;
   
//   @Autowired
//   ReportOperator operator;
//   
//   @Autowired
//   PDFBackendProcess pdfOperator;
//   
//   @Autowired
//   XMLBackendProcess xmlOperator;
//    
    
    @Override
    public void run() {
//                    try{
                           //runs
//                           ReportMessage msg =operator.getReportMessage().poll();
//                            boolean xml=false;
//                            boolean pdf=false;
//                           try{
//                                xml=xmlOperator.parseAndBuildXML(msg);
//                           }
//                           catch(Error e){
//                               System.out.println(e);
//                           }
//                           System.out.println("now"+xml);
//                           if(!xml){
//                                        try{
//                                                    pdf=pdfOperator.parseAndBuildPDF(msg);
//                                        }
//                                       catch(Exception e){
//                                           System.out.println(e);
//                                       }
//                                        if(pdf){
//                                            System.out.println("PDF parsing Done");
//                                        }
//                                        else{
//                                            System.out.println("Parsing failed");
//                                        }
//                                   }
//                           else{
//                               System.out.println("XML parsing Done");
//                           }
//                               
//                           status=!status;
//                    }
//                    catch(Exception e){
//                            e.printStackTrace();
//                    }
          }
           
           
          
     
    
   
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

   
}
