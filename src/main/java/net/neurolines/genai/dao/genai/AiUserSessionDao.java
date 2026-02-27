package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.AiUserSession;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AiUserSessionDao {

    List<AiUserSession> selectAiUserSession(Map<String, String> param);

    int insertAiUserSession(AiUserSession aiUserSession);

    int deleteAiUserSession(int aiUsRegNo);

    int updateAiUserSession(AiUserSession aiUserSession);

}
