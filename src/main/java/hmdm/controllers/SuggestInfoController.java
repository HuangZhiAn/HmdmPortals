package hmdm.controllers;

import hmdm.dto.*;
import hmdm.service.*;
import hmdm.activiti.SuggestActiviti;
import hmdm.util.Word2Html2;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by PC on 2017/8/9.
 */
@Controller
public class SuggestInfoController {
    @Autowired
    private SuggestInfoService suggestInfoService;
    @Autowired
    private SuggestActiviti suggestActiviti;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SuggestImagesService suggestImagesService;

    @Autowired
    IMailService mailService;

    @Autowired
    CustomerService customerService;
    /**
     * 用户反馈方法
     * 传入SuggestInfo suggestInfo
     * @param suggestInfo
     */
    @RequestMapping("/suggestInfo/suggest")
    public @ResponseBody String suggest(SuggestInfo suggestInfo){
        //根据functionid查询处理的人
        //模拟数据
        List<String> list = new ArrayList<String>();
        list.add("1001");
        list.add("1002");
        list.add("1003");
        list.add("1004");

        suggestInfo.setFunctionId(1L);
        //suggestInfo.s;
        suggestInfo.setSuggestInfo("hmdm使用删除有问题");
        //发布流程
        String instanceId = suggestActiviti.startProcess(list);
        //设置流程id
        suggestInfo.setProcessInstanceId(instanceId);
        int i = suggestInfoService.insertSelective(suggestInfo);
        if(i > 0){
            return "提交成功！";
        }else{
            return "提交失败";
        }
    }

    @RequestMapping("/suggestInfo/insert")
    @ResponseBody
    public String insertTest(){
        SuggestInfo suggestInfo = new SuggestInfo();
        suggestInfo.setCustomerId(1003L);
        suggestInfo.setProcessInstanceId("701");
        suggestInfo.setSuggestInfo("测试");
        suggestInfo.setFunctionId(1L);
        int i = suggestInfoService.insert(suggestInfo);
        System.out.println("insert result"+i);
        return i+"";
    }
    
    @RequestMapping("/suggestInfo/uploadTest")
    @ResponseBody
    public String uploadTest(@RequestParam("files") MultipartFile[] files, HttpServletRequest request){
        return files.length+"";
    }
    

