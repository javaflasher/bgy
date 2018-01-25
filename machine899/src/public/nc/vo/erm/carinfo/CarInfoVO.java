package nc.vo.erm.carinfo;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class CarInfoVO extends SuperVO {
	/**
	 * 会计年
	 */
	public String accyear66;
	/**
	 * 年限额
	 */
	public UFDouble annuallimit;
	/**
	 * 审批时间
	 */
	public UFDateTime approvedate;
	/**
	 * 审批人
	 */
	public String approver;
	/**
	 * 审批状态
	 */
	public Integer approvestatus;
	/**
	 * 启用会计月
	 */
	public Integer beginaccmonth;
	/**
	 * 单据日期
	 */
	public UFDate billdate;
	/**
	 * 制单人
	 */
	public String billmaker;
	/**
	 * 单据号
	 */
	public String billno;
	/**
	 * 单据状态
	 */
	public Integer billstatus;
	/**
	 * 单据类型
	 */
	public String billtype;
	/**
	 * BIP账号
	 */
	public String bipno;
	/**
	 * 车辆编码
	 */
	public String carcode;
	/**
	 * 车辆名称
	 */
	public String carname;
	/**
	 * 车辆性质
	 */
	public String carproperty77;
	/**
	 * 创建时间
	 */
	public UFDateTime creationtime;
	/**
	 * 创建人
	 */
	public String creator;
	/**
	 * 自定义项1
	 */
	public String def1;
	/**
	 * 自定义项10
	 */
	public String def10;
	/**
	 * 自定义项11
	 */
	public String def11;
	/**
	 * 自定义项12
	 */
	public String def12;
	/**
	 * 自定义项13
	 */
	public String def13;
	/**
	 * 自定义项14
	 */
	public String def14;
	/**
	 * 自定义项15
	 */
	public String def15;
	/**
	 * 自定义项16
	 */
	public String def16;
	/**
	 * 自定义项17
	 */
	public String def17;
	/**
	 * 自定义项18
	 */
	public String def18;
	/**
	 * 自定义项19
	 */
	public String def19;
	/**
	 * 自定义项2
	 */
	public String def2;
	/**
	 * 自定义项20
	 */
	public String def20;
	/**
	 * 自定义项3
	 */
	public String def3;
	/**
	 * 自定义项4
	 */
	public String def4;
	/**
	 * 自定义项5
	 */
	public String def5;
	/**
	 * 自定义项6
	 */
	public String def6;
	/**
	 * 自定义项7
	 */
	public String def7;
	/**
	 * 自定义项8
	 */
	public String def8;
	/**
	 * 自定义项9
	 */
	public String def9;
	/**
	 * 报销类型
	 */
	public Integer exptype;
	/**
	 * 制单时间
	 */
	public UFDateTime maketime;
	/**
	 * 最后修改时间
	 */
	public UFDateTime modifiedtime;
	/**
	 * 最后修改人
	 */
	public String modifier;
	/**
	 * 操作类型
	 */
	public Integer opertype;
	/**
	 * 发放方式
	 */
	public Integer paymode;
	/**
	 * 车辆
	 */
	public String pk_car99;
	/**
	 * 车辆限额限量控制
	 */
	public String pk_car_limit;
	/**
	 * 主键
	 */
	public String pk_carinfo;
	/**
	 * 集团
	 */
	public String pk_group;
	/**
	 * 组织
	 */
	public String pk_org;
	/**
	 * 组织版本
	 */
	public String pk_org_v;
	/**
	 * 上级档案
	 */
	public String pk_pcar88;
	/**
	 * 备注
	 */
	public String remarks;
	/**
	 * 交易类型
	 */
	public String transtype;
	/**
	 * 交易类型pk
	 */
	public String transtypepk;
	/**
	 * 时间戳
	 */
	public UFDateTime ts;

	/**
	 * 获取会计年
	 * 
	 * @return 会计年
	 */
	public String getAccyear66() {
		return this.accyear66;
	}

	/**
	 * 设置会计年
	 * 
	 * @param accyear
	 *            会计年
	 */
	public void setAccyear66(String accyear66) {
		this.accyear66 = accyear66;
	}

	/**
	 * 获取年限额
	 * 
	 * @return 年限额
	 */
	public UFDouble getAnnuallimit() {
		return this.annuallimit;
	}

	/**
	 * 设置年限额
	 * 
	 * @param annuallimit
	 *            年限额
	 */
	public void setAnnuallimit(UFDouble annuallimit) {
		this.annuallimit = annuallimit;
	}

	/**
	 * 获取审批时间
	 * 
	 * @return 审批时间
	 */
	public UFDateTime getApprovedate() {
		return this.approvedate;
	}

	/**
	 * 设置审批时间
	 * 
	 * @param approvedate
	 *            审批时间
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.approvedate = approvedate;
	}

	/**
	 * 获取审批人
	 * 
	 * @return 审批人
	 */
	public String getApprover() {
		return this.approver;
	}

	/**
	 * 设置审批人
	 * 
	 * @param approver
	 *            审批人
	 */
	public void setApprover(String approver) {
		this.approver = approver;
	}

	/**
	 * 获取审批状态
	 * 
	 * @return 审批状态
	 * @see String
	 */
	public Integer getApprovestatus() {
		return this.approvestatus;
	}

	/**
	 * 设置审批状态
	 * 
	 * @param approvestatus
	 *            审批状态
	 * @see String
	 */
	public void setApprovestatus(Integer approvestatus) {
		this.approvestatus = approvestatus;
	}

	/**
	 * 获取启用会计月
	 * 
	 * @return 启用会计月
	 * @see String
	 */
	public Integer getBeginaccmonth() {
		return this.beginaccmonth;
	}

	/**
	 * 设置启用会计月
	 * 
	 * @param beginaccmonth
	 *            启用会计月
	 * @see String
	 */
	public void setBeginaccmonth(Integer beginaccmonth) {
		this.beginaccmonth = beginaccmonth;
	}

	/**
	 * 获取单据日期
	 * 
	 * @return 单据日期
	 */
	public UFDate getBilldate() {
		return this.billdate;
	}

	/**
	 * 设置单据日期
	 * 
	 * @param billdate
	 *            单据日期
	 */
	public void setBilldate(UFDate billdate) {
		this.billdate = billdate;
	}

	/**
	 * 获取制单人
	 * 
	 * @return 制单人
	 */
	public String getBillmaker() {
		return this.billmaker;
	}

	/**
	 * 设置制单人
	 * 
	 * @param billmaker
	 *            制单人
	 */
	public void setBillmaker(String billmaker) {
		this.billmaker = billmaker;
	}

	/**
	 * 获取单据号
	 * 
	 * @return 单据号
	 */
	public String getBillno() {
		return this.billno;
	}

	/**
	 * 设置单据号
	 * 
	 * @param billno
	 *            单据号
	 */
	public void setBillno(String billno) {
		this.billno = billno;
	}

	/**
	 * 获取单据状态
	 * 
	 * @return 单据状态
	 * @see String
	 */
	public Integer getBillstatus() {
		return this.billstatus;
	}

	/**
	 * 设置单据状态
	 * 
	 * @param billstatus
	 *            单据状态
	 * @see String
	 */
	public void setBillstatus(Integer billstatus) {
		this.billstatus = billstatus;
	}

	/**
	 * 获取单据类型
	 * 
	 * @return 单据类型
	 */
	public String getBilltype() {
		return this.billtype;
	}

	/**
	 * 设置单据类型
	 * 
	 * @param billtype
	 *            单据类型
	 */
	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	/**
	 * 获取BIP账号
	 * 
	 * @return BIP账号
	 */
	public String getBipno() {
		return this.bipno;
	}

	/**
	 * 设置BIP账号
	 * 
	 * @param bipno
	 *            BIP账号
	 */
	public void setBipno(String bipno) {
		this.bipno = bipno;
	}

	/**
	 * 获取车辆编码
	 * 
	 * @return 车辆编码
	 */
	public String getCarcode() {
		return this.carcode;
	}

	/**
	 * 设置车辆编码
	 * 
	 * @param carcode
	 *            车辆编码
	 */
	public void setCarcode(String carcode) {
		this.carcode = carcode;
	}

	/**
	 * 获取车辆名称
	 * 
	 * @return 车辆名称
	 */
	public String getCarname() {
		return this.carname;
	}

	/**
	 * 设置车辆名称
	 * 
	 * @param carname
	 *            车辆名称
	 */
	public void setCarname(String carname) {
		this.carname = carname;
	}

	/**
	 * 获取车辆性质
	 * 
	 * @return 车辆性质
	 */
	public String getCarproperty77() {
		return this.carproperty77;
	}

	/**
	 * 设置车辆性质
	 * 
	 * @param carproperty
	 *            车辆性质
	 */
	public void setCarproperty77(String carproperty77) {
		this.carproperty77 = carproperty77;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * 设置创建时间
	 * 
	 * @param creationtime
	 *            创建时间
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * 获取创建人
	 * 
	 * @return 创建人
	 */
	public String getCreator() {
		return this.creator;
	}

	/**
	 * 设置创建人
	 * 
	 * @param creator
	 *            创建人
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 获取自定义项1
	 * 
	 * @return 自定义项1
	 */
	public String getDef1() {
		return this.def1;
	}

	/**
	 * 设置自定义项1
	 * 
	 * @param def1
	 *            自定义项1
	 */
	public void setDef1(String def1) {
		this.def1 = def1;
	}

	/**
	 * 获取自定义项10
	 * 
	 * @return 自定义项10
	 */
	public String getDef10() {
		return this.def10;
	}

	/**
	 * 设置自定义项10
	 * 
	 * @param def10
	 *            自定义项10
	 */
	public void setDef10(String def10) {
		this.def10 = def10;
	}

	/**
	 * 获取自定义项11
	 * 
	 * @return 自定义项11
	 */
	public String getDef11() {
		return this.def11;
	}

	/**
	 * 设置自定义项11
	 * 
	 * @param def11
	 *            自定义项11
	 */
	public void setDef11(String def11) {
		this.def11 = def11;
	}

	/**
	 * 获取自定义项12
	 * 
	 * @return 自定义项12
	 */
	public String getDef12() {
		return this.def12;
	}

	/**
	 * 设置自定义项12
	 * 
	 * @param def12
	 *            自定义项12
	 */
	public void setDef12(String def12) {
		this.def12 = def12;
	}

	/**
	 * 获取自定义项13
	 * 
	 * @return 自定义项13
	 */
	public String getDef13() {
		return this.def13;
	}

	/**
	 * 设置自定义项13
	 * 
	 * @param def13
	 *            自定义项13
	 */
	public void setDef13(String def13) {
		this.def13 = def13;
	}

	/**
	 * 获取自定义项14
	 * 
	 * @return 自定义项14
	 */
	public String getDef14() {
		return this.def14;
	}

	/**
	 * 设置自定义项14
	 * 
	 * @param def14
	 *            自定义项14
	 */
	public void setDef14(String def14) {
		this.def14 = def14;
	}

	/**
	 * 获取自定义项15
	 * 
	 * @return 自定义项15
	 */
	public String getDef15() {
		return this.def15;
	}

	/**
	 * 设置自定义项15
	 * 
	 * @param def15
	 *            自定义项15
	 */
	public void setDef15(String def15) {
		this.def15 = def15;
	}

	/**
	 * 获取自定义项16
	 * 
	 * @return 自定义项16
	 */
	public String getDef16() {
		return this.def16;
	}

	/**
	 * 设置自定义项16
	 * 
	 * @param def16
	 *            自定义项16
	 */
	public void setDef16(String def16) {
		this.def16 = def16;
	}

	/**
	 * 获取自定义项17
	 * 
	 * @return 自定义项17
	 */
	public String getDef17() {
		return this.def17;
	}

	/**
	 * 设置自定义项17
	 * 
	 * @param def17
	 *            自定义项17
	 */
	public void setDef17(String def17) {
		this.def17 = def17;
	}

	/**
	 * 获取自定义项18
	 * 
	 * @return 自定义项18
	 */
	public String getDef18() {
		return this.def18;
	}

	/**
	 * 设置自定义项18
	 * 
	 * @param def18
	 *            自定义项18
	 */
	public void setDef18(String def18) {
		this.def18 = def18;
	}

	/**
	 * 获取自定义项19
	 * 
	 * @return 自定义项19
	 */
	public String getDef19() {
		return this.def19;
	}

	/**
	 * 设置自定义项19
	 * 
	 * @param def19
	 *            自定义项19
	 */
	public void setDef19(String def19) {
		this.def19 = def19;
	}

	/**
	 * 获取自定义项2
	 * 
	 * @return 自定义项2
	 */
	public String getDef2() {
		return this.def2;
	}

	/**
	 * 设置自定义项2
	 * 
	 * @param def2
	 *            自定义项2
	 */
	public void setDef2(String def2) {
		this.def2 = def2;
	}

	/**
	 * 获取自定义项20
	 * 
	 * @return 自定义项20
	 */
	public String getDef20() {
		return this.def20;
	}

	/**
	 * 设置自定义项20
	 * 
	 * @param def20
	 *            自定义项20
	 */
	public void setDef20(String def20) {
		this.def20 = def20;
	}

	/**
	 * 获取自定义项3
	 * 
	 * @return 自定义项3
	 */
	public String getDef3() {
		return this.def3;
	}

	/**
	 * 设置自定义项3
	 * 
	 * @param def3
	 *            自定义项3
	 */
	public void setDef3(String def3) {
		this.def3 = def3;
	}

	/**
	 * 获取自定义项4
	 * 
	 * @return 自定义项4
	 */
	public String getDef4() {
		return this.def4;
	}

	/**
	 * 设置自定义项4
	 * 
	 * @param def4
	 *            自定义项4
	 */
	public void setDef4(String def4) {
		this.def4 = def4;
	}

	/**
	 * 获取自定义项5
	 * 
	 * @return 自定义项5
	 */
	public String getDef5() {
		return this.def5;
	}

	/**
	 * 设置自定义项5
	 * 
	 * @param def5
	 *            自定义项5
	 */
	public void setDef5(String def5) {
		this.def5 = def5;
	}

	/**
	 * 获取自定义项6
	 * 
	 * @return 自定义项6
	 */
	public String getDef6() {
		return this.def6;
	}

	/**
	 * 设置自定义项6
	 * 
	 * @param def6
	 *            自定义项6
	 */
	public void setDef6(String def6) {
		this.def6 = def6;
	}

	/**
	 * 获取自定义项7
	 * 
	 * @return 自定义项7
	 */
	public String getDef7() {
		return this.def7;
	}

	/**
	 * 设置自定义项7
	 * 
	 * @param def7
	 *            自定义项7
	 */
	public void setDef7(String def7) {
		this.def7 = def7;
	}

	/**
	 * 获取自定义项8
	 * 
	 * @return 自定义项8
	 */
	public String getDef8() {
		return this.def8;
	}

	/**
	 * 设置自定义项8
	 * 
	 * @param def8
	 *            自定义项8
	 */
	public void setDef8(String def8) {
		this.def8 = def8;
	}

	/**
	 * 获取自定义项9
	 * 
	 * @return 自定义项9
	 */
	public String getDef9() {
		return this.def9;
	}

	/**
	 * 设置自定义项9
	 * 
	 * @param def9
	 *            自定义项9
	 */
	public void setDef9(String def9) {
		this.def9 = def9;
	}

	/**
	 * 获取报销类型
	 * 
	 * @return 报销类型
	 * @see String
	 */
	public Integer getExptype() {
		return this.exptype;
	}

	/**
	 * 设置报销类型
	 * 
	 * @param exptype
	 *            报销类型
	 * @see String
	 */
	public void setExptype(Integer exptype) {
		this.exptype = exptype;
	}

	/**
	 * 获取制单时间
	 * 
	 * @return 制单时间
	 */
	public UFDateTime getMaketime() {
		return this.maketime;
	}

	/**
	 * 设置制单时间
	 * 
	 * @param maketime
	 *            制单时间
	 */
	public void setMaketime(UFDateTime maketime) {
		this.maketime = maketime;
	}

	/**
	 * 获取最后修改时间
	 * 
	 * @return 最后修改时间
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * 设置最后修改时间
	 * 
	 * @param modifiedtime
	 *            最后修改时间
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	/**
	 * 获取最后修改人
	 * 
	 * @return 最后修改人
	 */
	public String getModifier() {
		return this.modifier;
	}

	/**
	 * 设置最后修改人
	 * 
	 * @param modifier
	 *            最后修改人
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * 获取操作类型
	 * 
	 * @return 操作类型
	 * @see String
	 */
	public Integer getOpertype() {
		return this.opertype;
	}

	/**
	 * 设置操作类型
	 * 
	 * @param opertype
	 *            操作类型
	 * @see String
	 */
	public void setOpertype(Integer opertype) {
		this.opertype = opertype;
	}

	/**
	 * 获取发放方式
	 * 
	 * @return 发放方式
	 * @see String
	 */
	public Integer getPaymode() {
		return this.paymode;
	}

	/**
	 * 设置发放方式
	 * 
	 * @param paymode
	 *            发放方式
	 * @see String
	 */
	public void setPaymode(Integer paymode) {
		this.paymode = paymode;
	}

	/**
	 * 获取车辆
	 * 
	 * @return 车辆
	 */
	public String getPk_car99() {
		return this.pk_car99;
	}

	/**
	 * 设置车辆
	 * 
	 * @param pk_car
	 *            车辆
	 */
	public void setPk_car99(String pk_car99) {
		this.pk_car99 = pk_car99;
	}

	/**
	 * 获取车辆限额限量控制
	 * 
	 * @return 车辆限额限量控制
	 */
	public String getPk_car_limit() {
		return this.pk_car_limit;
	}

	/**
	 * 设置车辆限额限量控制
	 * 
	 * @param pk_car_limit
	 *            车辆限额限量控制
	 */
	public void setPk_car_limit(String pk_car_limit) {
		this.pk_car_limit = pk_car_limit;
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public String getPk_carinfo() {
		return this.pk_carinfo;
	}

	/**
	 * 设置主键
	 * 
	 * @param pk_carinfo
	 *            主键
	 */
	public void setPk_carinfo(String pk_carinfo) {
		this.pk_carinfo = pk_carinfo;
	}

	/**
	 * 获取集团
	 * 
	 * @return 集团
	 */
	public String getPk_group() {
		return this.pk_group;
	}

	/**
	 * 设置集团
	 * 
	 * @param pk_group
	 *            集团
	 */
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * 获取组织
	 * 
	 * @return 组织
	 */
	public String getPk_org() {
		return this.pk_org;
	}

	/**
	 * 设置组织
	 * 
	 * @param pk_org
	 *            组织
	 */
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * 获取组织版本
	 * 
	 * @return 组织版本
	 */
	public String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * 设置组织版本
	 * 
	 * @param pk_org_v
	 *            组织版本
	 */
	public void setPk_org_v(String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * 获取上级档案
	 * 
	 * @return 上级档案
	 */
	public String getPk_pcar88() {
		return this.pk_pcar88;
	}

	/**
	 * 设置上级档案
	 * 
	 * @param pk_pcar
	 *            上级档案
	 */
	public void setPk_pcar88(String pk_pcar88) {
		this.pk_pcar88 = pk_pcar88;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemarks() {
		return this.remarks;
	}

	/**
	 * 设置备注
	 * 
	 * @param remarks
	 *            备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 获取交易类型
	 * 
	 * @return 交易类型
	 */
	public String getTranstype() {
		return this.transtype;
	}

	/**
	 * 设置交易类型
	 * 
	 * @param transtype
	 *            交易类型
	 */
	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

	/**
	 * 获取交易类型pk
	 * 
	 * @return 交易类型pk
	 */
	public String getTranstypepk() {
		return this.transtypepk;
	}

	/**
	 * 设置交易类型pk
	 * 
	 * @param transtypepk
	 *            交易类型pk
	 */
	public void setTranstypepk(String transtypepk) {
		this.transtypepk = transtypepk;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * 设置时间戳
	 * 
	 * @param ts
	 *            时间戳
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("erm.CarInfoVO");
	}
}