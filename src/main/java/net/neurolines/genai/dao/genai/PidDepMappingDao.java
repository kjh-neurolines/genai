package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.PidDepMappingDTO;
import net.neurolines.genai.model.genai.MaPidDepMapping;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PidDepMappingDao {

    List<PidDepMappingDTO> selectPidDepMappingList(Map<String, Object> param);

    int insertPidDepMapping(MaPidDepMapping pidDepMapping);

    PidDepMappingDTO selectPidDepMappingByProductIdAndDmCode(Map<String, Object> param);

    int updatePidDepMapping(MaPidDepMapping pidDepMapping);
}
