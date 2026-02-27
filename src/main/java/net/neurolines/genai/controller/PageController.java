package net.neurolines.genai.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.genai.IvDepartment;
import net.neurolines.genai.model.genai.IvHeadQuarters;
import net.neurolines.genai.service.AuthService;
import net.neurolines.genai.service.DepartmentService;
import net.neurolines.genai.service.HeadquaterService;
import net.neurolines.genai.service.WorkplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController {

    @Autowired
    HeadquaterService headquaterService;

    @Autowired
    WorkplaceService workplaceService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AuthService authService;

    @GetMapping("")
    public String home(Model model,
                       HttpServletRequest request,
                       HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }
        String page = "index";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", false);

        return "index";
    }

    @GetMapping("/index")
    public String index(Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "index";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", false);


        return "index";
    }

    @GetMapping("/inventory/dashboard")
    public String dashboard(Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "dashboard";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", false);


        return "inventory/dashboard";
    }

    @GetMapping("/inventory/inventory-register")
    public String inventoryRegister(Model model,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "inventory-register";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", false);


        return "inventory/inventory-register";
    }

    @GetMapping("/inventory/ai-auto-register")
    public String aiAutoRegister(Model model,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {


        List<AuthUser> userInfo = authService.user(request, response);

        String email = null;
        String userCompanyCode = null;
        String dmCode = null; //TODO 부서코드 처리 필요


        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        if(userInfo != null && userInfo.size() > 0){
            email = userInfo.get(0).getEmail();
            userCompanyCode = userInfo.get(0).getCompanyCode();
        }

        Map<String, Object> param = new HashMap<>();
        param.put("userCompanyCode", userCompanyCode);
        List<IvHeadQuarters> headQuarters = headquaterService.selectHeadquarterList(param);

        String page = "ai-auto-register";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", false);
        model.addAttribute("headquarterList", headQuarters);



        return "inventory/ai-auto-register";
    }

    @GetMapping("/inventory/ai-auto-register-verify")
    public String aiAutoRegisterVerify(Model model,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "ai-auto-register-verify";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", false);


        return "inventory/ai-auto-register-verify";
    }

    @GetMapping("/inventory/inventory-list")
    public String inventoryList(Model model,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "inventory-list";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", false);

        return "inventory/inventory-list";
    }


    @GetMapping("/inventory/setting-business-unit")
    public String settingBusinessUnit(Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "setting-business-unit";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", true);

        return "inventory/setting-business-unit";
    }

    @GetMapping("/inventory/setting-business-site")
    public String settingBusinessSite(Model model,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

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
        List<IvHeadQuarters> headQuarters = headquaterService.selectHeadquarterList(param);

        String page = "setting-business-site";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", true);
        model.addAttribute("headquarterList", headQuarters);

        return "inventory/setting-business-site";
    }

    @GetMapping("/inventory/setting-department")
    public String settingDepartment(Model model,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

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
        List<IvHeadQuarters> headQuarters = headquaterService.selectHeadquarterList(param);


        String page = "setting-department";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", true);
        model.addAttribute("headquarterList", headQuarters);

        return "inventory/setting-department";
    }

    @GetMapping("/inventory/setting-material-code")
    public String settingMaterialCode(Model model,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "setting-material-code";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", true);

        return "inventory/setting-material-code";
    }

    @GetMapping("/inventory/setting-vendor")
    public String settingVendor(Model model,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "setting-vendor";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", true);

        return "inventory/setting-vendor";
    }

    @GetMapping("/inventory/material-department")
    public String materialDepartment(Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {

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
        List<IvDepartment> departments = departmentService.selectDepartmentList(param);


        String page = "material-department";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", true);
        model.addAttribute("department", departments);

        return "inventory/material-department";
    }

    @GetMapping("/inventory/setting-user")
    public String settingUser(Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "setting-user";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", true);

        return "inventory/setting-user";
    }

    @GetMapping("/inventory/setting-permission")
    public String settingPermission(Model model,
                              HttpServletRequest request,
                              HttpServletResponse response) {

        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("email")) {
                    email = value;
                }
            }
        }

        String page = "setting-permission";

        model.addAttribute("email", email);
        model.addAttribute("page", page);
        model.addAttribute("setting", true);

        return "inventory/setting-permission";
    }

}
