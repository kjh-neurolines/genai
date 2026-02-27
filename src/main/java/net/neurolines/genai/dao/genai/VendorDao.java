package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.IvHeadQuarters;
import net.neurolines.genai.model.genai.MaVendor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VendorDao {


    List<MaVendor> selectVendorList(Map<String, Object> param);

    int insertVendor(MaVendor vendor);

    MaVendor selectVendorByVendorCode(Map<String, Object> param);

    int updateVendor(MaVendor vendor);

    int deleteVendor(Map<String, Object> param);

    List<Map<String, Object>> getVendorInfoByProductCode(Map<String, Object> param);
}

