package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.IvDepartment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepartmentDao {

    List<IvDepartment> selectDepartmentList(Map<String, Object> param);

    int insertDepartment(IvDepartment department);

    IvDepartment selectDepartmentByDmCode(Map<String, Object> param);

    int updateDepartment(IvDepartment department);
}
