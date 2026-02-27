package net.neurolines.genai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.genai.IvHeadQuarters;
import net.neurolines.genai.service.AuthService;
import net.neurolines.genai.service.HeadquaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hq")
public class HeadquaterController {

    @Autowired
    HeadquaterService headquaterService;

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


        return ResponseEntity.ok(headquaterService.selectPageHeadquarterList(param));
    }

    @PostMapping("/reg")
    public ResponseEntity<Object> insertHeadquater(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @RequestBody IvHeadQuarters headquater)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        headquater.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(headquaterService.insertHeadquarter(headquater));
    }

    @PostMapping("/{hqCode}")
    public ResponseEntity<Object> getHeadquarter(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @PathVariable String hqCode)
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

        return ResponseEntity.ok(headquaterService.getHeadquarter(param));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateHeadquarter(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @RequestBody IvHeadQuarters headquater)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }


        headquater.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(headquaterService.updateHeadquarter(headquater));
    }
}
