package net.neurolines.genai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.genai.IvHeadQuarters;
import net.neurolines.genai.model.genai.IvWorkplace;
import net.neurolines.genai.service.AuthService;
import net.neurolines.genai.service.WorkplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wp")
public class WorkplaceController {

    @Autowired
    WorkplaceService workplaceService;

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


        return ResponseEntity.ok(workplaceService.selectPageWorkplaceList(param));
    }

    @PostMapping("/reg")
    public ResponseEntity<Object> insertWorkplace(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @RequestBody IvWorkplace workplace)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        workplace.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(workplaceService.insertWorkplace(workplace));
    }

    @PostMapping("/{hqCode}/{wpCode}")
    public ResponseEntity<Object> getWorkplace(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @PathVariable String hqCode,
                                                 @PathVariable String wpCode)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        Map<String, Object> param = new HashMap<>();

        param.put("userCompanyCode", userCompanyCode);
        param.put("wpCode", wpCode);
        param.put("hqCode", hqCode);

        return ResponseEntity.ok(workplaceService.getWorkplace(param));
    }

    @PostMapping("/{hqCode}")
    public ResponseEntity<Object> findList(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @PathVariable String hqCode
                                           )
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        Map<String, Object> param = new HashMap<>();

        param.put("userCompanyCode", userCompanyCode);
        param.put("hqCode", hqCode);

        return ResponseEntity.ok(workplaceService.selectWorkplaceList(param));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateHeadquarter(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody IvWorkplace workplace)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        workplace.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(workplaceService.updateWorkplace(workplace));
    }

}
