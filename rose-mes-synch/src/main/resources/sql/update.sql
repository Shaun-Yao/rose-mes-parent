UPDATE color set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE SizeCaption set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE unit set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE series set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null
UPDATE mmf set EditDate = CONVERT ( VARCHAR (19), GETDATE(), 120) WHERE EditDate is null


ALTER TABLE YS_Colour add edit_date datetime not null;
ALTER TABLE YS_Size add edit_date datetime not null;
ALTER TABLE YS_AA_Unit add edit_date datetime not null;
ALTER TABLE YS_AA_InventoryClass add edit_date datetime not null;

