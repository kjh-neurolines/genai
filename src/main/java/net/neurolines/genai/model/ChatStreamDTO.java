package net.neurolines.genai.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatStreamDTO {

    String prompt;

    String answer;

    String session;

    List<Map<String, Object>> workflow;
}
