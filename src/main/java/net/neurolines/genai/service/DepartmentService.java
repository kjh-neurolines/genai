package net.neurolines.genai.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.neurolines.genai.dao.genai.DepartmentDao;
import net.neurolines.genai.model.genai.IvDepartment;
import net.neurolines.genai.model.genai.IvWorkplace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {

    @Autowired
    DepartmentDao departmentDao;

    public int insertDepartment(IvDepartment department)
    {
        return departmentDao.insertDepartment(department);
    }

    public PageInfo<IvDepartment> selectPageDepartmentList(Map<String, Object> param)
    {
        PageHelper.startPage(Integer.parseInt(param.get("page").toString()), Integer.parseInt(param.get("pageSize").toString()));

        List<IvDepartment> list = departmentDao.selectDepartmentList(param);

        return new PageInfo<>(list);

    }

    public List<IvDepartment> selectDepartmentList(Map<String, Object> param)
    {
        return departmentDao.selectDepartmentList(param);
    }

    public IvDepartment getDepartment(Map<String, Object> param){

        return departmentDao.selectDepartmentByDmCode(param);
    }

    public int updateDepartment(IvDepartment department)
    {
        return departmentDao.updateDepartment(department);
    }
}
