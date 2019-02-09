CREATE TABLE IF NOT EXISTS Member (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
    username VARCHAR(64) NOT NULL,
    socialType VARCHAR(20) NOT NULL,
    email VARCHAR(128) NULL,
    current DATE NULL,
    createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

ALTER TABLE Member
ADD CONSTRAINT IF NOT EXISTS MEMBER_UK
UNIQUE (username, socialType);

CREATE TABLE IF NOT EXISTS Timeline (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	memberId BIGINT NOT NULL,
	activeDate DATE NULL,
	activity VARCHAR(16) NULL,
	referenceId BIGINT NULL,
	state INTEGER NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

ALTER TABLE Timeline
ADD CONSTRAINT IF NOT EXISTS TIMELINE_MEMBER_ID_FK
FOREIGN KEY (memberId) REFERENCES Member;

CREATE TABLE IF NOT EXISTS Wallet (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	memberId BIGINT NOT NULL,
	balance BIGINT NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS WalletLog (
	walletId BIGINT NOT NULL,
	balance BIGINT NULL,
	transferTime TIMESTAMP NULL,
	transferType VARCHAR(10) NULL,
	referenceId BIGINT NULL
);

ALTER TABLE Wallet
ADD CONSTRAINT IF NOT EXISTS WALLET_MEMBER_ID_FK
FOREIGN KEY (memberId) REFERENCES Member;

ALTER TABLE WalletLog
ADD CONSTRAINT IF NOT EXISTS WALLETLOG_WALLET_ID_FK
FOREIGN KEY (walletId) REFERENCES Wallet;

CREATE TABLE IF NOT EXISTS FSSCompany (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	financeGroup VARCHAR(10) NULL,
	disclosureMonth VARCHAR(10) NULL,
	financeCompanyNumber VARCHAR(20) NULL,
	koreanFinanceCompanyName VARCHAR(64) NULL,
	disclosureChargeMan VARCHAR(128) NULL,
	homepageUrl VARCHAR(255) NULL,
	callCenterTel VARCHAR(30) NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS FSSCompanyOptions (
	fssCompanyId BIGINT NOT NULL,
	disclosureMonth VARCHAR(10) NULL,
	financeCompanyNumber VARCHAR(20) NULL,
	area VARCHAR(10) NULL,
	areaName VARCHAR(10) NULL,
	existsYn VARCHAR(10) NULL
);

ALTER TABLE FSSCompanyOptions
ADD CONSTRAINT IF NOT EXISTS FSSCOMPANY_ID_FK
FOREIGN KEY (fssCompanyId) REFERENCES FSSCompany;

CREATE TABLE IF NOT EXISTS FSSSaving (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	financeGroup VARCHAR(10) NULL,
	disclosureMonth VARCHAR(10) NULL,
	financeCompanyNumber VARCHAR(20) NULL,
	financeProductCode VARCHAR(40) NULL,
	koreanFinanceCompanyName VARCHAR(64) NULL,
	financeProductName VARCHAR(64) NULL,
	joinWay VARCHAR(255) NULL,
	maturityInterest VARCHAR(255) NULL,
	specialCondition VARCHAR(255) NULL,
	joinDeny INTEGER NULL,
	joinMember VARCHAR(255) NULL,
	etcNote VARCHAR(255) NULL,
	maxLimit BIGINT NULL,
	disclosureStartDay DATE NULL,
	disclosureEndDay DATE NULL,
	financeCompanySubmitDay TIMESTAMP NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS FSSSavingOptions (
	fssSavingId BIGINT NOT NULL,
	disclosureMonth VARCHAR(10) NULL,
	financeCompanyNumber VARCHAR(20) NULL,
	financeProductCode VARCHAR(40) NULL,
	interestRateType VARCHAR(10) NULL,
	interestRateTypeName VARCHAR(10) NULL,
	reserveType VARCHAR(10) NULL,
	reserveTypeName VARCHAR(10) NULL,
	saveTerm INTEGER,
	interestRate DOUBLE,
	primeRate DOUBLE
);

ALTER TABLE FSSSavingOptions
ADD CONSTRAINT IF NOT EXISTS FSSSAVING_ID_FK
FOREIGN KEY (fssSavingId) REFERENCES FSSSaving;

CREATE TABLE IF NOT EXISTS FSSDeposit (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	financeGroup VARCHAR(10) NULL,
	disclosureMonth VARCHAR(10) NULL,
	financeCompanyNumber VARCHAR(20) NULL,
	financeProductCode VARCHAR(40) NULL,
	koreanFinanceCompanyName VARCHAR(64) NULL,
	financeProductName VARCHAR(64) NULL,
	joinWay VARCHAR(255) NULL,
	maturityInterest VARCHAR(255) NULL,
	specialCondition VARCHAR(255) NULL,
	joinDeny INTEGER NULL,
	joinMember VARCHAR(255) NULL,
	etcNote VARCHAR(255) NULL,
	maxLimit BIGINT NULL,
	disclosureStartDay DATE NULL,
	disclosureEndDay DATE NULL,
	financeCompanySubmitDay TIMESTAMP NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS FSSDepositOptions (
	fssDepositId BIGINT NOT NULL,
	disclosureMonth VARCHAR(10) NULL,
	financeCompanyNumber VARCHAR(20) NULL,
	financeProductCode VARCHAR(40) NULL,
	interestRateType VARCHAR(10) NULL,
	interestRateTypeName VARCHAR(10) NULL,
	saveTerm INTEGER,
	interestRate DOUBLE,
	primeRate DOUBLE
);

ALTER TABLE FSSDepositOptions
ADD CONSTRAINT IF NOT EXISTS FSSDEPOSIT_ID_FK
FOREIGN KEY (fssDepositId) REFERENCES FSSDeposit;

CREATE TABLE IF NOT EXISTS FSSResult (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	financeGroup VARCHAR(10) NULL,
	productDivision VARCHAR(10) NULL,
	errorCode VARCHAR(10) NULL,
	errorMessage VARCHAR(255) NULL,
	totalCount INTEGER,
	maxPageNumber INTEGER,
	nowPageNumber INTEGER,
	connectionTime TIMESTAMP,
	createdDate TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS FinanceCompany (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	financeGroup VARCHAR(10) NULL,
	disclosureMonth VARCHAR(10) NULL,
	code VARCHAR(20) NULL,
	name VARCHAR(64) NULL,
	disclosureChargeman VARCHAR(128) NULL,
	webSite VARCHAR(255) NULL,
	tel VARCHAR(30) NULL,
	testState VARCHAR(10) NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS FinanceCompanyOptions (
	financeCompanyId BIGINT NOT NULL,
	area VARCHAR(10) NULL,
	exist BOOLEAN NULL
);

ALTER TABLE FinanceCompanyOptions
ADD CONSTRAINT IF NOT EXISTS FINANCECOMPANY_ID_FK
FOREIGN KEY (financeCompanyId) REFERENCES FinanceCompany;

CREATE TABLE IF NOT EXISTS Product (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	productType VARCHAR(10) NULL,
	financeGroup VARCHAR(10) NULL,
	financeCompanyId BIGINT NOT NULL,
	disclosureMonth VARCHAR(10) NULL,
	companyNumber VARCHAR(20) NULL,
	companyName VARCHAR(64) NULL,
	code VARCHAR(40) NULL,
	name VARCHAR(64) NULL,
	joinWay VARCHAR(255) NULL,
	maturityInterest VARCHAR(255) NULL,
	specialCondition VARCHAR(255) NULL,
	joinDeny INTEGER NULL,
	joinMember VARCHAR(255) NULL,
	etcNote VARCHAR(255) NULL,
	minAge INTEGER,
	maxAge INTEGER,
	depositProtect BOOLEAN,
	nonTaxable BOOLEAN,
	minPeriod INTEGER,
	maxPeriod INTEGER,
	periodUnit VARCHAR(10) NULL,
	minBalance BIGINT,
	maxBalance BIGINT,
	interestPaymentType VARCHAR(20) NULL,
	testState VARCHAR(10),
	state VARCHAR(10),
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

ALTER TABLE Product
ADD CONSTRAINT IF NOT EXISTS PRODUCT_FINANCECOMPANY_ID_FK
FOREIGN KEY (financeCompanyId) REFERENCES FinanceCompany;

CREATE TABLE IF NOT EXISTS Product_ProductOptions (
	product_Id BIGINT NOT NULL,
	productOptions_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS ProductOption (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	interestRateType VARCHAR(10) NULL,
	reserveType VARCHAR(10) NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

ALTER TABLE Product_ProductOptions
ADD CONSTRAINT IF NOT EXISTS PRODUCT_PRODUCTOPTION_ID_UK
UNIQUE (productOptions_id);

ALTER TABLE Product_ProductOptions
ADD CONSTRAINT IF NOT EXISTS PRODUCT_OPTION_ID_FK
FOREIGN KEY (productOptions_id) REFERENCES ProductOption;

ALTER TABLE Product_ProductOptions
ADD CONSTRAINT IF NOT EXISTS PRODUCT_ID_FK
FOREIGN KEY (product_Id) REFERENCES Product;

CREATE TABLE IF NOT EXISTS ProductOptionInterestRate (
	productOptionId BIGINT NOT NULL,
	contractPeriod INTEGER,
	rate DOUBLE,
	primeRate DOUBLE
);

ALTER TABLE ProductOptionInterestRate
ADD CONSTRAINT IF NOT EXISTS PRODUCT_OPTION_IR_ID_FK
FOREIGN KEY (productOptionId) REFERENCES ProductOption;

CREATE TABLE IF NOT EXISTS Account (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	productId BIGINT NOT NULL,
	memberId BIGINT NOT NULL,
	name VARCHAR(64) NULL,
	productType VARCHAR(10) NULL,
	balance BIGINT,
	interest VARCHAR(30) NULL,
	interestRate DOUBLE,
	contractDate DATE,
	expiryDate DATE,
	paymentFrequency VARCHAR(10) NULL,
	taxRate DOUBLE,
	taxType VARCHAR(20) NULL,
	reserveType VARCHAR(10) NULL,
	dateOfPayment INTEGER,
	state VARCHAR(10) NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

ALTER TABLE Account
ADD CONSTRAINT IF NOT EXISTS ACCOUNT_PRODUCT_ID_FK
FOREIGN KEY (productId) REFERENCES Product;

ALTER TABLE Account
ADD CONSTRAINT IF NOT EXISTS ACCOUNT_MEMBER_ID_FK
FOREIGN KEY (memberId) REFERENCES Member;

CREATE TABLE IF NOT EXISTS Account_AccountDetails (
	account_Id BIGINT NOT NULL,
	accountDetails_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS AccountDetail (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	turn INTEGER,
	balance BIGINT,
	depositDate DATE NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

ALTER TABLE Account_AccountDetails
ADD CONSTRAINT IF NOT EXISTS ACCOUNT_ACCOUNTDETAIL_ID_UK
UNIQUE (accountDetails_id);

ALTER TABLE Account_AccountDetails
ADD CONSTRAINT IF NOT EXISTS ACCOUNT_DETAIL_ID_FK
FOREIGN KEY (accountDetails_id) REFERENCES AccountDetail;

ALTER TABLE Account_AccountDetails
ADD CONSTRAINT IF NOT EXISTS ACCOUNT_ID_FK
FOREIGN KEY (account_Id) REFERENCES Account;

CREATE TABLE users(
	username VARCHAR_IGNORECASE(50) NOT NULL PRIMARY KEY,
	password VARCHAR_IGNORECASE(200) NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
	username VARCHAR_IGNORECASE(50) NOT NULL,
	authority VARCHAR_IGNORECASE(50) NOT NULL,
	CONSTRAINT FK_AUTHORITIES_USERS FOREIGN KEY(username) REFERENCES USERS(username)
);

CREATE TABLE IF NOT EXISTS Comment (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	memberId BIGINT NOT NULL,
	contents VARCHAR(256) NULL,
	createdDate TIMESTAMP NULL,
    lastModifiedDate TIMESTAMP NULL
);

ALTER TABLE Comment
ADD CONSTRAINT IF NOT EXISTS COMMENT_MEMBER_ID_FK
FOREIGN KEY (memberId) REFERENCES Member;