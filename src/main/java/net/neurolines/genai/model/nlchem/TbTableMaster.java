package net.neurolines.genai.model.nlchem;

import lombok.Data;

/**
 * 테이블 마스터(임시용)
 */
@Data
public class TbTableMaster {

    String tableName;

    String kind;

    String skind;

    String tableDescription;

    String tableCount;

    String uri;

    String type;

    String uname;

    int udate;
}
