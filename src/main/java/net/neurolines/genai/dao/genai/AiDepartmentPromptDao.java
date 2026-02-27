package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.AiDepartmentPrompt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

@Mapper
public interface AiDepartmentPromptDao {

    List<AiDepartmentPrompt> selectDepartmentPrompt(Map<String, String> param);

}
