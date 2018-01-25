package nc.itf.erm.constants;

public interface CarInforConstants {
	
	/**
	 * 报销类型：实报实销
	 */
	public static final int bxlx_sbsx = 1 ; 
	/**
	 * 报销类型：限额报销
	 */
	public static final int bxlx_xebx = 2 ;
	/**
	 * 发放方式：工资发放
	 */
	public static final int fffs_gzff=1 ; 
	/**
	 * 发放方式：报销发放
	 */
	public static final int fffs_bxff=2 ;
	/**
	 * 操作类型：新增
	 */
	public static final int opertype_add = 1 ;
	/**
	 * 操作类型：变更
	 */
	public static final int	opertype_change = 2 ;
	/**
	 * 操作类型：停用
	 */
	public static final int opertype_disable = 3 ;
	/**
	 * 启用
	 */
	public static final int	opertype_enable = 4 ;
	/**
	 * 单据状态：未审批
	 */
	public static final int	billstatus_wsp = 1 ;
	/**
	 * 单据状态：审批中
	 */
	public static final int billstatus_spz = 2 ;
	/**
	 * 单据状态：审批通过
	 */
	public static final int billstatus_sptg = 3 ;
	/**
	 * 审批状态：审批通过
	 */
	public static final int approval_sptg = 1 ;
	/**
	 * 审批状态：审批中
	 */
	public static final int approval_spz = 2 ;
	/**
	 * 审批状态：提交
	 */
	public static final int approval_submit = 3 ;
	
	/**
	 * 审批状态：审批未通过
	 */
	public static final int approval_disagree = 0 ;
	/**
	 * 审批状态：自由
	 */
	public static final int approval_free = -1 ;
}
