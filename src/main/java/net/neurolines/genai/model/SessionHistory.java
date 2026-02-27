package net.neurolines.genai.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SessionHistory {

    String id;

    String role;

    String content;

    String timestamp;

    String toolCalls;

}
