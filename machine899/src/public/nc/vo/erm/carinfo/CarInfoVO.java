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
	 * �����
	 */
	public String accyear66;
	/**
	 * ���޶�
	 */
	public UFDouble annuallimit;
	/**
	 * ����ʱ��
	 */
	public UFDateTime approvedate;
	/**
	 * ������
	 */
	public String approver;
	/**
	 * ����״̬
	 */
	public Integer approvestatus;
	/**
	 * ���û����
	 */
	public Integer beginaccmonth;
	/**
	 * ��������
	 */
	public UFDate billdate;
	/**
	 * �Ƶ���
	 */
	public String billmaker;
	/**
	 * ���ݺ�
	 */
	public String billno;
	/**
	 * ����״̬
	 */
	public Integer billstatus;
	/**
	 * ��������
	 */
	public String billtype;
	/**
	 * BIP�˺�
	 */
	public String bipno;
	/**
	 * ��������
	 */
	public String carcode;
	/**
	 * ��������
	 */
	public String carname;
	/**
	 * ��������
	 */
	public String carproperty77;
	/**
	 * ����ʱ��
	 */
	public UFDateTime creationtime;
	/**
	 * ������
	 */
	public String creator;
	/**
	 * �Զ�����1
	 */
	public String def1;
	/**
	 * �Զ�����10
	 */
	public String def10;
	/**
	 * �Զ�����11
	 */
	public String def11;
	/**
	 * �Զ�����12
	 */
	public String def12;
	/**
	 * �Զ�����13
	 */
	public String def13;
	/**
	 * �Զ�����14
	 */
	public String def14;
	/**
	 * �Զ�����15
	 */
	public String def15;
	/**
	 * �Զ�����16
	 */
	public String def16;
	/**
	 * �Զ�����17
	 */
	public String def17;
	/**
	 * �Զ�����18
	 */
	public String def18;
	/**
	 * �Զ�����19
	 */
	public String def19;
	/**
	 * �Զ�����2
	 */
	public String def2;
	/**
	 * �Զ�����20
	 */
	public String def20;
	/**
	 * �Զ�����3
	 */
	public String def3;
	/**
	 * �Զ�����4
	 */
	public String def4;
	/**
	 * �Զ�����5
	 */
	public String def5;
	/**
	 * �Զ�����6
	 */
	public String def6;
	/**
	 * �Զ�����7
	 */
	public String def7;
	/**
	 * �Զ�����8
	 */
	public String def8;
	/**
	 * �Զ�����9
	 */
	public String def9;
	/**
	 * ��������
	 */
	public Integer exptype;
	/**
	 * �Ƶ�ʱ��
	 */
	public UFDateTime maketime;
	/**
	 * ����޸�ʱ��
	 */
	public UFDateTime modifiedtime;
	/**
	 * ����޸���
	 */
	public String modifier;
	/**
	 * ��������
	 */
	public Integer opertype;
	/**
	 * ���ŷ�ʽ
	 */
	public Integer paymode;
	/**
	 * ����
	 */
	public String pk_car99;
	/**
	 * �����޶���������
	 */
	public String pk_car_limit;
	/**
	 * ����
	 */
	public String pk_carinfo;
	/**
	 * ����
	 */
	public String pk_group;
	/**
	 * ��֯
	 */
	public String pk_org;
	/**
	 * ��֯�汾
	 */
	public String pk_org_v;
	/**
	 * �ϼ�����
	 */
	public String pk_pcar88;
	/**
	 * ��ע
	 */
	public String remarks;
	/**
	 * ��������
	 */
	public String transtype;
	/**
	 * ��������pk
	 */
	public String transtypepk;
	/**
	 * ʱ���
	 */
	public UFDateTime ts;

	/**
	 * ��ȡ�����
	 * 
	 * @return �����
	 */
	public String getAccyear66() {
		return this.accyear66;
	}

	/**
	 * ���û����
	 * 
	 * @param accyear
	 *            �����
	 */
	public void setAccyear66(String accyear66) {
		this.accyear66 = accyear66;
	}

	/**
	 * ��ȡ���޶�
	 * 
	 * @return ���޶�
	 */
	public UFDouble getAnnuallimit() {
		return this.annuallimit;
	}

	/**
	 * �������޶�
	 * 
	 * @param annuallimit
	 *            ���޶�
	 */
	public void setAnnuallimit(UFDouble annuallimit) {
		this.annuallimit = annuallimit;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getApprovedate() {
		return this.approvedate;
	}

	/**
	 * ��������ʱ��
	 * 
	 * @param approvedate
	 *            ����ʱ��
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.approvedate = approvedate;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getApprover() {
		return this.approver;
	}

	/**
	 * ����������
	 * 
	 * @param approver
	 *            ������
	 */
	public void setApprover(String approver) {
		this.approver = approver;
	}

	/**
	 * ��ȡ����״̬
	 * 
	 * @return ����״̬
	 * @see String
	 */
	public Integer getApprovestatus() {
		return this.approvestatus;
	}

	/**
	 * ��������״̬
	 * 
	 * @param approvestatus
	 *            ����״̬
	 * @see String
	 */
	public void setApprovestatus(Integer approvestatus) {
		this.approvestatus = approvestatus;
	}

	/**
	 * ��ȡ���û����
	 * 
	 * @return ���û����
	 * @see String
	 */
	public Integer getBeginaccmonth() {
		return this.beginaccmonth;
	}

	/**
	 * �������û����
	 * 
	 * @param beginaccmonth
	 *            ���û����
	 * @see String
	 */
	public void setBeginaccmonth(Integer beginaccmonth) {
		this.beginaccmonth = beginaccmonth;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public UFDate getBilldate() {
		return this.billdate;
	}

	/**
	 * ���õ�������
	 * 
	 * @param billdate
	 *            ��������
	 */
	public void setBilldate(UFDate billdate) {
		this.billdate = billdate;
	}

	/**
	 * ��ȡ�Ƶ���
	 * 
	 * @return �Ƶ���
	 */
	public String getBillmaker() {
		return this.billmaker;
	}

	/**
	 * �����Ƶ���
	 * 
	 * @param billmaker
	 *            �Ƶ���
	 */
	public void setBillmaker(String billmaker) {
		this.billmaker = billmaker;
	}

	/**
	 * ��ȡ���ݺ�
	 * 
	 * @return ���ݺ�
	 */
	public String getBillno() {
		return this.billno;
	}

	/**
	 * ���õ��ݺ�
	 * 
	 * @param billno
	 *            ���ݺ�
	 */
	public void setBillno(String billno) {
		this.billno = billno;
	}

	/**
	 * ��ȡ����״̬
	 * 
	 * @return ����״̬
	 * @see String
	 */
	public Integer getBillstatus() {
		return this.billstatus;
	}

	/**
	 * ���õ���״̬
	 * 
	 * @param billstatus
	 *            ����״̬
	 * @see String
	 */
	public void setBillstatus(Integer billstatus) {
		this.billstatus = billstatus;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getBilltype() {
		return this.billtype;
	}

	/**
	 * ���õ�������
	 * 
	 * @param billtype
	 *            ��������
	 */
	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	/**
	 * ��ȡBIP�˺�
	 * 
	 * @return BIP�˺�
	 */
	public String getBipno() {
		return this.bipno;
	}

	/**
	 * ����BIP�˺�
	 * 
	 * @param bipno
	 *            BIP�˺�
	 */
	public void setBipno(String bipno) {
		this.bipno = bipno;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getCarcode() {
		return this.carcode;
	}

	/**
	 * ���ó�������
	 * 
	 * @param carcode
	 *            ��������
	 */
	public void setCarcode(String carcode) {
		this.carcode = carcode;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getCarname() {
		return this.carname;
	}

	/**
	 * ���ó�������
	 * 
	 * @param carname
	 *            ��������
	 */
	public void setCarname(String carname) {
		this.carname = carname;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getCarproperty77() {
		return this.carproperty77;
	}

	/**
	 * ���ó�������
	 * 
	 * @param carproperty
	 *            ��������
	 */
	public void setCarproperty77(String carproperty77) {
		this.carproperty77 = carproperty77;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ����ʱ��
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * ���ô���ʱ��
	 * 
	 * @param creationtime
	 *            ����ʱ��
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public String getCreator() {
		return this.creator;
	}

	/**
	 * ���ô�����
	 * 
	 * @param creator
	 *            ������
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * ��ȡ�Զ�����1
	 * 
	 * @return �Զ�����1
	 */
	public String getDef1() {
		return this.def1;
	}

	/**
	 * �����Զ�����1
	 * 
	 * @param def1
	 *            �Զ�����1
	 */
	public void setDef1(String def1) {
		this.def1 = def1;
	}

	/**
	 * ��ȡ�Զ�����10
	 * 
	 * @return �Զ�����10
	 */
	public String getDef10() {
		return this.def10;
	}

	/**
	 * �����Զ�����10
	 * 
	 * @param def10
	 *            �Զ�����10
	 */
	public void setDef10(String def10) {
		this.def10 = def10;
	}

	/**
	 * ��ȡ�Զ�����11
	 * 
	 * @return �Զ�����11
	 */
	public String getDef11() {
		return this.def11;
	}

	/**
	 * �����Զ�����11
	 * 
	 * @param def11
	 *            �Զ�����11
	 */
	public void setDef11(String def11) {
		this.def11 = def11;
	}

	/**
	 * ��ȡ�Զ�����12
	 * 
	 * @return �Զ�����12
	 */
	public String getDef12() {
		return this.def12;
	}

	/**
	 * �����Զ�����12
	 * 
	 * @param def12
	 *            �Զ�����12
	 */
	public void setDef12(String def12) {
		this.def12 = def12;
	}

	/**
	 * ��ȡ�Զ�����13
	 * 
	 * @return �Զ�����13
	 */
	public String getDef13() {
		return this.def13;
	}

	/**
	 * �����Զ�����13
	 * 
	 * @param def13
	 *            �Զ�����13
	 */
	public void setDef13(String def13) {
		this.def13 = def13;
	}

	/**
	 * ��ȡ�Զ�����14
	 * 
	 * @return �Զ�����14
	 */
	public String getDef14() {
		return this.def14;
	}

	/**
	 * �����Զ�����14
	 * 
	 * @param def14
	 *            �Զ�����14
	 */
	public void setDef14(String def14) {
		this.def14 = def14;
	}

	/**
	 * ��ȡ�Զ�����15
	 * 
	 * @return �Զ�����15
	 */
	public String getDef15() {
		return this.def15;
	}

	/**
	 * �����Զ�����15
	 * 
	 * @param def15
	 *            �Զ�����15
	 */
	public void setDef15(String def15) {
		this.def15 = def15;
	}

	/**
	 * ��ȡ�Զ�����16
	 * 
	 * @return �Զ�����16
	 */
	public String getDef16() {
		return this.def16;
	}

	/**
	 * �����Զ�����16
	 * 
	 * @param def16
	 *            �Զ�����16
	 */
	public void setDef16(String def16) {
		this.def16 = def16;
	}

	/**
	 * ��ȡ�Զ�����17
	 * 
	 * @return �Զ�����17
	 */
	public String getDef17() {
		return this.def17;
	}

	/**
	 * �����Զ�����17
	 * 
	 * @param def17
	 *            �Զ�����17
	 */
	public void setDef17(String def17) {
		this.def17 = def17;
	}

	/**
	 * ��ȡ�Զ�����18
	 * 
	 * @return �Զ�����18
	 */
	public String getDef18() {
		return this.def18;
	}

	/**
	 * �����Զ�����18
	 * 
	 * @param def18
	 *            �Զ�����18
	 */
	public void setDef18(String def18) {
		this.def18 = def18;
	}

	/**
	 * ��ȡ�Զ�����19
	 * 
	 * @return �Զ�����19
	 */
	public String getDef19() {
		return this.def19;
	}

	/**
	 * �����Զ�����19
	 * 
	 * @param def19
	 *            �Զ�����19
	 */
	public void setDef19(String def19) {
		this.def19 = def19;
	}

	/**
	 * ��ȡ�Զ�����2
	 * 
	 * @return �Զ�����2
	 */
	public String getDef2() {
		return this.def2;
	}

	/**
	 * �����Զ�����2
	 * 
	 * @param def2
	 *            �Զ�����2
	 */
	public void setDef2(String def2) {
		this.def2 = def2;
	}

	/**
	 * ��ȡ�Զ�����20
	 * 
	 * @return �Զ�����20
	 */
	public String getDef20() {
		return this.def20;
	}

	/**
	 * �����Զ�����20
	 * 
	 * @param def20
	 *            �Զ�����20
	 */
	public void setDef20(String def20) {
		this.def20 = def20;
	}

	/**
	 * ��ȡ�Զ�����3
	 * 
	 * @return �Զ�����3
	 */
	public String getDef3() {
		return this.def3;
	}

	/**
	 * �����Զ�����3
	 * 
	 * @param def3
	 *            �Զ�����3
	 */
	public void setDef3(String def3) {
		this.def3 = def3;
	}

	/**
	 * ��ȡ�Զ�����4
	 * 
	 * @return �Զ�����4
	 */
	public String getDef4() {
		return this.def4;
	}

	/**
	 * �����Զ�����4
	 * 
	 * @param def4
	 *            �Զ�����4
	 */
	public void setDef4(String def4) {
		this.def4 = def4;
	}

	/**
	 * ��ȡ�Զ�����5
	 * 
	 * @return �Զ�����5
	 */
	public String getDef5() {
		return this.def5;
	}

	/**
	 * �����Զ�����5
	 * 
	 * @param def5
	 *            �Զ�����5
	 */
	public void setDef5(String def5) {
		this.def5 = def5;
	}

	/**
	 * ��ȡ�Զ�����6
	 * 
	 * @return �Զ�����6
	 */
	public String getDef6() {
		return this.def6;
	}

	/**
	 * �����Զ�����6
	 * 
	 * @param def6
	 *            �Զ�����6
	 */
	public void setDef6(String def6) {
		this.def6 = def6;
	}

	/**
	 * ��ȡ�Զ�����7
	 * 
	 * @return �Զ�����7
	 */
	public String getDef7() {
		return this.def7;
	}

	/**
	 * �����Զ�����7
	 * 
	 * @param def7
	 *            �Զ�����7
	 */
	public void setDef7(String def7) {
		this.def7 = def7;
	}

	/**
	 * ��ȡ�Զ�����8
	 * 
	 * @return �Զ�����8
	 */
	public String getDef8() {
		return this.def8;
	}

	/**
	 * �����Զ�����8
	 * 
	 * @param def8
	 *            �Զ�����8
	 */
	public void setDef8(String def8) {
		this.def8 = def8;
	}

	/**
	 * ��ȡ�Զ�����9
	 * 
	 * @return �Զ�����9
	 */
	public String getDef9() {
		return this.def9;
	}

	/**
	 * �����Զ�����9
	 * 
	 * @param def9
	 *            �Զ�����9
	 */
	public void setDef9(String def9) {
		this.def9 = def9;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 * @see String
	 */
	public Integer getExptype() {
		return this.exptype;
	}

	/**
	 * ���ñ�������
	 * 
	 * @param exptype
	 *            ��������
	 * @see String
	 */
	public void setExptype(Integer exptype) {
		this.exptype = exptype;
	}

	/**
	 * ��ȡ�Ƶ�ʱ��
	 * 
	 * @return �Ƶ�ʱ��
	 */
	public UFDateTime getMaketime() {
		return this.maketime;
	}

	/**
	 * �����Ƶ�ʱ��
	 * 
	 * @param maketime
	 *            �Ƶ�ʱ��
	 */
	public void setMaketime(UFDateTime maketime) {
		this.maketime = maketime;
	}

	/**
	 * ��ȡ����޸�ʱ��
	 * 
	 * @return ����޸�ʱ��
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * ��������޸�ʱ��
	 * 
	 * @param modifiedtime
	 *            ����޸�ʱ��
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	/**
	 * ��ȡ����޸���
	 * 
	 * @return ����޸���
	 */
	public String getModifier() {
		return this.modifier;
	}

	/**
	 * ��������޸���
	 * 
	 * @param modifier
	 *            ����޸���
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 * @see String
	 */
	public Integer getOpertype() {
		return this.opertype;
	}

	/**
	 * ���ò�������
	 * 
	 * @param opertype
	 *            ��������
	 * @see String
	 */
	public void setOpertype(Integer opertype) {
		this.opertype = opertype;
	}

	/**
	 * ��ȡ���ŷ�ʽ
	 * 
	 * @return ���ŷ�ʽ
	 * @see String
	 */
	public Integer getPaymode() {
		return this.paymode;
	}

	/**
	 * ���÷��ŷ�ʽ
	 * 
	 * @param paymode
	 *            ���ŷ�ʽ
	 * @see String
	 */
	public void setPaymode(Integer paymode) {
		this.paymode = paymode;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_car99() {
		return this.pk_car99;
	}

	/**
	 * ���ó���
	 * 
	 * @param pk_car
	 *            ����
	 */
	public void setPk_car99(String pk_car99) {
		this.pk_car99 = pk_car99;
	}

	/**
	 * ��ȡ�����޶���������
	 * 
	 * @return �����޶���������
	 */
	public String getPk_car_limit() {
		return this.pk_car_limit;
	}

	/**
	 * ���ó����޶���������
	 * 
	 * @param pk_car_limit
	 *            �����޶���������
	 */
	public void setPk_car_limit(String pk_car_limit) {
		this.pk_car_limit = pk_car_limit;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_carinfo() {
		return this.pk_carinfo;
	}

	/**
	 * ��������
	 * 
	 * @param pk_carinfo
	 *            ����
	 */
	public void setPk_carinfo(String pk_carinfo) {
		this.pk_carinfo = pk_carinfo;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getPk_group() {
		return this.pk_group;
	}

	/**
	 * ���ü���
	 * 
	 * @param pk_group
	 *            ����
	 */
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * ��ȡ��֯
	 * 
	 * @return ��֯
	 */
	public String getPk_org() {
		return this.pk_org;
	}

	/**
	 * ������֯
	 * 
	 * @param pk_org
	 *            ��֯
	 */
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * ��ȡ��֯�汾
	 * 
	 * @return ��֯�汾
	 */
	public String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * ������֯�汾
	 * 
	 * @param pk_org_v
	 *            ��֯�汾
	 */
	public void setPk_org_v(String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * ��ȡ�ϼ�����
	 * 
	 * @return �ϼ�����
	 */
	public String getPk_pcar88() {
		return this.pk_pcar88;
	}

	/**
	 * �����ϼ�����
	 * 
	 * @param pk_pcar
	 *            �ϼ�����
	 */
	public void setPk_pcar88(String pk_pcar88) {
		this.pk_pcar88 = pk_pcar88;
	}

	/**
	 * ��ȡ��ע
	 * 
	 * @return ��ע
	 */
	public String getRemarks() {
		return this.remarks;
	}

	/**
	 * ���ñ�ע
	 * 
	 * @param remarks
	 *            ��ע
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getTranstype() {
		return this.transtype;
	}

	/**
	 * ���ý�������
	 * 
	 * @param transtype
	 *            ��������
	 */
	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

	/**
	 * ��ȡ��������pk
	 * 
	 * @return ��������pk
	 */
	public String getTranstypepk() {
		return this.transtypepk;
	}

	/**
	 * ���ý�������pk
	 * 
	 * @param transtypepk
	 *            ��������pk
	 */
	public void setTranstypepk(String transtypepk) {
		this.transtypepk = transtypepk;
	}

	/**
	 * ��ȡʱ���
	 * 
	 * @return ʱ���
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ����ʱ���
	 * 
	 * @param ts
	 *            ʱ���
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("erm.CarInfoVO");
	}
}