package net.neurolines.genai.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.neurolines.genai.dao.genai.ProductIdDao;
import net.neurolines.genai.model.genai.IvHeadQuarters;
import net.neurolines.genai.model.genai.MaProductId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductIdService {

    @Autowired
    ProductIdDao productIdDao;

    public int insertProductId(MaProductId productId)
    {
        return productIdDao.insertProductCode(productId);
    }

    public PageInfo<MaProductId> selectPageProductIdList(Map<String, Object> param)
    {
        PageHelper.startPage(Integer.parseInt(param.get("page").toString()), Integer.parseInt(param.get("pageSize").toString()));

        List<MaProductId> list = productIdDao.selectProductIdList(param);

        return new PageInfo<>(list);

    }

    public List<MaProductId> selectProductIdList(Map<String, Object> param)
    {
        return productIdDao.selectProductIdList(param);
    }

    public MaProductId getProductId(Map<String, Object> param){

        return productIdDao.selectProductId(param);
    }

    public int updateProductId(MaProductId productId)
    {
        return productIdDao.updateProductId(productId);
    }
}
