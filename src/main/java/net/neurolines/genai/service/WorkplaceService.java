package net.neurolines.genai.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.neurolines.genai.dao.genai.WorkplaceDao;
import net.neurolines.genai.model.genai.IvHeadQuarters;
import net.neurolines.genai.model.genai.IvWorkplace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WorkplaceService {

    @Autowired
    WorkplaceDao workplaceDao;

    public int insertWorkplace(IvWorkplace workplace)
    {
        return workplaceDao.insertWorkplace(workplace);
    }

    public PageInfo<IvWorkplace> selectPageWorkplaceList(Map<String, Object> param)
    {
        PageHelper.startPage(Integer.parseInt(param.get("page").toString()), Integer.parseInt(param.get("pageSize").toString()));

        List<IvWorkplace> list = workplaceDao.selectWorkplaceList(param);

        return new PageInfo<>(list);

    }

    public List<IvWorkplace> selectWorkplaceList(Map<String, Object> param)
    {
        return workplaceDao.selectWorkplaceList(param);
    }

    public IvWorkplace getWorkplace(Map<String, Object> param){

        return workplaceDao.selectWorkplaceByWpCode(param);
    }

    public int updateWorkplace(IvWorkplace ivWorkplace)
    {
        return workplaceDao.updateWorkplace(ivWorkplace);
    }
}
