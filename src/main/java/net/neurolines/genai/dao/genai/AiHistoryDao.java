package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.AiHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AiHistoryDao {

    int insertChatHistory(AiHistory aiHistory);

    List<AiHistory>  selectChatHistory(Map<String, String> param);

}
