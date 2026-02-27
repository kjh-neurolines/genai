package net.neurolines.genai.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.neurolines.genai.dao.genai.HeadquarterDao;
import net.neurolines.genai.model.genai.IvHeadQuarters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HeadquaterService {

    @Autowired
    HeadquarterDao headquaterDao;

    public int insertHeadquarter(IvHeadQuarters headQuarters)
    {
        return headquaterDao.insertHeadquarter(headQuarters);
    }

    public PageInfo<IvHeadQuarters> selectPageHeadquarterList(Map<String, Object> param)
    {
        PageHelper.startPage(Integer.parseInt(param.get("page").toString()), Integer.parseInt(param.get("pageSize").toString()));

        List<IvHeadQuarters> list = headquaterDao.selectHeadquarterList(param);

        return new PageInfo<>(list);

    }

    public List<IvHeadQuarters> selectHeadquarterList(Map<String, Object> param)
    {
        return headquaterDao.selectHeadquarterList(param);
    }

    public IvHeadQuarters getHeadquarter(Map<String, Object> param){

        return headquaterDao.selectHeadquarterByHqCode(param);
    }

    public int updateHeadquarter(IvHeadQuarters ivHeadQuarters)
    {
        return headquaterDao.updateHeadquarter(ivHeadQuarters);
    }
}
