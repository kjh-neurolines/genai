package net.neurolines.genai.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.neurolines.genai.dao.genai.PidDepMappingDao;
import net.neurolines.genai.model.PidDepMappingDTO;
import net.neurolines.genai.model.genai.MaPidDepMapping;
import net.neurolines.genai.model.genai.MaProductId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PidDepMappingService {

    @Autowired
    PidDepMappingDao pidDepMappingDao;

    public int insertPidDepMapping(MaPidDepMapping pidDepMapping)
    {
        return pidDepMappingDao.insertPidDepMapping(pidDepMapping);
    }

    public PageInfo<PidDepMappingDTO> selectPagePidDepMappingList(Map<String, Object> param)
    {
        PageHelper.startPage(Integer.parseInt(param.get("page").toString()), Integer.parseInt(param.get("pageSize").toString()));

        List<PidDepMappingDTO> list = pidDepMappingDao.selectPidDepMappingList(param);

        return new PageInfo<>(list);

    }

    public List<PidDepMappingDTO> selectPidDepMappingList(Map<String, Object> param)
    {
        return pidDepMappingDao.selectPidDepMappingList(param);
    }

    public PidDepMappingDTO getPidDepMapping(Map<String, Object> param){

        return pidDepMappingDao.selectPidDepMappingByProductIdAndDmCode(param);
    }

    public int updatePidDepMapping(MaPidDepMapping pidDepMapping)
    {
        return pidDepMappingDao.updatePidDepMapping(pidDepMapping);
    }

}