    @RequestMapping("/suggestInfo/Submit")
    @ResponseBody
    public String suggestionSubmit(@RequestParam("files") MultipartFile[] files,HttpServletRequest request, HttpServletResponse response,SuggestInfo suggestInfo){
        //实例化流程
        List<String> names = employeeService.selectNameByFunctionId(suggestInfo.getFunctionId());
        String instanceId = suggestActiviti.startProcess(names);

        suggestInfo.setProcessInstanceId(instanceId);
        Customer customer =(Customer) request.getSession().getAttribute("customer");
        suggestInfo.setCustomerId(customer.getCustomerId());

        System.out.println("customer: "+customer);
        System.out.println("suggestInfo: "+suggestInfo);
        //将反馈信息存到数据库
        int i = suggestInfoService.insertSelective(suggestInfo);

        if(i<1){
            System.out.println("suggestInfo插入失败");
        }
        //获取反馈信息的ID
        Long suggestId = suggestInfoService.lastInsertId();
        System.out.println("反馈ID"+suggestId);
        System.out.println("获取文件");

        //获取文件名列表
        System.out.println("文件数量："+files.length);
        List<String> filePaths = new ArrayList<String>();
        for (int n = 0;n<files.length;n++) {
            System.out.println("获取文件成功");
            String fileName = files[n].getOriginalFilename();

            String appPath = request.getSession().getServletContext().getRealPath("");
            String imagePath =appPath+"/images/suggestInfo/"; //Thread.currentThread().getContextClassLoader().getResource("/").getPath();

            System.out.println("imagePath:"+imagePath+ " appPath:"+appPath);
            //图片路径
            filePaths.add("/images/suggestInfo/"+fileName);
            try {
                files[n].transferTo(new File(imagePath+fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //将图片信息存到数据库
        SuggestImages suggestImages = new SuggestImages();
        suggestImages.setSuggestId(suggestId);
        int i1 = suggestImagesService.insertSelective(suggestImages, filePaths);
        if(i1<1){
            System.out.println("suggestImages插入失败");
        }
        return "success";
    }

    @RequestMapping("/suggest/querySuggest")
    public String querySuggest(HttpServletRequest request,HttpServletResponse response,
                               @RequestParam(value = "processInstanceId") String processInstanceId,
                               @RequestParam(value = "createTime",required = false) String createTime,
                               @RequestParam(value = "processDefineName") String processDefineName,
                               @RequestParam(value = "customerId") String customerId,
                               @RequestParam(value = "id") String id) throws Exception {

        //获得functionId
        Employee employee =  (Employee)request.getSession().getAttribute("employee");
        List<SuggestInfo> list = null;
        if(employee==null||employee.getFunctionId()==null){
            response.sendRedirect("/backstage/jsp/login.jsp");
        }else{
            //查询对应的suggestInfo
            SuggestInfo suggestInfo = new SuggestInfo();
            suggestInfo.setFunctionId(employee.getFunctionId());
            suggestInfo.setProcessInstanceId(processInstanceId);
            list = suggestInfoService.selectSuggest(suggestInfo);
            if(list==null||list.size()==0){
                request.getRequestDispatcher("/suggest/noSuggestInfo").forward(request,response);
                return null;
            }
        }
        if(createTime!=null&&!createTime.equals("")){
            request.setAttribute("createTime",createTime);
        }
        if(processDefineName!=null&&!processDefineName.equals("")){
            request.setAttribute("processDefineName",processDefineName);
        }
        if(customerId!=null&&!customerId.equals("")){
            list.get(0).setCustomerId(Long.parseLong(customerId));
        }
        if(id!=null&&!id.equals("")){
            request.setAttribute("taskId",id);
        }
        request.setAttribute("suggestInfo",list.get(0));
        System.out.println(list.get(0));
        return "/backstage/jsp/flowDetail";
    }

    /**
     * 查询employee的任务
     */
    @RequestMapping("/suggest/queryTask")
    public @ResponseBody List<SuggestTask> queryTask(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //获取employee
        Employee employee =  (Employee)request.getSession().getAttribute("employee");
        List<SuggestTask> list = null;
        if(employee==null||employee.getFunctionId()==null){
            response.sendRedirect("/backstage/jsp/login.jsp");
        }else{
            list = suggestActiviti.queryGroupTask(employee.getName());
        }
        if(list!=null&&list.size()!=0){
            List<String> processInstanceIds = new ArrayList<>();
            for (SuggestTask task:list) {
                processInstanceIds.add(task.getProcessInstanceId());
            }
            List<SuggestInfo> suggestInfos = suggestInfoService.selectCustomerNameByProcessInstanceId(processInstanceIds);
            for (SuggestTask task:list) {
                for (SuggestInfo suggestInfo:suggestInfos) {
                    if(task.getProcessInstanceId().equals(suggestInfo.getProcessInstanceId())){
                        task.setOwner(suggestInfo.getCustomer().getName());
                        task.setCustomerId(suggestInfo.getCustomer().getCustomerId());
                        break;
                    }
                }
                task.setProcessDefineName(suggestActiviti.getDefinitionName(task.getProcessInstanceId()));
            }
        }
        return list;
    }

    /**
     *
     * 根据传入的任务Id完成任务
     * @param taskId
     * @return
     */
    @RequestMapping("/suggest/complete")
    public @ResponseBody String completeTask(HttpServletRequest request,HttpServletResponse response,String taskId,String result,String customerId) throws Exception {
        if(!EmployeeController.isLogin(request)){
            response.sendRedirect("/backstage/jsp/login.jsp");
        }
        Employee employee=(Employee) request.getSession().getAttribute("employee");
        suggestActiviti.completeTask(taskId,employee.getName(),result);
        List<byte[]> list = new ArrayList<byte[]>();
        List filename = new ArrayList<String>();
        CustomerExample example = new CustomerExample();
        example.createCriteria().andNameEqualTo("emailSender");
        List<Customer> customers = customerService.selectByExample(example);
        if(customers==null||customers.size()==0){
            throw new Exception("Can't find emailSender customer");
        }
        String user = customers.get(0).getEmail();
        String password = customers.get(0).getPassword();

        example.clear();
        example.createCriteria().andCustomerIdEqualTo(Long.parseLong(customerId));
        List<Customer> customers1 = customerService.selectByExample(example);
        if(customers1==null||customers1.size()==0){
            throw new Exception("Can't find customer");
        }
        String customerEmail = customers1.get(0).getEmail();
        try {
            mailService.initProperties("smtp","smtp.163.com","25",user,customerEmail);
            mailService.sendMultipleEmail("反馈处理结果",result,list,"ccc",filename,user,password);
        } catch (Exception e) {
            e.printStackTrace();
            return "Send result Email failure";
        }
        return "success";
    }

    @RequestMapping("/suggest/noSuggestInfo")
    @ResponseBody
    public String noSuggestInfo(){
        return "NoSuggestInfo";
    }

    /**
     * 查询employee历史的任务信息
     */
    @RequestMapping("/suggest/getHisTasksList")
    public @ResponseBody List<Comment> getHistoryTasksList(HttpServletRequest request){
        //获得functionId
        Employee employee = (Employee)request.getSession().getAttribute("employee");
        //模拟数据
        //Long functionId=1L;
        //通过functionId拿到所有的ProcessInstanceId集合
        SuggestInfoExample example = new SuggestInfoExample();
        SuggestInfoExample.Criteria c = example.createCriteria();
        c.andFunctionIdEqualTo(employee.getFunctionId());
        List<SuggestInfo> suggestInfos = suggestInfoService.selectByExample(example);
        List<String> proInstances = new ArrayList<>();
        for(SuggestInfo s:suggestInfos){
            proInstances.add(s.getProcessInstanceId());
        }
        //调用查询审批记录的方法
        List<Comment> commentsList = null;
        try {
            commentsList = suggestActiviti.findCommentsList(proInstances);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("流程历史"+commentsList);
        return commentsList;
    }

    /**
     * 通过流程实例id查询历史的任务审批信息
     */
    @RequestMapping("/suggest/getHisTasks")
    public @ResponseBody List<Comment> getHistoryTasks(String processInstanceId){
        //模拟数据
        //String processInstanceId="1001";
        //调用查询审批记录的方法
        return suggestActiviti.findComments(processInstanceId);
    }

}
