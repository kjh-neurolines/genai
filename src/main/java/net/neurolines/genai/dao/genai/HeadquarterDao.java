package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.IvHeadQuarters;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HeadquarterDao {

    List<IvHeadQuarters> selectHeadquarterList(Map<String, Object> param);

    int insertHeadquarter(IvHeadQuarters headQuarters);

    IvHeadQuarters selectHeadquarterByHqCode(Map<String, Object> param);

    int updateHeadquarter(IvHeadQuarters headQuarters);
}
