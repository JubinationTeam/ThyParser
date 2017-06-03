package com.jubination.io.parser.controller;



import com.jubination.io.parser.init.Init;
import com.jubination.io.parser.model.pojo.report.Report;
import com.jubination.io.parser.service.ReportService;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class RedirectController {
   @Autowired
    ReportService service;
    public RedirectController() {
    }
    
    
     @RequestMapping(value="/",method=RequestMethod.GET,headers="Accept=*/*")
    public String  getReportView(HttpServletRequest request) {
        Report r =new Report();
        r.setReportId("12");
            service.buildReport(r);
            return "done";
     }
   
    @PostConstruct
    public void init() {
        Init.main(null);
    }
    
}
