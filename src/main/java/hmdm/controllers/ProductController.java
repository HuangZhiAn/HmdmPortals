package hmdm.controllers;

import hmdm.dto.Customer;
import hmdm.dto.CustomerExample;
import hmdm.dto.Product;
import hmdm.dto.ProductExample;
import hmdm.service.CustomerService;
import hmdm.service.IMailService;
import hmdm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * Created by JoeHuang on 2017/8/1.
 */
@RestController("productController")
public class ProductController{

    @Autowired
    private IMailService mailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Value("#{config['latest.version']}")
    public String latestVersion;

    @Value("#{config['product.name']}")
    public String defaultProductName;

    @Value("#{config['domain.name']}")
    public String domainName;

    @Value("#{config[port]}")
    public String port;

    @RequestMapping("/sendDownloadEmail")
    public String sendDownloadEmail(@RequestParam(value = "productName",required = false) String productName,
                                    @RequestParam(value = "version",required = false)String version,
                                    HttpServletRequest request) throws Exception {
        if(productName==null||productName.equals("")){
            productName=defaultProductName;
        }
        if(version==null||version.equals("")){
            version=latestVersion;
        }
        HttpSession session = request.getSession();
        Customer customer =(Customer) session.getAttribute("customer");
        CustomerExample example = new CustomerExample();
        example.createCriteria().andNameEqualTo("emailSender");
        List<Customer> customers = customerService.selectByExample(example);
        if(customers==null||customers.size()==0){
            throw new Exception("Can't find emailSender customer");
        }
        String user = customers.get(0).getEmail();
        String password = customers.get(0).getPassword();
        mailService.initProperties("smtp","smtp.163.com","25",
                user,customer.getEmail());

        String downloadToken = new Date().getTime()+"";
        request.getServletContext().setAttribute(downloadToken,downloadToken);
        List<byte[]> list = new ArrayList<byte[]>();
        List filename = new ArrayList<String>();
        String downloadUrl = "http://"+domainName+":"+port+"/download?productName="+productName+"&version="+version+"&customerId="+customer.getCustomerId()+"&downloadToken="+downloadToken;
        String text = "<span style='font-size: 15px'>点击<a href='"+downloadUrl +
                "'>这里</a>或复制下面连接到浏览器打开<br/>"+downloadUrl+"</span>";
        try {
            mailService.sendMultipleEmail("产品下载",text,list,"ccc",filename,user,password);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @RequestMapping("/download")
    public String downloadFile(@RequestParam("productName") String productName,
                               @RequestParam(value = "version")String version,
                               @RequestParam("downloadToken") String downloadToken,
                               @RequestParam("customerId") String customerId,
                               HttpServletRequest request, HttpServletResponse response) {
        String downloadToken1 =(String) request.getServletContext().getAttribute(downloadToken);
        if (productName != null&&downloadToken1!=null&&!downloadToken1.equals("")) {
            String realPath = request.getServletContext().getRealPath("WEB-INF/products/");
            File file = null;
            String downloadFile = null;
            if(version!=null){
                downloadFile = productName+version;
                file = new File(realPath, downloadFile.trim()+".rar");
            }
            long length = file.length();
            if (file.exists()) {
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + downloadFile+".rar");// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    close(bis);
                    close(fis);
                }
            }
            request.getServletContext().setAttribute(downloadToken,null);
        }else{
            return "Link Failure";
        }
        return "Download success";
    }

    private void close(InputStream in){
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                in=null;
            }
        }
    }


    /**
     * 查询产品信息
     * @return
     */
    @RequestMapping(value = "/product/query",method = RequestMethod.GET)
    public @ResponseBody
    List<Product> qeury(){
        List<Product> list = productService.selectByExample(null);
        for (Product p : list) {
            p.setState(null);
        }
        return list;
    }

    @RequestMapping("/test")
    public void test(){
        System.out.println("Test方法");
    }

}
