package nc.impl.erm;

import nc.impl.pub.ace.AceCarinfoPubServiceImpl;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.vo.erm.carinfo.AggCarInfoVO;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.bill.pagination.PaginationTransferObject;
import nc.vo.pubapp.bill.pagination.util.PaginationUtils;
import nc.impl.pubapp.pattern.data.bill.BillQuery;

public class CarinfoMaintainImpl extends AceCarinfoPubServiceImpl implements nc.itf.erm.ICarinfoMaintain {

      @Override
    public void delete(AggCarInfoVO[] clientFullVOs,AggCarInfoVO[] originBills) throws BusinessException {
        super.pubdeleteBills(clientFullVOs,originBills);
    }
  
      @Override
    public AggCarInfoVO[] insert(AggCarInfoVO[] clientFullVOs,AggCarInfoVO[] originBills) throws BusinessException {
        return super.pubinsertBills(clientFullVOs,originBills);
    }
    
      @Override
    public AggCarInfoVO[] update(AggCarInfoVO[] clientFullVOs,AggCarInfoVO[] originBills) throws BusinessException {
        return super.pubupdateBills(clientFullVOs,originBills);    
    }
  


	  @Override
  public String[] queryPKs(IQueryScheme queryScheme)
      throws BusinessException {
      return super.pubquerypkbills(queryScheme);
  }
  @Override
  public AggCarInfoVO[] queryBillByPK(String[] pks)
      throws BusinessException {
      return super.pubquerybillbypkbills(pks);
  }

  @Override
  public AggCarInfoVO[] save(AggCarInfoVO[] clientFullVOs,AggCarInfoVO[] originBills)
      throws BusinessException {
      return super.pubsendapprovebills(clientFullVOs,originBills);
  }

  @Override
  public AggCarInfoVO[] unsave(AggCarInfoVO[] clientFullVOs,AggCarInfoVO[] originBills)
      throws BusinessException {
      return super.pubunsendapprovebills(clientFullVOs,originBills);
  }

  @Override
  public AggCarInfoVO[] approve(AggCarInfoVO[] clientFullVOs,AggCarInfoVO[] originBills)
      throws BusinessException {
      return super.pubapprovebills(clientFullVOs,originBills);
  }

  @Override
  public AggCarInfoVO[] unapprove(AggCarInfoVO[] clientFullVOs,AggCarInfoVO[] originBills)
      throws BusinessException {
      return super.pubunapprovebills(clientFullVOs,originBills);
  }

}
