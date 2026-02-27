package net.neurolines.genai.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.neurolines.genai.dao.genai.VendorDao;
import net.neurolines.genai.model.genai.IvWorkplace;
import net.neurolines.genai.model.genai.MaVendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VendorService {

    @Autowired
    VendorDao vendorDao;

    public int insertVendor(MaVendor vendor)
    {
        return vendorDao.insertVendor(vendor);
    }

    public PageInfo<MaVendor> selectPageVendorList(Map<String, Object> param)
    {
        PageHelper.startPage(Integer.parseInt(param.get("page").toString()), Integer.parseInt(param.get("pageSize").toString()));

        List<MaVendor> list = vendorDao.selectVendorList(param);

        return new PageInfo<>(list);

    }

    public List<MaVendor> selectVendorList(Map<String, Object> param)
    {
        return vendorDao.selectVendorList(param);
    }

    public MaVendor getVendor(Map<String, Object> param){

        return vendorDao.selectVendorByVendorCode(param);
    }

    public int updateVendor(MaVendor vendor)
    {
        return vendorDao.updateVendor(vendor);
    }

    public List<Map<String, Object>> selectVendorListByProductCode(Map<String, Object> param)
    {
        return vendorDao.getVendorInfoByProductCode(param);
    }

}
