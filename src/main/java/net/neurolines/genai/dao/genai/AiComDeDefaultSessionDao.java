package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.AiComDeDefaultSession;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AiComDeDefaultSessionDao {

    List<AiComDeDefaultSession> selectComDeDefaultSession(Map<String, String> param);

}
