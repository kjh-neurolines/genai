package net.neurolines.genai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.genai.IvWorkplace;
import net.neurolines.genai.model.genai.MaVendor;
import net.neurolines.genai.service.AuthService;
import net.neurolines.genai.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    VendorService vendorService;

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


        return ResponseEntity.ok(vendorService.selectPageVendorList(param));
    }

    @PostMapping("/find")
    public ResponseEntity<Object> searchByProductCode(HttpServletRequest request,
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


        return ResponseEntity.ok(vendorService.selectVendorListByProductCode(param));
    }

    @PostMapping("/search")
    public ResponseEntity<Object> search(HttpServletRequest request,
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


        return ResponseEntity.ok(vendorService.selectVendorList(param));
    }

    @PostMapping("/reg")
    public ResponseEntity<Object> insertVendor(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @RequestBody MaVendor vendor)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        vendor.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(vendorService.insertVendor(vendor));
    }

    @PostMapping("/{vendorCode}")
    public ResponseEntity<Object> getVendor(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @PathVariable String vendorCode)
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
        param.put("vendorCode", vendorCode);

        return ResponseEntity.ok(vendorService.getVendor(param));
    }
    @PostMapping("/update")
    public ResponseEntity<Object> updateVendor(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody MaVendor vendor)
    {

        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        vendor.setUserCompanyCode(userCompanyCode);

        return ResponseEntity.ok(vendorService.updateVendor(vendor));
    }


}
