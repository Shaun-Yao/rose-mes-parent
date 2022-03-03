UPDATE color set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE SizeCaption set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE unit set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE series set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE mmf set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE provtype set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE provider set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null

ALTER TABLE YS_Colour add edit_date datetime not null;
ALTER TABLE YS_Size add edit_date datetime not null;
ALTER TABLE YS_AA_Unit add edit_date datetime not null;
ALTER TABLE YS_AA_InventoryClass add edit_date datetime not null;


ALTER TABLE YS_AA_PartnerClass add edit_date datetime;
UPDATE YS_AA_PartnerClass SET edit_date = CONVERT ( VARCHAR (19), GETDATE(), 120)
ALTER TABLE YS_AA_PartnerClass ALTER COLUMN edit_date datetime not null;

ALTER TABLE YS_AA_Partner add edit_date datetime;
UPDATE YS_AA_Partner SET edit_date = CONVERT ( VARCHAR (19), GETDATE(), 120)
ALTER TABLE YS_AA_Partner ALTER COLUMN edit_date datetime not null;

ALTER TABLE JY_PU_PurchaseOrder add edit_date datetime;
UPDATE JY_PU_PurchaseOrder SET edit_date = CONVERT ( VARCHAR (19), GETDATE(), 120)
ALTER TABLE JY_PU_PurchaseOrder ALTER COLUMN edit_date datetime not null;

ALTER TABLE JY_PU_PurchaseOrder_b add edit_date datetime;
UPDATE JY_PU_PurchaseOrder_b SET edit_date = CONVERT ( VARCHAR (19), GETDATE(), 120)
ALTER TABLE JY_PU_PurchaseOrder_b ALTER COLUMN edit_date datetime not null;

