package net.neurolines.genai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.genai.IvDepartment;
import net.neurolines.genai.model.genai.IvWorkplace;
import net.neurolines.genai.service.AuthService;
import net.neurolines.genai.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dm")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AuthService authService;

    @PostMapping("/list")
    public ResponseEntity<Object> list(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody Map<String, Object> param)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        param.put("userCompanyCode", userCompanyCode);

        return ResponseEntity.ok(departmentService.selectPageDepartmentList(param));
    }

    @PostMapping("/reg")
    public ResponseEntity<Object> insertWorkplace(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @RequestBody IvDepartment department)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        department.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(departmentService.insertDepartment(department));
    }

    @PostMapping("/{hqCode}/{wpCode}")
    public ResponseEntity<Object> getDepartmentList(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @PathVariable String hqCode,
                                                 @PathVariable String wpCode
    )
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        //  String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        Map<String, Object> param = new HashMap<>();

        param.put("userCompanyCode", userCompanyCode);
        param.put("wpCode", wpCode);
        param.put("hqCode", hqCode);

        return ResponseEntity.ok(departmentService.selectDepartmentList(param));
    }

    @PostMapping("/{hqCode}/{wpCode}/{dmCode}")
    public ResponseEntity<Object> getDepartment(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @PathVariable String hqCode,
                                                 @PathVariable String wpCode,
                                                 @PathVariable String dmCode
    )
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
      //  String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        Map<String, Object> param = new HashMap<>();

        param.put("userCompanyCode", userCompanyCode);
        param.put("wpCode", wpCode);
        param.put("hqCode", hqCode);
        param.put("dmCode", dmCode);

        return ResponseEntity.ok(departmentService.getDepartment(param));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateHeadquarter(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody IvDepartment department)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        department.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(departmentService.updateDepartment(department));
    }


}
