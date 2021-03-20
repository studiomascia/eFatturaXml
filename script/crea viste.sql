/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *   Vista che crea una tabella con 2 campi ottenuti dalla tabella xmlfatturabase: Id e xml_string che viene convertito in formato xml
 * Author:  Admin
 * Created: 22-gen-2020
 */
//

Create View View_FattureXml
 as select ID as IdXmlFatturaBase,
 CAST(REPLACE(CAST([xml_string] AS NVARCHAR(MAX)),'utf-8','utf-16') AS XML) as FatturaXml
 FROM[xmlfatturabase]


CREATE VIEW View_FattureXml_Fornitori
as SELECT  IdXmlFatturaBase,
	 -- T.Nodo.value('(IdCodice)[1]', 'varchar(max)') as PIVA,
	  T2.ID as IdFornitore
  FROM [View_FattureXml]
    CROSS APPLY FatturaXml.nodes('/*/FatturaElettronicaHeader/CedentePrestatore/DatiAnagrafici/IdFiscaleIVA') as T(Nodo), Anagrafica_societa T2
	Where  T.Nodo.value('(IdCodice)[1]', 'varchar(max)') = piva


USE [GestionaleConcreteDB_Svil]
GO
UPDATE 
     t1
SET 
     t1.id_anagrafica_societa = t2.idfornitore
FROM 
     [dbo].[xmlfatturabase] t1 
     INNER JOIN [dbo].[View_FattureXml_Fornitori] t2 
     ON t1.id = t2.idxmlfatturabase;
go