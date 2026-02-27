package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.MaProductId;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductIdDao {

    List<MaProductId> selectProductIdList(Map<String, Object> param);

    int insertProductCode(MaProductId productId);

    MaProductId selectProductId(Map<String, Object> param);

    int updateProductId(MaProductId productId);
}
