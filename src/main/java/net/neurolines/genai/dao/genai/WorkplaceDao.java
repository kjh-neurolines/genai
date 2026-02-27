package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.IvWorkplace;
import org.apache.ibatis.annotations.Mapper;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface WorkplaceDao {

    List<IvWorkplace> selectWorkplaceList(Map<String, Object> param);

    int insertWorkplace(IvWorkplace ivWorkplace);

    IvWorkplace selectWorkplaceByWpCode(Map<String, Object> param);

    int updateWorkplace(IvWorkplace workplace);
}
