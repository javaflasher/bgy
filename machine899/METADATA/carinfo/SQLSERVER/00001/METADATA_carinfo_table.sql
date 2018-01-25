create table er_carinfo (
pk_carinfo CHAR(20) NOT NULL,
pk_group VARCHAR(20) default '~' NULL,
pk_org VARCHAR(20) default '~' NULL,
pk_org_v VARCHAR(20) default '~' NULL,
opertype INT NULL,
pk_car VARCHAR(101) default '~' NULL,
carcode VARCHAR(100) NULL,
carname VARCHAR(200) NULL,
pk_pcar VARCHAR(101) default '~' NULL,
carproperty VARCHAR(101) default '~' NULL,
bipno VARCHAR(100) NULL,
exptype INT NULL,
paymode INT NULL,
accyear VARCHAR(101) NULL,
beginaccmonth INT NULL,
annuallimit DECIMAL(28,8) NULL,
remarks VARCHAR(500) NULL,
pk_car_limit VARCHAR(20) NULL,
billdate CHAR(19) NULL,
billno VARCHAR(50) NULL,
billtype VARCHAR(20) NULL,
transtype VARCHAR(50) NULL,
transtypepk VARCHAR(20) NULL,
billstatus INT NULL,
approvestatus INT NULL,
billmaker VARCHAR(20) default '~' NULL,
maketime CHAR(19) NULL,
approver VARCHAR(20) default '~' NULL,
approvedate CHAR(19) NULL,
creator VARCHAR(20) default '~' NULL,
creationtime CHAR(19) NULL,
modifier VARCHAR(20) default '~' NULL,
modifiedtime CHAR(19) NULL,
def1 VARCHAR(101) NULL,
def2 VARCHAR(101) NULL,
def3 VARCHAR(101) NULL,
def4 VARCHAR(101) NULL,
def5 VARCHAR(101) NULL,
def6 VARCHAR(101) NULL,
def7 VARCHAR(101) NULL,
def8 VARCHAR(101) NULL,
def9 VARCHAR(101) NULL,
def10 VARCHAR(101) NULL,
def11 VARCHAR(101) NULL,
def12 VARCHAR(101) NULL,
def13 VARCHAR(101) NULL,
def14 VARCHAR(101) NULL,
def15 VARCHAR(101) NULL,
def16 VARCHAR(101) NULL,
def17 VARCHAR(101) NULL,
def18 VARCHAR(101) NULL,
def19 VARCHAR(101) NULL,
def20 VARCHAR(50) NULL,
CONSTRAINT PK_ER_CARINFO PRIMARY KEY (pk_carinfo),
ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') NULL,
 dr smallint default 0 NULL 
)


create table er_carinfo_b (
pk_carinfo_b CHAR(20) NOT NULL,
pk_carinfo VARCHAR(20) NULL,
rowno VARCHAR(20) NULL,
accmonth VARCHAR(20) NULL,
monthlylimit DECIMAL(28,8) NULL,
remarks VARCHAR(500) NULL,
def1 VARCHAR(101) NULL,
def2 VARCHAR(101) NULL,
def3 VARCHAR(101) NULL,
def4 VARCHAR(101) NULL,
def5 VARCHAR(101) NULL,
def6 VARCHAR(101) NULL,
def7 VARCHAR(101) NULL,
def8 VARCHAR(101) NULL,
def9 VARCHAR(101) NULL,
def10 VARCHAR(101) NULL,
def11 VARCHAR(101) NULL,
def12 VARCHAR(101) NULL,
def13 VARCHAR(101) NULL,
def14 VARCHAR(101) NULL,
def15 VARCHAR(101) NULL,
def16 VARCHAR(101) NULL,
def17 VARCHAR(101) NULL,
def18 VARCHAR(101) NULL,
def19 VARCHAR(101) NULL,
def20 VARCHAR(101) NULL,
CONSTRAINT PK_ER_CARINFO_B PRIMARY KEY (pk_carinfo_b),
ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') NULL,
 dr smallint default 0 NULL 
)


