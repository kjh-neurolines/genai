package net.neurolines.genai.dao.genai;

import net.neurolines.genai.model.genai.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InventoryDao {

    int insertDefaultInfo(IvInventory inventory);

    int insertInventory(Map<String, Object> param);

    int insertInventoryChemical(List<IvInventoryChemical> chemicalList);

    int insertInventoryClassResult(List<IvInventoryClassResult> classResults);

    int insertInventoryPictureInfo(List<IvInventoryPicture> pictures);

    int insertInventoryHcode(List<IvInventoryHcode> hcodes);

    int insertInventoryPcode(List<IvInventoryPcode> pcodes);

    int insertInventoryTransport(IvInventoryTransport inventoryTransport);

    int insertInventoryFileInfo(IvInventoryFile ivInventoryFile);

    int updateInventoryChemical(IvInventoryChemical ivInventoryChemical);

    int updateInventory(IvInventory inventory);

    int insertInventoryOrgData(IvInventoryOrgData ivInventoryOrgData);
}
